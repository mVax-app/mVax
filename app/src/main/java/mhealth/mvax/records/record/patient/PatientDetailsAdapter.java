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
package mhealth.mvax.records.record.patient;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.detail.Detail;
import mhealth.mvax.records.utilities.WatcherEditText;

/**
 * @author Robert Steilberg
 * <p>
 * Abstract adapter for displaying patient details about a record
 */
public abstract class PatientDetailsAdapter extends RecyclerView.Adapter<PatientDetailsAdapter.ViewHolder> {

    protected List<Detail> mDetails;

    protected PatientDetailsAdapter(List<Detail> details) {
        mDetails = details;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public View row;
        public TextView field;
        public WatcherEditText value;

        ViewHolder(View itemView) {
            super(itemView);
            row = itemView;
            field = itemView.findViewById(R.id.field);
            value = itemView.findViewById(R.id.value);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_record_detail, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public int getItemCount() {
        return mDetails.size();
    }

    /**
     * Refresh the data source with new data and re-render the ListView
     *
     * @param newData is the sectioned, new data with which to populate the data source
     */
    public void refresh(List<Detail> newData) {
        mDetails = newData;
        notifyDataSetChanged();
    }

}
