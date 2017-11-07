package mhealth.mvax.records.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import mhealth.mvax.model.Record;

/**
 * @author Alison Huang
 *
 * Contains algorithms for filtering database results based
 * on various queries
 */

class Filter {

    private Map<String, Record> mRecords;

    private SearchResultAdapter mAdapter;

    private EditText mSearchBar;

    private String mFilter;

    Filter(Map<String, Record> records, SearchResultAdapter adapter, EditText searchBar) {
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
                    System.out.println("PRINT: filter = "+mFilter+", attribute value = "+attribute);
                    if (attribute.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtered.add(p);
                    }
                }

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
