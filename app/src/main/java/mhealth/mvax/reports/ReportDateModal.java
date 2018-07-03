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
package mhealth.mvax.reports;

import android.app.AlertDialog;
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
public class ReportDateModal extends CustomModal {

    private TypeRunnable<Long> mPositiveAction;

    public ReportDateModal(TypeRunnable<Long> positiveAction, View view) {
        super(view);
        mPositiveAction = positiveAction;
    }

    @Override
    public void createAndShow() {
        final View view = mInflater.inflate(R.layout.modal_report_date_picker, mParent, false);
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
        mDialog = builder.create();
        show();
    }

}
