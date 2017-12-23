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
package mhealth.mvax.records.views.detail;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
    public void valueViewListener(EditText valueView) {
        valueView.requestFocus();
        // force keyboard to pop up
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(valueView, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void configureValueView(final EditText valueView) {
        // auto-capitalize every word
        valueView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        valueView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void updateStringValue(String value) {
        setStringValue(value);
    }

}
