/*
Copyright (C) 2018 Duke University

This file is part of mVax.

mVax is free software: you can redistribute it and/or
modify it under the terms of the GNU Affero General Public License
as published by the Free Software Foundation, either version 3,
or (at your option) any later version.

mVax is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU General Public
License along with mVax; see the file LICENSE. If not, see
<http://www.gnu.org/licenses/>.
*/
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
