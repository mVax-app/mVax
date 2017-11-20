package mhealth.mvax.model;

/**
 * Created by mtribby on 11/20/17.
 */

public class UserRequest extends User{
    private String mUid;

    public UserRequest(){
        //required empty constructor for Firebase
    }

    public UserRequest(String uid, String firstName, String lastName, String email, String role){
        super(firstName, lastName, email, role);
        this.mUid = uid;
    }

    /**
     * Gets uid field
     * @return user id
     */
    public String getUid(){
        return mUid;
    }

    /**
     * Sets the user id
     * @param uid
     */
    public void setUid(String uid){
        this.mUid = uid;
    }
}
