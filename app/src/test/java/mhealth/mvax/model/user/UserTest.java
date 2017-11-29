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
