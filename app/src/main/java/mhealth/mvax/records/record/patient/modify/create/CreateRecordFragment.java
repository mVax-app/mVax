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
package mhealth.mvax.records.record.patient.modify.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.record.patient.modify.ModifiablePatientFragment;

/**
 * @author Robert Steilberg
 * <p>
 * Fragment for creating a new record
 */
public class CreateRecordFragment extends ModifiablePatientFragment {

    public static CreateRecordFragment newInstance() {
        return new CreateRecordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_record_details, container, false);
        setTitle(view, R.string.new_record_title);
        // create Patient object for new record
        mPatient = new Patient(mPatientRef.push().getKey());
        initSaveButton(view.findViewById(R.id.header_button));
        renderListView(view.findViewById(R.id.details_list));
        return view;
    }

}
