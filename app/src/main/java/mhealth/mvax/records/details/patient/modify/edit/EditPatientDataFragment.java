package mhealth.mvax.records.details.patient.modify.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.patient.modify.ModifiableRecordFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for editing existing record patient data
 */

public class EditPatientDataFragment extends ModifiableRecordFragment {

    //================================================================================
    // Static methods
    //================================================================================

    public static EditPatientDataFragment newInstance() {
        return new EditPatientDataFragment();
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_record_details, container, false);
        mInflater = inflater;

        mNewRecord = (Record) getArguments().get("record");
        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        mDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(masterTable)
                .child(recordTable)
                .child(mNewRecord.getDatabaseId());
        renderListView(view);

        return view;
    }

    @Override
    public void onBack() {
    }

}
