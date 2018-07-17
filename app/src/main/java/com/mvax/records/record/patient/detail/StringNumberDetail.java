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

import android.content.Context;
import android.text.InputType;

import com.mvax.records.utilities.WatcherEditText;

/**
 * @author Robert Steilberg
 * <p>
 * Detail for storing String fields which will ONLY be represented by
 * numbers
 */

public class StringNumberDetail extends StringDetail {

    public StringNumberDetail(String value, int labelStringId, int hintStringId, boolean required, Context context) {
        super(value, labelStringId, hintStringId, required, context);
    }

    @Override
    public void configureValueView(WatcherEditText valueView) {
        super.configureValueView(valueView);
        valueView.setInputType(InputType.TYPE_CLASS_NUMBER); // number keypad for input
    }

}
