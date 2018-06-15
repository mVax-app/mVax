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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.PatientDetailsAdapter;
import mhealth.mvax.records.record.patient.detail.Detail;

/**
 * @author Robert Steilberg
 * <p>
 * Adapter for rendering RecyclerView row items for viewing patient details
 */
public class ViewPatientAdapter extends PatientDetailsAdapter {

    ViewPatientAdapter(Context context, List<Detail> details) {
        super(details);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_record_detail, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.field.setText(mDetails.get(position).getLabelStringId()); // label
        holder.value.setText(mDetails.get(position).getStringValue()); // value
        holder.value.setFocusable(false); // not editable in view mode
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

}
