package mhealth.mvax.model.record;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

/**
 * @author Robert Steilberg
 */
public class VaccineTest {

    @Test
    public void addDose() throws Exception {
        Vaccine vaccine = new Vaccine("foo", "Hepatitis");

        assertEquals("new vaccine not initialized with empty dose list",
                0,
                vaccine.getDoses().size());

        Dose dose = new Dose("foo", "bar");
        vaccine.addDose(dose);

        assertEquals("new doses not properly added to Vaccine's dose list",
                1,
                vaccine.getDoses().size());
    }

    @Test
    public void compareTo() throws Exception {
        Vaccine a = new Vaccine("foo", "Rotavirus");
        Vaccine b = new Vaccine("bar", "Rotavirus");
        Vaccine c = new Vaccine("baz", "Yellow Fever");

        assertThat("vaccines not sorted by ascending name",
                a.compareTo(c),
                lessThan(0));

        assertThat("vaccines not sorted by ascending name",
                c.compareTo(a),
                greaterThan(0));

        assertEquals("vaccines with same name not sorted together",
                0,
                a.compareTo(b));
    }

}