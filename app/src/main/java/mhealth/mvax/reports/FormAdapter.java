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
package mhealth.mvax.reports;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Vaccine;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.record.patient.detail.Detail;

/**
 * @author Robert Steilberg
 * <p>
 * Adapted for rendering a report
 */
public class FormAdapter extends BaseExpandableListAdapter {

    private List<ExpandablePatient> mPatients;

    FormAdapter() {
        mPatients = new ArrayList<>();
        notifyDataSetChanged(); // clean out anything old
    }

    public void refresh(List<ExpandablePatient> patients) {
        mPatients = patients;
        notifyDataSetChanged();
    }

    @Override
    public ExpandablePatient getGroup(int groupPosition) {
        return mPatients.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return mPatients.size();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpandablePatient patient = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_report, parent, false);
        }

        TextView patientName = convertView.findViewById(R.id.patient_name);
        patientName.setText(patient.getPatient().getName());

        ImageView arrow = convertView.findViewById(R.id.indicator_arrow);
        if (isExpanded) {
            arrow.setImageResource(R.drawable.ic_arrow_down);
        } else {
            arrow.setImageResource(R.drawable.ic_arrow_up);
        }
        return convertView;
    }

    @Override
    public Row getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).getRow(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).getNumRows();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        int numPatientDetails = getGroup(groupPosition).getNumDetails();
        Row row = getChild(groupPosition, childPosition);

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (childPosition < numPatientDetails) { // patient info
            convertView = inflater.inflate(R.layout.list_item_report_patient_detail, parent, false);
        } else { // vaccination
            convertView = inflater.inflate(R.layout.list_item_report_vaccination_detail, parent, false);
        }

        TextView label = convertView.findViewById(R.id.label);
        TextView value = convertView.findViewById(R.id.value);

        label.setText(row.getLabel());
        value.setText(row.getValue());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
