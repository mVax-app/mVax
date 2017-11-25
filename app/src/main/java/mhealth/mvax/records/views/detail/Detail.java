package mhealth.mvax.records.views.detail;

import android.content.Context;
import android.widget.EditText;

/**
 * @author Robert Steilberg
 *         <p>
 *         Abstract class for storing information about a generic Detail,
 *         which is used to popuate a ListView when creating, editing, or
 *         viewing a Record
 */

public abstract class Detail<T> {

    //================================================================================
    // Properties
    //================================================================================

    private String label;

    private String hint;

    private T mValue;

    private String stringValue;

    private Runnable setter;

    private Context mContext;


    //================================================================================
    // Constructors
    //================================================================================

    Detail(String label, String hint, T value, Context context) {
        this.label = label;
        this.hint = hint;
        mContext = context;
        initValue(value);
    }

    //================================================================================
    // Abstract methods
    //================================================================================

    /**
     * Listener to attach to the View displaying the value
     *
     * @param valueView the View on which the listener is attached
     */
    public abstract void setValueViewListener(EditText valueView);

    /**
     * Perform setup operations on the View displaying the value
     * (i.e. define the keyboard type)
     *
     * @param valueView the View on which setup is performed
     */
    public abstract void configureValueView(EditText valueView);

    /**
     * Update the string representation of the value
     * @param value the raw value of the detail
     */
    public abstract void updateStringValue(T value);


    //================================================================================
    // Methods
    //================================================================================

    /**
     * Internal setter for setting the value and triggering
     * string value update; called once on Detail instantiation
     * @param value the value to set
     */
    private void initValue(T value) {
        mValue = value;
        updateStringValue(value);
    }

    /**
     * Updates the value with a new value
     * @param newValue the new value
     */
    void updateValue(T newValue) {
        mValue = newValue;
        updateStringValue(newValue);
        setter.run();
    }

    /**
     * Implementation-internal setter for defining
     * a string representation of the value
     *
     * @param value string value to set
     */
    void setStringValue(String value) {
        stringValue = value;
    }

    //================================================================================
    // Getters methods
    //================================================================================

    public String getLabel() {
        return this.label;
    }

    public String getHint() {
        return this.hint;
    }

    public T getValue() {
        return mValue;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public Runnable getSetter() {
        return this.setter;
    }

    public Context getContext() {
        return mContext;
    }

    //================================================================================
    // Setters methods
    //================================================================================

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setSetter(Runnable setter) {
        this.setter = setter;
    }

}
