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
import mhealth.mvax.records.utilities.TypeRunnable;
import mhealth.mvax.utilities.StringFetcher;
import mhealth.mvax.records.modals.SexModal;

/**
 * @author Robert Steilberg
 * <p>
 * Detail for storing Sex fields
 */

public class SexDetail extends Detail<Sex> {

    public SexDetail(Sex sex, int labelStringId, int hintStringId, boolean required) {
        super(sex, labelStringId, hintStringId, required);
    }

    @Override
    public void getValueViewListener(EditText valueView) {
        final TypeRunnable<Sex> positiveAction = sex -> {
            setValue(sex);
            valueView.setText(mStringValue);
        };
        final DialogInterface.OnClickListener neutralAction = (dialog, which) -> {
            setValue(null);
            valueView.setText(mStringValue);
        };
        final SexModal sexModal = new SexModal(getValue(), positiveAction, neutralAction, valueView);
        sexModal.createAndShow();
    }

    @Override
    public void configureValueView(EditText valueView) {
        valueView.setFocusable(false); // disable interaction because of the dialog
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
