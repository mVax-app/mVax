package mhealth.mvax.records.views.detail;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

/**
 * @author Robert Steilberg
 *         <p>
 *         Detail for storing String fields represented by numbers
 *         only
 */

public class StringNumberDetail extends StringDetail {

    //================================================================================
    // Constructors
    //================================================================================


    public StringNumberDetail(String label, String hint, String value, Context context) {
        super(label, hint, value, context);
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public void configureValueView(EditText valueView) {
        super.configureValueView(valueView);
        valueView.setInputType(InputType.TYPE_CLASS_NUMBER); // number keypad for input
    }

}
