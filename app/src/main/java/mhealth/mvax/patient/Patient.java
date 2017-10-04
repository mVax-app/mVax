package mhealth.mvax.patient;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Robert Steilberg
 *
 * An object for describing a patient
 */

public class Patient implements Serializable {

    private String _firstName;

    private String _lastName;

    private Gender _gender;

    private Date _DOB;

    private String _community;

    private String _parent;

    public Patient(String first, String last, Gender gender, Date DOB, String community) {
        this._firstName = first;
        this._lastName = last;
        this._gender = gender;
        this._DOB = DOB;
        this._community = community;
    }

    // Getters

    public String getFirstName() {
        return this._firstName;
    }

    public String getLastName() {
        return this._lastName;
    }

    public String getFullName() {
        return this._firstName + " " + this._lastName;
    }

    public Gender getGender() {
        return this._gender;
    }

    public Date getDOB() {
        return this._DOB;
    }

    public String getCommunity() {
        return this._community;
    }

    // Setters

    public void setFirstName(String first) {
        this._firstName = first;
    }

    public void setLastName(String last) {
        this._lastName = last;
    }

    public void setGender(Gender gender) {
        this._gender = gender;
    }

    public void setDOB(long millis) {
        this._DOB = new Date(millis);
    }

    public void setCommunity(String community) {
        this._community = community;
    }
}
