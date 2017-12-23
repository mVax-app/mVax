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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import mhealth.mvax.records.utilities.FirebaseJobs;
import mhealth.mvax.records.utilities.RecordJobs;
import mhealth.mvax.records.views.detail.Detail;

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
    private ChildEventListener mPatientListener;
    private ChildEventListener mGuardianListener;
    private PatientDataAdapter mAdapter;
    private Patient mPatient;
    private Guardian mGuardian;
    private DatabaseReference mPatientRef;
    private DatabaseReference mGuardianRef;

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

        //        mRecord = (Record) getArguments().getSerializable("record");
//        render();
        return mView;
    }

    @Override
    public void onDestroyView() {
        mPatientRef.removeEventListener(mPatientListener);
        mGuardianRef.removeEventListener(mGuardianListener);
        super.onDestroyView();
    }


    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Initialize record details; this method should only be called once per
     * fragment instance
     */
    public void render() {
        setRecordName();

        LinkedHashMap<String, List<Detail>> sectionedDetails = RecordJobs.getSectionedDetails(getContext(), mPatient, mGuardian);
        mAdapter = new ViewPatientDataAdapter(getContext(), sectionedDetails);


        ListView detailsListView = mView.findViewById(R.id.details_list_view);
        detailsListView.setAdapter(mAdapter);

        addEditButton(detailsListView);
        renderDeleteButton(detailsListView);
    }

    /**
     * Update record details after they have already been initialized
     * via renderRecordDetails()
     *
     * @param updatedPerson contains the patient data with which to update details
     */
    public void update(Person updatedPerson) {
        if (updatedPerson instanceof Patient) {
            mPatient = (Patient) updatedPerson;
            setRecordName();
        } else if (updatedPerson instanceof Guardian) {
            mGuardian = (Guardian) updatedPerson;
        }
        LinkedHashMap<String, List<Detail>> sectionedDetails = RecordJobs.getSectionedDetails(getContext(), mPatient, mGuardian);
        mAdapter.refresh(sectionedDetails);
    }

    //================================================================================
    // Private methods
    //================================================================================

    private void initPatientListener(String databaseKey) {
        mPatientListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mPatient = dataSnapshot.getValue(Patient.class);
                assert mPatient != null;
                initGuardianListener(mPatient.getGuardianDatabaseKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Patient updatedPatient = dataSnapshot.getValue(Patient.class);
                update(updatedPatient);
                Toast.makeText(getActivity(), R.string.patient_update, Toast.LENGTH_SHORT).show();
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

        String masterTable = getResources().getString(R.string.dataTable);
        String patientTable = getResources().getString(R.string.patientTable);

        mPatientRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(patientTable);

        mPatientRef
                .orderByKey()
                .equalTo(databaseKey)
                .addChildEventListener(mPatientListener);
    }

    private void initGuardianListener(String databaseKey) {
        mGuardianListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mGuardian = dataSnapshot.getValue(Guardian.class);
                render();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Guardian updatedGuardian = dataSnapshot.getValue(Guardian.class);
                update(updatedGuardian);
                Toast.makeText(getActivity(), R.string.guardian_update, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // TODO test
                // handled by mPatientListener
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO test
                Toast.makeText(getActivity(), R.string.unsuccessful_guardian_download, Toast.LENGTH_SHORT).show();
            }
        };

        String masterTable = getResources().getString(R.string.dataTable);
        String guardianTable = getResources().getString(R.string.guardianTable);

        mGuardianRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(guardianTable);

        mGuardianRef
                .orderByKey()
                .equalTo(databaseKey)
                .addChildEventListener(mGuardianListener);
    }


    private void setRecordName() {
        TextView recordNameTextView = mView.findViewById(R.id.record_details_title);
        recordNameTextView.setText(mPatient.getName(getContext()));
    }

    private void addEditButton(ListView vaccineListView) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Button editButton = (Button) inflater.inflate(R.layout.button_edit_record, null);

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

    private void renderDeleteButton(ListView vaccineListView) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Button deleteButton = (Button) inflater.inflate(R.layout.button_delete_record, null);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptForRecordDelete();
            }
        });
        vaccineListView.addHeaderView(deleteButton);
    }

    private void promptForRecordDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.modal_record_delete_title);
        builder.setMessage(R.string.modal_record_delete_message);
        builder.setPositiveButton(getResources().getString(R.string.modal_new_dosage_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCurrentRecord();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.modal_new_dosage_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void deleteCurrentRecord() {
        FirebaseJobs.deleteRecord(mPatient);
        // segue out of patient detail handled by Firebase listener
    }

}
