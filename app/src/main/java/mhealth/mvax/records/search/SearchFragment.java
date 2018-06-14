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
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.algolia.search.saas.Client;
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

import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.modify.create.CreateRecordFragment;

/**
 * @author Robert Steilberg, Alison Huang
 * <p>
 * Fragment for searching for patients and seguing to detail
 * and immunization views
 */
public class SearchFragment extends Fragment {

    private static final String ALGOLIA_INDEX = "patients";

    private Index mSearchIndex;
    private SearchResultAdapter mAdapter;
    private View mView;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        initNewRecordButton();
        initSearchIndex();
        mView.findViewById(R.id.search_bar).requestFocus();
        return mView;
    }

    private void initNewRecordButton() {
        final Button newRecordButton = mView.findViewById((R.id.new_record_button));
        newRecordButton.setOnClickListener(v -> getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frame, CreateRecordFragment.newInstance())
                .addToBackStack(null) // back button brings us back to SearchFragment
                .commit());
    }

    private void initSearchIndex() {
        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.configTable))
                .child(getString(R.string.algoliaTable))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<Map<String, String>> t = new GenericTypeIndicator<Map<String, String>>() {
                        };
                        Map<String, String> configVars = dataSnapshot.getValue(t);
                        if (configVars != null) {
                            String applicationId = configVars.get("application_id");
                            String searchAPIKey = configVars.get("api_key_search");
                            Client algoliaClient = new Client(applicationId, searchAPIKey);
                            mSearchIndex = algoliaClient.getIndex(ALGOLIA_INDEX);
                            // searching now possible, render the views
                            initSearchBar();
                            renderListView();
                        } else {
                            Toast.makeText(getActivity(), R.string.search_init_fail, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), R.string.search_init_fail, Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void afterTextChanged(Editable editable) {
                search(editable.toString());
            }
        });
    }

    private void search(String rawQuery) {
        showSpinner();

        String query = rawQuery.trim();
        mAdapter.clearSearchResults(); // clear out results from previous search
        mAdapter.setHashCode(query.hashCode()); // debouce
        if (query.isEmpty()) {
            hideSpinner();
            return;
        }

        mSearchIndex.searchAsync(new Query(query), (result, e) -> {
            try {
                JSONArray hits = (JSONArray) result.get("hits");
                for (int i = 0; i < hits.length(); i++) {
                    // TODO handle not having a field
                    JSONObject patient = (JSONObject) hits.get(i);
                    SearchResult s = new SearchResult((String) patient.get("databaseKey"));
                    s.setFirstName((String) patient.get("firstName"));
                    s.setLastName((String) patient.get("lastName"));
                    if (patient.has("dob")) {
                        s.setDOB((Long) patient.get("dob"));
                    }
                    s.setCommunity((String) patient.get("community"));
                    mAdapter.addSearchResult(s, query.hashCode());
                }
                hideSpinner();
            } catch (JSONException e1) {
                Toast.makeText(getActivity(), R.string.search_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderListView() {
        RecyclerView usersList = mView.findViewById(R.id.search_results);
        mAdapter = new SearchResultAdapter(getActivity());
        usersList.setAdapter(mAdapter);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(getContext()));
        usersList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        checkForLeftoverQuery();
    }

    private void checkForLeftoverQuery() {
        final EditText searchBar = mView.findViewById(R.id.search_bar);
        final String leftoverQuery = searchBar.getText().toString();
        if (!leftoverQuery.isEmpty()) {
            search(leftoverQuery);
        }
    }

    private void showSpinner() {
        final ProgressBar spinner = mView.findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
    }

    private void hideSpinner() {
        final ProgressBar spinner = mView.findViewById(R.id.spinner);
        spinner.setVisibility(View.INVISIBLE);
    }
}
