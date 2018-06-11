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

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.AuthInputValidator;
import mhealth.mvax.auth.utilities.FirebaseUtilities;
import mhealth.mvax.model.user.User;
import mhealth.mvax.records.utilities.StringFetcher;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for requesting a new mVax account
 */
public class RequestAccountModal extends CustomModal {

    private AlertDialog mBuilder;

    private ProgressBar mSpinner;
    private List<View> mViews;

    private TextView mDisplayName;
    private TextView mEmail;
    private TextView mConfirmEmail;
    private TextView mPassword;
    private TextView mConfirmPassword;

    public RequestAccountModal(View view) {
        super(view);
        mViews = new ArrayList<>();
    }

    @Override
    AlertDialog createDialog() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.modal_register_title))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_request_account, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(getString(R.string.button_reset_password_submit), null)
                .setNegativeButton(getString(R.string.button_reset_password_cancel), null)
                .create();

        mBuilder.setOnShowListener(dialogInterface -> {
            mSpinner = mBuilder.findViewById(R.id.register_spinner);

            // subtitle TextView
            mViews.add(mBuilder.findViewById(R.id.textView_register_subtitle));

            // display name EditText
            mDisplayName = mBuilder.findViewById(R.id.display_name);
            mViews.add(mDisplayName);

            // email EditText
            mEmail = mBuilder.findViewById(R.id.edittext_register_email);
            mViews.add(mEmail);

            // confirm email EditText
            mConfirmEmail = mBuilder.findViewById(R.id.edittext_register_email_confirm);
            mViews.add(mConfirmEmail);

            // password EditText
            mPassword = mBuilder.findViewById(R.id.edittext_register_password);
            mViews.add(mPassword);

            // confirm password EditText
            mConfirmPassword = mBuilder.findViewById(R.id.edittext_register_password_confirm);
            mConfirmPassword.setOnEditorActionListener((v, actionId, event) -> {
                if (event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN
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

            final Button positiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> validateFields());
            mViews.add(positiveButton);

            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE));
        });
        return mBuilder;
    }

    private void validateFields() {
        if (noEmptyFields() && authFieldsValid()) {
            showSpinner(mSpinner, mViews);
            final String displayName = mDisplayName.getText().toString();
            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();
            // validation complete, go for actual register request
            registerNewUser(displayName, email, password);
        }
    }

    private boolean noEmptyFields() {
        boolean noEmptyFields = true;
        // order of if cases specifies that topmost field displays error
        // no confirm password
        if (TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
            mConfirmPassword.setError(getString(R.string.error_empty_field));
            mConfirmPassword.requestFocus();
            noEmptyFields = false;
        }
        // no password
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            mPassword.setError(getString(R.string.error_empty_field));
            mPassword.requestFocus();
            noEmptyFields = false;
        }
        // no confirm email
        if (TextUtils.isEmpty(mConfirmEmail.getText().toString())) {
            mConfirmEmail.setError(getString(R.string.error_empty_field));
            mConfirmEmail.requestFocus();
            noEmptyFields = false;
        }
        // no email
        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.error_empty_field));
            mEmail.requestFocus();
            noEmptyFields = false;
        }
        // no last name
        if (TextUtils.isEmpty(mDisplayName.getText().toString())) {
            mDisplayName.setError(getString(R.string.error_empty_field));
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
            mEmail.setError(getString(R.string.error_invalid_email));
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
            mPassword.setError(getString(R.string.error_invalid_password));
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
                        Toast.makeText(getActivity(), R.string.request_submit_fail, Toast.LENGTH_LONG).show();
                        hideSpinner(mSpinner, mViews);
                    }
                });
    }

    private void addRequest(String email, String displayName, String uid) {
        final String requestsTable = getString(R.string.userRequestsTable);
        final DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference()
                .child(requestsTable)
                .child(uid);

        final User newUser = new User(uid);
        newUser.setDisplayName(displayName);
        newUser.setEmail(email);

        requestsRef.setValue(newUser).addOnCompleteListener(addUserRequestTask -> {
            hideSpinner(mSpinner, mViews);
            if (addUserRequestTask.isSuccessful()) {
                mBuilder.dismiss();
                sendConfirmEmail(newUser);
                Toast.makeText(getActivity(), R.string.request_submit_success, Toast.LENGTH_LONG).show();
            } else {
                // unable to push request to UserRequest table, so attempt to delete the disabled
                // user out of the auth table
                FirebaseUtilities.deleteUser(uid);
                Toast.makeText(getActivity(), R.string.request_submit_unknown_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendConfirmEmail(User newUser) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.configTable))
                .child(getString(R.string.mail_table));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            @SuppressWarnings(value = "unchecked")
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final HashMap<String, String> credentials = (HashMap<String, String>) dataSnapshot.getValue();
                assert credentials != null;
                final String email = credentials.get(getString(R.string.email_value));
                final String password = credentials.get(getString(R.string.password_value));

                final String subject = getString(R.string.confirm_email_subject);
                final String body = String.format(StringFetcher.fetchString(R.string.confirm_email_body),
                        newUser.getDisplayName());

                BackgroundMail.newBuilder(getContext())
                        .withUsername(email)
                        .withPassword(password)
                        .withMailto(newUser.getEmail())
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject(subject)
                        .withBody(body)
                        .withProcessVisibility(false)
                        .withOnFailCallback(() -> Toast.makeText(getActivity(), R.string.confirm_email_error, Toast.LENGTH_LONG).show())
                        .send();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.confirm_email_error, Toast.LENGTH_LONG).show();
            }
        });
    }

}
