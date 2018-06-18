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

import mhealth.mvax.R;
import mhealth.mvax.records.utilities.NullableDateFormat;
import mhealth.mvax.utilities.StringFetcher;
import mhealth.mvax.records.views.DateModal;

/**
 * @author Robert Steilberg
 * <p>
 * Detail for storing Date fields
 */

public class DateDetail extends Detail<Long> {

    public DateDetail(Long value, int labelStringId, int hintStringId) {
        super(value, labelStringId, hintStringId);
    }

    @Override
    public void getValueViewListener(final EditText valueView) {
        final DateModal dateModal = new DateModal(getValue(), valueView.getContext());

        dateModal.setPositiveButtonAction(date -> {
            setValue(date);
            valueView.setText(mStringValue);
        });
        dateModal.setNeutralButtonAction((dialogInterface, i) -> {
            setValue(null);
            valueView.setText(mStringValue);
        });

        dateModal.show();
    }

    @Override
    public void configureValueView(EditText valueView) {
        valueView.setFocusable(false); // disable interaction because of the dialog
    }

    @Override
    public void updateStringValue(Long date) {
        String dateFormat = StringFetcher.fetchString(R.string.date_format);
        final NullableDateFormat formatter = new NullableDateFormat(dateFormat);
        mStringValue = formatter.getString(date);
    }
}
