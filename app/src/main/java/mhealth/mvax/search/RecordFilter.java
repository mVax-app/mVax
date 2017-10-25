package mhealth.mvax.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import mhealth.mvax.record.Record;

/**
 * @author Alison Huang
 *
 * Contains algorithms for filtering database results based
 * on various queries
 */

class RecordFilter {

    private Map<String, Record> _records;

    private SearchResultAdapter _adapter;

    private EditText _searchBar;

    private String filter;

    RecordFilter(Map<String, Record> records, SearchResultAdapter adapter, EditText searchBar) {
        _records = records;
        _adapter = adapter;
        _searchBar = searchBar;
    }

    void addFilters() {
        _searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressWarnings("Since15")
            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Record> filtered = new ArrayList<Record>();
                for (Record p : _records.values()) {
                    String attribute = getAttribute(p, filter);
                    System.out.println("PRINT: filter = "+filter+", attribute value = "+attribute);
                    if (attribute.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtered.add(p);
                    }
                }

                filtered.sort(new Comparator<Record>() {
                    @Override
                    public int compare(Record patient1, Record patient2) {
                        String attr1 = getAttribute(patient1, filter);
                        String attr2 = getAttribute(patient2, filter);

                        if (attr1.toLowerCase().indexOf(charSequence.toString().toLowerCase())
                                < attr2.toLowerCase().indexOf(charSequence.toString().toLowerCase())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

                _adapter.refresh(filtered);
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
                return record.getId();
            case "Patient name":
                return record.getFullName();
            case "Year of birth":
                Date date = new Date(record.getDOB());
                return date.toString().substring(24, 28);
            case "Community":
                return record.getCommunity();
            default:
                return record.getId();
        }
    }

    void setFilter(String newFilter) {
        filter = newFilter;
    }

}
