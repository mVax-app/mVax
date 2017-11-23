package mhealth.mvax.records.details.record.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import mhealth.mvax.records.details.record.RecordDetailsAdapter;
import mhealth.mvax.records.details.record.modify.edit.EditRecordFragment;

/**
 * @author Robert Steilberg
 */

public class RecordDetailsTab extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;

    private LayoutInflater mInflater;

    private Record mRecord;

    private RecordDetailsAdapter mAdapter;

    //================================================================================
    // Static methods
    //================================================================================

    public static RecordDetailsTab newInstance() {
        return new RecordDetailsTab();
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_record_details, container, false);
        mInflater = inflater;
        renderRecordDetails((Record) getArguments().getSerializable("record"));
        return mView;
    }


    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Initialize record details; this method should only be called once per fragment instance
     *
     * @param record is the record with which to render details
     */
    public void renderRecordDetails(Record record) {
        mRecord = record;
        setRecordName();
        ListView detailsListView = mView.findViewById(R.id.details_list_view);
        mAdapter = new ExistingRecordDetailsAdapter(getContext(), mRecord.getSectionedAttributes(getContext(), mInflater));
        detailsListView.setAdapter(mAdapter);
        addEditButton(detailsListView);
        addDeleteButton(detailsListView);
    }

    /**
     * Update record details after they have already been initialized via renderRecordDetails()
     *
     * @param record is the record with which to update details
     */
    public void updateRecordDetails(Record record) {
        mRecord = record;
        setRecordName();
        mAdapter.refresh(record.getSectionedAttributes(getContext(), mInflater));
    }


    //================================================================================
    // Private methods
    //================================================================================

    private void setRecordName() {
        TextView recordName = mView.findViewById(R.id.record_details_title);
        recordName.setText(mRecord.getFullName());
    }

    private void addEditButton(ListView vaccineListView) {
        Button deleteButton = (Button) mInflater.inflate(R.layout.button_edit_record, null);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditRecordFragment editRecordFrag = EditRecordFragment.newInstance();

                Bundle args = new Bundle();
                args.putSerializable("record", mRecord);
                editRecordFrag.setArguments(args);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, editRecordFrag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        vaccineListView.addFooterView(deleteButton);
    }

    private void addDeleteButton(ListView vaccineListView) {
        Button deleteButton = (Button) mInflater.inflate(R.layout.button_delete_record, null);
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
        getActivity().onBackPressed(); // we deleted the current record, so end the activity
    }

}
