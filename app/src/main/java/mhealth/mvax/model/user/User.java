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

/**
 * @author Matthew Tribby
 *         Object for storing information about mVax users
 */

public class User {
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mRole;

    /**
     * Default Firebase constructor; should not
     * be used internally
     */
    public User() {}

    public User(String firstName, String lastName, String email, String role){
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mEmail = email;
        this.mRole = role;
    }

    /**
     * For the mFirstName attribute, return for Firebase
     * @return First name of user
     */
    public String getFirstName(){
        return mFirstName;
    }

    /**
     * For the mFirstName attribute, sets value
     * @param firstName
     */
    public void setFirstName(String firstName){
        this.mFirstName = firstName;
    }

    /**
     * For the mLastName attribute, return for Firebase
     * @return First name of user
     */
    public String getLastName(){
        return mLastName;
    }

    /**
     * For the mLastName attribute, sets value
     * @param lastName
     */
    public void setLastName(String lastName){
        this.mLastName = lastName;
    }

    /**
     * For the mRole attribute, return for Firebase
     * @return UserRole
     */
    public String getRole(){
        return mRole;
    }

    /**
     * Sets the UserRole enum value
     * @param role
     */
    public void setRole(String role){
        this.mRole = role;
    }

    /**
     * For the mEmail attribute, return for Firebase
     * @return email string
     */
    public String getEmail(){
        return mEmail;
    }

    /**
     * Sets the email value
     * @param email
     */
    public void setEmail(String email){
        this.mEmail = email;
    }

}
