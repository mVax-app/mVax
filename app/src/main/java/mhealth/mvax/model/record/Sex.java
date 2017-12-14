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

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 *         <p>
 *         Enum for storing supported sexes
 */

public enum Sex {
    MALE(R.string.male_enum),
    FEMALE(R.string.female_enum);

    private int mResourceId;

    Sex(int resourceId) {
        this.mResourceId = resourceId;
    }

    /**
     * @return id used to find the string value of the Sex
     */
    public int getResourceId() {
        return this.mResourceId;
    }

}