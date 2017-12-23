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
package mhealth.mvax.records.details.vaccine;

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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import mhealth.mvax.R;
import mhealth.mvax.model.record.DueDate;
import mhealth.mvax.model.record.Person;
import mhealth.mvax.model.record.Vaccination;
import mhealth.mvax.model.record.Vaccine;
import mhealth.mvax.records.details.RecordTab;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for managing an mVax record's vaccine schedule
 */

public class VaccineScheduleTab extends Fragment implements RecordTab {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;
    private VaccineAdapter mAdapter;
    private String mPatientDatbaseKey;
    private DatabaseReference mVaccineRef;
    private ChildEventListener mVaccineListener;
    private DatabaseReference mVaccinationsRef;
    private ChildEventListener mVaccinationsListener;
    private DatabaseReference mDueDatesRef;
    private ChildEventListener mDueDatesListener;
    private HashMap<String, Vaccine> mVaccines = new HashMap<>();
    private HashMap<String, Vaccination> mVaccinations = new HashMap<>();
    private HashMap<String, DueDate> mDueDates = new HashMap<>();
    // TODO may want to be linkedHashMap


    //================================================================================
    // Static methods
    //================================================================================

    public static VaccineScheduleTab newInstance(String patientDatabaseKey) {
        VaccineScheduleTab newInstance = new VaccineScheduleTab();

        Bundle args = new Bundle();
        args.putString("patientDatabaseKey", patientDatabaseKey);
        newInstance.setArguments(args);

        return newInstance;
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_vaccine_history, container, false);
        mPatientDatbaseKey = getArguments().getString("patientDatabaseKey");
        initVaccineListener();


        //        mRecord = (Record) getArguments().getSerializable("record");
//        render();
        return mView;
    }

    @Override
    public void onDestroyView() {
        mVaccineRef.removeEventListener(mVaccineListener);
        mVaccinationsRef.removeEventListener(mVaccinationsListener);
        mDueDatesRef.removeEventListener(mDueDatesListener);
        super.onDestroyView();
    }


    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Performs the initial render of the record's vaccine schedule
     * using the record passed in to the fragment as an argument
     */
    public void render() {
        ListView vaccineListView = mView.findViewById(R.id.vaccines_list_view);
        mAdapter = new VaccineAdapter(getContext(), mPatientDatbaseKey, mVaccines, mVaccinations, mDueDates);
        vaccineListView.setAdapter(mAdapter);
    }

    /**
     * Updates the view with an updated record's vaccine schedule
     *
     * @param updatedRecord the updated record containing the vaccine schedule
     */
    public void update(Person updatedRecord) {
//        mAdapter.refresh(updatedRecord);
    }

    private void initVaccineListener() {
        String masterTable = getResources().getString(R.string.dataTable);
        String vaccineTable = getResources().getString(R.string.vaccineTable);
        mVaccineRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(vaccineTable);
        


        mVaccineListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                Vaccine v = dataSnapshot.getValue(Vaccine.class);
                assert v != null;
                mVaccines.put(v.getDatabaseKey(), v);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevKey) {
                onChildAdded(dataSnapshot, prevKey);
//                mAdapter.refresh(mVaccines);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Vaccine removedVaccine = dataSnapshot.getValue(Vaccine.class);
                assert removedVaccine != null;
                mVaccines.remove(removedVaccine.getDatabaseKey());
                //                mAdapter.refresh(mVaccines);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_vaccines_download, Toast.LENGTH_SHORT).show();
            }
        };

        mVaccineRef.addChildEventListener(mVaccineListener);

        // TODO MAKE SURE THIS IS ONLY CALLED ONCE AND NOT ON SUBSEQUENT UPDATES/DELETES
        mVaccineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initVaccinationsListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_vaccines_download, Toast.LENGTH_SHORT).show();
            }
        });

    }
    
    private void initVaccinationsListener() {
        String masterTable = getResources().getString(R.string.dataTable);
        String vaccinationsTable = getResources().getString(R.string.vaccinationsTable);
        mVaccinationsRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(vaccinationsTable);

        mVaccinationsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                Vaccination v = dataSnapshot.getValue(Vaccination.class);
                assert v != null;
                mVaccinations.put(v.getDoseDatabaseKey(), v);
                if (mAdapter != null) {
                    // new vaccination added after initial render
                    mAdapter.refresh(mVaccinations);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevKey) {
                onChildAdded(dataSnapshot, prevKey);
                // TODO NOTIFYDATASETCHANGED
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Vaccination removedVaccination = dataSnapshot.getValue(Vaccination.class);
                assert removedVaccination != null;
                mVaccinations.remove(removedVaccination.getDoseDatabaseKey());
                mAdapter.refresh(mVaccinations);
                // TODO NOTIFYDATASETCHANGED
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_vaccinations_download, Toast.LENGTH_SHORT).show();
            }
        };

        // only want vaccinations for the current patient
        mVaccinationsRef
                .orderByChild("patientDatabaseKey")
                .equalTo(mPatientDatbaseKey)
                .addChildEventListener(mVaccinationsListener);

        // TODO MAKE SURE THIS IS ONLY CALLED ONCE AND NOT ON SUBSEQUENT UPDATES/DELETES
        mVaccinationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initDueDatesListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_vaccinations_download, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDueDatesListener() {
        String masterTable = getResources().getString(R.string.dataTable);
        String dueDatesTable = getResources().getString(R.string.dueDatesTable);
        mDueDatesRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dueDatesTable);

        mDueDatesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevKey) {
                DueDate d = dataSnapshot.getValue(DueDate.class);
                assert d != null;
                mDueDates.put(d.getVaccineDatabaseKey(), d);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevKey) {
                onChildAdded(dataSnapshot, prevKey);
//                mAdapter.refresh(mDueDates);
                // TODO NOTIFYDATASETCHANGED
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DueDate removedDueDate = dataSnapshot.getValue(DueDate.class);
                assert removedDueDate != null;
                mDueDates.remove(removedDueDate.getVaccineDatabaseKey());
//                mAdapter.refresh(mDueDates);
                // TODO NOTIFYDATASETCHANGED
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_due_dates_download, Toast.LENGTH_SHORT).show();
            }
        };

        // only want due dates for the current patient
        mDueDatesRef
                .orderByChild("patientDatabaseKey")
                .equalTo(mPatientDatbaseKey)
                .addChildEventListener(mDueDatesListener);

        // TODO MAKE SURE THIS IS ONLY CALLED ONCE AND NOT ON SUBSEQUENT UPDATES/DELETES
        mDueDatesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                render();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_due_dates_download, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
