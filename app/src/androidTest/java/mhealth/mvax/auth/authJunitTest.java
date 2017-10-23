package mhealth.mvax.auth;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;

/**
 * Created by mtribby on 10/23/17.
 */


public class authJunitTest {
    public static final String TEST_USERNAME = "testusernameMVAX@mvaxtest.com";
    public static final String TEST_BAD_USERNAME = "notAnEmail";
    public static final String TEST_PASSWORD = "password";
    public static final String TEST_BAD_PASSWORD = "";

    @Test
    public void checkAuthWorks(){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(TEST_USERNAME, TEST_PASSWORD);
        auth.signInWithEmailAndPassword(TEST_USERNAME, TEST_PASSWORD);
        Boolean correctUser = auth.getCurrentUser().getEmail().equals(TEST_USERNAME);

        //Delete user to allow for reproducing test
        auth.getCurrentUser().delete();

        assert(correctUser);
    }

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