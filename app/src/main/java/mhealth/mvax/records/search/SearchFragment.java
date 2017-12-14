/*
Copyright (C) 2018 Duke University

This file is part of mVax.

mVax is free software: you can redistribute it and/or
modify it under the terms of the GNU Affero General Public License
as published by the Free Software Foundation, either version 3,
or (at your option) any later version.

mVax is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU General Public
License along with mVax; see the file LICENSE. If not, see
<http://www.gnu.org/licenses/>.
*/
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.model.record.Vaccine;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.details.patient.modify.create.CreateRecordFragment;
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
    private SearchResultAdapter mSearchResultAdapter;
    private SearchFilter mSearchFilter;
    private LinkedHashMap<String, Vaccine> mVaccineMaster;
    private Map<String, Record> mPatientRecords;

    private DatabaseReference mDatabase;
    private ChildEventListener mVaccineMasterListener;
    private ChildEventListener mRecordListener;


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

        String table = getString(R.string.masterTable);
        String recordTable = getString(R.string.recordTable);
        String vaccineTable = getString(R.string.vaccineTable);
        DummyDataGenerator generator = new DummyDataGenerator(table, recordTable, vaccineTable);
        // uncomment the below lines to populate database with dummy data
        // NOTE: recommend you clear out the database beforehand
        for (int i = 0; i < 100; i++) {
//            generator.generateDummyPatientRecords();
        }
//        generator.generateDummyVaccineMaster();

        initDatabase(); // run this before touching mPatientRecords!

        mSearchResultAdapter = new SearchResultAdapter(view.getContext(), mPatientRecords.values());

        renderNewRecordButton(view, inflater);
        renderFilterSpinner(view);
        initRecordFilters(view);
        renderListView(view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remove all Firebase listeners
        String recordTable = getResources().getString(R.string.recordTable);
        String vaccineTable = getResources().getString(R.string.vaccineTable);
        mDatabase.child(recordTable).removeEventListener(mRecordListener);
        mDatabase.child(vaccineTable).removeEventListener(mVaccineMasterListener);
    }


    //================================================================================
    // Private methods
    //================================================================================

    /**
     * Initializes the Firebase connection and sets up data listeners
     *
     * @return true if authentication and initialization was successful, false otherwise
     */
    public boolean initDatabase() {

        // listener for records
        mRecordListener = new ChildEventListener() {
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
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO throw error
            }
        };

        // listeners for vaccine master list
        mVaccineMasterListener = new ChildEventListener() {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO throw error
            }
        };

//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
////        TODO handle auth fail
//        if (mFirebaseUser == null) {
////            Not logged in, launch the Log In activity
//        } else {
//            mUserId = mFirebaseUser.getUid();
//        }

        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        String vaccineTable = getResources().getString(R.string.vaccineTable);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(masterTable);
        mDatabase.child(recordTable).addChildEventListener(mRecordListener);
        mDatabase.child(vaccineTable).addChildEventListener(mVaccineMasterListener);

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
        // TODO refactor below out to separate method
        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recordId = mSearchResultAdapter.getPatientIdFromDataSource(position);

                DetailFragment recordFrag = DetailFragment.newInstance();

                Bundle args = new Bundle();
                args.putString("recordId", recordId);
                recordFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, recordFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}
