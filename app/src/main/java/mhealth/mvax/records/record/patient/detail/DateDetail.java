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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.EditText;

import mhealth.mvax.R;
import mhealth.mvax.records.utilities.NullableDateFormat;
import mhealth.mvax.records.utilities.StringFetcher;
import mhealth.mvax.records.views.DateModal;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Detail for storing Date fields
 */

public class DateDetail extends Detail<Long> {

    //================================================================================
    // Constructors
    //================================================================================

    public DateDetail(Long value, int labelStringId, int hintStringId) {
        super(value, labelStringId, hintStringId);
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public void getValueViewListener(final EditText valueView) {
        final DateModal dateModal = new DateModal(getValue(), valueView.getContext());

        dateModal.setPositiveButtonAction(new TypeRunnable<Long>() {
            @Override
            public void run(Long date) {
                setValue(date);
                valueView.setText(mStringValue);
            }
        });

        dateModal.setNeutralButtonAction(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setValue(null);
                valueView.setText(mStringValue);
            }
        });

        dateModal.show();
    }

    @Override
    public void configureValueView(EditText valueView) {
        valueView.setFocusable(false); // disable interaction because dialog
    }

    @Override
    public void updateStringValue(Long date) {
        String dateFormat = StringFetcher.fetchString(R.string.date_format);
        final NullableDateFormat formatter = new NullableDateFormat(dateFormat);
        mStringValue = formatter.getString(date);
    }
}
