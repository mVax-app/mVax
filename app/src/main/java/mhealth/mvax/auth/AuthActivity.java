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
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import mhealth.mvax.R;
import mhealth.mvax.main.MainActivity;
import mhealth.mvax.auth.modals.PasswordResetModal;
import mhealth.mvax.auth.modals.RequestAccountModal;

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

    private static boolean BYPASS_LOGIN = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mSpinner = findViewById(R.id.spinner);
        mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        initTextFields();
        initButtons();
        mSpinner.setX(mScreenWidth); // spinner rendered off screen

        if (BYPASS_LOGIN) {
            mAuth.signInWithEmailAndPassword("devadmin@mvax.com", "devadmin1")
                    .addOnCompleteListener(task -> {
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainIntent);
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // clear out any existing auth infoButton
        FirebaseAuth.getInstance().signOut();
        // ensure auth text fields are visible
        animateTextInputs(ANIMATION_SPEED_INSTANT, false);
    }

    private void initTextFields() {
        // in email EditText, enter on hardware keyboard submits for authentication
        mEmailView.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null
                    && event.getAction() == KeyEvent.ACTION_DOWN // debounce
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                authenticate();
                return true;
            }
            return false;
        });

        // in password EditText, enter on hardware keyboard submits for authentication;
        // "Done" button submits for authentication too
        mPasswordView.setOnEditorActionListener((v, actionId, event) -> {
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
        });

        // re-focusing to the password EditText clears out any text
        mPasswordView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mPasswordView.setText("");
            }
        });
    }

    private void initButtons() {
        final Button signInButton = findViewById(R.id.login_button);
        signInButton.setOnClickListener(v -> authenticate());

        final TextView registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(v -> new RequestAccountModal(v).createAndShow());

        final TextView forgotButton = findViewById(R.id.forgot_password);
        forgotButton.setOnClickListener(v -> new PasswordResetModal(v).createAndShow());
    }

    private void authenticate() {
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        if (inputsValid(email, password)) {
            animateTextInputs(ANIMATION_SPEED, true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, this::handleAuthCallback);
        }
        mPasswordView.setText("");
    }

    private boolean inputsValid(String email, String password) {
        boolean fieldsValid = true;
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getResources().getString(R.string.empty_field));
            mPasswordView.requestFocus();
            fieldsValid = false;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.empty_field));
            mEmailView.requestFocus();
            fieldsValid = false;
        }
        return fieldsValid;
    }

    private void handleAuthCallback(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // transition to root Activity
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
        } else {
            animateTextInputs(ANIMATION_SPEED, false); // bring back auth fields
            // see what the error was
            boolean noInternet = task.getException() instanceof FirebaseNetworkException;
            boolean authError = task.getException() instanceof FirebaseAuthException;
            if (noInternet) {
                Toast.makeText(AuthActivity.this, R.string.auth_fail_no_connection, Toast.LENGTH_LONG).show();
            } else if (authError) {
                FirebaseAuthException q = (FirebaseAuthException) task.getException();
                if (q.getErrorCode().equals("ERROR_USER_DISABLED")) {
                    Toast.makeText(AuthActivity.this, R.string.auth_fail_disabled_user, Toast.LENGTH_LONG).show();
                } else {
                    mPasswordView.requestFocus();
                    Toast.makeText(AuthActivity.this, R.string.auth_fail_bad_credentials, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(AuthActivity.this, R.string.auth_fail_unknown, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void animateTextInputs(int speed, boolean goOffscreen) {
        final int in = goOffscreen ? 0 : 1;
        final int out = goOffscreen ? 1 : 0;

        // move email and password fields
        final ConstraintLayout inputs = findViewById(R.id.auth_inputs);
        final ObjectAnimator animInputs = ObjectAnimator.ofFloat(inputs,
                View.TRANSLATION_X, -1 * mScreenWidth * in, -1 * mScreenWidth * out);
        animInputs.setDuration(speed).start();

        // move progress spinner
        final ObjectAnimator animProgress = ObjectAnimator.ofFloat(mSpinner,
                View.TRANSLATION_X, mScreenWidth * out, mScreenWidth * in);
        animProgress.setDuration(speed).start();
    }

}
