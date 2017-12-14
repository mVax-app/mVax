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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.search.SearchFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         Abstract class for modifying a record, either newly created or
 *         existing
 */

public abstract class ModifiableRecordFragment extends Fragment {

    protected LayoutInflater mInflater;

    protected DatabaseReference mDatabase;

    protected Record mNewRecord;

    @Override
    public void onDestroy() {
        onBack();
        super.onDestroy();
    }

    protected void renderListView(View view) {
        TextView recordName = view.findViewById(R.id.record_details_title);
        recordName.setText(R.string.new_record_title);

        final ListView detailsListView = view.findViewById(R.id.details_list_view);
        final EditPatientDataAdapter adapter = new EditPatientDataAdapter(getContext(), mNewRecord.getSectionedAttributes(getContext()));
        detailsListView.setAdapter(adapter);

        Button saveButton = (Button) mInflater.inflate(R.layout.save_record_button, null);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.setValue(mNewRecord);

                DetailFragment recordFrag = DetailFragment.newInstance();
                Bundle args = new Bundle();
                args.putString("recordId", mNewRecord.getDatabaseId());
                recordFrag.setArguments(args);

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
        detailsListView.addFooterView(saveButton);

    }

    public abstract void onBack();
}
