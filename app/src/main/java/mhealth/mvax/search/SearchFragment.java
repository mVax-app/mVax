package mhealth.mvax.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import mhealth.mvax.record.Gender;
import mhealth.mvax.record.Record;

/**
 * @author Robert Steilberg, Alison Huang
 *         <p>
 *         A fragment for handling the record search and detail pages
 */

public class SearchFragment extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    private Map<String, Record> _patientRecords;

    private SearchResultAdapter _SearchResultAdapter;

    private FirebaseAuth _auth;

    private DatabaseReference _database;

    private EditText searchBar;
    private Spinner filterSpinner;
    private String currentFilter = "Patient name";
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

        filterSpinner = (Spinner) view.findViewById(R.id.filter_spinner);
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.filter_spinner_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos != 0) {
                    currentFilter = filterSpinner.getItemAtPosition(pos).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchBar = (EditText)view.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            @SuppressWarnings("Since15")
            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Patient> filtered = new ArrayList<Patient>();
                for (Patient p : _patientRecords.values()) {
                    String attribute = getAttribute(p);
                    System.out.println("PRINT: attribute = "+attribute);
                    if (attribute.toLowerCase().indexOf(charSequence.toString().toLowerCase()) != -1) {
                        filtered.add(p);
                    }
                }

                filtered.sort(new Comparator<Patient>() {
                    @Override
                    public int compare(Patient patient1, Patient patient2) {
                        String attr1 = getAttribute(patient1);
                        String attr2 = getAttribute(patient2);

                        if (attr1.toLowerCase().indexOf(charSequence.toString().toLowerCase())
                                < attr2.toLowerCase().indexOf(charSequence.toString().toLowerCase())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

                _SearchResultAdapter.refresh(filtered);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ListView patientListView = view.findViewById(R.id.patient_list_view);

        // call this method to populate database with dummy data
        // NOTE: recommend you clear out the database beforehand
//        new DummyDataGenerator(_database).generateDummyData();

        initNewRecordButton(view, inflater);
        initDatabase();

        _SearchResultAdapter = new SearchResultAdapter(view.getContext(), _patientRecords.values());

        initRecordFilters(view);
        initListView(view);

        return view;
    }

    private String getAttribute(Patient patient) {
        switch (currentFilter) {
            case "Patient ID":
                return patient.getId();
            case "Patient name":
                return patient.getFullName();
            case "Year of birth":
                Date date = new Date(patient.getDOB());
                return date.toString().substring(24, 28);
            case "Community":
                return patient.getCommunity();
            default:
                return patient.getId();
        }
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

                Record newRecord = new Record(patientRecords.getKey(), firstName, lastName, gender[0], cal.getTimeInMillis(), community);

                // push the update to the database, which will trigger update listeners,
                // updating the view
                patientRecords.setValue(newRecord);
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

    private void initNewRecordButton(View view, final LayoutInflater inflater) {
        Button newRecordButton = view.findViewById((R.id.new_record_button));
        newRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPatient(v, inflater);
            }
        });
    }

    private void initRecordFilters(View view) {
        EditText searchBar = view.findViewById(R.id.search_bar);
        new RecordFilter(_patientRecords, _SearchResultAdapter, searchBar).addFilters();
    }

    private void initListView(View view) {
        ListView patientListView = view.findViewById(R.id.patient_list_view);
        patientListView.setAdapter(_SearchResultAdapter);
        final SearchFragment searchFragment = this;
        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String patientId = _SearchResultAdapter.getPatientIdFromDataSource(position);
                RecordDetailFragment fragment = RecordDetailFragment.newInstance();
                fragment.initWithRecord(patientId);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                transaction.replace( getId(), searchFragment).addToBackStack(null);
                transaction.replace(R.id.frame_layout, fragment);
                transaction.commit();
            }

        });
    }

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

        // TODO put database query strings in a values.xml file
        _database.child("patientRecords").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Record record = dataSnapshot.getValue(Record.class);
                _patientRecords.put(record.getId(), record);
                _SearchResultAdapter.refresh(_patientRecords.values());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                onChildAdded(dataSnapshot, prevChildKey);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Record record = dataSnapshot.getValue(Record.class);
                _patientRecords.remove(record.getId());
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

}
