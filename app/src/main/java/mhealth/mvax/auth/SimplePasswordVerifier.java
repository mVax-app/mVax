package mhealth.mvax.auth;

/**
 * @author Matthew Tribby
 *         Implementation of Simple Password Verifier that checks if password is 6 or more characters
 */

public class SimplePasswordVerifier implements PasswordVerifier{

    @Override
    public boolean checkPassword(String password) {
        return password.length() >= 6;
    }
}
