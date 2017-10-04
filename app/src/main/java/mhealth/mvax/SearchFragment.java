package mhealth.mvax;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mhealth.mvax.patient.Gender;
import mhealth.mvax.patient.Patient;
import mhealth.mvax.search.PatientDetailActivity;
import mhealth.mvax.search.SearchResultAdapter;

public class SearchFragment extends Fragment {

    private EditText searchBar;

    private ListView patientListView;

    private ArrayList<Patient> patientList;

    private SearchResultAdapter resultAdapter;

    private View view;

    private LayoutInflater inflater;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_search, container, false);


        Button newRecordButton = view.findViewById((R.id.new_record_button));
        newRecordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createNewPatient(v, inflater);
            }
        });

        searchBar = view.findViewById(R.id.search_bar);

        patientListView = view.findViewById(R.id.patient_list_view);

        patientList = new ArrayList<>();
        patientList.add(new Patient("Rob", "Steilberg", Gender.MALE, new Date(823237200000l), "Roatan"));
        patientList.add(new Patient("Alison", "Huang", Gender.FEMALE, new Date(1428206400000l), "West Bay"));
        patientList.add(new Patient("Steven", "Yang", Gender.MALE, new Date(1078635600000l), "Oakridge"));
        patientList.add(new Patient("Matt", "Tribby", Gender.MALE, new Date(1078635600000l), "Los Fuertes"));
        patientList.add(new Patient("Linh", "Bui", Gender.MALE, new Date(1410062400000l), "Punta Gorda"));

        resultAdapter = new SearchResultAdapter(view.getContext(), patientList);
        patientListView.setAdapter(resultAdapter);

        final Context context = view.getContext();
        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Patient selectedPatient = patientList.get(position);

                Intent detailIntent = new Intent(context, PatientDetailActivity.class);

                detailIntent.putExtra("patient", selectedPatient);

                startActivity(detailIntent);
            }

        });


        this.inflater = inflater;

        return view;
    }

    public void createNewPatient(View view, LayoutInflater inflater) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Create New Patient Record");

//        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.modal_new_record, null);
        builder.setView(dialogView);

        final EditText firstNameEditText = dialogView.findViewById(R.id.new_first_name);
        final EditText lastNameEditText = dialogView.findViewById(R.id.new_last_name);
        final EditText communityEditText = dialogView.findViewById(R.id.new_community);


        final Spinner spinner = dialogView.findViewById(R.id.gender_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.gender_spinner_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
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

                Patient newPatient = new Patient(firstName, lastName, gender[0], new Date(cal.getTimeInMillis()), community);
                patientList.add(newPatient);
                resultAdapter.notifyDataSetChanged();
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

}
