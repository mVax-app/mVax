/*
Copyright (C) 2018 Duke University

This file is part of mVax.

mVax is free software: you can redistribute it and/or
modify it under the terms of the GNU Affero General Public License
as published by the Free Software Foundation, either version 3,
or (at your option) any later version.

mVax is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU General Public
License along with mVax; see the file LICENSE. If not, see
<http://www.gnu.org/licenses/>.
*/
package mhealth.mvax.records.details.patient.detail;

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

    Detail(T value, String label, String hint, Context context) {
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
    public abstract void valueViewListener(EditText valueView);

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
