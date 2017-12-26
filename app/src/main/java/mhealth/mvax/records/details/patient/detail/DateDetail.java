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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.joda.time.LocalDate;

import mhealth.mvax.R;
import mhealth.mvax.records.details.patient.NullableDateFormat;

/**
 * @author Robert Steilberg
 *         <p>
 *         Detail for storing Date fields
 */

public class DateDetail extends Detail<Long> {

    //================================================================================
    // Constructors
    //================================================================================

    public DateDetail(Long value, String label, String hint, Context context) {
        super(value, label, hint, context);
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public void valueViewListener(final EditText valueView) {
        // TODO generalize this to a date dialog class
        final Context context = getContext();

        // init date picker dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.modal_title_choose_date));
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.modal_choose_date, null);
        builder.setView(dialogView);

        // set DatePicker date if current value is not null
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        if (getValue() != null) {
            LocalDate date = new LocalDate(getValue());
            datePicker.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        }

        builder.setPositiveButton(context.getString(R.string.modal_positive_choose_date), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                long millis = new LocalDate(year, month, day).toDate().getTime();

                updateValue(millis);
                valueView.setText(getStringValue());
            }
        });
        builder.setNegativeButton(context.getString(R.string.modal_negative_choose_date), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // TODO decide what to do about this
//        builder.setNeutralButton(context.getString(R.string.modal_neutral_choose_date), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                updateValue(null);
//                valueView.setText(getStringValue());
//
//            }
//        });

        builder.show();
    }

    @Override
    public void configureValueView(EditText valueView) {
        valueView.setFocusable(false); // disable interaction since dialog
    }

    @Override
    public void updateStringValue(Long value) {
        NullableDateFormat dateFormat = new NullableDateFormat(getContext().getString(R.string.date_format));
        setStringValue(dateFormat.getString(value));
    }
}
