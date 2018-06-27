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
package mhealth.mvax.records.record.patient.detail;

import android.widget.EditText;

import mhealth.mvax.utilities.WatcherEditText;

/**
 * @author Robert Steilberg
 * <p>
 * Abstract class for storing information about a generic Detail,
 * which is used to popuate a list when creating, editing, or
 * viewing Person data
 */
public abstract class Detail<T> {

    private T mValue; // raw value of the detail
    String mStringValue; // String representation of the value to be displayed
    private Runnable mSetter; // defines code that will set the value in the Person object
    private final int mLabelStringId; // label displayed next to value
    private final int mHintStringId; // hint displayed in the value field when there is no set value
    private final boolean mRequired;

    Detail(T value, int labelStringId, int hintStringId, boolean required) {
        this.mValue = value;
        this.mLabelStringId = labelStringId;
        this.mHintStringId = hintStringId;
        this.mRequired = required;
        updateStringValue(value);
    }

    /**
     * Perform setup operations on the EditText displaying the value
     * of the detail
     *
     * @param valueView the EditText on which setup is performed
     */
    public abstract void configureValueView(WatcherEditText valueView);

    /**
     * Listener to attach to the EditText displaying the value of the
     * detail
     *
     * @param valueView the EditText on which the listener is attached
     */
    public abstract void getValueViewListener(WatcherEditText valueView);

    /**
     * Performs operations to create the String representation of the Detail's
     * value and then sets it through setStringValue()
     *
     * @param value the raw value of the detail
     */
    public abstract void updateStringValue(T value);

    public T getValue() {
        return mValue;
    }

    public String getStringValue() {
        return this.mStringValue;
    }

    public int getLabelStringId() {
        return this.mLabelStringId;
    }

    public int getHintStringId() {
        return this.mHintStringId;
    }

    public boolean isRequired() {
        return this.mRequired;
    }

    /**
     * Updates the value of the Detail with a new value, updates the UI,
     * and runs the setter so that the Person object is updated with the
     * new value
     *
     * @param value the new value
     */
    protected void setValue(T value) {
        this.mValue = value;
        updateStringValue(value);
        mSetter.run();
    }

    /**
     * Defines the setter that is run when the Detail's value updates;
     * should be set immediately after Detail initialization
     *
     * @param setter setter that updates the value in the
     *               Person object
     */
    public void setSetter(Runnable setter) {
        this.mSetter = setter;
    }

}
