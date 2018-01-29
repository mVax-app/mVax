package mhealth.mvax.auth;

/**
 * @author Matthew Tribby
 *         Complex password verifier: 8 characters, at least one upper case and one lower case, one number, one special character
 */

public class ComplexPasswordVerifier implements PasswordVerifier{

    //Regex taken from this Stack Overflow post: https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    public boolean checkPassword(String password){
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

}
