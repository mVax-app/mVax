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
package com.mvax.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.mvax.R;
import com.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal for displaying an alert displaying the number of currently overdue
 * patients
 */
public class OverdueAlertModal extends CustomModal {

    private int mNumOverduePatients;
    private Runnable mViewAction;

    public OverdueAlertModal(View view, int numOverduePatients, Runnable viewAction) {
        super(view);
        mNumOverduePatients = numOverduePatients;
        mViewAction = viewAction;
    }

    @Override
    public void createAndShow() {
        mDialog = new AlertDialog.Builder(mContext)
                .setView(mInflater.inflate(R.layout.modal_overdue_patients_alert, mParent, false))
                .setPositiveButton(R.string.go_to_alerts, null)
                .setNegativeButton(R.string.dismiss, null)
                .create();

        mDialog.setOnShowListener(dialogInterface -> {
            int alertTextStringId;
            if (mNumOverduePatients == 1) {
                alertTextStringId = R.string.overdue_patients_alert_singular;
            } else {
                alertTextStringId = R.string.overdue_patients_alert_plural;
            }

            final String alertText = String.format(getString(alertTextStringId),
                    mNumOverduePatients);
            TextView alert = mDialog.findViewById(R.id.alert);
            alert.setText(alertText);

            mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
                mViewAction.run();
                dismiss();
            });
        });

        show();
    }

}
