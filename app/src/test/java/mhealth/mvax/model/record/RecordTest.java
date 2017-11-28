package mhealth.mvax.model.record;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

/**
 * @author Robert Steilberg
 */
public class RecordTest {

    @Before
    public void setup() {
    }


    @Test
    public void testVaccineListSort() {
        Vaccine a = new Vaccine("1", "a");
        Vaccine f = new Vaccine("2", "f");

        assertThat("Vaccines are not compared according to name",
                a.compareTo(f),
                lessThan(0));

        ArrayList<Vaccine> vaccines = new ArrayList<>();
        vaccines.add(f);
        vaccines.add(a);

        // record initialized with Vaccine array ["b", "a"]
        Record record = new Record("1", vaccines);
        String firstVaccineName = record.getVaccines().get(0).getName();

        assertEquals("Initial record vaccine list not sorted by vaccine name", "a", firstVaccineName);

        record.setVaccines(vaccines);

        assertEquals("Subsequent set record vaccine list not sorted by vaccine name", "a", firstVaccineName);
    }

    @Test
    public void testFullNameGetter() {
        Record record = new Record();

        record.setFirstName("Robert");
        record.setLastName("Steilberg");

        assertEquals("Full name not computed correctly for first and last", "Steilberg, Robert", record.getFullName());

        record.setMiddleName("Hays");

        assertEquals("Full name not computed correctly for first, middle, and last", "Steilberg, Robert Hays", record.getFullName());


    }

}