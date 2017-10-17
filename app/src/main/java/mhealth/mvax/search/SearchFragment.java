package mhealth.mvax.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import mhealth.mvax.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import mhealth.mvax.patient.Gender;
import mhealth.mvax.patient.Patient;
import mhealth.mvax.patient.vaccine.Dose;
import mhealth.mvax.patient.vaccine.Vaccine;

/**
 * @author Robert Steilberg
 *         <p>
 *         A fragment for handling the record search and detail pages
 */

public class SearchFragment extends Fragment {

    // TODO BUG: hitting back button in Android re-renders fragment, thus calls createDummyData()
    // TODO BUG: again and wipes all progress

    //================================================================================
    // Properties
    //================================================================================

    private Map<Integer, Patient> _patients;

    private SearchResultAdapter _SearchResultAdapter;


    //================================================================================
    // Public methods
    //================================================================================

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        final Context context = view.getContext();

        Button newRecordButton = view.findViewById((R.id.new_record_button));
        newRecordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createNewPatient(v, inflater);
            }
        });

//        searchBar = view.findViewById(R.id.search_bar);

        ListView patientListView = view.findViewById(R.id.patient_list_view);

        createDummyData();

        _SearchResultAdapter = new SearchResultAdapter(view.getContext(), _patients);
        patientListView.setAdapter(_SearchResultAdapter);

        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO potential bug?
                Patient selectedPatient = _patients.get(position);
                Intent detailIntent = new Intent(context, PatientDetailActivity.class);
                detailIntent.putExtra("patient", selectedPatient);
                // TODO fix response codes (may not need for Firebase)
                startActivityForResult(detailIntent,1);
            }

        });
        return view;
    }

    public void createNewPatient(View view, LayoutInflater inflater) {

        // create modal
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(getResources().getString(R.string.modal_new_record_title));

//        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.modal_new_record, null);
        builder.setView(dialogView);


        final EditText firstNameEditText = dialogView.findViewById(R.id.new_first_name);
        final EditText lastNameEditText = dialogView.findViewById(R.id.new_last_name);
        final EditText communityEditText = dialogView.findViewById(R.id.new_community);

        final Spinner spinner = dialogView.findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.gender_spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final Gender[] gender = new Gender[1];
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos != 0) {
                    switch (spinner.getItemAtPosition(pos).toString()) {
                        case "Male":
                            gender[0] = Gender.MALE;
                            break;
                        case "Female":
                            gender[0] = Gender.FEMALE;
                            break;
                    }
                }
            }
        });

        final DatePicker DOBpicker = dialogView.findViewById(R.id.dob_date_picker);

        builder.setPositiveButton(getResources().getString(R.string.modal_new_record_add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String community = communityEditText.getText().toString();

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, DOBpicker.getDayOfMonth());
                cal.set(Calendar.MONTH, DOBpicker.getMonth());
                cal.set(Calendar.YEAR, DOBpicker.getYear());

                Patient newPatient = new Patient(_patients.size(), firstName, lastName, gender[0], new Date(cal.getTimeInMillis()), community);
                _patients.put(newPatient.getId(), newPatient);
                _SearchResultAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.modal_new_record_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Get updated patient back from PatientDetailActivity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                Patient result = (Patient) data.getSerializableExtra("patient");
                _patients.put(result.getId(), result);
            }
        }
    }

    private void createDummyData() {

        Vaccine hepatitis = new Vaccine("Hepatitis B");
        hepatitis.addDose(new Dose("R.N."));

        Vaccine BCG = new Vaccine("BCG");
        BCG.addDose(new Dose("1"));

        Vaccine polio = new Vaccine("Polio");
        polio.addDose(new Dose("1", "VPI"));
        polio.addDose(new Dose("2", "VOP"));
        polio.addDose(new Dose("3", "VOP"));
        polio.addDose(new Dose("Refuerzo", "VOP"));

        Vaccine rotavirus = new Vaccine("Rotavirus");
        rotavirus.addDose(new Dose("1"));
        rotavirus.addDose(new Dose("2"));
        rotavirus.addDose(new Dose("3"));
        rotavirus.addDose(new Dose("4"));

        Vaccine varicella = new Vaccine("Varicella");
        varicella.addDose(new Dose("F.N."));

        Vaccine syphilis = new Vaccine("Syphilis");
        syphilis.addDose(new Dose("R.N.", "1"));
        syphilis.addDose(new Dose("R.N.", "3"));
        syphilis.addDose(new Dose("R.N.", "3"));
        syphilis.addDose(new Dose("R.N.", "3"));

        _patients = new TreeMap<>();

        Patient rob = new Patient(_patients.size(), "Rob", "Steilberg", Gender.MALE, new Date(823237200000l), "Roatan");
        rob.addVaccine(hepatitis);
        rob.addVaccine(BCG);
        rob.addVaccine(polio);
        rob.addVaccine(rotavirus);
        rob.addVaccine(varicella);
        rob.addVaccine(syphilis);

        _patients.put(rob.getId(), rob);

        Patient alison = new Patient(_patients.size(), "Alison", "Huang", Gender.FEMALE, new Date(1428206400000l), "West Bay");
        alison.addVaccine(hepatitis);
        alison.addVaccine(BCG);
        alison.addVaccine(polio);
        alison.addVaccine(rotavirus);
        alison.addVaccine(varicella);
        alison.addVaccine(syphilis);

        _patients.put(alison.getId(), alison);

        Patient steven = new Patient(_patients.size(), "Steven", "Yang", Gender.MALE, new Date(1078635600000l), "Oakridge");
        steven.addVaccine(hepatitis);
        steven.addVaccine(BCG);
        steven.addVaccine(polio);
        steven.addVaccine(rotavirus);
        steven.addVaccine(varicella);
        steven.addVaccine(syphilis);

        _patients.put(steven.getId(), steven);

    }

}
