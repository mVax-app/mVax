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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Sex;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 * <p>
 * Modal for selecting a Sex via a radio group
 */
public class SexModal extends TypeModal<Sex> {

    private RadioGroup mRadioGroup;

    public SexModal(Sex value, View view) {
        super(value, view);
    }

    @Override
    public AlertDialog initBuilder() {
        mDialog = new AlertDialog.Builder(getContext())
                .setView(LayoutInflater.from(getContext()).inflate(R.layout.modal_sex_picker, mParent, false))
                .setTitle(R.string.modal_sex_title)
                .create();

        mRadioGroup = mDialog.findViewById(R.id.choose_sex_radio_group);
        // if a value is already defined, check the respective Sex in the radio group
        if (mValue == Sex.MALE) {
            mRadioGroup.check(R.id.male_radio_button);
        } else if (mValue == Sex.FEMALE) {
            mRadioGroup.check(R.id.female_radio_button);
        }
        return mDialog;
    }

    /**
     * Set an action to be called when the modal's positive button is clicked;
     * provides chosen sex to action, represented by Sex enum
     *
     * @param sexRunnable SexRunnable that contains the code to be called
     */
    @Override
    public void setPositiveButtonAction(final TypeRunnable<Sex> sexRunnable) {
        this.setPositiveButton(R.string.modal_sex_confirm, (dialogInterface, which) -> {
            final int chosenButtonId = mRadioGroup.getCheckedRadioButtonId();
            final int chosenIdx = mRadioGroup.indexOfChild(mRadioGroup.findViewById(chosenButtonId));
            switch (chosenIdx) {
                case 0: // male (first item) chosen
                    mValue = Sex.MALE;
                    break;
                case 1: // female (second item) chosen
                    mValue = Sex.FEMALE;
                    break;
                default: // nothing chosen
                    break;
            }
            sexRunnable.run(mValue);
        });
    }

    @Override
    public void setNeutralButtonAction(DialogInterface.OnClickListener listener) {
        setNeutralButton(R.string.modal_sex_neutral, listener);
    }

}
