package mhealth.mvax.model.user;

/**
 * A model class which repesents a prospective user's request to be added as an approved user
 * to the application. This model object is used to push user requests to the database so that
 * admins may view the said requests.
 *
 * @author Matthew Tribby
 * November, 2017
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
