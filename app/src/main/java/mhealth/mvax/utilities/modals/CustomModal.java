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
package mhealth.mvax.utilities.modals;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Steilberg
 * <p>
 * Abstract class the standardizes auth-related AlertDialogs
 */
public abstract class CustomModal {

    protected Context mContext;
    protected ViewGroup mParent;
    protected LayoutInflater mInflater;

    protected AlertDialog mDialog;
    protected ProgressBar mSpinner;
    protected List<View> mViews;

    public CustomModal(View view) {
        mContext = view.getContext();
        mParent = (ViewGroup) view.getParent();
        mInflater = LayoutInflater.from(mContext);
        mViews = new ArrayList<>();
    }

    public abstract void createAndShow();

    public void dismiss() {
        mDialog.dismiss();
    }

    protected String getString(int id) {
        return mContext.getResources().getString(id);
    }

    protected void showSpinner() {
        mViews.forEach(view -> view.setVisibility(View.INVISIBLE));
        mSpinner.setVisibility(View.VISIBLE);
    }

    protected void hideSpinner() {
        mViews.forEach(view -> view.setVisibility(View.VISIBLE));
        mSpinner.setVisibility(View.INVISIBLE);
    }

}
