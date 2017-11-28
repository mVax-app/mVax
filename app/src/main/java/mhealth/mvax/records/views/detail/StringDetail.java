package mhealth.mvax.records.views.detail;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author Robert Steilberg
 *         <p>
 *         Detail for storing String fields
 */

public class StringDetail extends Detail<String> {

    //================================================================================
    // Constructors
    //================================================================================


    public StringDetail(String label, String hint, String value, Context context) {
        super(label, hint, value, context);
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public void setValueViewListener(EditText valueView) {
        valueView.requestFocus();
        // force keyboard to pop up
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(valueView, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void configureValueView(EditText valueView) {
        valueView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    EditText val = (EditText) view;
                    updateValue(val.getText().toString());
                }
            }
        });
    }

    @Override
    public void updateStringValue(String value) {
        setStringValue(value);
    }

}