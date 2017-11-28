package mhealth.mvax.model.record;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Object for storing information about mVax doses;
 *         implements Serializable so that it can be bassed as
 *         a Bundle argument to fragments
 *         <p>
 *         PLEASE READ DOCUMENTATION BEFORE ADDING, REMOVING,
 *         OR MODIFYING PROPERTIES
 */

public class Dose implements Serializable {

    //================================================================================
    // Constructors
    //================================================================================

    /**
     * Default Firebase constructor; should not
     * be used internally
     */
    public Dose() {
    }

    public Dose(String label1, String label2) {
        mLabel1 = label1;
        mLabel2 = label2;
    }

    public Dose(String label1) {
        mLabel1 = label1;
    }

    //================================================================================
    // Properties
    //================================================================================

    /**
     * The first label for the dose
     */
    private String mLabel1;

    public String getLabel1() {
        return this.mLabel1;
    }

    public void setLabel1(String label) {
        this.mLabel1 = label;
    }

    /**
     * The second label for the dose, if there is one
     */
    private String mLabel2;

    public String getLabel2() {
        return this.mLabel2;
    }

    public void setLabel2(String label) {
        this.mLabel2 = label;
    }

    /**
     * The amount of time until the next dose in the
     * vaccine regimen should be administered
     */
    private Long mTimeUntilNextDose;

    public Long getTimeUntilNextDose() {
        return this.mTimeUntilNextDose;
    }

    public void setTimeUntilNextDose(Long millis) {
        this.mTimeUntilNextDose = millis;
    }

    /**
     * The date at which the dose was completed
     */
    private Long mDateCompleted;

    public Long getDateCompleted() {
        return this.mDateCompleted;
    }

    public void setDateCompleted(Long date) {
        this.mDateCompleted = date;
        this.mCompleted = (date != null);
    }

    @Exclude
    private Boolean mCompleted = false;

    //================================================================================
    // Computed getters
    //================================================================================

    @Exclude
    public String getLabel() {
        StringBuilder sb = new StringBuilder();
        if (mLabel2 != null) {
            sb.append(mLabel1);
            sb.append(" (");
            sb.append(mLabel2);
            sb.append("):");
        } else {
            sb.append(mLabel1);
            sb.append(":");
        }
        return sb.toString();
    }

    public Boolean hasBeenCompleted() {
        return mCompleted;
    }

    //================================================================================
    // Computed setters
    //================================================================================

    @Exclude
    public void setLabels(String label1, String label2) {
        mLabel1 = label1;
        mLabel2 = label2;
    }

}