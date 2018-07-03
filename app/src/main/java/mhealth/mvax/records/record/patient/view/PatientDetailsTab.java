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
package mhealth.mvax.records.record.patient.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.record.RecordTab;
import mhealth.mvax.records.record.patient.modify.edit.EditPatientFragment;

/**
 * @author Robert Steilberg
 * <p>
 * Fragment for viewing a record's patient details
 */
public class PatientDetailsTab extends Fragment implements RecordTab {

    private View mView;
    private ViewPatientAdapter mAdapter;

    private Patient mPatient;
    private Query mPatientRef;
    private ChildEventListener mPatientListener;

    public static PatientDetailsTab newInstance(String databaseKey) {
        final PatientDetailsTab newInstance = new PatientDetailsTab();
        final Bundle args = new Bundle();
        args.putString("databaseKey", databaseKey);
        newInstance.setArguments(args);
        return newInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_record_details, container, false);
        mView.findViewById(R.id.spinner).setVisibility(View.VISIBLE);
        initPatientListener(getArguments().getString("databaseKey"));
        return mView;
    }

    @Override
    public void onDestroyView() {
        mPatientRef.removeEventListener(mPatientListener);
        super.onDestroyView();
    }

    @Override
    public void render() {
        mView.findViewById(R.id.spinner).setVisibility(View.GONE);
        setRecordName();
        initButtons();

        mAdapter = new ViewPatientAdapter(mPatient.getDetails());
        final RecyclerView detailsList = mView.findViewById(R.id.details_list);
        detailsList.setHasFixedSize(true);
        detailsList.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsList.setAdapter(mAdapter);
    }

    @Override
    public void refresh() {
        setRecordName();
        mAdapter.refresh(mPatient.getDetails());
    }

    private void initPatientListener(final String databaseKey) {
        final String dataTable = getResources().getString(R.string.data_table);
        final String patientTable = getResources().getString(R.string.patient_table);
        mPatientRef = FirebaseDatabase.getInstance().getReference()
                .child(dataTable)
                .child(patientTable)
                .orderByKey()
                .equalTo(databaseKey);

        // define listener
        mPatientListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                mPatient = dataSnapshot.getValue(Patient.class);
                render();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                mPatient = dataSnapshot.getValue(Patient.class);
                Toast.makeText(getActivity(), R.string.patient_update_notification, Toast.LENGTH_SHORT).show();
                refresh();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity(), R.string.patient_delete_success, Toast.LENGTH_SHORT).show();
                // pop "Record -> Search" from back stack and commit it
                getActivity().getFragmentManager().popBackStack();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.patient_download_fail, Toast.LENGTH_SHORT).show();
            }
        };

        // set listener to ref
        mPatientRef.addChildEventListener(mPatientListener);
    }

    private void setRecordName() {
        final TextView recordNameTextView = mView.findViewById(R.id.record_details_tab_title);
        recordNameTextView.setText(mPatient.getName());
    }

    private void initButtons() {
        final Button editButton = mView.findViewById(R.id.header_button);
        editButton.setVisibility(View.VISIBLE);
        editButton.setBackgroundResource(R.drawable.button_edit);
        editButton.setText(R.string.edit_record_button);
        editButton.setOnClickListener(view -> {
//            // pop "Record -> Search" from back stack and commit it
//            getActivity().getFragmentManager().popBackStack();
//            // commit "Search -> Record, adding "Record -> Search to back stack
//            final RecordFragment onBackFrag = RecordFragment.newInstance(mPatient.getDatabaseKey());
//            getActivity().getFragmentManager().beginTransaction()
//                    .replace(R.id.frame, onBackFrag)
//                    .addToBackStack(null)
//                    .commit();
//            // commit "Record -> Edit", adding "Edit -> Record to back stack
            final EditPatientFragment editDataFrag = EditPatientFragment.newInstance(mPatient);
            getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.frame, editDataFrag)
                    .addToBackStack(null)
                    .commit();
        });
    }

}
