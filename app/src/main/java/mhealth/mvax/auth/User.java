package mhealth.mvax.auth;

/**
 * Created by mtribby on 11/14/17.
 */

public class User {
    private String mFirstName;
    private String mLastName;
    private UserRole mRole;

    public User(String firstName, String lastName, UserRole role){
        this.mFirstName = firstName;
        this.mLastName = lastName;
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
    public UserRole getRole(){
        return mRole;
    }

    /**
     * Sets the UserRole enum value
     * @param role
     */
    public void setRole(UserRole role){
        this.mRole = role;
    }

}
