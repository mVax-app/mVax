package mhealth.mvax.model.user;

/**
 * Created by mtribby on 11/14/17.
 */

public class User {
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mRole;


    public User(){
        //empty required constructor
    }

    public User(String firstName, String lastName, String email, String role){
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mEmail = email;
        this.mRole = role;
    }

    /**
     * For the mFirstName attribute, return for Firebase
     * @return First name of user
     */
    public String getFirstName(){
        return mFirstName;
    }

    /**
     * For the mFirstName attribute, sets value
     * @param firstName
     */
    public void setFirstName(String firstName){
        this.mFirstName = firstName;
    }

    /**
     * For the mLastName attribute, return for Firebase
     * @return First name of user
     */
    public String getLastName(){
        return mLastName;
    }

    /**
     * For the mLastName attribute, sets value
     * @param lastName
     */
    public void setLastName(String lastName){
        this.mLastName = lastName;
    }

    /**
     * For the mRole attribute, return for Firebase
     * @return UserRole
     */
    public String getRole(){
        return mRole;
    }

    /**
     * Sets the UserRole enum value
     * @param role
     */
    public void setRole(String role){
        this.mRole = role;
    }

    /**
     * For the mEmail attribute, return for Firebase
     * @return email string
     */
    public String getEmail(){
        return mEmail;
    }

    /**
     * Sets the email value
     * @param email
     */
    public void setEmail(String email){
        this.mEmail = email;
    }




}
