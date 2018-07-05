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

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 * <p>
 * Adapted for rendering a report
 */
public class ReportAdapter extends BaseExpandableListAdapter {

    private List<ExpandablePatient> mPatients;

    ReportAdapter(List<ExpandablePatient> patients) {
        mPatients = patients;
        notifyDataSetChanged(); // clean out anything old
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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_report, parent, false);
        }

        ExpandablePatient patient = getGroup(groupPosition);
        TextView patientName = convertView.findViewById(R.id.patient_name);
        patientName.setText(patient.getPatientName());

        ImageView arrow = convertView.findViewById(R.id.indicator_arrow);
        if (isExpanded) {
            arrow.setImageResource(R.drawable.ic_arrow_down);
        } else {
            arrow.setImageResource(R.drawable.ic_arrow_up);
        }
        return convertView;
    }

    @Override
    public Pair<String, String> getChild(int groupPosition, int childPosition) {
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

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_report_detail, parent, false);
        }

        TextView label = convertView.findViewById(R.id.label);
        TextView value = convertView.findViewById(R.id.value);

        Typeface avenirMedium = ResourcesCompat.getFont(convertView.getContext(), R.font.avenir_medium);

        int numPatientDetails = getGroup(groupPosition).getNumPatientDetails();
        if (childPosition < numPatientDetails) { // patient info
            int dukeBlue = ContextCompat.getColor(convertView.getContext(), R.color.dukeBlue);
            int gray = ContextCompat.getColor(convertView.getContext(), R.color.lightGray);

            label.setTextColor(dukeBlue);
            label.setTypeface(avenirMedium);
            value.setTypeface(avenirMedium);
            value.setTextColor(gray);
        } else { // vaccination
            Typeface avenirHeavy = ResourcesCompat.getFont(convertView.getContext(), R.font.avenir_heavy);
            int lightBlue = ContextCompat.getColor(convertView.getContext(), R.color.lightBlue);
            int darkGray = ContextCompat.getColor(convertView.getContext(), R.color.darkGray);

            label.setTextColor(lightBlue);
            label.setTypeface(avenirMedium);
            value.setTextColor(darkGray);
            value.setTypeface(avenirHeavy);

        }

        Pair<String, String> row = getChild(groupPosition, childPosition);
        label.setText(row.first);
        value.setText(row.second);

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
