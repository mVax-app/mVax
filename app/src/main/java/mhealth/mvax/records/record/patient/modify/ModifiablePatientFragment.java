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

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.record.RecordFragment;
import mhealth.mvax.records.utilities.AlgoliaUtilities;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 * <p>
 * Abstract class for modifying Patient details, either newly created or
 * already existing
 */
public abstract class ModifiablePatientFragment extends Fragment {

    private static final String ALGOLIA_INDEX = "patients";

    protected Patient mPatient;
    protected DatabaseReference mPatientRef;
    protected ModifyPatientAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatabaseRefs();
    }

    private void initDatabaseRefs() {
        final String masterTable = getResources().getString(R.string.dataTable);
        final String patientTable = getResources().getString(R.string.patientTable);
        mPatientRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(patientTable);
    }

    protected void setTitle(View view, int stringId) {
        final TextView title = view.findViewById(R.id.record_details_tab_title);
        title.setText(stringId);
    }

    protected void renderListView(RecyclerView detailsList) {
        mAdapter = new ModifyPatientAdapter(mPatient.getDetails());
        detailsList.setAdapter(mAdapter);
        detailsList.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    protected void initSaveButton(final Button button) {
        button.setBackgroundResource(R.drawable.button_save);
        button.setText(R.string.save_record_button);
        button.setOnClickListener(v -> saveRecord());
    }

    protected void refreshDetails() {
        mAdapter.refresh(mPatient.getDetails());
    }

    private void saveRecord() {
        if (noEmptyRequiredFields()) {
            AlgoliaUtilities.saveObject(getActivity(), mPatient, this::saveToDatabase);
        }
    }

    private boolean noEmptyRequiredFields() {
        boolean noEmptyRequiredFields = true;
        final ArrayList<EditText> requiredFields = new ArrayList<>(mAdapter.getRequiredFields());
        for (EditText field : requiredFields) {
            if (field.getText().toString().isEmpty()) {
                field.setError(getString(R.string.empty_field));
                field.requestFocus();
                noEmptyRequiredFields = false;
            }
        }
        return noEmptyRequiredFields;
    }

    private void saveToDatabase() {
        mPatientRef
                .child(mPatient.getDatabaseKey())
                .setValue(mPatient, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        viewRecord();
                        Toast.makeText(getActivity(), R.string.patient_save_notification, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.patient_save_fail, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void viewRecord() {
        // in case of create, pop "Edit -> Search" from back stack and commit it
        // in case of edit, pop "Edit -> Record" from back stack and commit it
        getActivity().getFragmentManager().popBackStack();

        // in case of create, back stack is empty, so nothing happens
        // in cse of edit, pop "Record -> Search" from back stack and commit it
        getActivity().getFragmentManager().popBackStack();

        // commit "Search -> Record", adding "Record -> Search" to back stack
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frame, RecordFragment.newInstance(mPatient.getDatabaseKey()))
                .addToBackStack(null)
                .commit();
    }

}
