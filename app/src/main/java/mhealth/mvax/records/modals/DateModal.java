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
package mhealth.mvax.records.modals;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import mhealth.mvax.R;
import mhealth.mvax.records.utilities.TypeRunnable;
import mhealth.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal for selecting a date via a DatePicker
 */
public class DateModal extends CustomModal {

    private Long mDate;
    private TypeRunnable<Long> mPositiveAction;
    private DialogInterface.OnClickListener mNeutralAction;

    public DateModal(Long date, TypeRunnable<Long> positiveAction, DialogInterface.OnClickListener neutralAction, View view) {
        super(view);
        mDate = date;
        mPositiveAction = positiveAction;
        mNeutralAction = neutralAction;
    }

    public DateModal(Long value, TypeRunnable<Long> positiveAction, View view) {
        super(view);
        mDate = value;
        mPositiveAction = positiveAction;
    }

    @Override
    public void createAndShow() {
        final View view = mInflater.inflate(R.layout.modal_choose_date, mParent, false);
        final DatePicker datePicker = view.findViewById(R.id.date_picker);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setView(view)
                .setPositiveButton(R.string.modal_date_confirm, (dialog, which) -> {
                    final int day = datePicker.getDayOfMonth();
                    final int month = datePicker.getMonth() + 1;
                    final int year = datePicker.getYear();
                    final long millis = new LocalDate(year, month, day).toDate().getTime();
                    mPositiveAction.run(millis);
                })
                .setNegativeButton(R.string.modal_cancel, (dialog, which) -> dialog.cancel());
        if (mNeutralAction != null) builder.setNeutralButton(R.string.modal_date_neutral, mNeutralAction);
        mDialog = builder.create();

        // if value is already defined, select the respective date in the calendar
        if (mDate != null) {
            final LocalDate date = new LocalDate(mDate);
            datePicker.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        }
        show();
    }

}
