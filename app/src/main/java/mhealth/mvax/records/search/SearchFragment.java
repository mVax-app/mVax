package mhealth.mvax.records.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.Record;
import mhealth.mvax.model.Vaccine;
import mhealth.mvax.records.details.record.modify.create.CreateRecordFragment;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.utilities.DummyDataGenerator;

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

    private Map<String, Record> mPatientRecords;

    private SearchResultAdapter mSearchResultAdapter;

    private SearchFilter mSearchFilter;

    private LinkedHashMap<String, Vaccine> mVaccineMaster;


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
        mVaccineMaster = new LinkedHashMap<>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // uncomment the below lines to populate database with dummy data
        // NOTE: recommend you clear out the database beforehand
        String table = getString(R.string.masterTable);
        String recordTable = getString(R.string.recordTable);
        String vaccineTable = getString(R.string.vaccineTable);

        DummyDataGenerator generator = new DummyDataGenerator(table, recordTable, vaccineTable);
//        generator.generateDummyPatientRecords();
//        generator.generateDummyVaccineMaster();

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

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // TODO put database query strings in a values.xml file
        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        String vaccineTable = getResources().getString(R.string.vaccineTable);


        mDatabase.child(masterTable).child(vaccineTable).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                mVaccineMaster.put(vaccine.getDatabaseKey(), vaccine);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                mVaccineMaster.remove(vaccine.getDatabaseKey());
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // TODO test
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO throw error
            }
        });


        mDatabase.child(masterTable).child(recordTable).addChildEventListener(new ChildEventListener() {
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
                createNewRecord();
            }
        });
    }

    private void createNewRecord() {

        CreateRecordFragment newRecordFrag = CreateRecordFragment.newInstance();


        Bundle args = new Bundle();
        args.putSerializable("vaccines", new ArrayList<>(mVaccineMaster.values()));
        newRecordFrag.setArguments(args);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, newRecordFrag);
        transaction.addToBackStack(null);
        transaction.commit();
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
                    mSearchFilter.setFilter(spinner.getItemAtPosition(pos).toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initRecordFilters(View view) {
        EditText searchBar = view.findViewById(R.id.search_bar);
        mSearchFilter = new SearchFilter(mPatientRecords, mSearchResultAdapter, searchBar);
        mSearchFilter.addFilters();
    }

    private void renderListView(View view) {
        ListView patientListView = view.findViewById(R.id.record_list_view);
        patientListView.setAdapter(mSearchResultAdapter);
        final SearchFragment searchFragment = this;
        // TODO refactor below out to separate method
        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recordId = mSearchResultAdapter.getPatientIdFromDataSource(position);

                DetailFragment recordFrag = DetailFragment.newInstance();

                Bundle args = new Bundle();
                args.putString("recordId", recordId);
                recordFrag.setArguments(args);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
//                transaction.replace(getId(), searchFragment).addToBackStack(null); // so that back button works
                transaction.replace(R.id.frame_layout, recordFrag).addToBackStack(null);
                transaction.commit();
            }
        });
    }

}