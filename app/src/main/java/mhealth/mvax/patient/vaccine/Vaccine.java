package mhealth.mvax.patient.vaccine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Robert Steilberg
 *
 * Object for storing information about mVax vaccines
 */

public class Vaccine implements Serializable {

    //================================================================================
    // Properties
    //================================================================================

    private int _id;

    private String _name;

    private Map<Integer, Dose> _doses;

    //================================================================================
    // Constructors
    //================================================================================

    public Vaccine(String name) {
        _name = name;
        _doses = new TreeMap<>();
    }

    //================================================================================
    // Getters
    //================================================================================

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public int getNumDoses() {
        return _doses.size();
    }

    public ArrayList<Dose> getDoseList() {
        return new ArrayList<>(_doses.values());
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setId(int id) {
        _id = id;
    }

    public void setName(String name) {
        _name = name;
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
        dose.setId(id);
        _doses.put(id, dose);
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
