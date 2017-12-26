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
package mhealth.mvax.records.details.patient.view;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.Person;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.details.RecordTab;
import mhealth.mvax.records.details.patient.PatientDataAdapter;
import mhealth.mvax.records.details.patient.modify.edit.EditPatientFragment;
import mhealth.mvax.records.utilities.RecordJobs;
import mhealth.mvax.records.details.patient.detail.Detail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for managing an mVax record's patient details;
 *         takes a record as an argument, wrapped in a Bundle
 */

public class PatientDataTab extends Fragment implements RecordTab {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;
    private PatientDataAdapter mAdapter;

    private Patient mPatient;
    private DatabaseReference mPatientRef;
    private ChildEventListener mPatientListener;

    private Guardian mGuardian;
    private DatabaseReference mGuardianRef;
    private ChildEventListener mGuardianListener;

    //================================================================================
    // Static methods
    //================================================================================

    public static PatientDataTab newInstance(String databaseKey) {
        PatientDataTab newInstance = new PatientDataTab();
        Bundle args = new Bundle();
        args.putString("databaseKey", databaseKey);
        newInstance.setArguments(args);
        return newInstance;
    }

    //================================================================================
    //Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_record_details, container, false);
        initPatientListener(getArguments().getString("databaseKey"));
        return mView;
    }

    @Override
    public void onDestroyView() {
        mPatientRef.orderByKey().equalTo(mPatient.getDatabaseKey()).removeEventListener(mPatientListener);
        mGuardianRef.orderByKey().equalTo(mGuardian.getDatabaseKey()).removeEventListener(mGuardianListener);
        super.onDestroyView();
    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Initialize record details; this method should only be called once per
     * fragment instance
     */
    @Override
    public void render() {

        LinkedHashMap<String, List<Detail>> sectionedDetails = RecordJobs.getSectionedDetails(getContext(), mPatient, mGuardian);
        mAdapter = new ViewPatientDataAdapter(getContext(), sectionedDetails);

        ListView detailsListView = mView.findViewById(R.id.details_list_view);
        detailsListView.setAdapter(mAdapter);

        addEditButton(detailsListView);
    }

    @Override
    public void refresh() {
        if (mAdapter == null) {
            String f = "";
        }
        setRecordName();
        LinkedHashMap<String, List<Detail>> sectionedDetails = RecordJobs.getSectionedDetails(getContext(), mPatient, mGuardian);
        mAdapter.refresh(sectionedDetails);
    }

//    /**
//     * Update record details after they have already been initialized
//     * via renderRecordDetails()
//     *
//     * @param updatedPerson contains the patient data with which to update details
//     */
//    public void update(Person updatedPerson) {
//        if (updatedPerson instanceof Patient) {
//            mPatient = (Patient) updatedPerson;
//            setRecordName();
//        } else if (updatedPerson instanceof Guardian) {
//            mGuardian = (Guardian) updatedPerson;
//        }
//        LinkedHashMap<String, List<Detail>> sectionedDetails = RecordJobs.getSectionedDetails(getContext(), mPatient, mGuardian);
//        mAdapter.refresh(sectionedDetails);
//    }

    //================================================================================
    // Private methods
    //================================================================================

    private void initPatientListener(final String databaseKey) {
        // define database ref
        final String masterTable = getResources().getString(R.string.dataTable);
        final String patientTable = getResources().getString(R.string.patientTable);
        mPatientRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(patientTable);

        // define listener
        mPatientListener = new ChildEventListener() {

            boolean initialAdd = false;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mPatient = dataSnapshot.getValue(Patient.class);
                setRecordName();
                if (!initialAdd && mPatient != null) { // debounce
                    initGuardianListener(mPatient.getGuardianDatabaseKey());
                    initialAdd = true;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mPatient = dataSnapshot.getValue(Patient.class);
                Toast.makeText(getActivity(), R.string.patient_update, Toast.LENGTH_SHORT).show();
                refresh();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                getActivity().onBackPressed(); // transition back to search
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_patient_download, Toast.LENGTH_SHORT).show();
            }
        };

        // set listener to ref
        mPatientRef
                .orderByKey()
                .equalTo(databaseKey)
                .addChildEventListener(mPatientListener);
    }

    private void initGuardianListener(String databaseKey) {
        // define database ref
        final String masterTable = getResources().getString(R.string.dataTable);
        final String guardianTable = getResources().getString(R.string.guardianTable);
        mGuardianRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(guardianTable);

        // define listener
        mGuardianListener = new ChildEventListener() {

            boolean initialAdd = false;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mGuardian = dataSnapshot.getValue(Guardian.class);
                if (!initialAdd && mGuardian != null) { // debounce and check for
                    render();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mGuardian = dataSnapshot.getValue(Guardian.class);
                Toast.makeText(getActivity(), R.string.guardian_update, Toast.LENGTH_SHORT).show();
                refresh();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // handled by mPatientListener
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_guardian_download, Toast.LENGTH_SHORT).show();
            }
        };

        // set listener to ref
        mGuardianRef
                .orderByKey()
                .equalTo(databaseKey)
                .addChildEventListener(mGuardianListener);
    }

    private void setRecordName() {
        final TextView recordNameTextView = mView.findViewById(R.id.record_details_title);
        recordNameTextView.setText(mPatient.getName(getContext()));
    }

    private void addEditButton(ListView vaccineListView) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final Button editButton = (Button) inflater.inflate(R.layout.button_edit_record, null);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // pop "-> Search" from back stack
                getActivity().getSupportFragmentManager().popBackStack();

                // add "-> Detail" to back stack
                DetailFragment onBackFrag = DetailFragment.newInstance(mPatient.getDatabaseKey());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, onBackFrag)
                        .addToBackStack(null)
                        .commit();

                // transition to edit patient data fragment
                EditPatientFragment editDataFrag = EditPatientFragment.newInstance(mPatient, mGuardian);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, editDataFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        vaccineListView.addHeaderView(editButton);
    }

}
