/*
Copyright (C) 2018 Duke University

This file is part of mVax.

mVax is free software: you can redistribute it and/or
modify it under the terms of the GNU Affero General Public License
as published by the Free Software Foundation, either version 3,
or (at your option) any later version.

mVax is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU General Public
License along with mVax; see the file LICENSE. If not, see
<http://www.gnu.org/licenses/>.
*/
package mhealth.mvax.reports;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Dose;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.modals.DateModal;
import mhealth.mvax.records.record.patient.detail.StringDetail;
import mhealth.mvax.records.utilities.NullableDateFormat;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 * <p>
 * Fragment for exporting and visualizing data
 */
public class FormFragment extends Fragment {

    private View mView;
    private Set<String> mPatientKeys;
    private HashMap<String, ExpandablePatient> mPatients;
    private List<Vaccination> mVaccinations;
    private Map<String, Vaccine> mVaccines;
    private Map<String, List<Vaccination>> mVax;
    private FormAdapter mAdapter;

    private String mReportDate;


    public FormFragment() {
        mPatientKeys = new HashSet<>();
        mPatients = new HashMap<>();
        mVaccinations = new ArrayList<>();
        mVaccines = new HashMap<>();
        mVax = new HashMap<>();
    }

    public static FormFragment newInstance() {
        return new FormFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_reports, container, false);
        Button sinova2 = mView.findViewById(R.id.sinova_2);
        sinova2.setOnClickListener(v -> promptForDate());

        if (savedInstanceState != null) {
            setReportDate(savedInstanceState.getString("reportDate"));
            mPatients = (HashMap<String, ExpandablePatient>) savedInstanceState.getSerializable("patients");
        }
        render();
        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("reportDate", mReportDate);
        outState.putSerializable("patients", mPatients);
        super.onSaveInstanceState(outState);
    }

    private void promptForDate() {
        final TypeRunnable<Long> positiveAction = date -> {
            if (!mPatients.isEmpty()) {
                mVaccinations.clear();
                mPatientKeys.clear();
                mPatients.clear();
                mAdapter.refresh(new ArrayList<>(mPatients.values()));
            }
            initVaccinationQuery(date);
        };
        final DateModal dateModal = new DateModal(null, positiveAction, mView);
        dateModal.createAndShow();
    }

    private void initVaccinationQuery(Long date) {
        setReportDate(date);

        final String masterTable = getResources().getString(R.string.data_table);
        final String vaccinationTable = getResources().getString(R.string.vaccination_table);
        final String dateField = getResources().getString(R.string.date);
        Query vaccinationQuery = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(vaccinationTable)
                .orderByChild(dateField)
                .equalTo(date);
        vaccinationQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot vaccinationSnap : dataSnapshot.getChildren()) {
                    Vaccination v = vaccinationSnap.getValue(Vaccination.class);
                    if (v != null) {
                        mVaccinations.add(v);
                        mPatientKeys.add(v.getPatientDatabaseKey());
                    }
                }
                if (mPatientKeys.isEmpty()) { // no vaccinations
                    mView.findViewById(R.id.spinner).setVisibility(View.INVISIBLE);
                    mView.findViewById(R.id.no_vaccinations).setVisibility(View.VISIBLE);
                } else {
                    mView.findViewById(R.id.no_vaccinations).setVisibility(View.INVISIBLE);
                    initVaccineQuery();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mView.getContext(), R.string.vaccinations_download_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initVaccineQuery() {
        final String masterTable = getResources().getString(R.string.data_table);
        final String vaccineTable = getResources().getString(R.string.vaccine_table);

        for (Vaccination vaccination : mVaccinations) {
            String vaccineKey = vaccination.getVaccineKey();

            Query vaccineQuery = FirebaseDatabase.getInstance().getReference()
                    .child(masterTable)
                    .child(vaccineTable)
                    .orderByKey()
                    .equalTo(vaccineKey);
            vaccineQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot vaccineSnap : dataSnapshot.getChildren()) {
                        Vaccine vaccine = vaccineSnap.getValue(Vaccine.class);
                        if (vaccine != null) mVaccines.put(vaccine.getDatabaseKey(), vaccine);
                    }
                    initPatientQuery();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void initPatientQuery() {
        final String masterTable = getResources().getString(R.string.data_table);
        final String patientTable = getResources().getString(R.string.patient_table);


        for (String patientKey : mPatientKeys) {
            Query patientQuery = FirebaseDatabase.getInstance().getReference()
                    .child(masterTable)
                    .child(patientTable)
                    .orderByKey()
                    .equalTo(patientKey);
            patientQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot patientSnap : dataSnapshot.getChildren()) {
                        Patient p = patientSnap.getValue(Patient.class);
                        if (p != null) {
                            ArrayList<Vaccination> vaccinations = new ArrayList<>();
                            for (Vaccination v : mVaccinations) {
                                if (v.getPatientDatabaseKey().equals(p.getDatabaseKey())) {
                                    vaccinations.add(v);
                                }
                            }
                            ExpandablePatient expandablePatient = new ExpandablePatient(p);
                            mPatients.put(expandablePatient.getPatient().getDatabaseKey(), expandablePatient);
                            mVax.put(p.getDatabaseKey(), vaccinations);
                        }
                    }
                    mView.findViewById(R.id.spinner).setVisibility(View.INVISIBLE);
                    refresh();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mView.getContext(), R.string.vaccinations_download_fail, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void render() {
        final ExpandableListView queryResults = mView.findViewById(R.id.report_results);
        mAdapter = new FormAdapter();
        queryResults.setAdapter(mAdapter);
    }

    private void refresh() {
        for (ExpandablePatient p : mPatients.values()) {

            if (!p.isDone) {
                List<Vaccination> vaccinations = mVax.get(p.getPatient().getDatabaseKey());

                for (Vaccination vaccination : vaccinations) { // TODO implement sort
                    Vaccine vaccine = mVaccines.get(vaccination.getVaccineKey());
                    for (Dose dose : vaccine.getDoses()) {
                        if (dose.getDatabaseKey().equals(vaccination.getDoseKey())) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(dose.getLabel());
                            if (!vaccination.getYears().isEmpty()) {
                                sb.append(" ");
                                sb.append(vaccination.getYears());
                                sb.append("a");
                            }
                            if (!vaccination.getMonths().isEmpty()) {
                                sb.append(" ");
                                sb.append(vaccination.getMonths());
                                sb.append("m");
                            }
                            p.addRow(vaccine.getName(), sb.toString());
                        }


                    }

                }
                p.isDone = true;
            }
        }


        mAdapter.refresh(new ArrayList<>(mPatients.values()));
    }

    private void setReportDate(Long date) {
        TextView reportDate = mView.findViewById(R.id.report_date);
        mReportDate = NullableDateFormat.getString(date);
        reportDate.setText(mReportDate);
    }

    private void setReportDate(String date) {
        mReportDate = date;
        TextView reportDate = mView.findViewById(R.id.report_date);
        reportDate.setText(mReportDate);
    }

}
