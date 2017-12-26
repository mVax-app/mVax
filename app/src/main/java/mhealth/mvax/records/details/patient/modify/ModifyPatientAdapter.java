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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.details.patient.detail.Detail;
import mhealth.mvax.records.details.patient.PatientDataAdapter;

/**
 * @author Robert Steilberg
 *         <p>
 *         Adapter that allows user interaction with fields to edit data;
 *         changes automatically trigger listeners that save data to the
 *         database
 */

public class ModifyPatientAdapter extends PatientDataAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private LayoutInflater mInflater;


    //================================================================================
    // Constructors
    //================================================================================

    ModifyPatientAdapter(Context context, LinkedHashMap<String, List<Detail>> sectionedData) {
        super(sectionedData);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View getView(final int position, View rowView, ViewGroup viewGroup) {
        int rowType = getItemViewType(position);

        final TextView fieldView;
        final EditText valueView;

        switch (rowType) {
            case TYPE_SECTION:
                rowView = mInflater.inflate(R.layout.list_item_record_detail_section, viewGroup, false);
                fieldView = rowView.findViewById(R.id.record_detail_separator);
                fieldView.setText(mHeaders.get(position));
                break;
            case TYPE_FIELD:
                rowView = mInflater.inflate(R.layout.list_item_record_detail, viewGroup, false);
                fieldView = rowView.findViewById(R.id.textview_field);
                valueView = rowView.findViewById(R.id.textview_value);

                // perform any setup operations defined by the Detail type
                mDataSource.get(position).configureValueView(valueView);

                // set values
                fieldView.setText(mDataSource.get(position).getLabel());
                valueView.setHint(mDataSource.get(position).getHint());
                valueView.setText(mDataSource.get(position).getStringValue());

                // attach listeners
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDataSource.get(position).valueViewListener(valueView);
                    }
                });
                valueView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDataSource.get(position).valueViewListener(valueView);
                    }
                });

                // set keyboard tab button
                if (position == mDataSource.size() - 1) {
                    valueView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                } else {
                    valueView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                }

                // place cursor at end of text
                valueView.setSelection(valueView.getText().length());
                break;
        }
        return rowView;
    }

}
