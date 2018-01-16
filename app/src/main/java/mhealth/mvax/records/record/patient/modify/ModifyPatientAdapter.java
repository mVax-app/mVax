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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.detail.Detail;
import mhealth.mvax.records.record.patient.PatientDetailsAdapter;

/**
 * @author Robert Steilberg
 *         <p>
 *         Adapter that allows users to edit record details
 */
public class ModifyPatientAdapter extends PatientDetailsAdapter {

    //================================================================================
    // Constructors
    //================================================================================

    ModifyPatientAdapter(Context context, List<Detail> details) {
        super(context, details);
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View getView(final int position, View rowView, ViewGroup viewGroup) {
        rowView = mInflater.inflate(R.layout.list_item_record_detail, viewGroup, false);

        final TextView fieldView = rowView.findViewById(R.id.textview_field);
        final EditText valueView = rowView.findViewById(R.id.textview_value);

        // perform any setup operations defined by the Detail type
        mDataSource.get(position).configureValueView(valueView);

        // set field label, hint, and value
        fieldView.setText(mDataSource.get(position).getLabelStringId());
        valueView.setHint(mDataSource.get(position).getHintStringId());
        valueView.setText(mDataSource.get(position).getStringValue());

        // attach listeners to row view
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataSource.get(position).getValueViewListener(valueView);
            }
        });
        valueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataSource.get(position).getValueViewListener(valueView);
            }
        });

        if (position == mDataSource.size() - 1) { // last field has done button
            valueView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else { // otherwise has tab button to shift focus to next field in ListView
            valueView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }

        // place cursor at end of text
        valueView.setSelection(valueView.getText().length());
        return rowView;
    }

}
