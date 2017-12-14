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

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import mhealth.mvax.model.record.Record;

/**
 * @author Alison Huang
 *         <p>
 *         Contains algorithms for filtering database results based
 *         on various queries
 */

class SearchFilter {

    private Map<String, Record> mRecords;

    private SearchResultAdapter mAdapter;

    private EditText mSearchBar;

    private String mFilter;

    SearchFilter(Map<String, Record> records, SearchResultAdapter adapter, EditText searchBar) {
        mRecords = records;
        mAdapter = adapter;
        mSearchBar = searchBar;
    }

    void addFilters() {
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressWarnings("Since15")
            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Record> filtered = new ArrayList<Record>();
                for (Record p : mRecords.values()) {
                    String attribute = getAttribute(p, mFilter);
                    System.out.println("PRINT: filter = " + mFilter + ", attribute value = " + attribute);
                    if (attribute.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtered.add(p);
                    }
                }

                // TODO this throws an error when sorting more than ~500 records of randomly generated names
                filtered.sort(new Comparator<Record>() {
                    @Override
                    public int compare(Record patient1, Record patient2) {
                        String attr1 = getAttribute(patient1, mFilter);
                        String attr2 = getAttribute(patient2, mFilter);

                        if (attr1.toLowerCase().indexOf(charSequence.toString().toLowerCase())
                                < attr2.toLowerCase().indexOf(charSequence.toString().toLowerCase())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

                mAdapter.refresh(filtered);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private String getAttribute(Record record, String filter) {
        // TODO default search?
        if (filter == null) return record.getFullName();
        switch (filter) {
            case "Patient ID":
                return record.getDatabaseId();
            case "Patient name":
                return record.getFullName();
            case "Year of birth":
                Date date = new Date(record.getDOB());
                return date.toString().substring(24, 28);
            case "Community":
                return record.getCommunity();
            case "Parent ID":
                return record.getParentId();
            case "Parent name":
                return record.getParentFullName();
            default:
                return record.getDatabaseId();
        }
    }

    void setFilter(String newFilter) {
        mFilter = newFilter;
    }

}
