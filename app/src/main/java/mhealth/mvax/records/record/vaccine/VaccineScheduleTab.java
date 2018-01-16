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
package mhealth.mvax.records.record.vaccine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Date;
import mhealth.mvax.model.immunization.DueDate;
import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.immunization.Vaccine;
import mhealth.mvax.records.record.RecordTab;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for managing a patient's vaccine schedule through
 *         a ListView
 */
public class VaccineScheduleTab extends Fragment implements RecordTab {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;
    private VaccineAdapter mAdapter;

    private String mPatientDatabaseKey;

    private DatabaseReference mVaccineRef;
    private ChildEventListener mVaccineListener;
    private HashMap<String, Vaccine> mVaccines = new HashMap<>();

    private Query mVaccinationsQuery;
    private ChildEventListener mVaccinationsListener;

    private Query mDueDatesQuery;
    private ChildEventListener mDueDatesListener;

    // maps Dose to Vaccination and Vaccine to DueDate
    private HashMap<String, Date> mDates = new HashMap<>();

    //================================================================================
    // Static methods
    //================================================================================

    public static VaccineScheduleTab newInstance(String patientDatabaseKey) {
        final VaccineScheduleTab newInstance = new VaccineScheduleTab();
        final Bundle args = new Bundle();
        args.putString("patientDatabaseKey", patientDatabaseKey);
        newInstance.setArguments(args);
        return newInstance;
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_vaccine_schedule, container, false);
        mPatientDatabaseKey = getArguments().getString("patientDatabaseKey");
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

    //================================================================================
    // Public methods
    //================================================================================

    @Override
    public void render() {
        ListView vaccineListView = mView.findViewById(R.id.vaccines_list_view);
        mAdapter = new VaccineAdapter(getContext(), mPatientDatabaseKey, mVaccines, mDates);
        vaccineListView.setAdapter(mAdapter);
    }

    @Override
    public void refresh() {
        mAdapter.refresh(mVaccines, mDates);
    }

    //================================================================================
    // Private methods
    //================================================================================

    private void initVaccineListener() {
        // define database ref
        final String masterTable = getResources().getString(R.string.dataTable);
        final String vaccineTable = getResources().getString(R.string.vaccineTable);
        mVaccineRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(vaccineTable);

        // define listener
        mVaccineListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                final Vaccine addedVaccine = dataSnapshot.getValue(Vaccine.class);
                if (addedVaccine != null) {
                    mVaccines.put(addedVaccine.getDatabaseKey(), addedVaccine);
                    refresh();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevKey) {
                onChildAdded(dataSnapshot, prevKey);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final Vaccine removedVaccine = dataSnapshot.getValue(Vaccine.class);
                if (removedVaccine != null) {
                    mVaccines.remove(removedVaccine.getDatabaseKey());
                    refresh();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_vaccines_download, Toast.LENGTH_SHORT).show();
            }
        };

        // set listener to ref
        mVaccineRef.addChildEventListener(mVaccineListener);
    }

    private void initVaccinationsListener() {
        // define database query
        final String masterTable = getResources().getString(R.string.dataTable);
        final String vaccinationsTable = getResources().getString(R.string.vaccinationsTable);
        final String patientDatabaseKeyField = getResources().getString(R.string.patientDatabaseKeyField);
        mVaccinationsQuery = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(vaccinationsTable)
                .orderByChild(patientDatabaseKeyField)
                .equalTo(mPatientDatabaseKey);

        // define listener
        mVaccinationsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                final Vaccination addedVaccination = dataSnapshot.getValue(Vaccination.class);
                if (addedVaccination != null) {
                    mDates.put(addedVaccination.getDoseDatabaseKey(), addedVaccination);
                    refresh();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevKey) {
                onChildAdded(dataSnapshot, prevKey);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final Vaccination removedVaccination = dataSnapshot.getValue(Vaccination.class);
                if (removedVaccination != null) {
                    mDates.remove(removedVaccination.getDoseDatabaseKey());
                    refresh();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_vaccinations_download, Toast.LENGTH_SHORT).show();
            }
        };

        // set listener to query
        mVaccinationsQuery.addChildEventListener(mVaccinationsListener);
    }

    private void initDueDatesListener() {
        // define database query
        final String masterTable = getResources().getString(R.string.dataTable);
        final String dueDatesTable = getResources().getString(R.string.dueDatesTable);
        final String patientDatabaseKeyField = getContext().getString(R.string.patientDatabaseKeyField);
        mDueDatesQuery = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dueDatesTable)
                .orderByChild(patientDatabaseKeyField)
                .equalTo(mPatientDatabaseKey);

        // define listener
        mDueDatesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                final DueDate addedDueDate = dataSnapshot.getValue(DueDate.class);
                if (addedDueDate != null) {
                    mDates.put(addedDueDate.getVaccineDatabaseKey(), addedDueDate);
                    refresh();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevKey) {
                onChildAdded(dataSnapshot, prevKey);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final DueDate removedDueDate = dataSnapshot.getValue(DueDate.class);
                if (removedDueDate != null) {
                    mDates.remove(removedDueDate.getVaccineDatabaseKey());
                    refresh();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_due_dates_download, Toast.LENGTH_SHORT).show();
            }
        };

        // set listener to query
        mDueDatesQuery.addChildEventListener(mDueDatesListener);
    }

}
