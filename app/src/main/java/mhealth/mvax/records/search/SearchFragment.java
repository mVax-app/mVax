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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import mhealth.mvax.R;
import mhealth.mvax.records.record.RecordFragment;
import mhealth.mvax.records.record.patient.modify.create.CreateRecordFragment;
import mhealth.mvax.records.utilities.AlgoliaUtilities;
import mhealth.mvax.utilities.modals.LoadingModal;

/**
 * @author Robert Steilberg, Alison Huang
 * <p>
 * Fragment for searching for patients and seguing to detail
 * and immunization views
 */
public class SearchFragment extends Fragment {

    private View mView;
    private SearchResultAdapter mAdapter;
    private AlgoliaUtilities mSearchEngine;
    private LoadingModal mLoadingModal;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        mLoadingModal = new LoadingModal(mView);
        mLoadingModal.createAndShow();
        initNewRecordButton();
        initSearchIndex();

        // TODO get rid of this
//        final RecordFragment detailFrag = RecordFragment.newInstance("-LGjx9gtvqjqubEyEH3Y");
//        getFragmentManager().beginTransaction()
//                .replace(R.id.frame, detailFrag)
//                .addToBackStack(null) // back button brings us back to SearchFragment
//                .commit();

        return mView;
    }

    private void initNewRecordButton() {
        final Button newRecordButton = mView.findViewById((R.id.new_record_button));
        newRecordButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, CreateRecordFragment.newInstance())
                .addToBackStack(null) // back button brings us back to SearchFragment
                .commit());
    }

    private void initSearchIndex() {
        mSearchEngine = new AlgoliaUtilities(getActivity(), initSuccessful -> {
            mLoadingModal.dismiss();
            if (initSuccessful) {
                renderListView(); // render ListView first so we have somewhere to put results
                initSearchBar();
            }
        });
    }

    private void initSearchBar() {
        final EditText searchBar = mView.findViewById(R.id.search_bar);
        checkForLeftoverQuery(searchBar);
        searchBar.requestFocus();
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
        hideNoResultsMessage();
        showSpinner();

        final String query = rawQuery.trim();
        mAdapter.clearSearchResults(); // clear out results from previous search
        mAdapter.setHashCode(query.hashCode()); // debounce
        if (query.isEmpty()) {
            hideSpinner();
            showNoResultsMessage();
            return;
        }

        mSearchEngine.search(query, result -> {
            hideNoResultsMessage();
            mAdapter.addSearchResult(result, query.hashCode());
        }, () -> {
            hideSpinner();
            if (mAdapter.getItemCount() == 0) showNoResultsMessage();
        });
    }

    private void renderListView() {
        final RecyclerView usersList = mView.findViewById(R.id.search_results);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SearchResultAdapter(getActivity());
        usersList.setAdapter(mAdapter);
    }

    private void checkForLeftoverQuery(EditText searchBar) {
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

    private void showNoResultsMessage() {
        mView.findViewById(R.id.no_search_results).setVisibility(View.VISIBLE);
    }

    private void hideNoResultsMessage() {
        mView.findViewById(R.id.no_search_results).setVisibility(View.INVISIBLE);
    }
}
