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
import mhealth.mvax.records.views.detail.Detail;

/**
 * @author Robert Steilberg
 *         <p>
 *         DESCRIPTION HERE
 */
public class Guardian extends Person {

    private Guardian() {}

    public Guardian(String databaseKey) {
        this.databaseKey = databaseKey;
        dependents = new ArrayList<>();
    }

    private List<String> dependents = new ArrayList<>();

    public ArrayList<String> getDependents() {
        return new ArrayList<>(this.dependents);
    }

    public void setDependents(List<String> dependents) {
        this.dependents = dependents;
    }

    public Integer getNumDependents() {
        return this.dependents.size();
    }

    @Override
    public List<Detail> getDetails(Context context) {
        return getPersonDetails(context);
    }

    @Override
    public int getSectionTitleStringID() {
        return R.string.guardian_detail_section_title;
    }


    public void addDependent(Patient dependent) {
        dependents.add(dependent.getDatabaseKey());
    }

    public void addDependent(String databaseKey) {
        dependents.add(databaseKey);
    }
}
