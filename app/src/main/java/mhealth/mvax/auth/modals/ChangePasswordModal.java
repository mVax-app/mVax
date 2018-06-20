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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.AuthInputValidator;
import mhealth.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for changing an mVax user's password
 */
public class ChangePasswordModal extends CustomModal {

    private TextView mPassword;
    private TextView mConfirmPassword;

    public ChangePasswordModal(View view) {
        super(view);
    }

    @Override
    public AlertDialog initBuilder() {
        mBuilder = new AlertDialog.Builder(mActivity)
                .setTitle(R.string.change_password_modal_title)
                .setView(mInflater.inflate(R.layout.modal_change_password, mParent, false))
                .setPositiveButton(R.string.submit, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        mBuilder.setOnShowListener(dialogInterface -> {
            mSpinner = mBuilder.findViewById(R.id.spinner);

            mViews.add(mBuilder.findViewById(R.id.password_fields));
            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE));

            mPassword = mBuilder.findViewById(R.id.password);
            mConfirmPassword = mBuilder.findViewById(R.id.password_confirm);

            mConfirmPassword.setOnEditorActionListener((v, actionId, event) -> {
                if (event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN // debounce
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // enter on hardware keyboard submits request
                    attemptPasswordChange();
                    return true;
                }
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // "Done" button submits request
                    attemptPasswordChange();
                    return true;
                }
                return false;
            });

            final Button positiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> attemptPasswordChange());
            mViews.add(positiveButton);
        });
        return mBuilder;
    }

    private void attemptPasswordChange() {
        if (noEmptyFields() && passwordValid()) {
            showSpinner();
            changePasswordInAuthTable(mPassword.getText().toString());
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
        return noEmptyFields;
    }

    private boolean passwordValid() {
        boolean passwordValid = true;
        final String password = mPassword.getText().toString();
        final String confirmPassword = mConfirmPassword.getText().toString();
        if (!AuthInputValidator.passwordValid(password)) {
            mPassword.setError(getString(R.string.invalid_password_error));
            mPassword.requestFocus();
            passwordValid = false;
        } else if (!password.equals(confirmPassword)) { // password fields don't match
            mPassword.setError(getString(R.string.password_field_mismatch));
            mConfirmPassword.setError(getString(R.string.password_field_mismatch));
            mPassword.requestFocus();
            passwordValid = false;
        }
        return passwordValid;
    }

    private void changePasswordInAuthTable(String password) {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currUser == null) {
            FirebaseAuth.getInstance().signOut();
            mActivity.finish();
            return;
        }
        currUser.updatePassword(password).addOnCompleteListener(passwordChange -> {
            if (passwordChange.isSuccessful()) {
                hideSpinner();
                mBuilder.dismiss();
                Toast.makeText(mActivity, R.string.change_password_success, Toast.LENGTH_LONG).show();
            } else {
                hideSpinner();
                Toast.makeText(mActivity, R.string.change_password_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

}
