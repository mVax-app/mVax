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
package com.mvax.records.record.patient.modify;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import com.mvax.R;
import com.mvax.records.record.patient.detail.Detail;
import com.mvax.model.record.Patient;
import com.mvax.records.record.RecordFragment;
import com.mvax.records.utilities.AlgoliaUtilities;

/**
 * @author Robert Steilberg
 * <p>
 * Abstract class for modifying Patient details, either newly created or
 * already existing
 */
public abstract class ModifiablePatientFragment extends Fragment {

    private View mView;

    protected Patient mPatient;
    protected DatabaseReference mPatientRef;
    protected ModifyPatientAdapter mAdapter;
    protected AlgoliaUtilities mSearchEngine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatabaseRefs();
    }

    @Override
    @CallSuper
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_record_details, container, false);
        return mView;
    }

    private void initDatabaseRefs() {
        final String masterTable = getResources().getString(R.string.data_table);
        final String patientTable = getResources().getString(R.string.patient_table);
        mPatientRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(patientTable);
    }

    protected void setTitle(int stringId) {
        final TextView title = mView.findViewById(R.id.record_details_tab_title);
        title.setText(stringId);
    }

    protected void setTitle(String string) {
        final TextView title = mView.findViewById(R.id.record_details_tab_title);
        title.setText(string);
    }

    protected void renderListView(RecyclerView detailsList) {
        mAdapter = new ModifyPatientAdapter(getActivity(), mPatient.getDetails(getContext()));
        detailsList.setAdapter(mAdapter);
        detailsList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void initSaveButton(Button button) {
        button.setVisibility(View.VISIBLE);
        button.setBackgroundResource(R.drawable.button_save_record);
        button.setText(R.string.save_record_button);
        button.setOnClickListener(v -> saveRecord());
    }

    private void saveRecord() {
        if (noEmptyRequiredFields()) {
            showSpinner();
            saveRecordToDatabase();
        }
    }

    protected void showSpinner() {
        mView.findViewById(R.id.button_spinner).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.button_container).setVisibility(View.INVISIBLE);
    }

    protected void hideSpinner() {
        mView.findViewById(R.id.button_spinner).setVisibility(View.INVISIBLE);
        mView.findViewById(R.id.button_container).setVisibility(View.VISIBLE);
    }

    private boolean noEmptyRequiredFields() {
        boolean noEmptyRequiredFields = true;
        ArrayList<Detail> details = new ArrayList<>(mPatient.getDetails(getContext()));
        for (Detail detail : details) {
            if (detail.isRequired() && detail.getStringValue().isEmpty()) {
                detail.setError(true);
                noEmptyRequiredFields = false;
            } else {
                detail.setError(false);
            }
        }
        mAdapter.refresh(details);
        return noEmptyRequiredFields;
    }

    private void saveRecordToDatabase() {
        mPatientRef
                .child(mPatient.getDatabaseKey())
                .setValue(mPatient, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        mSearchEngine.saveObject(mPatient, this::viewRecord);
                    } else {
                        Toast.makeText(getActivity(), R.string.patient_save_fail, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void viewRecord() {
        hideSpinner();

        // dismiss soft keyboard
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
        }

        Toast.makeText(getActivity(), R.string.patient_save_notification, Toast.LENGTH_SHORT).show();

        // in case of create, pop "Edit -> Search" from back stack and commit it
        // in case of edit, pop "Edit -> Record" from back stack and commit it
        getActivity().getFragmentManager().popBackStack();

        // in case of create, back stack is empty, so nothing happens
        // in cse of edit, pop "Record -> Search" from back stack and commit it
        getActivity().getFragmentManager().popBackStack();

        // commit "Search -> Record", adding "Record -> Search" to back stack
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, RecordFragment.newInstance(mPatient.getDatabaseKey()))
                .addToBackStack(null)
                .commit();
    }

}
