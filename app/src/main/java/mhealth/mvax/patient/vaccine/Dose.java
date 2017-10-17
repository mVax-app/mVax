package mhealth.mvax.patient.vaccine;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Robert Steilberg
 *
 * Object for storing information about mVax doses
 */

public class Dose implements Serializable {

    //================================================================================
    // Properties
    //================================================================================

    private int _id;

    private String _label1;

    private String _label2;

    private Date _date;

    private boolean _completed = false;

    //================================================================================
    // Constructors
    //================================================================================

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

    public int getId() {
        return _id;
    }

    public String getLabel1() {
        return _label1;
    }

    public String getLabel2() {
        return _label2;
    }

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

    public Date getDate() {
        return _date;
    }

    public boolean hasBeenCompleted() {
        return _completed;
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setId(int id) {
        _id = id;
    }

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

    public void setDate(Date date) {
        _date = date;
        _completed = (date != null);
    }
}
