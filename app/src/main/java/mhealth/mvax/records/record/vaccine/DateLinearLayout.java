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
package mhealth.mvax.records.record.vaccine;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Robert Steilberg
 *         <p>
 *         Custom horizontal linear layout for defining
 *         a labeled TextView for displaying a date on
 *         which a listener can be set
 */
public class DateLinearLayout extends LinearLayout {

    //================================================================================
    // Properties
    //================================================================================

    private TextView mLabelView;
    private TextView mDateView;

    //================================================================================
    // Constructors
    //================================================================================

    public DateLinearLayout(Context context) {
        super(context);
        initLayout();
        initLabel();
        initDateView();
    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * @param label String value of the label
     */
    public void setLabel(String label) {
        this.mLabelView.setText(label);
    }

    /**
     * @param date String value of the date TextView
     *             (should be a date formatted by
     *             NullableDateFormat)
     */
    public void setDate(String date) {
        this.mDateView.setText(date);
    }

    /**
     * @param listener listener to be called when the date TextView is clicked
     */
    public void setDateViewOnClickListener(OnClickListener listener) {
        this.mDateView.setOnClickListener(listener);
    }

    //================================================================================
    // Private methods
    //================================================================================

    private void initLayout() {
        this.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        this.setPadding(0, 15, 0, 15);
    }

    private void initLabel() {
        mLabelView = new TextView(getContext());
        mLabelView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        mLabelView.setTextSize(22);
        mLabelView.setGravity(Gravity.CENTER);
        mLabelView.setPadding(0, 0, 15, 0);

        this.addView(mLabelView);
    }

    private void initDateView() {
        mDateView = new TextView(getContext());
        mDateView.setLayoutParams(new LinearLayout.LayoutParams(
                250,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        mDateView.setPadding(5, 5, 5, 5);
        mDateView.setGravity(Gravity.CENTER);
        mDateView.setTextSize(22);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.LTGRAY);
        mDateView.setBackground(gd);

        this.addView(mDateView);
    }
}
