package mhealth.mvax.model.record;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Robert Steilberg
 */
public class DoseTest {

    private Dose dose;

    @Before
    public void setup() {
        dose = new Dose();
    }

    @Test
    public void setDateCompleted() throws Exception {
        dose.setDateCompleted(823237200000L);

        assertEquals("completion date not properly set",
                823237200000L,
                (long) dose.getDateCompleted());
    }

    @Test
    public void getLabel() throws Exception {
//        assertEquals("empty dose doesn't have empty label",
//                "",
//                dose.getLabel());

        dose.setLabel1("Foo");

        assertEquals("getLabel does not properly format dose with one label",
                "Foo:",
                dose.getLabel());

        dose.setLabel2("Bar");

        assertEquals("getLabel does not properly format dose with one label",
                "Foo (Bar):",
                dose.getLabel());
    }

    @Test
    public void hasBeenCompleted() throws Exception {
        assertFalse("dose not initialized with hasBeenCompleted = false",
                dose.hasBeenCompleted());

        dose.setDateCompleted(823237200000L);

        assertTrue("setting completion date dose not trigger hasBeenCompleted",
                dose.hasBeenCompleted());
    }

}