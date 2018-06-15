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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.detail.Detail;
import mhealth.mvax.records.record.patient.PatientDetailsAdapterOld;

/**
 * @author Robert Steilberg
 *         <p>
 *         Adapter for rendering the ListView for viewing Patient and Guardian details
 */
public class ViewPatientAdapterOld extends PatientDetailsAdapterOld {

    //================================================================================
    // Constructors
    //================================================================================

    ViewPatientAdapterOld(Context context, List<Detail> details) {
        super(context, details);
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View getView(int position, View rowView, ViewGroup viewGroup) {
        rowView = mInflater.inflate(R.layout.list_item_record_detail, viewGroup, false);

        final TextView fieldView = rowView.findViewById(R.id.field);
        fieldView.setText(getItem(position).getLabelStringId()); // set field label

        final EditText valueView = rowView.findViewById(R.id.value);
        valueView.setText(getItem(position).getStringValue()); // set field value
        valueView.setFocusable(false); // EditText not editable in view record mode
        return rowView;
    }
}
