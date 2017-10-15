package mhealth.mvax;

import android.content.Intent;
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

import java.util.HashMap;

public class UserRegistrationActivity extends AppCompatActivity {
    final String REG_Breg = "REGISTER";
    static UserRegistrationActivity checkLogin;
    private FirebaseAuth mAuth;


    EditText newUserName, newUserEmail, newUserPassword;
    HashMap<String, String> registration = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        setOnClick();
        checkLogin = this;
        newUserName = (EditText)findViewById(R.id.TFname);
        newUserEmail = (EditText)findViewById(R.id.TFemail);
        newUserPassword = (EditText)findViewById(R.id.TFpassword);

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
                //Check if username and password are valid
                Log.d("email", newUserEmail.getText().toString());
                Log.d("password", newUserPassword.getText().toString());

                mAuth.createUserWithEmailAndPassword(newUserEmail.getText().toString(), newUserPassword.getText().toString())
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

                                // ...
                            }
                        });


                Intent main = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(main);
            }
        });
    }

    //Checks dictionary for valid email:password combination
    public boolean checkValidUser(String email, String password){
        if(registration.keySet().isEmpty()){
            return false;
        }
        if(registration.containsKey(email) && registration.get(email).contentEquals(password)) {
            return true;
        }
        else{
            return false;
        }
    }
}
