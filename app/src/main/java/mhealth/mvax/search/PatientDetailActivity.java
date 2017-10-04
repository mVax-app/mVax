package mhealth.mvax.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import mhealth.mvax.R;
import mhealth.mvax.patient.Patient;

public class PatientDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        Patient patient = (Patient) this.getIntent().getSerializableExtra("patient");

        setTitle(getResources().getString(R.string.patient_details));

        ((TextView) findViewById(R.id.patient_detail_name)).setText(patient.getFullName());

        String DOBprompt = getResources().getString(R.string.DOB_prompt);
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
        String DOBstr = DOBprompt + " " + sdf.format(patient.getDOB());
        ((TextView) findViewById(R.id.patient_detail_dob)).setText(DOBstr);

        String genderPrompt = getResources().getString(R.string.gender_prompt);
        String genderStr = genderPrompt + " " + patient.getGender();
        ((TextView) findViewById(R.id.patient_detail_gender)).setText(genderStr);

        String communityPrompt = getResources().getString(R.string.community_prompt);
        String communityStr = communityPrompt + " " + patient.getCommunity();
        ((TextView) findViewById(R.id.patient_detail_community)).setText(communityStr);

    }
}
