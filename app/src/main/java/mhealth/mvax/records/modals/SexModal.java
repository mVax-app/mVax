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
import android.widget.RadioGroup;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Sex;
import mhealth.mvax.records.utilities.TypeRunnable;
import mhealth.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal for selecting a Sex via a radio group
 */
public class SexModal extends CustomModal {

    private Sex mValue;
    private TypeRunnable<Sex> mPositiveAction;
    private DialogInterface.OnClickListener mNeutralAction;

    public SexModal(Sex value, TypeRunnable<Sex> positiveAction, DialogInterface.OnClickListener neutralAction, View view) {
        super(view);
        mValue = value;
        mPositiveAction = positiveAction;
        mNeutralAction = neutralAction;
    }

    @Override
    public void createAndShow() {
        final View view = mInflater.inflate(R.layout.modal_choose_sex, mParent, false);
        final RadioGroup radioGroup = view.findViewById(R.id.sex_radio_group);

        mDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .setPositiveButton(R.string.modal_sex_confirm, (dialog, which) -> {
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.male_radio_button:
                            mValue = Sex.MALE;
                            break;
                        case R.id.female_radio_button:
                            mValue = Sex.FEMALE;
                            break;
                        default:
                            break;
                    }
                    mPositiveAction.run(mValue);
                })
                .setNegativeButton(R.string.modal_cancel, (dialog, which) -> dialog.cancel())
                .setNeutralButton(R.string.modal_sex_neutral, mNeutralAction)
                .create();

        // if a value is already defined, check the respective Sex in the radio group
        if (mValue == Sex.MALE) {
            radioGroup.check(R.id.male_radio_button);
        } else if (mValue == Sex.FEMALE) {
            radioGroup.check(R.id.female_radio_button);
        }
        show();
    }

}
