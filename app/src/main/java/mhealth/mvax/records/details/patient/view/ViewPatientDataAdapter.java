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
package mhealth.mvax.records.details.patient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 *         Adapter for rendering the ListView for viewing patient details
 */

public class ViewPatientDataAdapter extends PatientDataAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private LayoutInflater mInflater;

    //================================================================================
    // Constructors
    //================================================================================

    ViewPatientDataAdapter(Context context, LinkedHashMap<String, List<Detail>> sectionedData) {
        super(sectionedData);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View getView(int position, View rowView, ViewGroup viewGroup) {
        int rowType = getItemViewType(position);

        TextView fieldView;
        EditText valueView;

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
                fieldView.setText(mDataSource.get(position).getLabel());
                valueView.setText(mDataSource.get(position).getStringValue());
                valueView.setFocusable(false);
                break;
        }

        return rowView;
    }
}
