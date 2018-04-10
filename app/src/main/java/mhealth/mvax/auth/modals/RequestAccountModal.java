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
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.AuthInputValidator;
import mhealth.mvax.model.user.User;

/**
 * @author Robert Steilberg
 * <p>
 * Displays a modal for requesting a new mVax account
 */
public class RequestAccountModal extends CustomModal {

    private AlertDialog mBuilder;

    private TextView mSubtitleView;
    private TextView mDisplayNameView;
    private TextView mEmailView;
    private TextView mConfirmEmailView;
    private TextView mPasswordView;
    private TextView mConfirmPasswordView;
    private ProgressBar mSpinner;
    private Button mPositiveButton;
    private Button mNegativeButton;

    public RequestAccountModal(View view) {
        super(view);
    }

    AlertDialog createDialog() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.modal_register_title))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_request_account, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(getString(R.string.button_reset_password_submit), null)
                .setNegativeButton(getString(R.string.button_reset_password_cancel), null)
                .create();

        mBuilder.setOnShowListener(dialogInterface -> {
            // get generated views
            mSubtitleView = mBuilder.findViewById(R.id.textView_register_subtitle);
            mDisplayNameView = mBuilder.findViewById(R.id.display_name);
            mEmailView = mBuilder.findViewById(R.id.edittext_register_email);
            mConfirmEmailView = mBuilder.findViewById(R.id.edittext_register_email_confirm);
            mPasswordView = mBuilder.findViewById(R.id.edittext_register_password);
            mConfirmPasswordView = mBuilder.findViewById(R.id.edittext_register_password_confirm);
            mSpinner = mBuilder.findViewById(R.id.register_spinner);
            mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            mNegativeButton = mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE);

            initListeners();
        });
        return mBuilder;
    }

    private void initListeners() {
        // hardware enter and Done button attempts to submit request
        mConfirmPasswordView.setOnEditorActionListener((v, actionId, event) -> {
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
        if (noEmptyFields() && authFieldsValid()) {
            String displayName = mDisplayNameView.getText().toString();
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();
            toggleSpinner(true);
            registerAccount(displayName, email, password);
        }
    }

    private boolean noEmptyFields() {
        boolean noEmptyFields = true;
        // no confirm password
        if (TextUtils.isEmpty(mConfirmPasswordView.getText().toString())) {
            mConfirmPasswordView.setError(getString(R.string.error_empty_field));
            mConfirmPasswordView.requestFocus();
            noEmptyFields = false;
        }
        // no password
        if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
            mPasswordView.setError(getString(R.string.error_empty_field));
            mPasswordView.requestFocus();
            noEmptyFields = false;
        }
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
        // no last name
        if (TextUtils.isEmpty(mDisplayNameView.getText().toString())) {
            mDisplayNameView.setError(getString(R.string.error_empty_field));
            mDisplayNameView.requestFocus();
            noEmptyFields = false;
        }
        return noEmptyFields;
    }

    private boolean authFieldsValid() {
        boolean authFieldsValid = true;

        String email = mEmailView.getText().toString();
        String emailConfirm = mConfirmEmailView.getText().toString();
        if (!TextUtils.equals(email, emailConfirm)) { // email fields don't match
            mEmailView.setError(getString(R.string.email_field_mismatch));
            mConfirmEmailView.setError(getString(R.string.email_field_mismatch));
            mEmailView.requestFocus();
            authFieldsValid = false;
        }
        if (!AuthInputValidator.emailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            authFieldsValid = false;
        }

        String password = mPasswordView.getText().toString();
        String passwordConfirm = mConfirmPasswordView.getText().toString();
        if (!TextUtils.equals(password, passwordConfirm)) { // password fields don't match
            mPasswordView.setError(getString(R.string.password_field_mismatch));
            mConfirmPasswordView.setError(getString(R.string.password_field_mismatch));
            mPasswordView.requestFocus();
            authFieldsValid = false;
        }
        if (!AuthInputValidator.passwordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
            authFieldsValid = false;
        }
        return authFieldsValid;
    }

    // validation complete, go for actual register request
    private void registerAccount(String displayName, String email, String password) {

        registerUser(email, password, displayName).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                String uid = task.getResult();

                final String requestsTable = getString(R.string.userRequestsTable);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child(requestsTable);

                User newUser = new User(ref.push().getKey(), uid);
                newUser.setDisplayName(displayName);
                newUser.setEmail(email);

                ref.child(newUser.getDatabaseKey()).setValue(newUser, (databaseError, databaseReference) -> {
                    toggleSpinner(false);
                    mBuilder.dismiss();
                    Toast.makeText(getActivity(), R.string.request_submit_success, Toast.LENGTH_LONG).show();
                });
            } else {
                Toast.makeText(getActivity(), R.string.request_submit_fail, Toast.LENGTH_LONG).show();
                toggleSpinner(false);
            }
        });



    }

    private Task<String> registerUser(String email, String password, String displayName) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("email", email);
        args.put("password", password);
        args.put("displayName", displayName);
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        return functions.getHttpsCallable("createDisabledAccount").call(args).continueWith(task -> (String) task.getResult().getData());
    }

    private void toggleSpinner(boolean showSpinner) {
        if (showSpinner) {
            mSubtitleView.setVisibility(View.INVISIBLE);
            mDisplayNameView.setVisibility(View.INVISIBLE);
            mEmailView.setVisibility(View.INVISIBLE);
            mConfirmEmailView.setVisibility(View.INVISIBLE);
            mPasswordView.setVisibility(View.INVISIBLE);
            mConfirmPasswordView.setVisibility(View.INVISIBLE);
            mPositiveButton.setVisibility(View.INVISIBLE);
            mNegativeButton.setVisibility(View.INVISIBLE);
            mSpinner.setVisibility(View.VISIBLE);
        } else {
            mSubtitleView.setVisibility(View.VISIBLE);
            mDisplayNameView.setVisibility(View.VISIBLE);
            mEmailView.setVisibility(View.VISIBLE);
            mConfirmEmailView.setVisibility(View.VISIBLE);
            mPasswordView.setVisibility(View.VISIBLE);
            mConfirmPasswordView.setVisibility(View.VISIBLE);
            mPositiveButton.setVisibility(View.VISIBLE);
            mNegativeButton.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.INVISIBLE);
        }
    }

}
