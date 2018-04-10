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
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.AuthInputValidator;

/**
 * @author Robert Steilberg
 * <p>
 * Displays a modal for requesting a new mVax account
 */
public class DenyUserRequestModal extends AlertDialog.Builder {

    private Activity mActivity;
    private View mView;
    private Resources mResources;
    private AlertDialog mBuilder;

    private TextView mSubtitleView;
    private TextView mNameView;
    private TextView mEmailView;
    private TextView mConfirmEmailView;
    private ProgressBar mSpinner;
    private Button mPositiveButton;
    private Button mNegativeButton;

    public DenyUserRequestModal(View view) {
        super(view.getContext());
        mActivity = (Activity) view.getContext();
        mView = view;
        mResources = view.getResources();
    }

    /**
     * Build and display the modal to request a new user account
     */
    @Override
    public AlertDialog show() {
        initBuilder().show();
        return null;
    }

    private AlertDialog initBuilder() {
        mBuilder = new AlertDialog.Builder(mActivity)
                .setTitle(mResources.getString(R.string.modal_deny_user_request_title))
                .setMessage(R.string.modal_deny_user_request_message)
                .setPositiveButton(mResources.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(mResources.getString(R.string.button_reset_password_cancel), null)
                .create();

        return mBuilder;
    }



    private void toggleSpinner(boolean showSpinner) {
        if (showSpinner) {
            mSubtitleView.setVisibility(View.INVISIBLE);
            mNameView.setVisibility(View.INVISIBLE);
            mEmailView.setVisibility(View.INVISIBLE);
            mConfirmEmailView.setVisibility(View.INVISIBLE);
            mPositiveButton.setVisibility(View.INVISIBLE);
            mNegativeButton.setVisibility(View.INVISIBLE);
            mSpinner.setVisibility(View.VISIBLE);
        } else {
            mSubtitleView.setVisibility(View.VISIBLE);
            mNameView.setVisibility(View.VISIBLE);
            mEmailView.setVisibility(View.VISIBLE);
            mConfirmEmailView.setVisibility(View.VISIBLE);
            mPositiveButton.setVisibility(View.VISIBLE);
            mNegativeButton.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.INVISIBLE);
        }
    }

}
