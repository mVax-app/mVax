package mhealth.mvax.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Robert Steilberg
 *         <p>
 *         Object for storing information about mVax vaccines
 *         <p>
 *         PLEASE READ DOCUMENTATION BEFORE ADDING, REMOVING,
 *         OR MODIFYING PROPERTIES
 */

public class Vaccine implements Serializable {

    //================================================================================
    // Properties
    //================================================================================

    /**
     * Name of the vaccine
     */
    private String mName;

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    /*
     * Target count for this month
     */

    private int mTargetCount;

    public int getTargetCount() {
        return this.mTargetCount;
    }

    public void setTargetCount(int target) {
        this.mTargetCount = target;
    }

    /*
     * Total count for this month
     */

    private int mAdministeredCount;

    public int getAdministeredCount() {
        return this.mAdministeredCount;
    }

    public void setGivenCount(int administered) {
        this.mAdministeredCount = administered;
    }

    /**
     * Date at which the associated record is due
     * to receive the next dose, expressed in
     * milliseconds since Unix epoch
     */
    private Long mDueDate;

    public Long getDueDate() {
        return this.mDueDate;
    }

    public void setDueDate(Long date) {
        this.mDueDate = date;
    }

    /**
     * Array containing the record's doses
     */
    private ArrayList<Dose> mDoses;

    public ArrayList<Dose> getDoses() {
        return this.mDoses;
    }

    public void setDoses(ArrayList<Dose> doses) {
        this.mDoses = doses;
    }

    //================================================================================
    // Constructors
    //================================================================================

    public Vaccine() {
        // empty constructor for Firebase
        mDoses = new ArrayList<>();
    }

    public Vaccine(String name) {
        mName = name;
        mDoses = new ArrayList<>();
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
        mDoses.add(dose);
    }

}
