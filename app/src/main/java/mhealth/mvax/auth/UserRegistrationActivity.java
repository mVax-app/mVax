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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.model.user.UserWithUID;

/**
 * This activity represents a form that allows prospective users to submit a user request which can
 * then be either approved or denied by ADMIN
 *
 * DEPENDENCIES: Firebase authentication and database
 *
 * @author Matthew Tribby
 */
public class UserRegistrationActivity extends AppCompatActivity {
    static UserRegistrationActivity checkLogin;

    //UI components
    private EditText newUserEmail;
    private EditText newUserPassword;
    private EditText newUserPasswordConfirm;
    private EditText firstName;
    private EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_registration);
        setOnClick();
        checkLogin = this;
        firstName = (EditText) findViewById(R.id.TFname);
        lastName = (EditText) findViewById(R.id.TFnameLast);
        newUserEmail = (EditText)findViewById(R.id.TFemail);
        newUserPassword = (EditText)findViewById(R.id.TFpassword);
        newUserPasswordConfirm = (EditText) findViewById(R.id.TFpasswordConfirm);
    }

    //Allow access to this activity through this method
    public static UserRegistrationActivity getInstance(){
        return checkLogin;
    }

    private void setOnClick() {
        Button Bregister = (Button)findViewById(R.id.Bregister);
        Bregister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegisterUser();
            }
        });
    }


    private void attemptRegisterUser(){
        if(checkFormFields()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(newUserEmail.getText().toString(), newUserPassword.getText().toString())
                    .addOnCompleteListener(UserRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("createCredentials", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(UserRegistrationActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                            else{
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference account = ref.child(getResources().getString(R.string.userRequestsTable)).child(uid);

                                //TODO fix null UserRole, doesn't break anything just sloppy
                                UserWithUID newUser = new UserWithUID(uid, firstName.getText().toString(), lastName.getText().toString(), newUserEmail.getText().toString(), null);
                                account.setValue(newUser);
                                Toast.makeText(UserRegistrationActivity.this, getResources().getString(R.string.REG_CONFIRMED), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


        }

    }

    private static boolean isPasswordValid(String password){
        PasswordVerifier verifier = new ComplexPasswordVerifier();
        return verifier.checkPassword(password);
    }
    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    //Returns a boolean which will evaluate whether registration should continue as well as set the error field for focusView
    private boolean checkFormFields(){
        //Set default errors
        newUserEmail.setError(null);
        newUserPassword.setError(null);
        newUserPasswordConfirm.setError(null);

        String email = newUserEmail.getText().toString();
        String password = newUserPassword.getText().toString();

        Boolean cancel = false;
        View focusView = null;

        if(!isEmailValid(email)){
            newUserEmail.setError(getResources().getString(R.string.REG_ERROR_EMAIL));
            focusView = newUserEmail;
            cancel = true;
        }

        if(!isPasswordValid(password)){
            newUserPassword.setError(getResources().getString(R.string.REG_ERROR_PASS_REQUIRE));
            focusView = newUserPassword;
            cancel = true;
        }

        if(!password.equals(newUserPasswordConfirm.getText().toString())){
            newUserPasswordConfirm.setError(getResources().getString(R.string.REG_ERROR_PASS_CONFIRM));
            focusView = newUserPasswordConfirm;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }

        return true;
    }



}
