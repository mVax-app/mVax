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

import org.junit.Test;

import mhealth.mvax.R;

import static org.junit.Assert.*;

/**
 * @author Robert Steilberg
 */
public class SexTest {

    @Test
    public void initialize() throws Exception {
        Sex male = Sex.MALE;

        assertEquals("male sex not configured with correct string ID",
                R.string.male_enum,
                male.getResourceId());

        Sex female = Sex.FEMALE;

        assertEquals("female sex not configured with correct string ID",
                R.string.female_enum,
                female.getResourceId());
    }

}