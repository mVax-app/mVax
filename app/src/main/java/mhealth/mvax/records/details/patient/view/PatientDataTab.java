package mhealth.mvax.records.details.patient.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.details.RecordTab;
import mhealth.mvax.records.details.patient.PatientDataAdapter;
import mhealth.mvax.records.details.patient.modify.edit.EditPatientDataFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for managing an mVax record's patient details;
 *         takes a record as an argument, wrapped in a Bundle
 */

public class PatientDataTab extends Fragment implements RecordTab {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;
    private PatientDataAdapter mAdapter;
    private Record mRecord;

    //================================================================================
    // Static methods
    //================================================================================

    public static PatientDataTab newInstance() {
        return new PatientDataTab();
    }


    //================================================================================
    //Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_record_details, container, false);
        mRecord = (Record) getArguments().getSerializable("record");
        render();
        return mView;
    }


    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Initialize record details; this method should only be called once per
     * fragment instance
     */
    public void render() {
        setRecordName();

        mAdapter = new ViewPatientDataAdapter(getContext(), mRecord.getSectionedAttributes(getContext()));
        ListView detailsListView = mView.findViewById(R.id.details_list_view);
        detailsListView.setAdapter(mAdapter);

        addEditButton(detailsListView);
        addDeleteButton(detailsListView);
    }

    /**
     * Update record details after they have already been initialized
     * via renderRecordDetails()
     *
     * @param record is the record with which to update details
     */
    public void update(Record record) {
        mRecord = record;
        setRecordName();
        mAdapter.refresh(record.getSectionedAttributes(getContext()));
    }


    //================================================================================
    // Private methods
    //================================================================================

    private void setRecordName() {
        TextView recordNameTextView = mView.findViewById(R.id.record_details_title);
        recordNameTextView.setText(mRecord.getFullName());
    }

    private void addEditButton(ListView vaccineListView) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Button editButton = (Button) inflater.inflate(R.layout.button_edit_record, null);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // pop "-> Search" from back stack
                getActivity().getSupportFragmentManager().popBackStack();

                // add "-> Detail" to back stack
                DetailFragment onBackFrag = DetailFragment.newInstance();
                Bundle args = new Bundle();
                args.putString("recordId", mRecord.getDatabaseId());
                onBackFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, onBackFrag)
                        .addToBackStack(null)
                        .commit();

                // transition to edit patient data fragment
                EditPatientDataFragment editDataFrag = EditPatientDataFragment.newInstance();
                args = new Bundle();
                args.putSerializable("record", mRecord);
                editDataFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, editDataFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        vaccineListView.addFooterView(editButton);
    }

    private void addDeleteButton(ListView vaccineListView) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Button deleteButton = (Button) inflater.inflate(R.layout.button_delete_record, null);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptForRecordDelete();
            }
        });
        vaccineListView.addFooterView(deleteButton);
    }

    private void promptForRecordDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.modal_record_delete_title);
        builder.setMessage(R.string.modal_record_delete_message);
        builder.setPositiveButton(getResources().getString(R.string.modal_new_dosage_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCurrentRecord();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.modal_new_dosage_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void deleteCurrentRecord() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        db.child(masterTable).child(recordTable).child(mRecord.getDatabaseId()).setValue(null);
        getActivity().onBackPressed(); // deleted the current record, so end the activity
    }

}
