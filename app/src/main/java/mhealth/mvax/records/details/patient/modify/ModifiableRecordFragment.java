package mhealth.mvax.records.details.patient.modify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.search.SearchFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         Abstract class for modifying a record, either newly created or
 *         existing
 */

public abstract class ModifiableRecordFragment extends Fragment {

    protected LayoutInflater mInflater;

    protected DatabaseReference mDatabase;

    protected Record mNewRecord;

    @Override
    public void onDestroy() {
        onBack();
        super.onDestroy();
    }

    protected void renderListView(View view) {
        TextView recordName = view.findViewById(R.id.record_details_title);
        recordName.setText(R.string.new_record_title);

        final ListView detailsListView = view.findViewById(R.id.details_list_view);
        final EditPatientDataAdapter adapter = new EditPatientDataAdapter(getContext(), mNewRecord.getSectionedAttributes(getContext()));
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

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new SearchFragment())
                        .addToBackStack(null)
                        .commit();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, recordFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        detailsListView.addFooterView(saveButton);

    }

    public abstract void onBack();
}
