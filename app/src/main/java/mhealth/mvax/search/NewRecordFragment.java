package mhealth.mvax.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.model.Record;

/**
 * @author Robert Steilberg
 */

public class NewRecordFragment extends Fragment {

    private LayoutInflater mInflater;

    private Record mNewRecord;

    //================================================================================
    // Static methods
    //================================================================================

    public static NewRecordFragment newInstance() {
        return new NewRecordFragment();
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_record_details, container, false);
        mInflater = inflater;

        DatabaseReference database = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("patientRecords")
                .push();

        mNewRecord = new Record(database.getKey());

        renderListView(view);
        return view;
    }


    //================================================================================
    // Private methods
    //================================================================================


    private void renderListView(View view) {

        TextView recordName = view.findViewById(R.id.record_details_title);
        recordName.setText(R.string.new_record_title);

        final ListView detailsListView = view.findViewById(R.id.details_list_view);

        final RecordDetailsAdapter adapter = new NewRecordDetailsAdapter(getContext(), mNewRecord.getSectionedAttributes(getContext()));
        detailsListView.setAdapter(adapter);

        Button deleteButton = (Button) mInflater.inflate(R.layout.save_record_button, null);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < detailsListView.getCount(); i++) {
                    View v = detailsListView.getChildAt(i);
                    EditText text = (EditText) v.findViewById(R.id.edittext_value);
                    if (text != null) {
                        // todo bugs here
                        String id = text.getHint().toString();
                        String val = text.getText().toString();
                        System.out.println(id);
                    }
                }
            }
        });
        detailsListView.addFooterView(deleteButton);

    }


}
