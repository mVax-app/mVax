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

import mhealth.mvax.model.record.Dose;
import mhealth.mvax.model.record.Vaccine;

/**
 * @author Robert Steilberg
 *
 * Adds functionality to TextView for storing Vaccine and Dose objects
 */

public class DoseDateView extends android.support.v7.widget.AppCompatTextView {

    //================================================================================
    // Properties
    //================================================================================

    private Dose mDose;

    //================================================================================
    // Constructors
    //================================================================================

    public DoseDateView(Context context, Dose dose) {
        super(context);
        mDose = dose;
    }

    //================================================================================
    // Getters
    //================================================================================

    public Dose getDose() {
        return mDose;
    }

    //================================================================================
    // Setters
    //================================================================================

    public void setDose(Dose dose) {
        mDose = dose;
    }
}
