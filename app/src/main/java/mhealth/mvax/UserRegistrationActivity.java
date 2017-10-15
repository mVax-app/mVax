package mhealth.mvax;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
                //Puts email and password combination into dictionary
               // registration.put(newUserEmail.getText().toString(), newUserPassword.getText().toString());



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
