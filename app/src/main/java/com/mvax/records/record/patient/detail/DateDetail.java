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
package com.mvax.records.record.patient.detail;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.LocalDate;

import java.util.Calendar;

import com.mvax.R;
import com.mvax.records.utilities.NullableDateFormat;
import com.mvax.records.utilities.WatcherEditText;

/**
 * @author Robert Steilberg
 * <p>
 * Detail for storing Date fields
 */
public class DateDetail extends Detail<Long> implements DatePickerDialog.OnDateSetListener {

    private EditText mValueView;

    public DateDetail(Long value, int labelStringId, int hintStringId, boolean required, Context context) {
        super(value, labelStringId, hintStringId, required, context);
    }

    @Override
    public void getValueViewListener(Activity activity, WatcherEditText valueView) {
        mValueView = valueView;
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        if (getValue() != null) {
            final LocalDate date = new LocalDate(getValue());
            day = date.getDayOfMonth();
            month = date.getMonthOfYear() - 1;
            year = date.getYear();
        }
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                DateDetail.this,
                year, month, day);
        final int dukeBlue = ContextCompat.getColor(valueView.getContext(), R.color.dukeBlue);
        datePicker.setAccentColor(dukeBlue);
        datePicker.show(activity.getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        final long date = new LocalDate(year, month + 1, day).toDate().getTime();
        setValue(date);
        mValueView.setText(mStringValue);
        mValueView.setError(null);
    }

    @Override
    public void configureValueView(WatcherEditText valueView) {
        valueView.setFocusable(false); // disable interaction because of the modal
    }

    @Override
    public void updateStringValue(Long date) {
        mStringValue = NullableDateFormat.getString(getContext(), date);
    }
}
