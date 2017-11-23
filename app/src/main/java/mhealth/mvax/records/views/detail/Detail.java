package mhealth.mvax.records.views.detail;

import android.content.Context;
import android.widget.EditText;

/**
 * @author Robert Steilberg
 */

public abstract class Detail<T> {

    private String label;

    private String hint;

    private String stringValue;

    private Runnable setter;

    private Context mContext;

    private T mValue;

    Detail(String label, String hint, T value, Context context) {
        this.label = label;
        this.hint = hint;
        mContext = context;
        initValue(value);
    }

    public T getValue() {
        return mValue;
    }


    public void initValue(T value) {
        mValue = value;
        updateStringValue(value);
    }

    public void updateValue(T value) {
        mValue = value;
        updateStringValue(value);
        setter.run();
    }

    public abstract void valueViewListener(EditText valueView);

    public String getStringValue() {
        return this.stringValue;
    }

//    public void setLabel(String label) {
//        this.label = label;
//    }

    public String getLabel() {
        return this.label;
    }

    public void setSetter(Runnable setter) {
        this.setter = setter;
    }

    public Runnable getSetter() {
        return this.setter;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return this.hint;
    }

    public abstract void updateStringValue(T value);

    public abstract void configureValueView(EditText valueView);

    public Context getContext() {
        return mContext;
    }

    /**
     * internal setter
     * @param value
     */
    void setStringValue(String value) {
        stringValue = value;
    }

//    /**
//     * internal setter
//     * @param value
//     */
//    void setValue(T value) {
//        mValue = value;
//    }
}
