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
package mhealth.mvax.auth.modals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Steilberg
 * <p>
 * Abstract class the standardizes auth-related AlertDialogs
 */
public abstract class CustomModal extends AlertDialog.Builder {

    private Activity mActivity;
    private View mView;
    private Resources mResources;

    AlertDialog mBuilder;
    ProgressBar mSpinner;
    List<View> mViews;

    CustomModal(View view) {
        super(view.getContext());
        mActivity = (Activity) view.getContext();
        mView = view;
        mResources = view.getResources();
        mViews = new ArrayList<>();
    }

    /**
     * Build and display the modal
     */
    @Override
    public AlertDialog show() {
        AlertDialog dialog = createDialog();
        dialog.show();
        return dialog;
    }

    /**
     * Perform any operations necessary to create the custom modal dialog
     *
     * @return the newly created AlertDialog
     */
    abstract AlertDialog createDialog();

    Activity getActivity() {
        return mActivity;
    }

    View getView() {
        return mView;
    }

    String getString(int id) {
        return mResources.getString(id);
    }

    void showSpinner() {
        mViews.forEach(view -> view.setVisibility(View.INVISIBLE));
        mSpinner.setVisibility(View.VISIBLE);
    }

    void hideSpinner() {
        mViews.forEach(view -> view.setVisibility(View.VISIBLE));
        mSpinner.setVisibility(View.INVISIBLE);
    }

}
