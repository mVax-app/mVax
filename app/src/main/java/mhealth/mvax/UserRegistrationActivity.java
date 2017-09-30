package mhealth.mvax;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class UserRegistrationActivity extends AppCompatActivity {
    final String REG_Breg = "REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        setOnClick();
    }


    private void setOnClick() {
        Button Bregister = (Button)findViewById(R.id.Bregister);
        Bregister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            }
        });
    }
}
