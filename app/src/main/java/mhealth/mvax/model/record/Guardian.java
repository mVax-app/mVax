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

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Steilberg
 *         <p>
 *         DESCRIPTION HERE
 */
public class Guardian extends Person {

    public Guardian() {
    }

    private List<String> dependents;

    public ArrayList<String> getDependents() {
        return new ArrayList<>(this.dependents);
    }

    public void setDependents(List<String> dependents) {
        this.dependents = dependents;
    }

    @Exclude
    private Integer numDependents;

    @Exclude
    public Integer getNumDependents() {
        return this.dependents.size();
    }
}
