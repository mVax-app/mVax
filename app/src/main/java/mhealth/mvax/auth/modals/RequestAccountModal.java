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
import mhealth.mvax.model.user.UserRequest;

/**
 * @author Robert Steilberg
 * <p>
 * Displays a modal for requesting a new mVax account
 */
public class RegisterModal extends CustomModal {

    private AlertDialog mBuilder;

    private TextView mSubtitleView;
    private TextView mNameView;
    private TextView mEmailView;
    private TextView mConfirmEmailView;
    private ProgressBar mSpinner;
    private Button mPositiveButton;
    private Button mNegativeButton;

    public RegisterModal(View view) {
        super(view);
    }

    AlertDialog createDialog() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.modal_register_title))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_register_account, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(getString(R.string.button_reset_password_submit), null)
                .setNegativeButton(getString(R.string.button_reset_password_cancel), null)
                .create();

        mBuilder.setOnShowListener(dialogInterface -> {
            // get generated views
            mSubtitleView = mBuilder.findViewById(R.id.textView_register_subtitle);
            mNameView = mBuilder.findViewById(R.id.edittext_register_name);
            mEmailView = mBuilder.findViewById(R.id.edittext_register_email);
            mConfirmEmailView = mBuilder.findViewById(R.id.edittext_register_email_confirm);
            mSpinner = mBuilder.findViewById(R.id.register_spinner);
            mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            mNegativeButton = mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE);

            initListeners();
        });
        return mBuilder;
    }

    private void initListeners() {
        // hardware enter and Done button attempts to submit request
        mConfirmEmailView.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null
                    && event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                attemptRegister();
                return true;
            }
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptRegister();
                return true;
            }
            return false;
        });

        mPositiveButton.setOnClickListener(view -> attemptRegister());
    }

    private void attemptRegister() {
        if (noEmptyFields() && emailFieldsValid()) {
            String name = mNameView.getText().toString();
            String email = mEmailView.getText().toString();
            toggleSpinner(true);
            registerAccount(name, email);
        }
    }

    private boolean noEmptyFields() {
        boolean noEmptyFields = true;
        // no confirm email
        if (TextUtils.isEmpty(mConfirmEmailView.getText().toString())) {
            mConfirmEmailView.setError(getString(R.string.error_empty_field));
            mConfirmEmailView.requestFocus();
            noEmptyFields = false;
        }
        // no email
        if (TextUtils.isEmpty(mEmailView.getText().toString())) {
            mEmailView.setError(getString(R.string.error_empty_field));
            mEmailView.requestFocus();
            noEmptyFields = false;
        }
        // no name
        if (TextUtils.isEmpty(mNameView.getText().toString())) {
            mNameView.setError(getString(R.string.error_empty_field));
            mNameView.requestFocus();
            noEmptyFields = false;
        }
        return noEmptyFields;
    }

    private boolean emailFieldsValid() {
        boolean emailFieldsValid = true;

        String email = mEmailView.getText().toString();
        String emailConfirm = mConfirmEmailView.getText().toString();
        if (!TextUtils.equals(email, emailConfirm)) { // email fields don't match
            mEmailView.setError(getString(R.string.email_field_mismatch));
            mConfirmEmailView.setError(getString(R.string.email_field_mismatch));
            mEmailView.requestFocus();
            emailFieldsValid = false;
        }
        if (!AuthInputValidator.emailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            emailFieldsValid = false;
        }
        return emailFieldsValid;
    }

    // validation complete, go for actual register request
    private void registerAccount(String name, String email) {
        final String requestsTable = getString(R.string.userRequestsTable);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(requestsTable);

        UserRequest request = new UserRequest(ref.push().getKey());
        request.setName(name);
        request.setEmail(email);

        ref.child(request.getDatabaseKey()).setValue(request, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                toggleSpinner(false);
                mBuilder.dismiss();
                Toast.makeText(getActivity(), R.string.request_submit_success, Toast.LENGTH_LONG).show();
            }
        });
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
