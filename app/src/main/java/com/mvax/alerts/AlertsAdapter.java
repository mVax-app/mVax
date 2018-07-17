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
package com.mvax.alerts;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import com.mvax.R;
import com.mvax.model.record.Patient;
import com.mvax.records.record.RecordFragment;
import com.mvax.records.utilities.NullableDateFormat;

/**
 * @author Robert Steilberg
 * <p>
 * Adapter for listing overdue patients
 */
public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.ViewHolder> {

    private FragmentActivity mActivity;
    private List<Patient> mPatients;

    AlertsAdapter(List<Patient> patients, FragmentActivity activity) {
        mPatients = patients;
        Collections.sort(mPatients);
        mActivity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View row;
        TextView name, phone, dob;

        ViewHolder(View view) {
            super(view);
            row = view;
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            dob = view.findViewById(R.id.dob);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_overdue_patient, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Patient patient = mPatients.get(position);
        holder.name.setText(patient.getName());


        final String DOBprompt = mActivity.getString(R.string.DOB_prompt);
        final String DOBstr = DOBprompt
                + " " + NullableDateFormat.getString(mActivity, patient.getDOB());
        holder.dob.setText(DOBstr);

        final String phonePrompt = mActivity.getString(R.string.phone_prompt)
                + " " + patient.getPhoneNumber();
        holder.phone.setText(phonePrompt);

        holder.row.setOnClickListener(v -> {
            final RecordFragment detailFrag = RecordFragment.newInstance(patient.getDatabaseKey());
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, detailFrag)
                    .addToBackStack(null) // back button brings us back to SearchFragment
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return mPatients.size();
    }

    /**
     * Refresh list view with a new data set
     */
    public void refresh(List<Patient> patients) {
        mPatients = patients;
        Collections.sort(mPatients);
        notifyDataSetChanged();
    }

    /**
     * Clear out all overdue patients from the list view
     */
    public void clearResults() {
        mPatients.clear();
        notifyDataSetChanged();
    }

}
