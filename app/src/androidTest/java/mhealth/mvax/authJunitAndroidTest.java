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
package mhealth.mvax;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;

/**
 * Created by mtribby on 10/23/17.
 */


public class authJunitAndroidTest {
    public static final String TEST_USERNAME = "testusernameMVAX@mvaxtest.com";
    public static final String TEST_PASSWORD = "password";


    @Test
    public void checkAuthWorks(){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(TEST_USERNAME, TEST_PASSWORD);
        auth.signInWithEmailAndPassword(TEST_USERNAME, TEST_PASSWORD);
        Boolean correctUser = auth.getCurrentUser().getEmail().equals(TEST_USERNAME);

        //Delete user to allow for reproducing test
        auth.getCurrentUser().delete();

        assert(correctUser);
    }


}