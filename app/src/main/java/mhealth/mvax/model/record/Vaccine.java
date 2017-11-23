package mhealth.mvax.model.record;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Robert Steilberg
 *         <p>
 *         Object for storing information about mVax vaccines;
 *         sorts by vaccine name
 *         <p>
 *         PLEASE READ DOCUMENTATION BEFORE ADDING, REMOVING,
 *         OR MODIFYING PROPERTIES
 */

public class Vaccine implements Serializable, Comparable<Vaccine> {

    //================================================================================
    // Constructors
    //================================================================================

    /**
     * Default Firebase constructor; should not
     * be used internally
     */
    public Vaccine() {
        mDoses = new ArrayList<>();
    }

    public Vaccine(String databaseKey, String name) {
        mDatabaseKey = databaseKey;
        mName = name;
        mDoses = new ArrayList<>();
    }

    //================================================================================
    // Properties
    //================================================================================

    /**
     * Name of the vaccine
     */
    private String mDatabaseKey;

    public String getDatabaseKey() {
        return this.mDatabaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.mDatabaseKey = databaseKey;
    }

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

    /**
     * Target count for a single month
     */
    private int mTargetCount;

    public int getTargetCount() {
        return this.mTargetCount;
    }

    public void setTargetCount(int target) {
        this.mTargetCount = target;
    }

    /**
     * Total count for a single month
     */
    private int mGivenCount;

    public int getGivenCount() {
        return this.mGivenCount;
    }

    public void setGivenCount(int given) {
        this.mGivenCount = given;
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
     * Array containing the vaccine's doses
     */
    private ArrayList<Dose> mDoses;

    public ArrayList<Dose> getDoses() {
        return this.mDoses;
    }

    public void setDoses(ArrayList<Dose> doses) {
        this.mDoses = doses;
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

    @Override
    public int compareTo(@NonNull Vaccine that) {
        return this.mName.compareTo(that.mName);
    }
}
