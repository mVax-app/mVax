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

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * @author Matthew Tribby, Robert Steilberg
 * <p>
 * Object for storing information about mVax users
 */

public class User implements Serializable, Comparable<User> {

    private User() {
        // Firebase POJO constructor
    }

    public User(String UID) {
        this.UID = UID;
    }

    /**
     * Firebase UID to link user to their auth table entry
     */
    private String UID;

    public String getUID() {
        return this.UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    /**
     * User's display name
     */
    private String displayName;

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * User's email/username
     */
    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * User's permission level
     */
    private UserRole role;

    public UserRole getRole() {
        return this.role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Sorts Users by name
     *
     * @param that User to sort against
     * @return negative integer, zero, or a positive integer if this
     * User is less than, equal to, or greater than that User, respectively
     */
    @Override
    public int compareTo(@NonNull User that) {
        return this.displayName.compareTo(that.displayName);
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

}
