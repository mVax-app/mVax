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
package com.mvax.reports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import com.mvax.model.record.Patient;
import com.mvax.records.record.patient.detail.Detail;

/**
 * @author Robert Steilberg
 * <p>
 * Object for storing information about a patient and their vaccinations
 * for generating reports
 */
public class ExpandablePatient implements Comparable<ExpandablePatient> {

    private Patient mPatient;
    private List<Pair<Object, String>> mRows;
    private int mNumPatientDetails;

    ExpandablePatient(Context context, Patient patient) {
        mPatient = patient;
        mRows = new ArrayList<>();
        List<Detail> patientDetails = mPatient.getDetails(context);
        mNumPatientDetails = patientDetails.size();
        for (Detail d : patientDetails) {
            String value = d.getStringValue();
            Pair<Object, String> row = new Pair<>(d.getLabelStringId(), value);
            mRows.add(row);
        }
    }

    public String getPatientName() {
        return mPatient.getName();
    }

    public Pair<Object, String> getRow(int index) {
        return mRows.get(index);
    }

    public void addRow(Pair<Object, String> row) {
        mRows.add(row);
    }

    public int getNumPatientDetails() {
        return mNumPatientDetails;
    }

    public int getNumRows() {
        return mRows.size();
    }

    @Override
    public int compareTo(@NonNull ExpandablePatient that) {
        return this.mPatient.getLastName().compareTo(that.mPatient.getLastName());
    }
}
