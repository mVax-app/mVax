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
package mhealth.mvax.auth;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.activities.MainActivity;

/**
 * @author Matthew Tribby, Steven Yang, Robert Steilberg
 *         <p>
 *         Activity that handles user authentication, password reset, and new user registration;
 *         all operations done via Firbase authentication API
 */
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout mEmailInput;
    private AutoCompleteTextView mEmailView;
    private TextInputLayout mPasswordInput;
    private EditText mPasswordView;
    private ProgressBar mProgressSpinner;
    private int mSpinnerOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // MUST BE CALLED BEFORE ANY OTHER FIREBASE API CALLS
        // enables offline data persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mEmailInput = findViewById(R.id.input_email);
        mEmailView = findViewById(R.id.edittext_email);

        mPasswordInput = findViewById(R.id.input_password);
        mPasswordView = findViewById(R.id.edittext_password);
        mPasswordView.setText("");

        mProgressSpinner = findViewById(R.id.login_progress);

        initTextFields();
        initButtons();
        configureSpinner();
    }

    @Override
    public void onResume() {
        super.onResume();
        // ensure auth text fields are visible
        animateTextInputs(false);
        mPasswordView.setText("");
    }

    private void initTextFields() {
        // in email EditText, enter on hardware keyboard submits for authentication
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    authenticate();
                    return true;
                }
                return false;
            }
        });

        // in password EditText, enter on hardware keyboard submits for authentication;
        // "Done" button submits for authentication too
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean result = false;
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    authenticate();
                    result = true;
                }
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    authenticate();
                    result = true;
                }
                return result;
            }
        });

        // re-focusing to the password EditText clears out any text
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPasswordView.setText("");
                }
            }
        });
    }

    private void initButtons() {
        // tie authenticate action to "Sign In" button
        Button signInButton = findViewById(R.id.button_authenticate);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticate();
            }
        });
        // tie register action to "Register" TextView
        TextView registerButton = findViewById(R.id.textview_register);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(), UserRegistrationActivity.class);
                startActivity(register);
            }
        });
        // tie reset password action to "Reset Password" TextView
        TextView forgotButton = findViewById(R.id.textview_reset_password);
        forgotButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                promptForPasswordReset(v);
            }
        });
    }

    private void configureSpinner() {
        mSpinnerOrigin = Resources.getSystem().getDisplayMetrics().widthPixels;
        mProgressSpinner.setX(mSpinnerOrigin);
    }

    private void animateTextInputs(boolean goingOffScreen) {
        int x = 1, y = 0;
        if (goingOffScreen) {
            x = 0;
            y = 1;
        }

        // move email and password fields
        LinearLayout inputs = findViewById(R.id.inputs);
        ObjectAnimator animInputs = ObjectAnimator.ofFloat(inputs,
                View.TRANSLATION_X, -1 * inputs.getWidth() * x, -1 * inputs.getWidth() * y);
        animInputs.setDuration(500).start();

        // move progress spinner
        ObjectAnimator animProgress = ObjectAnimator.ofFloat(mProgressSpinner,
                View.TRANSLATION_X, mSpinnerOrigin * y, mSpinnerOrigin * x);
        animProgress.setDuration(500).start();
    }

    /**
     * Attempt authentication; validate that email and password fields are valid;
     * if not, no authentication attempt is made
     */
    private void authenticate() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (fieldsValid(email, password)) {
            animateTextInputs(true);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("attemptedLogin", "signInWithEmail:attemped:" + task.isSuccessful());
                            if (task.isSuccessful()) {
                                Log.w("successLogin", "signInWithEmail:success", task.getException());
                                // login successful, transition to root Activity
                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mainIntent);
                            } else {
                                Log.w("failedLogin", "signInWithEmail:failed", task.getException());
                                animateTextInputs(false);
                                Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private boolean fieldsValid(String email, String password) {
        boolean fieldsValid = true;
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getResources().getString(R.string.error_empty_field));
            mPasswordView.requestFocus();
            fieldsValid = false;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            fieldsValid = false;
        }
        return fieldsValid;
    }

    private void promptForPasswordReset(View v) {

        final AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.modal_reset_title))
                .setView(getLayoutInflater().inflate(R.layout.modal_reset_password, (ViewGroup) v.getParent(), false))
                .setPositiveButton(getResources().getString(R.string.button_reset_password_submit), null)
                .setNegativeButton(getResources().getString(R.string.button_reset_password_cancel), null)
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final TextView emailTextView = builder.findViewById(R.id.emailReset);
                        String emailAddress = emailTextView.getText().toString();
                        if (TextUtils.isEmpty(emailAddress)) {
                            emailTextView.setError(getResources().getString(R.string.error_empty_field));
                            emailTextView.requestFocus();
                        } else {
                            sendResetEmail(emailAddress);
                            builder.dismiss();
                        }
                    }
                });
            }
        });

        builder.show();
    }

    private void sendResetEmail(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress);
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.reset_email_confirm), Toast.LENGTH_LONG).show();
    }

}
