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
package mhealth.mvax.records.details.patient.modify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.search.SearchFragment;
import mhealth.mvax.records.utilities.RecordJobs;
import mhealth.mvax.records.views.detail.Detail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Abstract class for modifying a record, either newly created or
 *         existing
 */

public abstract class ModifiableRecordFragment extends Fragment {

    protected LayoutInflater mInflater;

    protected DatabaseReference mPatientDatabaseRef;
    protected DatabaseReference mGuardianDatabaseRef;

    protected Patient mPatient;
    protected Guardian mGuardian;

    protected ModifyPatientAdapter mAdapter;

    protected ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String masterTable = getResources().getString(R.string.dataTable);
        String patientTable = getResources().getString(R.string.patientTable);
        String guardianTable = getResources().getString(R.string.guardianTable);

        mPatientDatabaseRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(masterTable)
                .child(patientTable);

        mGuardianDatabaseRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(masterTable)
                .child(guardianTable);
    }

    @Override
    public void onDestroy() {
        onBack();
        super.onDestroy();
    }

    protected void renderListView(View view) {

        LinkedHashMap<String, List<Detail>> sectionedDetails = RecordJobs.getSectionedDetails(getContext(), mPatient, mGuardian);


        mListView = view.findViewById(R.id.details_list_view);
        mAdapter = new ModifyPatientAdapter(getContext(), sectionedDetails);
        mListView.setAdapter(mAdapter);
        Button saveButton = (Button) mInflater.inflate(R.layout.save_record_button, null);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO maybe use firebase jobs
                mPatientDatabaseRef.child(mPatient.getDatabaseKey()).setValue(mPatient);
                mGuardianDatabaseRef.child(mGuardian.getDatabaseKey()).setValue(mGuardian);

                DetailFragment recordFrag = DetailFragment.newInstance(mPatient.getDatabaseKey());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new SearchFragment())
                        .addToBackStack(null)
                        .commit();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, recordFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        mListView.addHeaderView(saveButton);

    }

    public abstract void onBack();
}
