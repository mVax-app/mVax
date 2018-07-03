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
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Dose;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.utilities.NullableDateFormat;
import mhealth.mvax.records.utilities.TypeRunnable;
import mhealth.mvax.utilities.modals.LoadingModal;

/**
 * @author Robert Steilberg
 * <p>
 * Fragment for exporting and visualizing data
 */
public class FormFragment extends Fragment {

    private View mView;
    private ProgressBar mSpinner;
    private LoadingModal mLoadingModal;
    private Button mSinova1Button;
    private Button mSinova2Button;

    private ArrayList<Vaccine> mVaccines;
    private String mReportDate;
    private ArrayList<ExpandablePatient> mPatients;


    public FormFragment() {
        mVaccines = new ArrayList<>();
        mPatients = new ArrayList<>();
    }

    public static FormFragment newInstance() {
        return new FormFragment();
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_reports, container, false);
        mSpinner = mView.findViewById(R.id.spinner);
        mSinova1Button = mView.findViewById(R.id.sinova_1);
        mSinova1Button.setOnClickListener(v -> promptForDate());
        mSinova2Button = mView.findViewById(R.id.sinova_2);
        mLoadingModal = new LoadingModal(mView);

        if (savedInstanceState != null) {
            mVaccines = (ArrayList<Vaccine>) savedInstanceState.getSerializable("vaccines");
            mPatients = (ArrayList<ExpandablePatient>) savedInstanceState.getSerializable("patients");
            setReportDate(savedInstanceState.getString("reportDate"));
            render();
        } else {
            disableButtons();
            downloadVaccines();
        }

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("vaccines", mVaccines);
        outState.putSerializable("reportDate", mReportDate);
        outState.putSerializable("patients", mPatients);
        super.onSaveInstanceState(outState);
    }

    private void downloadVaccines() {
        mLoadingModal.createAndShow();
        final String masterTable = getResources().getString(R.string.data_table);
        final String vaccinationTable = getResources().getString(R.string.vaccine_table);

        DatabaseReference vaccineRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(vaccinationTable);
        vaccineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot vaccineSnap : dataSnapshot.getChildren()) {
                    Vaccine vaccine = vaccineSnap.getValue(Vaccine.class);
                    if (vaccine != null) mVaccines.add(vaccine);
                }
                Collections.sort(mVaccines);
                mLoadingModal.dismiss();
                enableButtons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mView.getContext(), R.string.report_init_fail, Toast.LENGTH_LONG).show();
                mLoadingModal.dismiss();
            }
        });
    }

    private void enableButtons() {
        mSinova1Button.setEnabled(true);
        mSinova2Button.setEnabled(true);
    }

    private void disableButtons() {
        mSinova1Button.setEnabled(false);
        mSinova2Button.setEnabled(false);
    }

    private void promptForDate() {
        final TypeRunnable<Long> positiveAction = date -> {
            mPatients.clear(); // clear out old results
            mView.findViewById(R.id.no_vaccinations).setVisibility(View.INVISIBLE);
            setReportDate(NullableDateFormat.getString(date));
            mSpinner.setVisibility(View.VISIBLE);
            downloadVaccinations(date);
        };
        final ReportDateModal dateModal = new ReportDateModal(positiveAction, mView);
        dateModal.createAndShow();
    }

    private void setReportDate(String date) {
        mReportDate = date;
        TextView reportDate = mView.findViewById(R.id.report_date);
        reportDate.setText(mReportDate);
    }

    private void downloadVaccinations(Long date) {
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
                HashMap<String, ArrayList<Vaccination>> vaccinations = new HashMap<>();

                for (DataSnapshot vaccinationSnap : dataSnapshot.getChildren()) {
                    Vaccination vaccination = vaccinationSnap.getValue(Vaccination.class);
                    if (vaccination != null) {
                        String patientKey = vaccination.getPatientDatabaseKey();
                        if (vaccinations.containsKey(patientKey)) {
                            vaccinations.get(patientKey).add(vaccination);
                        } else {
                            ArrayList<Vaccination> vacList = new ArrayList<>();
                            vacList.add(vaccination);
                            vaccinations.put(patientKey, vacList);
                        }
                    }
                }
                if (vaccinations.isEmpty()) {
                    mSpinner.setVisibility(View.INVISIBLE);
                    mView.findViewById(R.id.no_vaccinations).setVisibility(View.VISIBLE);
                } else {
                    downloadPatients(vaccinations);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mView.getContext(), R.string.report_download_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private int mNumPatients;
    private int mDownloadedPatients;

    private void downloadPatients(Map<String, ArrayList<Vaccination>> vaccinations) {
        mDownloadedPatients = 0;
        mNumPatients = vaccinations.keySet().size();
        for (String patientKey : vaccinations.keySet()) {
            downloadPatient(patientKey, vaccinations.get(patientKey));
        }
    }

    private void downloadPatient(String patientKey, List<Vaccination> vaccinations) {
        final String masterTable = getResources().getString(R.string.data_table);
        final String patientTable = getResources().getString(R.string.patient_table);

        Query patientQuery = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(patientTable)
                .orderByKey()
                .equalTo(patientKey);
        patientQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot patientSnap : dataSnapshot.getChildren()) {
                    Patient patient = patientSnap.getValue(Patient.class);

                    ExpandablePatient ep = new ExpandablePatient(patient);

                    // TODO clean this up
                    for (Vaccine vaccine : mVaccines) {
                        for (Vaccination vaccination : vaccinations) {
                            if (vaccine.getDatabaseKey().equals(vaccination.getVaccineKey())) {
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
                                        ep.addRow(new Pair<>(vaccine.getName(), sb.toString()));

                                    }
                                }
                            }
                        }
                    }
                    mPatients.add(ep);

                }

                if (++mDownloadedPatients == mNumPatients) { // all patients downloaded
                    mSpinner.setVisibility(View.INVISIBLE);
                    render();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mView.getContext(), R.string.report_download_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void render() {
        final ExpandableListView queryResults = mView.findViewById(R.id.report_results);
        queryResults.setAdapter(new FormAdapter(mPatients));
    }

}
