package mhealth.mvax.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mhealth.mvax.R;
import mhealth.mvax.model.Detail;
import mhealth.mvax.model.Record;

/**
 * @author Robert Steilberg
 */

public class NewRecordFragment extends Fragment {

    private LayoutInflater mInflater;

    private Record mNewRecord;

    private DatabaseReference mDatabase;

    private boolean isNewRecord;

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

        // FOR NEW RECORD
        String recordTableName = getResources().getString(R.string.recordTable);
        mDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(recordTableName)
                .push();
        mNewRecord = new Record(mDatabase.getKey());
        renderListView(view);

        // FOR EXISTING RECORD

//        mNewRecord = (Record) getArguments().get("record");
//        String recordTableName = getResources().getString(R.string.recordTable);
//        mDatabase = FirebaseDatabase
//                .getInstance()
//                .getReference()
//                .child(recordTableName)
//                .child(mNewRecord.getDatabaseId());
//        renderListView(view);

        return view;
    }

    //================================================================================
    // Private methods
    //================================================================================


    private void renderListView(View view) {

        TextView recordName = view.findViewById(R.id.record_details_title);
        recordName.setText(R.string.new_record_title);

        final ListView detailsListView = view.findViewById(R.id.details_list_view);
        final NewRecordDetailsAdapter adapter = new NewRecordDetailsAdapter(getContext(), mNewRecord.getSectionedAttributes(getContext(), mInflater));
        detailsListView.setAdapter(adapter);

        Button saveButton = (Button) mInflater.inflate(R.layout.save_record_button, null);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.setValue(mNewRecord);
                // transition to detail! back button brings you back to search

                RecordFragment recordFrag = RecordFragment.newInstance();
                Bundle args = new Bundle();
                args.putString("recordId", mNewRecord.getDatabaseId());
                recordFrag.setArguments(args);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);

                transaction.replace(R.id.frame_layout, recordFrag);
                transaction.commit();

            }
        });
        detailsListView.addFooterView(saveButton);

    }

    @Override
    public void onDestroy() {
        // FOR NEW RECORD
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
//        transaction.replace(R.id.frame_layout, new SearchFragment());
//        transaction.commit();
        super.onDestroy();

        // FOR EXISTING RECORD
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
