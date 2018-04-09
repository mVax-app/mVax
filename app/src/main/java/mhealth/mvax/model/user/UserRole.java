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
package mhealth.mvax.model.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Enum representing user role, which defines access levels to read/write
 * privileges of mVax data
 *
 * @author Matthew Tribby
 */
public enum UserRole implements Serializable {
    ADMIN,
    READER;

    // TODO try to get rid of this
    public static List<String> getRoles() {
        List<String> roles = new ArrayList<String>();
        UserRole[] values = UserRole.class.getEnumConstants();
        for (int i = 0; i < values.length; i++) {
            roles.add(values[i].name());
        }
        return roles;

    }
}


