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

import android.content.DialogInterface;
import android.widget.EditText;

import mhealth.mvax.model.record.Sex;
import mhealth.mvax.records.utilities.StringFetcher;
import mhealth.mvax.records.views.SexModal;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Detail for storing Sex fields
 */

public class SexDetail extends Detail<Sex> {

    //================================================================================
    // Constructors
    //================================================================================

    public SexDetail(Sex sex, int labelStringId, int hintStringId) {
        super(sex, labelStringId, hintStringId);
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public void getValueViewListener(final EditText valueView) {
        final SexModal sexModal = new SexModal(getValue(), valueView.getContext());

        sexModal.setPositiveButtonAction(new TypeRunnable<Sex>() {
            @Override
            public void run(Sex sex) {
                setValue(sex);
                valueView.setText(mStringValue);
            }
        });

        sexModal.setNeutralButtonAction(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                setValue(null);
                valueView.setText(mStringValue);
            }
        });

        sexModal.show();
    }

    @Override
    public void configureValueView(EditText valueView) {
        valueView.setFocusable(false); // disable interaction because dialog
    }

    @Override
    public void updateStringValue(Sex sex) {
        if (sex != null) {
            mStringValue = StringFetcher.fetchString(sex.getResourceId());
        } else {
            mStringValue = "";
        }
    }

}
