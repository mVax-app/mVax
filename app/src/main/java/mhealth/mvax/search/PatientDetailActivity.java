package mhealth.mvax.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mhealth.mvax.R;
import mhealth.mvax.patient.Patient;
import mhealth.mvax.patient.vaccine.Dose;
import mhealth.mvax.patient.vaccine.DoseDateView;
import mhealth.mvax.patient.vaccine.Vaccine;
import mhealth.mvax.patient.vaccine.VaccineAdapter;

/**
 * @author Robert Steilberg
 *         <p>
 *         An activity for displaying details about a patient record
 */

public class PatientDetailActivity extends AppCompatActivity {

    //================================================================================
    // Properties
    //================================================================================

    private Patient _patient;
    private FirebaseAuth _auth;
    private DatabaseReference _database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        setTitle(getResources().getString(R.string.patient_details));

        if (!initDatabase()) {
            // TODO: throw some kind of error to the UI here
        }
    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Render a modal for modifying a vaccine dose record
     *
     * @param view           the DoseDateView object that displays the dose date
     * @param vaccineAdapter the VaccineAdapter through which the vaccines are populated
     */
    public void createNewDose(final View view, final VaccineAdapter vaccineAdapter) {
        final DoseDateView sender = (DoseDateView) view;

        // create modal
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.modal_new_dosage_title);
        View dialogView = vaccineAdapter.getInflater().inflate(R.layout.modal_new_dosage, null);
        builder.setView(dialogView);

        final DatePicker datePicker = dialogView.findViewById(R.id.dosage_date_picker);

        builder.setPositiveButton(getResources().getString(R.string.modal_new_dosage_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.YEAR, datePicker.getYear());

                updateDose(sender.getVaccine(), sender.getDose(), cal.getTimeInMillis());

                vaccineAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.modal_new_dosage_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton(R.string.modal_new_dosage_neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDose(sender.getVaccine(), sender.getDose(), null);
            }
        });

        builder.show();
    }


    //================================================================================
    // Private methods
    //================================================================================

    /**
     * Initializes the Firebase connection and sets up data listeners
     *
     * @return true if authentication and initialization was successful, false otherwise
     */
    private boolean initDatabase() {
        _auth = FirebaseAuth.getInstance();
        FirebaseUser user = _auth.getCurrentUser();
        // TODO authentication validation, throw back false if failed
        _database = FirebaseDatabase.getInstance().getReference();

        String patientId = (String) this.getIntent().getSerializableExtra("patientId");

        _database.child("patientRecords").orderByChild("id").equalTo(patientId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                _patient = dataSnapshot.getValue(Patient.class);
                renderPatientDetails();
                renderVaccines();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                String f = "";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String f = "";
            }
        });
        return true;
    }

    private void updateDose(Vaccine vaccine, Dose dose, Long doseDate) {
        // TODO push exceptions to UI
        dose.setDate(doseDate);
        if (vaccine.updateDose(dose)) {
            if (_patient.updateVaccine(vaccine)) {
                // push the update to the database, which will trigger update listeners,
                // updating the view
                _database.child("patientRecords").child(_patient.getId()).setValue(_patient);
            } else {
                // TODO throw unable to update vaccine in patient
            }
        } else {
            // TODO throw unable to update dose in vaccine
        }
    }

    private void renderPatientDetails() {
        ((TextView) findViewById(R.id.patient_detail_name)).setText(_patient.getFullName());

        String DOBprompt = getResources().getString(R.string.DOB_prompt);
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.getDefault());
        String DOBstr = DOBprompt + " " + sdf.format(_patient.getDOB());
        ((TextView) findViewById(R.id.patient_detail_dob)).setText(DOBstr);

        String genderPrompt = getResources().getString(R.string.gender_prompt);
        String genderStr = genderPrompt + " " + _patient.getGender();
        ((TextView) findViewById(R.id.patient_detail_gender)).setText(genderStr);

        String communityPrompt = getResources().getString(R.string.community_prompt);
        String communityStr = communityPrompt + " " + _patient.getCommunity();
        ((TextView) findViewById(R.id.patient_detail_community)).setText(communityStr);
    }

    private void renderVaccines() {
        ArrayList<Vaccine> vaccineList = _patient.getVaccineList();
        ListView vaccineListView = (ListView) findViewById(R.id.vaccine_list_view);
        addDeleteButton(vaccineListView);
        VaccineAdapter vaccineAdapter = new VaccineAdapter(this, vaccineList);
        vaccineListView.setAdapter(vaccineAdapter);
    }

    private void addDeleteButton(ListView vaccineListView) {
        Button deleteButton = (Button) getLayoutInflater().inflate(R.layout.delete_button, null);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _database.child("patientRecords").child(_patient.getId()).setValue(null);
                finish();
            }
        });
        vaccineListView.addFooterView(deleteButton);
    }

}
