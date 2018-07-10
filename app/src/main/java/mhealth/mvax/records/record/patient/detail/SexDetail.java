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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import mhealth.mvax.model.record.Sex;
import mhealth.mvax.records.utilities.TypeRunnable;
import mhealth.mvax.records.modals.SexModal;
import mhealth.mvax.records.utilities.WatcherEditText;

/**
 * @author Robert Steilberg
 * <p>
 * Detail for storing Sex fields
 */

public class SexDetail extends Detail<Sex> {

    public SexDetail(Sex sex, int labelStringId, int hintStringId, boolean required, Context context) {
        super(sex, labelStringId, hintStringId, required, context);
    }

    @Override
    public void getValueViewListener(Activity activity, WatcherEditText valueView) {
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
    public void configureValueView(WatcherEditText valueView) {
        valueView.setFocusable(false); // disable interaction because of the modal
    }

    @Override
    public void updateStringValue(Sex sex) {
        if (sex != null) {
            mStringValue = getContext().getString(sex.getResourceId());
        } else {
            mStringValue = "";
        }
    }

}
