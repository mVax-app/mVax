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

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import mhealth.mvax.utilities.WatcherEditText;

/**
 * @author Robert Steilberg
 * <p>
 * Detail for storing String values
 */

public class StringDetail extends Detail<String> {

    public StringDetail(String value, int labelStringId, int hintStringId, boolean required) {
        super(value, labelStringId, hintStringId, required);
    }

    @Override
    public void getValueViewListener(WatcherEditText valueView) {
        valueView.requestFocus();
        // force keyboard to appear
        final InputMethodManager imm = (InputMethodManager) valueView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    public void configureValueView(WatcherEditText valueView) {
        valueView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        valueView.setFocusable(true);
        valueView.clearTextChangedListeners();
        valueView.addTextChangedListener(new TextWatcher() { // set new value every time text is changed
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                setValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void updateStringValue(String value) {
        mStringValue = value;
    }

}
