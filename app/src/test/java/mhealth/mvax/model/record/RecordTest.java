package mhealth.mvax.model.record;

import org.junit.Test;

import java.util.ArrayList;

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
        Vaccine a = new Vaccine("foo", "Hepatitis");
        Vaccine b = new Vaccine("bar", "Rotavirus");

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