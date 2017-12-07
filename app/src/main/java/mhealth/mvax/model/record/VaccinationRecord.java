package mhealth.mvax.model.record;

import java.io.Serializable;

/**
 * Created by mtribby on 11/26/17.
 */

public class VaccinationRecord implements Serializable {
    //date should be in yyyymmdd and is used that way internally
   // private String date;
    private String type;
    private String patientUID;

    public VaccinationRecord(){
        //Required empty public constructor, should not be used internally
    }

    public VaccinationRecord(String type, String patientUID){
        this.type = type;
        this.patientUID = patientUID;
    }


    /**
     * Returns the vaccine type as a String.
     * @return vaccine type
     */
    public String getType(){
        return type;
    }

    /**
     * Sets the vaccine type
     * @param type
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Returns the uid of the patient who received the vaccine
     * @return uid
     */
    public String getPatientUID(){
        return patientUID;
    }

    /**
     * Sets the paitent uid instance variable
     * @param uid String
     */
    public void setPatientUID(String uid){
        this.patientUID = uid;
    }

}
