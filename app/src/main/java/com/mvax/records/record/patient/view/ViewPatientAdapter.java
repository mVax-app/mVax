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
package com.mvax.records.record.patient.view;

import android.support.annotation.NonNull;

import java.util.List;

import com.mvax.records.record.patient.PatientDetailsAdapter;
import com.mvax.records.record.patient.detail.Detail;

/**
 * @author Robert Steilberg
 * <p>
 * Adapter that allows users to view, but not edit, record details
 */
public class ViewPatientAdapter extends PatientDetailsAdapter {

    ViewPatientAdapter(List<Detail> details) {
        super(details);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Detail detail = mDetails.get(position);
        holder.field.setText(detail.getLabelStringId());
        holder.value.setText(detail.getStringValue());
        holder.value.setFocusable(false); // not editable in view mode
    }

}
