package mhealth.mvax.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
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

    RecordFilter(Map<String, Record> records, SearchResultAdapter adapter, EditText searchBar) {
        _records = records;
        _adapter = adapter;
        _searchBar = searchBar;
    }

    void addFilters() {
        _searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // count characters beginning at start are about to be replaced with new text of length after
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                ArrayList<Record> filtered = new ArrayList<>();
                for (Record record : _records.values()) {
                    String recordName = record.getFullName();
                    if (recordName.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtered.add(record);
                    }
                }
                _adapter.refresh(filtered);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
