package mhealth.mvax.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Robert Steilberg
 *
 * Object for storing information about mVax vaccines,
 * with proper getters and setters for Firebase storage
 *
 * NOTE: Tampering with non-excluded getters or setters may break
 * Firebase integration!
 */

public class Vaccine implements Serializable {

    //================================================================================
    // Properties
    //================================================================================

    private String _id;

    private String _name;

    private Map<String, Dose> _doses;

    private Long mDueDate;

    public Long getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Long date) {
        mDueDate = date;
    }

    //================================================================================
    // Constructors
    //================================================================================

    public Vaccine() {
        // empty constructor for Firebase
        _doses = new TreeMap<>();
    }

    // TODO: ensure that id naming protocol for doses and vaccines is working as expected
    public Vaccine(String name) {
        _name = name;
        _doses = new TreeMap<>();
    }

    //================================================================================
    // Getters
    //================================================================================

    public String getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    /**
     * Use this getter internally, instead of getDoses()
     * @return an ArrayList of Doses associated with the Vaccine
     */
    @Exclude
    public ArrayList<Dose> getDoseList() {
        return new ArrayList<>(_doses.values());
    }

    /**
     * This getter should only be used externally by Firebase
     */
    public Map<String, Dose> getDoses() {
        return _doses;
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setId(String id) {
        _id = id;
    }

    public void setName(String name) {
        _name = name;
    }

    /**
     * This setter should only be used externally by Firebase;
     * use addDose or updateDose to modify associated Doses
     */
    public void setDoses(Map<String, Dose> doses) {
        _doses = doses;
    }

    //================================================================================
    // Public Methods
    //================================================================================

    /**
     * Adds a new dose to the vaccine's associated doses
     *
     * @param dose the new dose to add
     */
    public void addDose(Dose dose) {
        int id = _doses.size() + 1;
        dose.setId(_id + "_" + Integer.toString(id));
        _doses.put(dose.getId(), dose);
    }

    /**
     * Overwrites an existing dose; fails if given dose doesn't already exist
     * (new doses should be added via add dose)
     *
     * @param dose the existing dose to update
     * @return true if dose updated, false if existing dose not found
     */
    public boolean updateDose(Dose dose) {
        if (_doses.get(dose.getId()) != null) {
            _doses.put(dose.getId(), dose);
            return true;
        } else {
            return false;
        }
    }

}
