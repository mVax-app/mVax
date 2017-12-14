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
package mhealth.mvax.records.details.patient.modify.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.patient.modify.ModifiableRecordFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for editing existing record patient data
 */

public class EditPatientDataFragment extends ModifiableRecordFragment {

    //================================================================================
    // Static methods
    //================================================================================

    public static EditPatientDataFragment newInstance() {
        return new EditPatientDataFragment();
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_record_details, container, false);
        mInflater = inflater;

        mNewRecord = (Record) getArguments().get("record");
        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        mDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(masterTable)
                .child(recordTable)
                .child(mNewRecord.getDatabaseId());
        renderListView(view);

        return view;
    }

    @Override
    public void onBack() {
    }

}
