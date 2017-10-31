package mhealth.mvax.record.vaccine;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * @author Robert Steilberg
 *
 * Object for storing information about mVax doses,
 * with proper getters and setters for Firebase storage
 *
 * NOTE: Tampering with non-excluded getters or setters may break
 * Firebase integration!
 */

public class Dose implements Serializable {

    //================================================================================
    // Properties
    //================================================================================

    private String _id;

    private String _label1;

    private String _label2;

    private Long _date;

    @Exclude
    private Boolean _completed = false;

    //================================================================================
    // Constructors
    //================================================================================

    public Dose() {
        // empty constructor for Firebase
    }

    public Dose(String label1, String label2) {
        _label1 = label1;
        _label2 = label2;
    }

    public Dose(String label1) {
        _label1 = label1;
    }

    //================================================================================
    // Getters
    //================================================================================

    public String getId() {
        return _id;
    }

    public String getLabel1() {
        return _label1;
    }

    public String getLabel2() {
        return _label2;
    }

    @Exclude
    public String getLabel() {
        StringBuilder sb = new StringBuilder();
        if (_label2 != null) {
            sb.append(_label1);
            sb.append(" (");
            sb.append(_label2);
            sb.append("):");
        } else {
            sb.append(_label1);
            sb.append(":");
        }
        return sb.toString();
    }

    public Long getDate() {
        return _date;
    }

    public Boolean hasBeenCompleted() {
        return _completed;
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setId(String id) {
        _id = id;
    }

    @Exclude
    public void setLabels(String label1, String label2) {
        _label1 = label1;
        _label2 = label2;
    }

    public void setLabel1(String label1) {
        _label1 = label1;
    }

    public void setLabel2(String label2) {
        _label2 = label2;
    }

    public void setDate(Long date) {
        _date = date;
        _completed = (date != null);
    }

}
