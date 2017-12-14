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
 * A model class which repesents a prospective user's request to be added as an approved user
 * to the application. This model object is used to push user requests to the database so that
 * admins may view the said requests.
 *
 * @author Matthew Tribby
 * November, 2017
 */
public class UserRequest extends User{
    private String mUid;

    public UserRequest(){
        //required empty constructor for Firebase
    }

    public UserRequest(String uid, String firstName, String lastName, String email, String role){
        super(firstName, lastName, email, role);
        this.mUid = uid;
    }

    /**
     * Gets uid field
     * @return user id
     */
    public String getUid(){
        return mUid;
    }

    /**
     * Sets the user id
     * @param uid
     */
    public void setUid(String uid){
        this.mUid = uid;
    }
}
