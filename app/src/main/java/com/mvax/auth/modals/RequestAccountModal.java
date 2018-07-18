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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mvax.R;
import com.mvax.auth.utilities.AuthInputValidator;
import com.mvax.auth.utilities.FirebaseUtilities;
import com.mvax.auth.utilities.Mailer;
import com.mvax.model.user.User;
import com.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for requesting a new mVax account
 */
public class RequestAccountModal extends CustomModal {

    private TextView mDisplayName;
    private TextView mEmail;
    private TextView mConfirmEmail;
    private TextView mPassword;
    private TextView mConfirmPassword;

    public RequestAccountModal(View view) {
        super(view);
    }

    @Override
    public void createAndShow() {
        mDialog = new AlertDialog.Builder(mContext)
                .setView(mInflater.inflate(R.layout.modal_request_account, mParent, false))
                .setPositiveButton(getString(R.string.submit), null)
                .setNegativeButton(getString(R.string.cancel), null)
                .create();

        mDialog.setOnShowListener(dialogInterface -> {
            mSpinner = mDialog.findViewById(R.id.search_spinner);

            mViews.add(mDialog.findViewById(R.id.request_subtitle));

            mDisplayName = mDialog.findViewById(R.id.display_name);
            mViews.add(mDisplayName);

            mEmail = mDialog.findViewById(R.id.email);
            mViews.add(mEmail);

            mConfirmEmail = mDialog.findViewById(R.id.email_confirm);
            mViews.add(mConfirmEmail);

            mPassword = mDialog.findViewById(R.id.password);
            mViews.add(mPassword);

            mConfirmPassword = mDialog.findViewById(R.id.password_confirm);
            mConfirmPassword.setOnEditorActionListener((v, actionId, event) -> {
                if (event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN // debounce
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // enter on hardware keyboard submits request
                    validateFields();
                    return true;
                }
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // "Done" button submits request
                    validateFields();
                    return true;
                }
                return false;
            });
            mViews.add(mConfirmPassword);

            final Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> validateFields());
        });
        show();
    }

    private void validateFields() {
        if (noEmptyFields() && authFieldsValid()) {
            showSpinner();
            final String displayName = mDisplayName.getText().toString();
            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();
            // validation complete, go for actual register request
            registerNewUser(displayName, email, password);
        }
    }

    private boolean noEmptyFields() {
        boolean noEmptyFields = true;
        if (TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
            mConfirmPassword.setError(getString(R.string.empty_field));
            mConfirmPassword.requestFocus();
            noEmptyFields = false;
        }
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            mPassword.setError(getString(R.string.empty_field));
            mPassword.requestFocus();
            noEmptyFields = false;
        }
        if (TextUtils.isEmpty(mConfirmEmail.getText().toString())) {
            mConfirmEmail.setError(getString(R.string.empty_field));
            mConfirmEmail.requestFocus();
            noEmptyFields = false;
        }
        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.empty_field));
            mEmail.requestFocus();
            noEmptyFields = false;
        }
        if (TextUtils.isEmpty(mDisplayName.getText().toString())) {
            mDisplayName.setError(getString(R.string.empty_field));
            mDisplayName.requestFocus();
            noEmptyFields = false;
        }
        return noEmptyFields;
    }

    private boolean authFieldsValid() {
        boolean authFieldsValid = true;
        final String email = mEmail.getText().toString();
        final String confirmEmail = mConfirmEmail.getText().toString();

        if (!AuthInputValidator.emailValid(email)) {
            mEmail.setError(getString(R.string.invalid_email_error));
            mEmail.requestFocus();
            authFieldsValid = false;
        } else if (!TextUtils.equals(email, confirmEmail)) { // email fields don't match
            mEmail.setError(getString(R.string.email_field_mismatch));
            mConfirmEmail.setError(getString(R.string.email_field_mismatch));
            mEmail.requestFocus();
            authFieldsValid = false;
        }

        final String password = mPassword.getText().toString();
        final String confirmPassword = mConfirmPassword.getText().toString();
        if (!AuthInputValidator.passwordValid(password)) {
            mPassword.setError(getString(R.string.invalid_password_error));
            mPassword.requestFocus();
            authFieldsValid = false;
        } else if (!TextUtils.equals(password, confirmPassword)) { // password fields don't match
            mPassword.setError(getString(R.string.password_field_mismatch));
            mConfirmPassword.setError(getString(R.string.password_field_mismatch));
            mPassword.requestFocus();
            authFieldsValid = false;
        }
        return authFieldsValid;
    }

    private void registerNewUser(String displayName, String email, String password) {
        FirebaseUtilities.createDisabledUser(email, password, displayName)
                .addOnCompleteListener(createTask -> {
                    if (createTask.isSuccessful()) {
                        // get UID from result
                        addRequest(email, displayName, createTask.getResult());
                    } else {
                        Toast.makeText(mContext, R.string.request_submit_fail, Toast.LENGTH_LONG).show();
                        hideSpinner();
                    }
                });
    }

    private void addRequest(String email, String displayName, String uid) {
        final DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.user_requests_table))
                .child(uid);

        final User newUser = new User(uid);
        newUser.setDisplayName(displayName);
        newUser.setEmail(email);

        requestsRef.setValue(newUser).addOnCompleteListener(addUserRequest -> {
            if (addUserRequest.isSuccessful()) {
                hideSpinner();
                dismiss();
                sendConfirmationEmail(newUser);
                Toast.makeText(mContext, R.string.request_submit_success, Toast.LENGTH_LONG).show();
            } else {
                hideSpinner();
                // unable to push request to UserRequest table, so attempt to delete the disabled
                // user out of the auth table
                FirebaseUtilities.deleteUser(uid);
                Toast.makeText(mContext, R.string.request_submit_unknown_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendConfirmationEmail(User newUser) {
        final String subject = getString(R.string.confirm_email_subject);
        final String body = String.format(getString(R.string.confirm_email_body),
                newUser.getDisplayName());
        new Mailer(mContext)
                .withMailTo(newUser.getEmail())
                .withSubject(subject)
                .withBody(body)
                .withProcessVisibility(false)
                .send();
    }

}
