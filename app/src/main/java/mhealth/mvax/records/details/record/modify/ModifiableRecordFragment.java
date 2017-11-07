package mhealth.mvax.records.details.record.modify;

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

import mhealth.mvax.R;
import mhealth.mvax.model.Record;
import mhealth.mvax.records.details.DetailFragment;
import mhealth.mvax.records.search.SearchFragment;

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
