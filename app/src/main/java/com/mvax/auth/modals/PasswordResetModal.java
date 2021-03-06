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
package com.mvax.auth.modals;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;

import com.mvax.R;
import com.mvax.auth.utilities.AuthInputValidator;
import com.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for resetting an mVax user's password
 */
public class PasswordResetModal extends CustomModal {

    public PasswordResetModal(View view) {
        super(view);
    }

    @Override
    public void createAndShow() {
        mDialog = new AlertDialog.Builder(mContext)
                .setView(mInflater.inflate(R.layout.modal_reset_password, mParent, false))
                .setPositiveButton(R.string.submit, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        mDialog.setOnShowListener(dialogInterface -> {
            mSpinner = mDialog.findViewById(R.id.search_spinner);

            mViews.add(mDialog.findViewById(R.id.reset_password_subtitle));
            mViews.add(mDialog.findViewById(R.id.email));

            final TextView emailTextView = mDialog.findViewById(R.id.email);
            emailTextView.setOnEditorActionListener((v, actionId, event) -> {
                if (event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN // debounce
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // enter on hardware keyboard submits reset request
                    attemptPasswordReset(emailTextView);
                    return true;
                }
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // "Done" button submits reset request
                    attemptPasswordReset(emailTextView);
                    return true;
                }
                return false;
            });

            final Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> attemptPasswordReset(emailTextView));
        });
        show();
    }

    private void attemptPasswordReset(final TextView emailTextView) {
        final String emailAddress = emailTextView.getText().toString();
        if (TextUtils.isEmpty(emailAddress)) {
            emailTextView.setError(getString(R.string.empty_field));
            emailTextView.requestFocus();
        } else if (!AuthInputValidator.emailValid(emailAddress)) {
            emailTextView.setError(getString(R.string.invalid_email_error));
            emailTextView.requestFocus();
        } else {
            showSpinner();
            sendResetEmail(emailAddress);
        }
    }

    private void sendResetEmail(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(task -> {
            if (task.getException() instanceof FirebaseNetworkException) {
                // only show error for no internet; don't let user know if email
                // isn't associated with an account
                Toast.makeText(mContext, R.string.auth_fail_no_connection, Toast.LENGTH_LONG).show();
            } else { // success
                dismiss();
                Toast.makeText(mContext, getString(R.string.reset_email_confirm), Toast.LENGTH_LONG).show();
            }
            hideSpinner();
        });
    }

}
