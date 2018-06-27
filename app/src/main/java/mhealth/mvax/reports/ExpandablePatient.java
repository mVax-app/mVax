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

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.model.immunization.Vaccination;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.record.patient.detail.Detail;
import mhealth.mvax.utilities.StringFetcher;

/**
 * @author Robert Steilberg
 * <p>
 * Object for storing information about a patient and their vaccinations
 * for generating reports
 */
public class ExpandablePatient implements Comparable<ExpandablePatient> {

    private Patient mPatient;
    private List<Pair<String, String>> mRows;

    ExpandablePatient(Patient patient) {
        mPatient = patient;
        mRows = new ArrayList<>();
        for (Detail d : mPatient.getDetails()) {
            String label = StringFetcher.fetchString(d.getLabelStringId());
            String value = d.getStringValue();
            Pair<String, String> row = new Pair<>(label, value);
            mRows.add(row);
        }
    }

    public String getPatientName() {
        return mPatient.getName();
    }

    public Pair<String, String> getRow(int index) {
        return mRows.get(index);
    }

    public int getNumPatientDetails() {
        return mPatient.getDetails().size();
    }

    public int getNumRows() {
        return mRows.size();
    }

    @Override
    public int compareTo(@NonNull ExpandablePatient that) {
        return this.mPatient.getLastName().compareTo(that.mPatient.getLastName());
    }
}
