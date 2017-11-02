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
import java.util.HashMap;
import java.util.Map;

import mhealth.mvax.record.Sex;
import mhealth.mvax.record.Record;

/**
 * @author Robert Steilberg, Alison Huang
 *         <p>
 *         A fragment for handling the record search and segues to the detail pages
 */

public class SearchFragment extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private Map<String, Record> mPatientRecords;

    private SearchResultAdapter mSearchResultAdapter;

    private RecordFilter mRecordFilter;


    //================================================================================
    // Static methods
    //================================================================================

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPatientRecords = new HashMap<>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // uncomment the below line to populate database with dummy data
        // NOTE: recommend you clear out the database beforehand
//        new DummyDataGenerator().generateDummyData();

        initDatabase(); // run this before touching mPatientRecords!

        mSearchResultAdapter = new SearchResultAdapter(view.getContext(), mPatientRecords.values());

        renderNewRecordButton(view, inflater);
        renderFilterSpinner(view);
        initRecordFilters(view);
        renderListView(view);

        return view;
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
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        // TODO handle auth fail
//        if (mFirebaseUser == null) {
////            Not logged in, launch the Log In activity
//        } else {
//            mUserId = mFirebaseUser.getUid();
//        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // TODO put database query strings in a values.xml file
        mDatabase.child("patientRecords").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Record record = dataSnapshot.getValue(Record.class);
                mPatientRecords.put(record.getDatabaseId(), record);
                mSearchResultAdapter.refresh(mPatientRecords.values());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                onChildAdded(dataSnapshot, prevChildKey);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Record record = dataSnapshot.getValue(Record.class);
                mPatientRecords.remove(record.getDatabaseId());
                mSearchResultAdapter.refresh(mPatientRecords.values());
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return true;
    }

    private void renderNewRecordButton(View view, final LayoutInflater inflater) {
        Button newRecordButton = view.findViewById((R.id.new_record_button));
        newRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPatient(view, inflater);
            }
        });
    }

    private void createNewPatient(View view, LayoutInflater inflater) {
        // create modal
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(getResources().getString(R.string.modal_new_record_title));

        View dialogView = inflater.inflate(R.layout.modal_new_record, null);
        builder.setView(inflater.inflate(R.layout.modal_new_record, null));

        final EditText firstNameEditText = dialogView.findViewById(R.id.new_first_name);
        final EditText lastNameEditText = dialogView.findViewById(R.id.new_last_name);
        final EditText communityEditText = dialogView.findViewById(R.id.new_community);

        final Spinner spinner = dialogView.findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.gender_spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Sex[] gender = new Sex[1];
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos != 0) {
                    switch (spinner.getItemAtPosition(pos).toString()) {
                        case "Male":
                            gender[0] = Sex.MALE;
                            break;
                        case "Female":
                            gender[0] = Sex.FEMALE;
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

                DatabaseReference patientRecords = mDatabase.child("patientRecords").push();

//                Record newRecord = new Record(patientRecords.getKey(), firstName, lastName, gender[0], cal.getTimeInMillis(), community);
                // push the update to the database, which will trigger update listeners,
                // updating the view
//                patientRecords.setValue(newRecord);
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

    private void renderFilterSpinner(View view) {
        final Spinner spinner = view.findViewById(R.id.search_filter_spinner);
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.filter_spinner_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filterAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos != 0) {
                    mRecordFilter.setFilter(spinner.getItemAtPosition(pos).toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initRecordFilters(View view) {
        EditText searchBar = view.findViewById(R.id.search_bar);
        mRecordFilter = new RecordFilter(mPatientRecords, mSearchResultAdapter, searchBar);
        mRecordFilter.addFilters();
    }

    private void renderListView(View view) {
        ListView patientListView = view.findViewById(R.id.record_list_view);
        patientListView.setAdapter(mSearchResultAdapter);
        final SearchFragment searchFragment = this;
        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recordId = mSearchResultAdapter.getPatientIdFromDataSource(position);

                RecordFragment recordFrag = RecordFragment.newInstance();

                Bundle args = new Bundle();
                args.putString("recordId", recordId);
                recordFrag.setArguments(args);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                transaction.replace(getId(), searchFragment).addToBackStack(null); // so that back button works
                transaction.replace(R.id.frame_layout, recordFrag);
                transaction.commit();
            }
        });
    }

}
