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
import android.widget.RadioGroup;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Sex;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Modal for selecting a Sex via a RadioGroup
 */
public class SexModal extends TypeModal<Sex> {

    //================================================================================
    // Properties
    //================================================================================

    private RadioGroup mRadioGroup;

    //================================================================================
    // Constructors
    //================================================================================

    public SexModal(Sex value, Context context) {
        super(value, context);
    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Set an action to be called when the modal's positive button is clicked;
     * provides chosen sex to action, represented by Sex enum
     *
     * @param sexRunnable SexRunnable that contains the code to be called
     */
    @Override
    public void setPositiveButtonAction(final TypeRunnable<Sex> sexRunnable) {
        this.setPositiveButton(R.string.modal_sex_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
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
            }
        });
    }

    @Override
    public void setNeutralButtonAction(DialogInterface.OnClickListener listener) {
        setNeutralButton(R.string.modal_sex_neutral, listener);
    }

    //================================================================================
    // Private methods
    //================================================================================

    @Override
    void initBuilder() {
        setTitle(R.string.modal_sex_title);

        // render the radio group
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View dialogView = inflater.inflate(R.layout.modal_choose_sex, null);
        setView(dialogView);
        mRadioGroup = dialogView.findViewById(R.id.choose_sex_radio_group);

        // if a value is already defined, check the respective Sex in the radio group
        if (mValue == Sex.MALE) {
            mRadioGroup.check(R.id.radiobutton_male);
        } else if (mValue == Sex.FEMALE) {
            mRadioGroup.check(R.id.radiobutton_female);
        }
    }

}
