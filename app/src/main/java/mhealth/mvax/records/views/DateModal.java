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
package mhealth.mvax.records.views;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import mhealth.mvax.R;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Modal for selecting a date via a DatePicker
 */
public class DateModal extends TypeModal<Long> {

    //================================================================================
    // Properties
    //================================================================================

    private DatePicker mDatePicker;

    //================================================================================
    // Constructors
    //================================================================================

    public DateModal(Long value, Context context) {
        super(value, context);
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
    @Override
    public void setPositiveButtonAction(final TypeRunnable<Long> dateRunnable) {
        this.setPositiveButton(R.string.modal_date_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                final int day = mDatePicker.getDayOfMonth();
                final int month = mDatePicker.getMonth() + 1;
                final int year = mDatePicker.getYear();
                final long millis = new LocalDate(year, month, day).toDate().getTime();
                dateRunnable.run(millis);
            }
        });
    }

    @Override
    public void setNeutralButtonAction(DialogInterface.OnClickListener listener) {
        this.setNeutralButton(R.string.modal_date_neutral, listener);
    }

    //================================================================================
    // Private methods
    //================================================================================

    @Override
    void initBuilder() {
        setTitle(R.string.modal_date_title);

        // render the date picker
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.modal_choose_date, null);
        setView(dialogView);
        mDatePicker = dialogView.findViewById(R.id.date_picker);

        // if value is already defined, select the respective date in the calendar
        if (mValue != null) {
            final LocalDate date = new LocalDate(mValue);
            mDatePicker.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        }
    }

}
