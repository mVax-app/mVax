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
package mhealth.mvax.records.record.patient.modify.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.record.patient.modify.ModifiablePatientFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for editing existing record patient data
 */
public class EditPatientFragment extends ModifiablePatientFragment {

    //================================================================================
    // Properties
    //================================================================================

    private ChildEventListener mPatientListener;

    //================================================================================
    // Static methods
    //================================================================================

    public static EditPatientFragment newInstance(Patient patient) {
        final EditPatientFragment newInstance = new EditPatientFragment();
        final Bundle args = new Bundle();
        args.putSerializable("patient", patient);
        newInstance.setArguments(args);
        return newInstance;
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_record_details, container, false);
        setFragmentTitle(view, R.string.edit_record_title);

        mPatient = (Patient) getArguments().getSerializable("patient");
        initPatientListener();

        renderListView(view);
        addDeleteButton();
        return view;
    }

    @Override
    public void onDestroyView() {
        mPatientRef.orderByKey().equalTo(mPatient.getDatabaseKey()).removeEventListener(mPatientListener);
        super.onDestroyView();
    }

    //================================================================================
    // Private methods
    //================================================================================

    private void initPatientListener() {
        // define listener
        mPatientListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mPatient = dataSnapshot.getValue(Patient.class);
                update();
                Toast.makeText(getActivity(), R.string.patient_update, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity(), R.string.patient_delete, Toast.LENGTH_SHORT).show();
                // pop "Edit -> Record" from back stack and commit it
                getActivity().getFragmentManager().popBackStack();
                // pop "Record -> Search" from back stack and commit it
                getActivity().getFragmentManager().popBackStack();
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
                .equalTo(mPatient.getDatabaseKey())
                .addChildEventListener(mPatientListener);
    }

    private void addDeleteButton() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final Button deleteButton = (Button) inflater.inflate(R.layout.button_delete_record, mListView, false);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptForRecordDelete();
            }
        });
        mListView.addFooterView(deleteButton);
    }

    private void promptForRecordDelete() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.modal_record_delete_title);
        builder.setMessage(R.string.modal_record_delete_message);
        builder.setPositiveButton(getResources().getString(R.string.modal_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCurrentRecord();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.modal_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void deleteCurrentRecord() {
        mPatientRef.child(mPatient.getDatabaseKey()).setValue(null);
        // segue out of patient detail handled by mPatientListener
    }

}
