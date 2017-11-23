package mhealth.mvax.records.details.record.modify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;

import mhealth.mvax.model.record.Record;

/**
 * @author Robert Steilberg
 */

public abstract class ModifiableRecordFragment extends Fragment {

    protected LayoutInflater mInflater;

    protected DatabaseReference mDatabase;

    protected Record mNewRecord;

    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDestroy() {
        onBack();
        super.onDestroy();
    }

    public abstract void onBack();

    //================================================================================
    // Private methods
    //================================================================================

}
