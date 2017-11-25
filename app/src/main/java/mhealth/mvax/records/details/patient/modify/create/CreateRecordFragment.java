package mhealth.mvax.records.details.patient.modify.create;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.model.record.Vaccine;
import mhealth.mvax.records.details.patient.modify.ModifiableRecordFragment;
import mhealth.mvax.records.search.SearchFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for creating patient data for a new record
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

        // TODO fix unchecked cast
        ArrayList<Vaccine> masterVaccines = (ArrayList<Vaccine>) getArguments().getSerializable("vaccines");

        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        mDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(masterTable)
                .child(recordTable)
                .push();
        mNewRecord = new Record(mDatabase.getKey(), masterVaccines);

        renderListView(view);
        return view;
    }

    @Override
    public void onBack() {
        // add "-> Search" to back stack
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new SearchFragment());
        transaction.commit();
    }

}
