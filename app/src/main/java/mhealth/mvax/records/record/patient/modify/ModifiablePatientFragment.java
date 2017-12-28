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
package mhealth.mvax.records.record.patient.modify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.Person;
import mhealth.mvax.records.record.RecordFragment;
import mhealth.mvax.records.record.patient.detail.Detail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Abstract class for modifying Patient or Guardian details,
 *         either newly created or already existing
 */
public abstract class ModifiablePatientFragment extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    protected ModifyPatientAdapter mAdapter;
    protected ListView mListView;

    protected Patient mPatient;
    protected DatabaseReference mPatientRef;

    protected Guardian mGuardian;
    protected DatabaseReference mGuardianRef;

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatabaseRefs();
    }

    //================================================================================
    // Protected methods
    //================================================================================

    protected void setFragmentTitle(View view, int stringId) {
        final TextView fragmentTitle = view.findViewById(R.id.record_details_title);
        fragmentTitle.setText(stringId);
    }

    /**
     * Render an editable ListView containing details from mPatient and
     * mGuardian with a button to save any changes
     */
    protected void renderListView(View view) {
        final LinkedHashMap<Integer, List<Detail>> sectionedDetails =
                Person.getSectionedDetails(mPatient, mGuardian);
        mListView = view.findViewById(R.id.details_list_view);
        mAdapter = new ModifyPatientAdapter(getContext(), sectionedDetails);
        mListView.setAdapter(mAdapter);
        addSaveButton();
    }

    /**
     * Update the UI when either mPatient or mGuardian has changed
     */
    protected void update() {
        mAdapter.refresh(Person.getSectionedDetails(mPatient, mGuardian));
    }

    //================================================================================
    // Private methods
    //================================================================================

    private void initDatabaseRefs() {
        // initialize database references
        final String masterTable = getResources().getString(R.string.dataTable);
        final String patientTable = getResources().getString(R.string.patientTable);
        final String guardianTable = getResources().getString(R.string.guardianTable);
        mPatientRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(patientTable);
        mGuardianRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(guardianTable);
    }

    private void addSaveButton() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final Button saveButton = (Button) inflater.inflate(R.layout.button_save_record, mListView, false);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecord();
            }
        });
        mListView.addHeaderView(saveButton);
    }

    private void saveRecord() {
        // save Patient object to database
        mPatientRef.child(mPatient.getDatabaseKey()).setValue(mPatient, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                // on Patient save completion, save Guardian object to database
                mGuardianRef.child(mGuardian.getDatabaseKey()).setValue(mGuardian, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // on Guardian save completion, transition to view record mode (RecordFragment)
                        viewRecord();
                    }
                });
            }
        });
    }

    private void viewRecord() {
        // in case of create, pop "Edit -> Search" from back stack and commit it
        // in case of edit, pop "Edit -> Record" from back stack and commit it
        getActivity().getSupportFragmentManager().popBackStack();

        // in case of create, back stack is empty, so nothing happens
        // in cse of edit, pop "Record -> Search" from back stack and commit it
        getActivity().getSupportFragmentManager().popBackStack();

        // commit "Search -> Record", adding "Record -> Search" to back stack
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, RecordFragment.newInstance(mPatient.getDatabaseKey()))
                .addToBackStack(null)
                .commit();
    }

}
