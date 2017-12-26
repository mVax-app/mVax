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

import java.util.ArrayList;

import mhealth.mvax.model.immunization.Vaccine;

import static org.junit.Assert.*;

// https://github.com/ravidsrk/android-testing-guide#espresso
// view coverage
// https://developer.android.com/studio/test/index.html

/**
 * @author Robert Steilberg
 */
public class RecordTest {

    @Test
    public void vaccineListSort() throws Exception {
        Vaccine a = new Vaccine();
        a.setName("Hepatitis");
        Vaccine b = new Vaccine();
        b.setName("Rotavirus");

        ArrayList<Vaccine> vaccines = new ArrayList<>();
        // record will be initialized with Vaccine array ["Rotavirus", "Hepatitis"]
        vaccines.add(a);
        vaccines.add(b);

        Record record = new Record("foo", vaccines);
        String firstVaccineName = record.getVaccines().get(0).getName();

        assertEquals("record's vaccine list not sorted by vaccine name after initialization",
                "Hepatitis",
                firstVaccineName);

        record.setVaccines(vaccines);

        assertEquals("record's vaccine list not sorted by vaccine name after setter",
                "Hepatitis",
                firstVaccineName);
    }

    @Test
    public void fullNameGetter() throws Exception {
        Record record = new Record();

        record.setFirstName("Foo");
        record.setLastName("Bar");

        assertEquals("full name not computed correctly for first and last",
                "Bar, Foo",
                record.getFullName());

        record.setMiddleName("Baz");

        assertEquals("full name not computed correctly for first, middle, and last",
                "Bar, Foo Baz",
                record.getFullName());
    }



}