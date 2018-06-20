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

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.AuthInputValidator;
import mhealth.mvax.utilities.modals.CustomModal;

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
    public AlertDialog initBuilder() {
        mBuilder = new AlertDialog.Builder(mActivity)
                .setTitle(R.string.password_reset_modal_title)
                .setView(mInflater.inflate(R.layout.modal_password_reset, mParent, false))
                .setPositiveButton(R.string.submit, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        mBuilder.setOnShowListener(dialogInterface -> {

            mSpinner = mBuilder.findViewById(R.id.spinner);

            mViews.add(mBuilder.findViewById(R.id.reset_password_subtitle));
            mViews.add(mBuilder.findViewById(R.id.email));
            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE));

            final TextView emailTextView = mBuilder.findViewById(R.id.email);
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

            final Button positiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> attemptPasswordReset(emailTextView));
            mViews.add(positiveButton);
        });
        return mBuilder;
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
                Toast.makeText(mActivity, R.string.auth_fail_no_connection, Toast.LENGTH_LONG).show();
            } else { // success
                mBuilder.dismiss();
                Toast.makeText(mActivity, getString(R.string.reset_email_confirm), Toast.LENGTH_LONG).show();
            }
            hideSpinner();
        });
    }

}
