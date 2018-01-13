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

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.detail.Detail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Abstract adapter for displaying Person details,
 *         getView() left up to implementation
 */
public abstract class PatientDetailsAdapter extends BaseAdapter {

    //================================================================================
    // Properties
    //================================================================================

    protected final LayoutInflater mInflater;
//    protected static final int TYPE_SECTION = 0; // Section title row
//    protected static final int TYPE_FIELD = 1; // Data row
    protected List<Detail> mDataSource; // holds Patient and Guardian details
//    private SparseIntArray mHeaders; // maps header position to header title string id

    //================================================================================
    // Constructors
    //================================================================================

    protected PatientDetailsAdapter(Context context, List<Detail> details) {
        mDataSource = details;
        mInflater = LayoutInflater.from(context);
    }

    //================================================================================
    // Abstract methods
    //================================================================================

    @Override
    abstract public View getView(int position, View rowView, ViewGroup viewGroup);

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Detail getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return (mHeaders.get(position) != 0) ? TYPE_SECTION : TYPE_FIELD;
//    }

//    @Override
//    public int getViewTypeCount() {
//        return 2; // type 0 is header, type 1 is field, so 2 total view types
//    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Refresh the data source with new data and re-render the ListView
     *
     * @param newData is the sectioned, new data with which to populate the data source
     */
    public void refresh(List<Detail> newData) {
        mDataSource = newData;
        notifyDataSetChanged();
    }

    //================================================================================
    // Protected methods
    //================================================================================

//    /**
//     * @param position position of the header in the ListView
//     * @param parent   parent to inflate the row view with
//     * @return row view for a section header
//     */
//    protected View getSectionHeaderView(int position, ViewGroup parent) {
//        final View view = mInflater.inflate(R.layout.list_item_record_detail_section, parent, false);
//        final TextView fieldView = view.findViewById(R.id.record_detail_separator);
//        fieldView.setText(mHeaders.get(position));  // set section title text
//        return view;
//    }

    //================================================================================
    // Private methods
    //================================================================================

//    /**
//     * Properly set up a single data source containing both Patient and Guardian details
//     * along with section titles
//     *
//     * @param sectionedData a map, ordered by key, that maps a String resource id,
//     *                      representing a section's title, to a Person object's details
//     */
//    private void setDataSource(LinkedHashMap<Integer, List<Detail>> sectionedData) {
//        mDataSource = new ArrayList<>();
//        mHeaders = new SparseIntArray(getViewTypeCount());
//        for (Integer key : sectionedData.keySet()) {
//            List<Detail> values = sectionedData.get(key);
//            mDataSource.add(null); // add a null to the data source for separator
//            mHeaders.put(mDataSource.size() - 1, key); // store the header's position in mDataSource
//            mDataSource.addAll(values);  // add Detail objects to data source
//        }
//    }

}
