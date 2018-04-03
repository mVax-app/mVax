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

import org.junit.Test;

/**
 * Created by mtribby on 10/23/17.
 */

public class authJunitTest {
    public static final String TEST_BAD_USERNAME = "notAnEmail";
    public static final String TEST_BAD_PASSWORD = "";

    @Test
    public void checkInvalidEmail(){
        //need to add more edge cases
        //assert(AuthActivity.isEmailValid(TEST_BAD_USERNAME) == false);
    }

    @Test
    public void checkInvalidPassword(){
        //Need to add more edge cases / define proper password requirements
        //assert(AuthActivity.isPasswordValid(TEST_BAD_PASSWORD) == false);
    }

}
