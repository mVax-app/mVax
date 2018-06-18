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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.record.patient.modify.ModifiablePatientFragment;

/**
 * @author Robert Steilberg
 * <p>
 * Fragment for editing existing record patient data
 */
public class EditPatientFragment extends ModifiablePatientFragment {

    private static final String ALGOLIA_INDEX = "patients";

    private View mView;
    private ChildEventListener mPatientListener;

    public static EditPatientFragment newInstance(Patient patient) {
        final EditPatientFragment newInstance = new EditPatientFragment();
        final Bundle args = new Bundle();
        args.putSerializable("patient", patient);
        newInstance.setArguments(args);
        return newInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_record_details, container, false);
        setTitle(mView, R.string.edit_record_title);

        mPatient = (Patient) getArguments().getSerializable("patient");
        initPatientListener();

        initSaveButton(mView.findViewById(R.id.header_button));
        renderListView(mView.findViewById(R.id.details_list));
        initDeleteButton();
        return mView;
    }

    @Override
    public void onDestroyView() {
        destroyListener();
        super.onDestroyView();
    }

    private void initPatientListener() {
        mPatientListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                // this should never happen
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                mPatient = dataSnapshot.getValue(Patient.class);
                refreshDetails();
                Toast.makeText(getActivity(), R.string.patient_update_notification, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity(), R.string.patient_delete_notification, Toast.LENGTH_SHORT).show();
                exit();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // this should never happen
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.patient_download_fail, Toast.LENGTH_SHORT).show();
            }
        };

        mPatientRef
                .orderByKey()
                .equalTo(mPatient.getDatabaseKey())
                .addChildEventListener(mPatientListener);
    }

    private void initDeleteButton() {
        final Button deleteButton = mView.findViewById(R.id.footer_button);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setBackgroundResource(R.drawable.button_delete);
        deleteButton.setText(R.string.delete_record_button);
        deleteButton.setOnClickListener(view -> promptForRecordDelete());
    }

    private void promptForRecordDelete() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.modal_record_delete_title);
        builder.setMessage(R.string.modal_record_delete_message);
        builder.setPositiveButton(getResources().getString(R.string.modal_confirm), (dialog, which) -> deleteCurrentRecord());
        builder.setNegativeButton(getResources().getString(R.string.modal_cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void deleteCurrentRecord() {
        destroyListener(); // prevent onChildRemoved action from firing before listener

        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.configTable))
                .child(getString(R.string.algoliaTable))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<Map<String, String>> t = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> configVars = dataSnapshot.getValue(t);
                        if (configVars != null) {
                            String applicationId = configVars.get("application_id");
                            String apiKey = configVars.get("api_key");
                            Client algoliaClient = new Client(applicationId, apiKey);
                            Index index = algoliaClient.getIndex(ALGOLIA_INDEX);
                            // searching now possible, render the views




                            index.deleteObjectAsync(mPatient.getDatabaseKey(), (jsonObject, e) -> {
                                String f = "";
                            });


                        } else {
                            Toast.makeText(getActivity(), "Unable to init search index", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), R.string.search_init_fail, Toast.LENGTH_SHORT).show();
                    }
                });




        mPatientRef.child(mPatient.getDatabaseKey()).setValue(null).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), R.string.patient_delete_notification, Toast.LENGTH_SHORT).show();
                exit();
            } else {
                Toast.makeText(getActivity(), R.string.patient_delete_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void destroyListener() {
        mPatientRef
                .orderByKey()
                .equalTo(mPatient.getDatabaseKey())
                .removeEventListener(mPatientListener);
    }

    private void exit() {
        // pop "Edit -> Record" from back stack and commit it
        getActivity().getFragmentManager().popBackStack();
        // pop "Record -> Search" from back stack and commit it
        getActivity().getFragmentManager().popBackStack();
    }

}
