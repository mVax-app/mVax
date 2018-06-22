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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.AuthInputValidator;
import mhealth.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for changing an mVax user's email
 */
public class ChangeEmailModal extends CustomModal {

    private TextView mEmail;
    private TextView mConfirmEmail;

    public ChangeEmailModal(View view) {
        super(view);
    }

    @Override
    public void createAndShow() {
        mDialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.change_email_modal_title)
                .setView(mInflater.inflate(R.layout.modal_change_email, mParent, false))
                .setPositiveButton(R.string.submit, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        mDialog.setOnShowListener(dialogInterface -> {
            mSpinner = mDialog.findViewById(R.id.spinner);
            mViews.add(mDialog.findViewById(R.id.email_fields));

            mEmail = mDialog.findViewById(R.id.email);
            mConfirmEmail = mDialog.findViewById(R.id.email_confirm);
            mConfirmEmail.setOnEditorActionListener((v, actionId, event) -> {
                if (event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN // debounce
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // enter on hardware keyboard submits request
                    attemptEmailChange();
                    return true;
                }
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // "Done" button submits request
                    attemptEmailChange();
                    return true;
                }
                return false;
            });

            mViews.add(mDialog.getButton(AlertDialog.BUTTON_NEGATIVE));
            final Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> attemptEmailChange());
            mViews.add(positiveButton);
        });
        mDialog.show();
    }

    private void attemptEmailChange() {
        if (noEmptyFields() && emailValid()) {
            showSpinner();
            changeEmailInAuthTable(mEmail.getText().toString());
        }
    }

    private boolean noEmptyFields() {
        boolean noEmptyFields = true;
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
        return noEmptyFields;
    }

    private boolean emailValid() {
        boolean emailValid = true;
        final String email = mEmail.getText().toString();
        final String confirmEmail = mConfirmEmail.getText().toString();
        if (!AuthInputValidator.emailValid(email)) {
            mEmail.setError(getString(R.string.invalid_email_error));
            mEmail.requestFocus();
            emailValid = false;
        } else if (!email.equals(confirmEmail)) { // email fields don't match
            mEmail.setError(getString(R.string.email_field_mismatch));
            mConfirmEmail.setError(getString(R.string.email_field_mismatch));
            mEmail.requestFocus();
            emailValid = false;
        }
        return emailValid;
    }

    private void changeEmailInAuthTable(String email) {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currUser != null) {
            if (currUser.getEmail() != null && currUser.getEmail().equals(email)) {
                // no need to actually change email
                hideSpinner();
                dismiss();
                Toast.makeText(mContext, R.string.change_email_success, Toast.LENGTH_LONG).show();
            }
            currUser.updateEmail(email).addOnCompleteListener(emailChange -> {
                if (emailChange.isSuccessful()) {
                    changeEmailInDatabase(currUser.getUid(), email);
                } else {
                    hideSpinner();
                    Toast.makeText(mContext, R.string.change_email_fail, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void changeEmailInDatabase(String UID, String email) {
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.user_table))
                .child(UID)
                .child(getString(R.string.email));

        userRef.setValue(email).addOnCompleteListener(emailChange -> {
            if (emailChange.isSuccessful()) {
                // email change workflow completed
                hideSpinner();
                dismiss();
                Toast.makeText(mContext, R.string.change_email_success, Toast.LENGTH_LONG).show();
            } else {
                hideSpinner();
                Toast.makeText(mContext, R.string.change_email_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

}
