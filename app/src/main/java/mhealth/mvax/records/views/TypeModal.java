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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.TextView;

import mhealth.mvax.R;
import mhealth.mvax.records.utilities.TypeRunnable;

/**
 * @author Robert Steilberg
 *         <p>
 *         Abstract generic class for displaying a modal to choose
 *         a value of type T
 */
abstract class TypeModal<T> extends AlertDialog.Builder {

    //================================================================================
    // Properties
    //================================================================================

    T mValue;

    //================================================================================
    // Constructors
    //================================================================================

    TypeModal(T value, Context context) {
        super(context);
        this.mValue = value;

        initBuilder();

        // set negative button to cancel action and close modal
        setNegativeButton(getContext().getString(R.string.modal_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    /**
     * Initialize builder view and perform any setup operations
     */
    abstract void initBuilder();

    /**
     * Set an action to be called when the modal's positive button is clicked
     *
     * @param runnable contains code to be called taking in a param of type T
     */
    abstract void setPositiveButtonAction(TypeRunnable<T> runnable);

    /**
     * Set an action to be called when the modal's neutral button is clicked
     *
     * @param listener DialogInterface.OnClickListener that contains the code
     *                 to be called
     */
    abstract void setNeutralButtonAction(DialogInterface.OnClickListener listener);

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public AlertDialog show() {
        AlertDialog dialog = super.show();
        int textSize = 22;
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextSize(textSize);
        dialog.getButton(Dialog.BUTTON_NEUTRAL).setTextSize(textSize);
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextSize(textSize);
        return null;
    }

    @Override
    public AlertDialog.Builder setTitle(CharSequence title) {
        TextView titleView = getTitleTextView();
        titleView.setText(title);
        setCustomTitle(titleView);
        return null;
    }

    @Override
    public AlertDialog.Builder setTitle(int titleStringId) {
        TextView titleView = getTitleTextView();
        titleView.setText(titleStringId);
        setCustomTitle(titleView);
        return null;
    }

    //================================================================================
    // Private methods
    //================================================================================

    private TextView getTitleTextView() {
        TextView titleView = new TextView(getContext());
        titleView.setTextSize(30);
        titleView.setPaddingRelative(50,50,50,0);
        titleView.setTextColor(Color.BLACK);
        return titleView;
    }
}
