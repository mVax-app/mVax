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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.record.RecordFragment;
import mhealth.mvax.records.record.patient.modify.create.CreateRecordFragment;

/**
 * @author Robert Steilberg, Alison Huang
 *         <p>
 *         Fragment for searching for patients and seguing to
 *         detail and immunization views
 */
public class SearchFragment extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    private Map<String, Patient> mPatients;

    private SearchResultAdapter mSearchResultAdapter;
    private SearchFilter mSearchFilter;

    private DatabaseReference mPatientRef;
    private ChildEventListener mPatientListener;

    private View mView;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        mPatients = new HashMap<>();
        mSearchResultAdapter = new SearchResultAdapter(mView.getContext(), mPatients.values());

//        initDatabase();
        renderNewRecordButton(mView, inflater);
//        renderFilterSpinner(view);


        initSearchBar();


        renderListView(mView);

        return mView;
    }

    @Override
    public void onDestroyView() {
//        mPatientRef.removeEventListener(mPatientListener);
        super.onDestroyView();
    }

    //================================================================================
    // Private methods
    //================================================================================


    private Timer searchTimer = new Timer();

    private void initSearchBar() {
        final EditText searchBar = mView.findViewById(R.id.search_bar);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                try {
                    searchTimer.cancel();
                } catch (IllegalStateException ignored) {
                }
                searchTimer = new Timer();
                searchTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        search(charSequence.toString());
                    }
                }, 1000);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void search(String query) {
        // define database ref
        final String dataTable = getResources().getString(R.string.dataTable);
        final String patientTable = getResources().getString(R.string.patientTable);
        Query q = FirebaseDatabase.getInstance().getReference()
                .child(dataTable)
                .child(patientTable)
                .orderByChild("fullName")
                .equalTo(query);


        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Patient patient = dataSnapshot.getValue(Patient.class);
                if (patient != null) {
                    mPatients.put(patient.getDatabaseKey(), patient);
                    mSearchResultAdapter.refresh(mPatients.values());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Initializes the Firebase connection and sets up data listeners
     */
    private void initDatabase() {
        // define database ref
        final String dataTable = getResources().getString(R.string.dataTable);
        final String patientTable = getResources().getString(R.string.patientTable);
        mPatientRef = FirebaseDatabase.getInstance().getReference().child(dataTable).child(patientTable);

        // define listener
        mPatientListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                final Patient patient = dataSnapshot.getValue(Patient.class);
                if (patient != null) {
                    mPatients.put(patient.getDatabaseKey(), patient);
                    mSearchResultAdapter.refresh(mPatients.values());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                onChildAdded(dataSnapshot, prevChildKey);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final Patient patient = dataSnapshot.getValue(Patient.class);
                if (patient != null) {
                    mPatients.remove(patient.getDatabaseKey());
                    mSearchResultAdapter.refresh(mPatients.values());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_records_download, Toast.LENGTH_SHORT).show();
            }
        };

        // set listener to ref
        mPatientRef.addChildEventListener(mPatientListener);
    }

    private void renderNewRecordButton(View view, final LayoutInflater inflater) {
        final Button newRecordButton = view.findViewById((R.id.new_record_button));
        newRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewRecord();
            }
        });
    }

    private void createNewRecord() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, CreateRecordFragment.newInstance())
                .addToBackStack(null) // back button brings us back to SearchFragment
                .commit();
    }

//    private void renderFilterSpinner(View view) {
//        // TODO replace all of this with Firebase queries
//        final Spinner spinner = view.findViewById(R.id.search_filter_spinner);
//        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(view.getContext(),
//                R.array.filter_spinner_array, android.R.layout.simple_spinner_item);
//        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(filterAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//                if (pos != 0) {
//                    mSearchFilter.setFilter(spinner.getItemAtPosition(pos).toString());
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
//    }

//    private void initRecordFilters(View view) {
//        final EditText searchBar = view.findViewById(R.id.search_bar);
//        mSearchFilter = new SearchFilter(mPatients, mSearchResultAdapter, searchBar);
//        mSearchFilter.addFilters();
//    }

    private void renderListView(View view) {
        final ListView patientListView = view.findViewById(R.id.record_list_view);
        patientListView.setAdapter(mSearchResultAdapter);
        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedDatabaseKey = mSearchResultAdapter.getSelectedDatabaseKey(position);

                final RecordFragment detailFrag = RecordFragment.newInstance(selectedDatabaseKey);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, detailFrag)
                        .addToBackStack(null) // back button brings us back to SearchFragment
                        .commit();
            }
        });
    }


}
