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
package com.mvax.records.record.vaccine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mvax.R;
import com.mvax.model.immunization.DueDate;
import com.mvax.model.immunization.Vaccination;
import com.mvax.model.immunization.Vaccine;
import com.mvax.records.record.RecordTab;

/**
 * @author Robert Steilberg
 * <p>
 * Fragment for managing a patient's vaccine schedule
 */
public class VaccineScheduleTab extends Fragment implements RecordTab {

    private View mView;
    private VaccineAdapter mAdapter;

    private List<Vaccine> mVaccines;
    private Map<String, Vaccination> mVaccinations;
    private Map<String, DueDate> mDueDates;

    private int mVaccineDatabaseId;
    private int mVaccinationDatabaseId;

    private String mPatientDatabaseKey;
    private DatabaseReference mVaccineRef;
    private ChildEventListener mVaccineListener;
    private Query mVaccinationsQuery;
    private ChildEventListener mVaccinationsListener;
    private Query mDueDatesQuery;
    private ChildEventListener mDueDatesListener;

    public VaccineScheduleTab() {
        mVaccines = new ArrayList<>();
        mVaccinations = new HashMap<>();
        mDueDates = new HashMap<>();
    }

    public static VaccineScheduleTab newInstance(String patientDatabaseKey, int vaccineDatabaseId, int vaccinationDatabaseId) {
        final VaccineScheduleTab newInstance = new VaccineScheduleTab();
        final Bundle args = new Bundle();
        args.putString("patientDatabaseKey", patientDatabaseKey);
        args.putInt("vaccineDatabaseId", vaccineDatabaseId);
        args.putInt("vaccinationDatabaseId", vaccinationDatabaseId);
        newInstance.setArguments(args);
        return newInstance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_vaccine_schedule, container, false);

        mVaccineDatabaseId = getArguments().getInt("vaccineDatabaseId");
        mVaccinationDatabaseId = getArguments().getInt("vaccinationDatabaseId");
        mPatientDatabaseKey = getArguments().getString("patientDatabaseKey");

        setTabTitle();
        initVaccineListener();
        initVaccinationsListener();
        initDueDatesListener();
        render();
        return mView;
    }

    @Override
    public void onDestroyView() {
        mVaccineRef.removeEventListener(mVaccineListener);
        mVaccinationsQuery.removeEventListener(mVaccinationsListener);
        mDueDatesQuery.removeEventListener(mDueDatesListener);
        super.onDestroyView();
    }

    @Override
    public void render() {
        mAdapter = new VaccineAdapter(mPatientDatabaseKey, mVaccinationDatabaseId);
        RecyclerView vaccineList = mView.findViewById(R.id.vaccine_list);
        vaccineList.setHasFixedSize(true);
        vaccineList.setLayoutManager(new LinearLayoutManager(getContext()));
        vaccineList.setAdapter(mAdapter);
    }

    @Override
    public void refresh() {
        mView.findViewById(R.id.search_spinner).setVisibility(View.GONE);
        mAdapter.refresh(mVaccines, mVaccinations, mDueDates);
    }

    private void setTabTitle() {
        TextView title = mView.findViewById(R.id.vaccine_tab_title);
        switch (mVaccineDatabaseId) {
            case R.string.sinova_1_vaccine_table:
                title.setText(R.string.sinova_1_tab_title);
                break;
            case R.string.sinova_2_vaccine_table:
                title.setText(R.string.sinova_2_tab_title);
                break;
            default:
                break;
        }
    }

    private void initVaccineListener() {
        mVaccineRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.data_table))
                .child(getString(mVaccineDatabaseId));

        mVaccineListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevKey) {
                final Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                if (vaccine != null) {
                    mVaccines.add(vaccine);
                    refresh();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevKey) {
                final Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                if (vaccine != null) {
                    mVaccines.removeIf(v -> v.getDatabaseKey().equals(vaccine.getDatabaseKey()));
                    mVaccines.add(vaccine);
                    refresh();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // deleting vaccines is not allowed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.vaccines_download_fail, Toast.LENGTH_SHORT).show();
            }
        };
        mVaccineRef.addChildEventListener(mVaccineListener);
    }

    private void initVaccinationsListener() {
        mVaccinationsQuery = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.data_table))
                .child(getString(mVaccinationDatabaseId))
                .orderByChild(getString(R.string.patient_database_key))
                .equalTo(mPatientDatabaseKey);

        mVaccinationsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevKey) {
                final Vaccination vaccination = dataSnapshot.getValue(Vaccination.class);
                if (vaccination != null) {
                    mVaccinations.put(vaccination.getDoseKey(), vaccination);
                    refresh();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevKey) {
                onChildAdded(dataSnapshot, prevKey);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                final Vaccination vaccination = dataSnapshot.getValue(Vaccination.class);
                if (vaccination != null) {
                    mVaccinations.remove(vaccination.getDoseKey());
                    refresh();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String prevKey) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.vaccinations_download_fail, Toast.LENGTH_SHORT).show();
            }
        };
        mVaccinationsQuery.addChildEventListener(mVaccinationsListener);
    }

    private void initDueDatesListener() {
        mDueDatesQuery = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.data_table))
                .child(getString(R.string.due_date_table))
                .orderByChild(getString(R.string.patient_database_key))
                .equalTo(mPatientDatabaseKey);

        mDueDatesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevKey) {
                final DueDate dueDate = dataSnapshot.getValue(DueDate.class);
                if (dueDate != null) {
                    mDueDates.put(dueDate.getVaccineDatabaseKey(), dueDate);
                    refresh();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevKey) {
                onChildAdded(dataSnapshot, prevKey);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                final DueDate dueDate = dataSnapshot.getValue(DueDate.class);
                if (dueDate != null) {
                    mDueDates.remove(dueDate.getVaccineDatabaseKey());
                    refresh();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String prevKey) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_due_dates_download, Toast.LENGTH_SHORT).show();
            }
        };
        mDueDatesQuery.addChildEventListener(mDueDatesListener);
    }

}
