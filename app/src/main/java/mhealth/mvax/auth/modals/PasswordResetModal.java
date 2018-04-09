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
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.AuthInputValidator;

/**
 * @author Robert Steilberg
 * <p>
 * Displays a modal for resetting an mVax user's password
 */
public class PasswordResetModal extends AlertDialog.Builder {

    private Activity mActivity;
    private View mView;
    private Resources mResources;
    private AlertDialog mBuilder;

    private LinearLayout mFields;
    private ProgressBar mSpinner;
    private Button mPositiveButton;
    private Button mNegativeButton;

    public PasswordResetModal(View view) {
        super(view.getContext());
        mActivity = (Activity) view.getContext();
        mView = view;
        mResources = view.getResources();
    }

    /**
     * Build and display the modal to reset user password
     */
    @Override
    public AlertDialog show() {
        initBuilder().show();
        return null;
    }

    private AlertDialog initBuilder() {
        mBuilder = new AlertDialog.Builder(mActivity)
                .setTitle(mResources.getString(R.string.modal_reset_title))
                .setView(mActivity.getLayoutInflater().inflate(R.layout.modal_reset_password, (ViewGroup) mView.getParent(), false))
                .setPositiveButton(mResources.getString(R.string.button_reset_password_submit), null)
                .setNegativeButton(mResources.getString(R.string.button_reset_password_cancel), null)
                .create();

        // attach listener; ensure text field isn't empty on submit
        mBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                mFields = mBuilder.findViewById(R.id.reset_fields);
                mSpinner = mBuilder.findViewById(R.id.reset_spinner);
                mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                mNegativeButton = mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE);

                final TextView emailTextView = mBuilder.findViewById(R.id.textview_email_reset);

                // in email EditText, enter on hardware keyboard submits for authentication;
                // "Done" button submits for reset too
                emailTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (event != null
                                && event.getAction() == KeyEvent.ACTION_DOWN
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            attemptPasswordReset(emailTextView);
                            return true;
                        }
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            attemptPasswordReset(emailTextView);
                            return true;
                        }
                        return false;
                    }
                });

                mPositiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptPasswordReset(emailTextView);
                    }
                });
            }
        });
        return mBuilder;
    }

    private void attemptPasswordReset(final TextView emailTextView) {
        String emailAddress = emailTextView.getText().toString();
        if (TextUtils.isEmpty(emailAddress)) { // trying to submit with no email
            emailTextView.setError(mResources.getString(R.string.error_empty_field));
            emailTextView.requestFocus();
        } else if (!AuthInputValidator.emailValid(emailAddress)) { // invalid email
            emailTextView.setError(mResources.getString(R.string.error_invalid_email));
            emailTextView.requestFocus();
        } else {
            toggleSpinner(true);
            sendResetEmail(emailAddress);
        }
    }

    private void sendResetEmail(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        handleCallback(task);
                    }
                });
    }

    private void handleCallback(Task<Void> task) {
        Log.d("attemptedReset", "resetPassword:attempted:" + task.isSuccessful());
        if (task.getException() instanceof FirebaseNetworkException) {
            // only show error for no internet; don't let user know if email
            // isn't associated with an account
            Log.w("failedReset", "resetPassword:failed", task.getException());
            Toast.makeText(mActivity, R.string.firebase_fail_no_connection, Toast.LENGTH_LONG).show();
        } else { // success
            Log.w("successReset", "resetPassword:success", task.getException());
            mBuilder.dismiss();
            Toast.makeText(mActivity, mResources.getString(R.string.reset_email_confirm), Toast.LENGTH_LONG).show();
        }
        toggleSpinner(false);
    }

    private void toggleSpinner(boolean showSpinner) {
        if (showSpinner) {
            mFields.setVisibility(View.INVISIBLE);
            mPositiveButton.setVisibility(View.INVISIBLE);
            mNegativeButton.setVisibility(View.INVISIBLE);
            mSpinner.setVisibility(View.VISIBLE);
        } else {
            mFields.setVisibility(View.VISIBLE);
            mPositiveButton.setVisibility(View.VISIBLE);
            mNegativeButton.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.INVISIBLE);
        }
    }

}
