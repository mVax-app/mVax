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