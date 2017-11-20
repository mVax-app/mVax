package mhealth.mvax.records.details.record.modify.create;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mhealth.mvax.R;
import mhealth.mvax.model.Record;
import mhealth.mvax.model.Vaccine;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.details.record.modify.ModifiableRecordDetailsAdapter;
import mhealth.mvax.records.details.record.modify.ModifiableRecordFragment;
import mhealth.mvax.records.search.SearchFragment;

/**
 * @author Robert Steilberg
 */

public class CreateRecordFragment extends ModifiableRecordFragment {

    //================================================================================
    // Static methods
    //================================================================================

    public static CreateRecordFragment newInstance() {
        return new CreateRecordFragment();
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_record_details, container, false);
        mInflater = inflater;


        ArrayList<Vaccine> vaccines = (ArrayList<Vaccine>) getArguments().getSerializable("vaccines");

        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        mDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(masterTable)
                .child(recordTable)
                .push();
        mNewRecord = new Record(mDatabase.getKey(), vaccines);
        renderListView(view);
        return view;
    }

    @Override
    public void onBack() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new SearchFragment());
        transaction.commit();
    }

    protected void renderListView(View view) {

        TextView recordName = view.findViewById(R.id.record_details_title);
        recordName.setText(R.string.new_record_title);

        final ListView detailsListView = view.findViewById(R.id.details_list_view);
        final ModifiableRecordDetailsAdapter adapter = new ModifiableRecordDetailsAdapter(getContext(), mNewRecord.getSectionedAttributes(getContext(), mInflater));
        detailsListView.setAdapter(adapter);

        Button saveButton = (Button) mInflater.inflate(R.layout.save_record_button, null);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.setValue(mNewRecord);

                DetailFragment recordFrag = DetailFragment.newInstance();
                Bundle args = new Bundle();
                args.putString("recordId", mNewRecord.getDatabaseId());
                recordFrag.setArguments(args);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, recordFrag);
                transaction.commit();

            }
        });
        detailsListView.addFooterView(saveButton);

    }


}