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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import mhealth.mvax.records.record.patient.detail.Detail;

/**
 * @author Robert Steilberg
 * <p>
 * Abstract adapter for displaying patient details about a record
 */
public abstract class PatientDetailsAdapterOld extends BaseAdapter {

    protected final LayoutInflater mInflater;
    protected List<Detail> mDataSource;

    protected PatientDetailsAdapterOld(Context context, List<Detail> details) {
        mDataSource = details;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    abstract public View getView(int position, View rowView, ViewGroup viewGroup);

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

    /**
     * Refresh the data source with new data and re-render the ListView
     *
     * @param newData is the sectioned, new data with which to populate the data source
     */
    public void refresh(List<Detail> newData) {
        mDataSource = newData;
        notifyDataSetChanged();
    }

}
