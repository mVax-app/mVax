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
package mhealth.mvax.records.details.patient.modify.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.details.patient.modify.ModifiableRecordFragment;
import mhealth.mvax.records.search.SearchFragment;
import mhealth.mvax.records.utilities.RecordJobs;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for editing existing record patient data
 */

public class EditPatientFragment extends ModifiableRecordFragment {

    private ChildEventListener mPatientListener;
    private ChildEventListener mGuardianListener;
    private View mView;

    //================================================================================
    // Static methods
    //================================================================================

    public static EditPatientFragment newInstance(Patient patient, Guardian guardian) {
        EditPatientFragment newInstance = new EditPatientFragment();
        Bundle args = new Bundle();
        args.putSerializable("patient", patient);
        args.putSerializable("guardian", guardian);
        newInstance.setArguments(args);
        return newInstance;
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_record_details, container, false);
        mInflater = inflater;


        TextView recordName = mView.findViewById(R.id.record_details_title);
        recordName.setText(R.string.edit_record_title);

        mPatient = (Patient) getArguments().getSerializable("patient");
        mGuardian = (Guardian) getArguments().getSerializable("guardian");


        // PUT IN ABSTRACT CLASS

//        String masterTable = getResources().getString(R.string.masterTable);
//        String patientTable = getResources().getString(R.string.patientTable);
//        String guardianTable = getResources().getString(R.string.guardianTable);
//
//        mPatientDatabaseRef = FirebaseDatabase
//                .getInstance()
//                .getReference()
//                .child(masterTable)
//                .child(patientTable);
//
//        mGuardianDatabaseRef = FirebaseDatabase
//                .getInstance()
//                .getReference()
//                .child(masterTable)
//                .child(guardianTable);
        // ================================================================

        initPatientListener();
        initGuardianListener();

        renderListView(mView);

        return mView;
    }

    @Override
    public void onDestroyView() {
        mPatientDatabaseRef.removeEventListener(mPatientListener);
        mGuardianDatabaseRef.removeEventListener(mGuardianListener);
        super.onDestroyView();
    }

    @Override
    public void onBack() {
    }


    private void initPatientListener() {
        mPatientListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mPatient = dataSnapshot.getValue(Patient.class);
                mAdapter.refresh(RecordJobs.getSectionedDetails(getContext(), mPatient, mGuardian));
                Toast.makeText(getActivity(), R.string.patient_update, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // pop "-> DetailFragment" from back stack
                getActivity().getSupportFragmentManager().popBackStack();
                // pop "-> SearchFragment" from back stack
                getActivity().getSupportFragmentManager().popBackStack();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, SearchFragment.newInstance())
                        .commit();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_patient_download, Toast.LENGTH_SHORT).show();
            }
        };
        mPatientDatabaseRef
                .orderByKey()
                .equalTo(mPatient.getDatabaseKey())
                .addChildEventListener(mPatientListener);
    }

    private void initGuardianListener() {
        mGuardianListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mGuardian = dataSnapshot.getValue(Guardian.class);
                mAdapter.refresh(RecordJobs.getSectionedDetails(getContext(), mPatient, mGuardian));
                Toast.makeText(getActivity(), R.string.guardian_update, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // handled by mPatientListener onChildRemoved()
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.unsuccessful_guardian_download, Toast.LENGTH_SHORT).show();
            }
        };
        mGuardianDatabaseRef
                .orderByKey()
                .equalTo(mGuardian.getDatabaseKey())
                .addChildEventListener(mGuardianListener);
    }


//    private void initPatientListener(String databaseKey) {
//        mPatientListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                mPatient = dataSnapshot.getValue(Patient.class);
//                assert mPatient != null;
//                initGuardianListener(mPatient.getGuardianDatabaseKey());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Patient updatedPatient = dataSnapshot.getValue(Patient.class);
//                // TODO
////                update(updatedPatient);
//                Toast.makeText(getActivity(), R.string.patient_update, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                // TODO fix
//                getActivity().onBackPressed(); // transition back to search
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getActivity(), R.string.unsuccessful_record_download, Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//        String masterTable = getResources().getString(R.string.masterTable);
//        String patientTable = getResources().getString(R.string.patientTable);
//        db.child(masterTable).child(patientTable)
//                .orderByKey()
//                .equalTo(databaseKey)
//                .addChildEventListener(mPatientListener);
//    }
//
//    private void initGuardianListener(String databaseKey) {
//        mGuardianListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                mGuardian = dataSnapshot.getValue(Guardian.class);
//                renderListView();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Guardian updatedGuardian = dataSnapshot.getValue(Guardian.class);
////                update(updatedGuardian);
//                Toast.makeText(getActivity(), R.string.guardian_update, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                // TODO test
//                // handled by mPatientListener
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // TODO test
//                Toast.makeText(getActivity(), R.string.unsuccessful_guardian_download, Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//        String masterTable = getResources().getString(R.string.masterTable);
//        String guardianTable = getResources().getString(R.string.guardianTable);
//        db.child(masterTable).child(guardianTable)
//                .orderByKey()
//                .equalTo(databaseKey)
//                .addChildEventListener(mGuardianListener);
//    }





}
