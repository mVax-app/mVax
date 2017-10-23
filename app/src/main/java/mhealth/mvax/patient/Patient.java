package mhealth.mvax.patient;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mhealth.mvax.patient.vaccine.Vaccine;

/**
 * @author Robert Steilberg
 *
 * Object for storing information about mVax patients
 */

public class Patient implements Serializable {

    //================================================================================
    // Properties
    //================================================================================

    private String _id;

    private String _firstName;

    private String _lastName;

    private Gender _gender;

    private Long _DOB;

    private String _community;

    private Map<String, Vaccine> _vaccines;

    //================================================================================
    // Constructors
    //================================================================================

    public Patient() {
        // default constructor for database
        _vaccines = new TreeMap<>();
    }

    public Patient(String id, String first, String last, Gender gender, Long DOB, String community) {
        _id = id;
        _firstName = first;
        _lastName = last;
        _gender = gender;
        _DOB = DOB;
        _community = community;
        _vaccines = new TreeMap<>();
    }


    //================================================================================
    // Getters
    //================================================================================

    public String getId() {
        return _id;
    }

    public String getFirstName() {
        return this._firstName;
    }

    public String getLastName() {
        return this._lastName;
    }

    @Exclude
    public String getFullName() {
        return this._firstName + " " + this._lastName;
    }

    public Gender getGender() {
        return this._gender;
    }

    public Long getDOB() {
        return this._DOB;
    }

    public String getCommunity() {
        return this._community;
    }

    /**
     * Use this getter internally, instead of getVaccines()
     * @return an ArrayList of Vaccines associated with the Patient
     */
    @Exclude
    public ArrayList<Vaccine> getVaccineList() {
        return new ArrayList<>(_vaccines.values());
    }

    /**
     * This getter should only be used externally by Firebase
     */
    public Map<String, Vaccine> getVaccines() {
        return _vaccines;
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setId(String id) {
        this._id = id;
    }

    public void setFirstName(String first) {
        this._firstName = first;
    }

    public void setLastName(String last) {
        this._lastName = last;
    }

    public void setGender(Gender gender) {
        this._gender = gender;
    }

    public void setDOB(Long millis) {
        this._DOB = millis;
    }

    public void setCommunity(String community) {
        this._community = community;
    }

    /**
     * This setter should only be used externally by Firebase;
     * use addVaccine or updateVaccine to modify associated Vaccines
     */
    public void setVaccines(Map<String, Vaccine> vaccines) {
        this._vaccines = vaccines;
    }

    //================================================================================
    // Public Methods
    //================================================================================

    /**
     * Adds a new vaccine to the patient's vaccine records
     *
     * @param vaccine the new vaccine to add
     */
    public void addVaccine(Vaccine vaccine) {
        int id = _vaccines.size() + 1;
        vaccine.setId(_id + "_" + Integer.toString(id));
        _vaccines.put(vaccine.getId(), vaccine);
    }

    /**
     * Overwrites an existing vaccine; fails if given vaccine doesn't already exist
     * (new vaccines should be added via add vaccine)
     *
     * @param vaccine the existing dose to update
     * @return true if dose updated, false if existing dose not found
     */
    public boolean updateVaccine(Vaccine vaccine) {
        if (_vaccines.get(vaccine.getId()) != null) {
            _vaccines.put(vaccine.getId(), vaccine);
            return true;
        } else {
            return false;
        }
    }

}
