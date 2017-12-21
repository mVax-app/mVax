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
package mhealth.mvax.records.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.patient.RecordDateFormat;

/**
 * @author Robert Steilberg
 *         <p>
 *         An adapter for listing patient record search results
 */

public class SearchResultAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Patient> mDataSource;


    //================================================================================
    // Constructors
    //================================================================================

    SearchResultAdapter(Context context, Collection<Patient> patients) {
        mContext = context;
        mDataSource = new ArrayList<>(patients);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //================================================================================
    // Public methods
    //================================================================================

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        ViewHolder holder;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_item_search_result, parent, false);
            holder = new ViewHolder();

            holder.titleTextView = rowView.findViewById(R.id.search_result_title);
            holder.subtitleTextView = rowView.findViewById(R.id.search_result_subtitle);
            holder.rightTextView = rowView.findViewById(R.id.search_result_right);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        TextView rightTextView = holder.rightTextView;

        Patient result = (Patient) getItem(position);

        titleTextView.setText(result.getName(rowView.getContext()));

        String DOBprompt = mContext.getResources().getString(R.string.DOB_prompt);
        RecordDateFormat dateFormat = new RecordDateFormat(mContext.getResources().getString(R.string.date_format));
        String DOBstr = DOBprompt + " " + dateFormat.getString(result.getDOB());

        subtitleTextView.setText(DOBstr);

        rightTextView.setText(result.getCommunity());

        return rowView;
    }

    void refresh(Collection<Patient> values) {
        mDataSource = new ArrayList<>(values);
        notifyDataSetChanged();
    }

    String getSelectedDatabaseKey(int position) {
        return mDataSource.get(position).getDatabaseKey();
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        TextView rightTextView;
    }
}
