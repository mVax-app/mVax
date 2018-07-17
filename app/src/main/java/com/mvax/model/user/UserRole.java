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
package com.mvax.model.user;

import java.io.Serializable;

import com.mvax.R;

/**
 * @author Matthew Tribby, Robert Steilberg
 * <p>
 * Enum for representing supported mVax user roles
 */
public enum UserRole implements Serializable {
    ADMIN(R.string.admin_enum),
    USER(R.string.user_enum);

    private int mResourceId;

    UserRole(int resourceId) {
        this.mResourceId = resourceId;
    }

    /**
     * @return String id used to find the string value of the role
     */
    public int getResourceId() {
        return this.mResourceId;
    }

}
