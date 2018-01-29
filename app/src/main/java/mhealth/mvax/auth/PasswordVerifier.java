package mhealth.mvax.auth;

/**
 * @author Matthew Tribby
 *         <p>
 *         Description Here
 */

public interface PasswordVerifier {

    public boolean checkPassword(String password);

}
