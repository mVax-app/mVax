package mhealth.mvax.records.details.vaccine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.RecordTab;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for managing an mVax record's vaccine schedule
 */

public class VaccineScheduleTab extends Fragment implements RecordTab {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;
    private VaccineAdapter mAdapter;
    private Record mRecord;


    //================================================================================
    // Static methods
    //================================================================================

    public static VaccineScheduleTab newInstance() {
        return new VaccineScheduleTab();
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_vaccine_history, container, false);
        mRecord = (Record) getArguments().getSerializable("record");
        render();
        return mView;
    }


    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Performs the initial render of the record's vaccine schedule
     * using the record passed in to the fragment as an argument
     */
    public void render() {
        ListView vaccineListView = mView.findViewById(R.id.vaccines_list_view);
        mAdapter = new VaccineAdapter(getContext(), mRecord);
        vaccineListView.setAdapter(mAdapter);
    }

    /**
     * Updates the view with an updated record's vaccine schedule
     *
     * @param updatedRecord the updated record containing the vaccine schedule
     */
    public void update(Record updatedRecord) {
        mAdapter.refresh(updatedRecord);
    }

}
