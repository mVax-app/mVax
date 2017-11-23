package mhealth.mvax.records.details.vaccine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.model.record.Vaccine;

/**
 * @author Robert Steilberg
 *         <p>
 *         A Fragment for managing an mVax record's vaccine and dose history
 */

public class VaccineHistoryTab extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;
    private VaccineAdapter mAdapter;
    private Record mRecord;


    //================================================================================
    // Static methods
    //================================================================================

    public static VaccineHistoryTab newInstance() {
        return new VaccineHistoryTab();
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_vaccine_history, container, false);
//        renderVaccineHistory((Record) getArguments().getSerializable("record"));
        return mView;
    }


    //================================================================================
    // Public methods
    //================================================================================

    public void renderVaccineHistory(Record record) {
        ArrayList<Vaccine> vaccineList = record.getVaccines();
        ListView vaccineListView = mView.findViewById(R.id.vaccines_list_view);
        mAdapter = new VaccineAdapter(getContext(), vaccineList, record);
        vaccineListView.setAdapter(mAdapter);
    }

    public void updateVaccineHistory(Record record) {
        mAdapter.refresh(record);
    }

}
