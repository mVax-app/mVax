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
package mhealth.mvax.records.details.patient;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.records.views.detail.Detail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Abstract adapter for listing details about a patient
 *         from a Record, leaving implementation for getView up
 *         to implementation
 */

public abstract class PatientDataAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    protected static final int TYPE_SECTION = 0;
    protected static final int TYPE_FIELD = 1;
    protected List<Detail> mDataSource;
    // contains position of headers mapped to their titles
    protected Map<Integer, String> mHeaders;


    //================================================================================
    // Constructors
    //================================================================================

    protected PatientDataAdapter(LinkedHashMap<String, ArrayList<Detail>> sectionedData) {
        setDataSource(sectionedData);
    }


    //================================================================================
    // Abstract methods
    //================================================================================

    @Override
    abstract public View getView(int position, View rowView, ViewGroup viewGroup);

    public static class ViewHolder {
        public TextView fieldView;
        public EditText valueView;
    }

    //================================================================================
    // Override methods
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
    public int getItemViewType(int position) {
        return mHeaders.keySet().contains((position)) ? TYPE_SECTION : TYPE_FIELD;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // type 1 is field, type 2 is header
    }


    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Refresh the data source with new data
     *
     * @param newData is the new data with which to populate the data source
     */
    public void refresh(LinkedHashMap<String, ArrayList<Detail>> newData) {
        setDataSource(newData);
        notifyDataSetChanged();
    }


    //================================================================================
    // Private methods
    //================================================================================

    private void setDataSource(LinkedHashMap<String, ArrayList<Detail>> sectionedData) {
        mDataSource = new ArrayList<>();
        mHeaders = new HashMap<>();
        for (String key : sectionedData.keySet()) {
            ArrayList<Detail> values = sectionedData.get(key);
            // add a null to the data source so that separator doesn't mess up position ordering
            mDataSource.add(null);
            // store the header's index in the data source
            mHeaders.put(mDataSource.size() - 1, key);
            mDataSource.addAll(values);
        }
    }

}
