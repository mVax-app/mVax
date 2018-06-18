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

import android.widget.EditText;

import mhealth.mvax.model.record.Sex;
import mhealth.mvax.utilities.StringFetcher;
import mhealth.mvax.records.views.SexModal;

/**
 * @author Robert Steilberg
 * <p>
 * Detail for storing Sex fields
 */

public class SexDetail extends Detail<Sex> {

    public SexDetail(Sex sex, int labelStringId, int hintStringId) {
        super(sex, labelStringId, hintStringId);
    }

    @Override
    public void getValueViewListener(final EditText valueView) {
        final SexModal sexModal = new SexModal(getValue(), valueView.getContext());

        sexModal.setPositiveButtonAction(sex -> {
            setValue(sex);
            valueView.setText(mStringValue);
        });
        sexModal.setNeutralButtonAction((dialogInterface, which) -> {
            setValue(null);
            valueView.setText(mStringValue);
        });

        sexModal.show();
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
