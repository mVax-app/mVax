package mhealth.mvax.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        _patient = (Patient) this.getIntent().getSerializableExtra("patient");

        setTitle(getResources().getString(R.string.patient_details));

        renderPatientDetails();

        renderVaccines();
    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Render a modal for modifying a vaccine dose record
     *
     * @param view the DoseDateView object that displays the dose date
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
                Date doseDate = new Date(cal.getTimeInMillis());

                try {
                    updateDose(sender.getVaccine(), sender.getDose(), doseDate);
                } catch (Exception e) {
                    // TODO push exceptions to UI
                    e.printStackTrace();
                }

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
                try {
                    updateDose(sender.getVaccine(), sender.getDose(), null);
                } catch (Exception e) {
                    // TODO push exceptions to UI
                    e.printStackTrace();
                }
            }
        });

        builder.show();
    }


    //================================================================================
    // Private methods
    //================================================================================

    private void updateDose(Vaccine vaccine, Dose dose, Date dosageDate) throws Exception {
        // TODO push exceptions to UI
        dose.setDate(dosageDate);
        if (vaccine.updateDose(dose)) {
            if (_patient.updateVaccine(vaccine)) {
                Intent intent = getIntent();
                intent.putExtra("patient", _patient);
                setResult(1, intent);
                finish();
            } else {
                throw new Exception("unable to update vaccine in patient");
            }
        } else {
            throw new Exception("unable to update dose in vaccine");
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
        ListView _vaccineListView = (ListView) findViewById(R.id.vaccine_list_view);

        VaccineAdapter vaccineAdapter = new VaccineAdapter(this, vaccineList);
        _vaccineListView.setAdapter(vaccineAdapter);
    }

}
