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
package mhealth.mvax.model.record;

import android.content.Context;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.records.details.patient.detail.Detail;

/**
 * @author Robert Steilberg
 *         <p>
 *         Extends Person to store additional information
 *         and define functionality specific to a Guardian,
 *         which is associated with one or more Patients
 */
public class Guardian extends Person {

    //================================================================================
    // Constructors
    //================================================================================

    private Guardian() {
        // Firebase POJO constructor
    }

    public Guardian(String databaseKey, String patientDatabaseKey) {
        super(databaseKey);
        // TODO determine if orphaned guardians should be allowed; if so,
        // TODO don't require patientDatabaseKey in constructor
        this.dependents.add(patientDatabaseKey);
    }

    //================================================================================
    // Properties
    //================================================================================

    /**
     * List of unique Firebase database keys, each of
     * which represents a Patient who is one of the
     * Guardian's dependents
     */
    private List<String> dependents = new ArrayList<>();

    public ArrayList<String> getDependents() {
        return new ArrayList<>(this.dependents);
    }

    public void setDependents(List<String> dependents) {
        this.dependents = dependents;
    }

    //================================================================================
    // Computed functions
    //================================================================================

    @Exclude
    public Integer getNumDependents() {
        return this.dependents.size();
    }

    /**
     * Adds a Patient to the Guardian's list of patients,
     * denoting the Patient as a dependent of the Guardian
     *
     * @param dependentDatabaseKey Firebase unique database key
     *                             representing the dependent
     */
    @Exclude
    public void addDependent(String dependentDatabaseKey) {
        dependents.add(dependentDatabaseKey);
    }

    @Override
    @Exclude
    public List<Detail> getDetails(Context context) {
        // nothing additional to return past what is
        // already included at the Person level
        return getPersonDetails(context);
    }

    @Override
    @Exclude
    public int getSectionTitleStringID() {
        return R.string.guardian_detail_section_title;
    }

}