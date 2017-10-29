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

import mhealth.mvax.R;

public class UserRegistrationActivity extends AppCompatActivity {
    static UserRegistrationActivity checkLogin;
    private FirebaseAuth mAuth;

    //UI components
    private EditText newUserName;
    private EditText newUserEmail;
    private EditText newUserPassword;
    private EditText newUserPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        setOnClick();
        checkLogin = this;
        newUserName = (EditText)findViewById(R.id.TFname);
        newUserEmail = (EditText)findViewById(R.id.TFemail);
        newUserPassword = (EditText)findViewById(R.id.TFpassword);
        newUserPasswordConfirm = (EditText) findViewById(R.id.TFpasswordConfirm);

        mAuth= FirebaseAuth.getInstance();
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

                //Set default errors
                newUserEmail.setError(null);
                newUserPassword.setError(null);
                newUserPasswordConfirm.setError(null);


                String email = newUserEmail.getText().toString();
                String password = newUserPassword.getText().toString();

                Boolean cancel = false;
                View focusView = null;

                if(!LoginActivity.isEmailValid(email)){
                    newUserEmail.setError(getResources().getString(R.string.REG_ERROR_EMAIL));
                    focusView = newUserEmail;
                    cancel = true;
                }

                if(!LoginActivity.isPasswordValid(password)){
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


                if(!cancel) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(UserRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("createCredentials", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(UserRegistrationActivity.this, R.string.auth_failed,
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    Toast.makeText(UserRegistrationActivity.this, getResources().getString(R.string.REG_CONFIRMED), Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
