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
package mhealth.mvax.records.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 *         <p>
 *         Modal for selecting a date via a DatePicker
 */
public class DateModal extends AlertDialog.Builder {

    //================================================================================
    // Properties
    //================================================================================

    private DatePicker mDatePicker;

    //================================================================================
    // Constructors
    //================================================================================

    public DateModal(Context context) {
        super(context);
        initBuilder();
    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Set an action to be called when the modal's positive button is clicked;
     * provides chosen date to action, represented by milliseconds since Unix
     * epoch
     *
     * @param dateRunnable DateRunnable that contains the code to be called
     */
    public void setPositiveButtonAction(final DateRunnable dateRunnable) {
        this.setPositiveButton(R.string.modal_date_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
//                cal.set(Calendar.MONTH, mDatePicker.getMonth());
//                cal.set(Calendar.YEAR, mDatePicker.getYear());
                cal.set(mDatePicker.getYear(),
                        mDatePicker.getMonth(),
                        mDatePicker.getDayOfMonth(),
                        0,
                        0,
                        0);
                final Long date = cal.getTimeInMillis();
                dateRunnable.run(date);
            }
        });
    }

    /**
     * Set an action to be called when the modal's neutral button is clicked
     *
     * @param listener DialogInterface.OnClickListener that contains the code
     *                 to be called
     */
    public void setNeutralButtonAction(DialogInterface.OnClickListener listener) {
        this.setNeutralButton(R.string.modal_date_neutral, listener);
    }

    //================================================================================
    // Private methods
    //================================================================================

    private void initBuilder() {
        this.setTitle(R.string.modal_date_title);
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.modal_choose_date, null);
        this.setView(dialogView);
        mDatePicker = dialogView.findViewById(R.id.date_picker);
        this.setNegativeButton(getContext().getString(R.string.modal_date_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

}
