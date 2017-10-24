package mhealth.mvax.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.v4.app.FragmentTransaction;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;

import java.util.ArrayList;
import java.util.Calendar;
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

    //================================================================================
    // Properties
    //================================================================================

    private Map<String, Patient> _patientRecords;

    private SearchResultAdapter _SearchResultAdapter;

    private FirebaseAuth _auth;

    private DatabaseReference _database;

    private EditText searchBar;
    //================================================================================
    // Public methods
    //================================================================================

    public SearchFragment() {
        _patientRecords = new TreeMap<>();
    }

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
        newRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPatient(v, inflater);
            }
        });

        searchBar = (EditText)view.findViewById(R.id.search_bar);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("TEXT CHANGE: "+charSequence);
                ArrayList<Patient> filtered = new ArrayList<Patient>();
                for (Patient p : _patientRecords.values()) {
                    String name = p.getFullName();
                    if (name.toLowerCase().indexOf(charSequence.toString().toLowerCase()) != -1) {
                        filtered.add(p);
                    }
                }

                _SearchResultAdapter.refresh(filtered);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ListView patientListView = view.findViewById(R.id.patient_list_view);

        // call this method to populate database with dummy data
        // NOTE: recommend you clear out the database beforehand
//        createDummyData();

        initDatabase();

        _SearchResultAdapter = new SearchResultAdapter(view.getContext(), _patientRecords.values());
        patientListView.setAdapter(_SearchResultAdapter);

        final SearchFragment searchFragment = this;

        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String patientId = _SearchResultAdapter.getPatientIdFromDataSource(position);


                RecordDetailFragment fragment = RecordDetailFragment.newInstance();
                fragment.initWithPatient(patientId);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                transaction.replace( getId(), searchFragment).addToBackStack(null);
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();


//                Intent detailIntent = new Intent(context, PatientDetailActivity.class);
//                detailIntent.putExtra("patientId", patientId);
//                startActivity(detailIntent);
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

                DatabaseReference patientRecords = _database.child("patientRecords").push();

                Patient newPatient = new Patient(patientRecords.getKey(), firstName, lastName, gender[0], cal.getTimeInMillis(), community);

                // push the update to the database, which will trigger update listeners,
                // updating the view
                patientRecords.setValue(newPatient);
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
        FirebaseUser mFirebaseUser = _auth.getCurrentUser();
        // TODO handle auth fail
//        if (mFirebaseUser == null) {
////            Not logged in, launch the Log In activity
//        } else {
//            mUserId = mFirebaseUser.getUid();
//        }

        _database = FirebaseDatabase.getInstance().getReference();

        _database.child("patientRecords").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                _patientRecords.put(patient.getId(), patient);
                _SearchResultAdapter.refresh(_patientRecords.values());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                onChildAdded(dataSnapshot, prevChildKey);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                _patientRecords.remove(patient.getId());
                _SearchResultAdapter.refresh(_patientRecords.values());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }


    private void createDummyData() {

        Vaccine hepatitis = new Vaccine("Hepatitis B");
        Dose dose1 = new Dose("R.N.");
        dose1.setDate(823237200000l);
        hepatitis.addDose(dose1);


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


        DatabaseReference patientRecords = _database.child("patientRecords").push();
        Patient rob = new Patient(patientRecords.getKey(), "Rob", "Steilberg", Gender.MALE, 823237200000l, "Roatan");
        rob.addVaccine(hepatitis);
        rob.addVaccine(BCG);
        rob.addVaccine(polio);
        rob.addVaccine(rotavirus);
        rob.addVaccine(varicella);
        rob.addVaccine(syphilis);
        patientRecords.setValue(rob);

        patientRecords = _database.child("patientRecords").push();
        Patient alison = new Patient(patientRecords.getKey(), "Alison", "Huang", Gender.FEMALE, 1428206400000l, "West Bay");
        alison.addVaccine(hepatitis);
        alison.addVaccine(BCG);
        alison.addVaccine(polio);
        alison.addVaccine(rotavirus);
        alison.addVaccine(varicella);
        alison.addVaccine(syphilis);
        patientRecords.setValue(alison);

        patientRecords = _database.child("patientRecords").push();
        Patient steven = new Patient(patientRecords.getKey(), "Steven", "Yang", Gender.MALE, 1078635600000l, "Oakridge");
        steven.addVaccine(hepatitis);
        steven.addVaccine(BCG);
        steven.addVaccine(polio);
        steven.addVaccine(rotavirus);
        steven.addVaccine(varicella);
        steven.addVaccine(syphilis);
        patientRecords.setValue(steven);
    }

}
