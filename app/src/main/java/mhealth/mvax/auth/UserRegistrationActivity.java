package mhealth.mvax.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;

public class UserRegistrationActivity extends AppCompatActivity {
    static UserRegistrationActivity checkLogin;
    private FirebaseAuth mAuth;

    //UI components
    private EditText newUserName;
    private EditText newUserEmail;
    private EditText newUserPassword;
    private EditText newUserPasswordConfirm;
    private EditText firstName;
    private EditText lastName;
    private Spinner userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        setOnClick();
        checkLogin = this;
        firstName = (EditText) findViewById(R.id.TFname);
        lastName = (EditText) findViewById(R.id.TFnameLast);
        newUserName = (EditText)findViewById(R.id.TFname);
        newUserEmail = (EditText)findViewById(R.id.TFemail);
        newUserPassword = (EditText)findViewById(R.id.TFpassword);
        newUserPasswordConfirm = (EditText) findViewById(R.id.TFpasswordConfirm);

        userRole = (Spinner) this.findViewById(R.id.TFrole);
        List<String> rolesList = getRoles();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UserRegistrationActivity.this,
                android.R.layout.simple_spinner_item, rolesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userRole.setAdapter(dataAdapter);

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
                                    else{
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        DatabaseReference account = ref.child(getResources().getString(R.string.userTable)).child(uid);
                                        User newUser = new User(firstName.getText().toString(), lastName.getText().toString(), userRole.getSelectedItem().toString());
                                        account.setValue(newUser);
                                    }
                                }
                            });

                    Toast.makeText(UserRegistrationActivity.this, getResources().getString(R.string.REG_CONFIRMED), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private List<String> getRoles(){
        List<String> roles = new ArrayList<String>();
        UserRole[] values = UserRole.class.getEnumConstants();
        for(int i = 0; i < values.length; i++){
            roles.add(values[i].name());
        }
        return roles;

    }

}
