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

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mhealth.mvax.R;
import mhealth.mvax.model.record.SearchResult;
import mhealth.mvax.records.record.RecordFragment;
import mhealth.mvax.records.utilities.NullableDateFormat;

/**
 * @author Robert Steilberg
 * <p>
 * Adapter for listing record search results
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private ArrayList<SearchResult> mSearchResults;
    private FragmentActivity mActivity;
    private int mHashCode;

    SearchResultAdapter(FragmentActivity activity) {
        mSearchResults = new ArrayList<>();
        mActivity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View row;
        TextView name, medicalId, dob;

        ViewHolder(View view) {
            super(view);
            row = view;
            name = view.findViewById(R.id.name);
            medicalId = view.findViewById(R.id.medicalId);
            dob = view.findViewById(R.id.dob);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_result, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SearchResult result = mSearchResults.get(position);
        holder.name.setText(result.getName());

        final String DOBprompt = mActivity.getString(R.string.DOB_prompt);
        final String DOBstr = DOBprompt
                + " " + NullableDateFormat.getString(result.getDOB());
        holder.dob.setText(DOBstr);

        final String medicalIdPrompt = mActivity.getString(R.string.medical_id_prompt)
                + " " + result.getMedicalId();
        holder.medicalId.setText(medicalIdPrompt);

        holder.row.setOnClickListener(v -> {
            SearchResult chosenResult = mSearchResults.get(position);
            final RecordFragment detailFrag = RecordFragment.newInstance(chosenResult.getDatabaseKey());
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, detailFrag)
                    .addToBackStack(null) // back button brings us back to SearchFragment
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }

    /**
     * Adds a newly received SearchResult to the adapter, pushing changes to the
     * UI; search result is only added if it corresponds to the most recent query (debounce)
     *
     * @param result   SearchResult to add
     * @param hashCode hashCode of the current query
     */
    public void addSearchResult(SearchResult result, int hashCode) {
        if (hashCode == mHashCode) {
            mSearchResults.add(result);
        }
        notifyDataSetChanged();
    }

    /**
     * Clear out all search results from the UI
     */
    public void clearSearchResults() {
        mSearchResults.clear();
        notifyDataSetChanged();
    }

    /**
     * Sets the hash code of the most recent query; used for debouncing
     *
     * @param hashCode the hash code of the most recent query
     */
    public void setHashCode(int hashCode) {
        mHashCode = hashCode;
    }

}
