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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.details.RecordTab;
import mhealth.mvax.records.details.patient.PatientDataAdapter;
import mhealth.mvax.records.details.patient.modify.edit.EditPatientDataFragment;
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
        // kill listeners
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

        LinkedHashMap<String, List<Detail>> details = new LinkedHashMap<>();

        details.put(getContext().getString(R.string.patient_detail_section_title), mPatient.getDetails(getContext()));
        details.put(getContext().getString(R.string.guardian_detail_section_title), mGuardian.getDetails(getContext()));

        mAdapter = new ViewPatientDataAdapter(getContext(), details);
        ListView detailsListView = mView.findViewById(R.id.details_list_view);
        detailsListView.setAdapter(mAdapter);

        addEditButton(detailsListView);
        addDeleteButton(detailsListView);
    }

    /**
     * Update record details after they have already been initialized
     * via renderRecordDetails()
     *
     * @param patient contains the patient data with which to update details
     */
    public void update(Patient patient) {

//        mRecord = record;
//        setRecordName();
//        mAdapter.refresh(record.getSectionedAttributes(getContext()));
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
                // TODO
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // TODO test this
                getActivity().onBackPressed(); // transition back to search
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.unsuccessful_record_download, Toast.LENGTH_SHORT).show();
            }
        };

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String masterTable = getResources().getString(R.string.masterTable);
        String patientTable = getResources().getString(R.string.patientTable);
        db.child(masterTable).child(patientTable)
                .orderByKey()
                .equalTo(databaseKey)
                .addChildEventListener(mPatientListener);
    }

    private void initGuardianListener(String databaseKey) {
        mGuardianListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mGuardian = dataSnapshot.getValue(Guardian.class);
                // TODO test for patient with no guardian
                render();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // TODO
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

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String masterTable = getResources().getString(R.string.masterTable);
        String guardianTable = getResources().getString(R.string.guardianTable);
        db.child(masterTable).child(guardianTable)
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
                DetailFragment onBackFrag = DetailFragment.newInstance();
                Bundle args = new Bundle();
                args.putString("recordId", mPatient.getDatabaseKey());
                onBackFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, onBackFrag)
                        .addToBackStack(null)
                        .commit();

                // transition to edit patient data fragment
                EditPatientDataFragment editDataFrag = EditPatientDataFragment.newInstance();
                args = new Bundle();
                args.putSerializable("record", mPatient);
                editDataFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, editDataFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        vaccineListView.addHeaderView(editButton);
    }

    private void addDeleteButton(ListView vaccineListView) {
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
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        db.child(masterTable).child(recordTable).child(mPatient.getDatabaseKey()).setValue(null);
        getActivity().onBackPressed(); // deleted the current record, so end the activity
    }

}
