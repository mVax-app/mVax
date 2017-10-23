package mhealth.mvax;

import org.junit.Test;

import mhealth.mvax.auth.LoginActivity;

/**
 * Created by mtribby on 10/23/17.
 */

public class authJunitTest {
    public static final String TEST_BAD_USERNAME = "notAnEmail";
    public static final String TEST_BAD_PASSWORD = "";

    @Test
    public void checkInvalidEmail(){
        //need to add more edge cases
        assert(LoginActivity.isEmailValid(TEST_BAD_USERNAME) == false);
    }

    @Test
    public void checkInvalidPassword(){
        //Need to add more edge cases / define proper password requirements
        assert(LoginActivity.isPasswordValid(TEST_BAD_PASSWORD) == false);
    }

}
