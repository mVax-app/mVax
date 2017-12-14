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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by AlisonHuang on 11/28/17.
 */

public class UserTest {

    @Test
    public void firstNameGetter() throws Exception {
        User person = new User();
        person.setFirstName("Foobar");

        assertEquals("User's first name not set",
                "Foobar",
                person.getFirstName());
    }

    @Test
    public void lastNameGetter() throws Exception {
        User person = new User();
        person.setLastName("Dood");

        assertEquals("User's last name not set",
                "Dood",
                person.getLastName());
    }

    @Test
    public void userRoleGetter() throws Exception {
        User person = new User();
        person.setRole("READER");

        assertEquals("User's role not set",
                "READER",
                person.getRole());
    }

    @Test
    public void userEmailGetter() throws Exception {
        User person = new User();
        person.setEmail("devadmin@mvax.com");

        assertEquals("User's email not set",
                "devadmin@mvax.com",
                person.getEmail());
    }

}
