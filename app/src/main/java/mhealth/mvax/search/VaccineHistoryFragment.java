package mhealth.mvax.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import mhealth.mvax.R;
import mhealth.mvax.model.Record;
import mhealth.mvax.record.vaccine.Vaccine;

/**
 * @author Robert Steilberg
 *         <p>
 *         A Fragment for managing an mVax record's vaccine and dose history
 */

public class VaccineHistoryFragment extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;


    //================================================================================
    // Static methods
    //================================================================================

    public static VaccineHistoryFragment newInstance() {
        return new VaccineHistoryFragment();
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_vaccine_history, container, false);
        return mView;
    }


    //================================================================================
    // Public methods
    //================================================================================

    void renderVaccineHistory(Record record) {
        ArrayList<Vaccine> vaccineList = record.getVaccineList();
        ListView vaccineListView = mView.findViewById(R.id.vaccines_list_view);
        VaccineAdapter adapter = new VaccineAdapter(getContext(), vaccineList, record);
        vaccineListView.setAdapter(adapter);
    }

}
