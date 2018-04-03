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
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import mhealth.mvax.R;
import mhealth.mvax.activities.MainActivity;
import mhealth.mvax.auth.modals.PasswordResetModal;
import mhealth.mvax.auth.modals.RegisterModal;

/**
 * @author Matthew Tribby, Steven Yang, Robert Steilberg
 * <p>
 * Activity that handles user authentication, password reset, and new user registration;
 * all operations done via Firbase authentication and database API
 */
public class AuthActivity extends Activity {

    private static final int ANIMATION_SPEED = 500;
    private static final int ANIMATION_SPEED_INSTANT = 0;

    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressBar mSpinner;
    private int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmailView = findViewById(R.id.edittext_email);
        mPasswordView = findViewById(R.id.edittext_password);
        mSpinner = findViewById(R.id.login_progress);
        mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        initTextFields();
        initButtons();
        mSpinner.setX(mScreenWidth);
    }

    @Override
    public void onResume() {
        super.onResume();
        // clear out any existing auth info
        FirebaseAuth.getInstance().signOut();
        // ensure auth text fields are visible
        animateTextInputs(ANIMATION_SPEED_INSTANT, false);
    }

    private void initTextFields() {
        // in email EditText, enter on hardware keyboard submits for authentication
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN // debounce
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
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
                if (event != null
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    authenticate();
                    return true;
                }
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    authenticate();
                    return true;
                }
                return false;
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
                RegisterModal registerModal = new RegisterModal(view);
                registerModal.show();
            }
        });
        // tie reset password action to "Reset Password" TextView
        TextView forgotButton = findViewById(R.id.textview_reset_password);
        forgotButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordResetModal resetModal = new PasswordResetModal(view);
                resetModal.show();
            }
        });
    }

    /**
     * Attempt authentication; validate that email and password fields are valid;
     * if not, no authentication attempt is made
     */
    private void authenticate() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (fieldsValid(email, password)) {
            animateTextInputs(ANIMATION_SPEED, true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            handleAuthCallback(task);
                        }
                    });
        }
        mPasswordView.setText("");
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

    private void handleAuthCallback(Task<AuthResult> task) {
        Log.d("attemptedLogin", "signInWithEmail:attempted:" + task.isSuccessful());
        if (task.isSuccessful()) {
            Log.w("successLogin", "signInWithEmail:success", task.getException());
            // login successful, transition to root Activity
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
        } else {
            Log.w("failedLogin", "signInWithEmail:failed", task.getException());
            animateTextInputs(ANIMATION_SPEED, false); // bring back auth fields
            // see what the error was
            boolean noInternet = task.getException() instanceof FirebaseNetworkException;
            boolean badCredentials = task.getException() instanceof FirebaseAuthException;
            if (noInternet) {
                Toast.makeText(AuthActivity.this, R.string.firebase_fail_no_connection, Toast.LENGTH_LONG).show();
            } else if (badCredentials) {
                Toast.makeText(AuthActivity.this, R.string.auth_fail_bad_credentials, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AuthActivity.this, R.string.firebase_fail_unknown, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void animateTextInputs(int speed, boolean goingOffScreen) {
        int in = goingOffScreen ? 0 : 1;
        int out = goingOffScreen ? 1 : 0;

        // move email and password fields
        LinearLayout inputs = findViewById(R.id.auth_inputs);
        ObjectAnimator animInputs = ObjectAnimator.ofFloat(inputs,
                View.TRANSLATION_X, -1 * mScreenWidth * in, -1 * mScreenWidth * out);
        animInputs.setDuration(speed).start();

        // move progress spinner
        ObjectAnimator animProgress = ObjectAnimator.ofFloat(mSpinner,
                View.TRANSLATION_X, mScreenWidth * out, mScreenWidth * in);
        animProgress.setDuration(speed).start();
    }

}
