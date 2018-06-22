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

import android.content.DialogInterface;
import android.widget.EditText;

import mhealth.mvax.R;
import mhealth.mvax.records.utilities.NullableDateFormat;
import mhealth.mvax.records.utilities.TypeRunnable;
import mhealth.mvax.utilities.StringFetcher;
import mhealth.mvax.records.modals.DateModal;

/**
 * @author Robert Steilberg
 * <p>
 * Detail for storing Date fields
 */

public class DateDetail extends Detail<Long> {

    public DateDetail(Long value, int labelStringId, int hintStringId, boolean required) {
        super(value, labelStringId, hintStringId, required);
    }

    @Override
    public void getValueViewListener(EditText valueView) {
        final TypeRunnable<Long> positiveAction = date -> {
            setValue(date);
            valueView.setText(mStringValue);
        };
        final DialogInterface.OnClickListener neutralAction = (dialog, which) -> {
            setValue(null);
            valueView.setText(mStringValue);
        };
        final DateModal dateModal = new DateModal(getValue(), positiveAction, neutralAction, valueView);
        dateModal.createAndShow();
    }

    @Override
    public void configureValueView(EditText valueView) {
        valueView.setFocusable(false); // disable interaction because of the dialog
    }

    @Override
    public void updateStringValue(Long date) {
        mStringValue = NullableDateFormat.getString(date);
    }
}
