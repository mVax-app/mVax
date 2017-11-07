package mhealth.mvax.model;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

/**
 * @author Robert Steilberg
 */

public class StringNumberDetail extends StringDetail {

    StringNumberDetail(String label, String hint, String value, Context context) {
        super(label, hint, value, context);
    }

    @Override
    public void configureValueView(EditText valueView) {
        super.configureValueView(valueView);
        valueView.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

}
