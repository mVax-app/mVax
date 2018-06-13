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

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import mhealth.mvax.R;
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

    private static final String ALGOLIA_INDEX = "patients";

    private Index mSearchIndex;
    private Map<String, SearchResult> mSearchResults;
    private SearchResultAdapter mSearchResultAdapter;
    private Timer searchTimer = new Timer();
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
        mSearchResults = new HashMap<>();
        mSearchResultAdapter = new SearchResultAdapter(mView.getContext(), mSearchResults.values());
        renderNewRecordButton(mView);
        initSearchIndex();
        return mView;
    }

    //================================================================================
    // Private methods
    //================================================================================

    private void renderNewRecordButton(View view) {
        final Button newRecordButton = view.findViewById((R.id.new_record_button));
        newRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, CreateRecordFragment.newInstance())
                        .addToBackStack(null) // back button brings us back to SearchFragment
                        .commit();
            }
        });
        view.findViewById(R.id.search_bar).requestFocus(); // TODO refactor
    }

    private void initSearchIndex() {
        final String configTable = getResources().getString(R.string.configTable);
        final String algoliaTable = getResources().getString(R.string.algoliaTable);
        FirebaseDatabase.getInstance().getReference()
                .child(configTable)
                .child(algoliaTable)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<Map<String, String>> t = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> configVars = dataSnapshot.getValue(t);
                        if (configVars != null) {
                            // get Algolia application ID and API key from server
                            String applicationId = configVars.get("application_id");
                            String searchAPIKey = configVars.get("api_key_search");
                            Client algoliaClient = new Client(applicationId, searchAPIKey);
                            mSearchIndex = algoliaClient.getIndex(ALGOLIA_INDEX);
                            // searching is now possible, render the views
                            initSearchBar();
                            renderListView(mView);
                        } else {
                            Toast.makeText(getActivity(), R.string.failure_search_init, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), R.string.failure_search_init, Toast.LENGTH_SHORT).show();
                    }
                });
    }

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
                        // perform the query if nothing typed for `delay` milliseconds
                        search(charSequence.toString());
                    }
                }, 1000);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void search(String rawQuery) {
        String query = rawQuery.trim();
        mSearchResults.clear(); // clear out results from previous search

        if (query.isEmpty()) {
            // no query, don't return anything
            refreshSearchResults();
            return;
        }

        mSearchIndex.searchAsync(new Query(query), new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject result, AlgoliaException e) {
                try {
                    JSONArray hits = (JSONArray) result.get("hits");
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject patient = (JSONObject) hits.get(i);
                        SearchResult s = new SearchResult((String) patient.get("databaseKey"));
                        s.setFirstName((String) patient.get("firstName"));
                        s.setLastName((String) patient.get("lastName"));
                        s.setDOB((Long) patient.get("dob"));
                        s.setCommunity((String) patient.get("community"));
                        mSearchResults.put(s.getDatabaseKey(), s);
                        refreshSearchResults();
                    }
                } catch (JSONException e1) {
                    Toast.makeText(getActivity(), R.string.failure_search, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refreshSearchResults() {
        // force UI updates to the main thread
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSearchResultAdapter.refresh(mSearchResults.values());
            }
        });
    }

    private void renderListView(View view) {
        final ListView patientListView = view.findViewById(R.id.record_list_view);
        patientListView.setAdapter(mSearchResultAdapter);
        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedDatabaseKey = mSearchResultAdapter.getSelectedDatabaseKey(position);
                final RecordFragment detailFrag = RecordFragment.newInstance(selectedDatabaseKey);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, detailFrag)
                        .addToBackStack(null) // back button brings us back to SearchFragment
                        .commit();
            }
        });
    }
}
