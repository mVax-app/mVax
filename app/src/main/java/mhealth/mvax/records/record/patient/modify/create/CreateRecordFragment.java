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
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.records.record.patient.modify.ModifiablePatientFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for creating a record; creates a Patient and Guardian object,
 *         creates an association between them, and allows the user to add details
 *         for each object
 */
public class CreateRecordFragment extends ModifiablePatientFragment {

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
        final View view = inflater.inflate(R.layout.record_details, container, false);
        setFragmentTitle(view, R.string.new_record_title);

        // create Patient and Guardian objects for new record
        mPatient = new Patient(mPatientRef.push().getKey());
        // create association between Patient and Guardian
        mGuardian = new Guardian(mGuardianRef.push().getKey(), mPatient.getDatabaseKey());
        mPatient.setGuardianDatabaseKey(mGuardian.getDatabaseKey());

        renderListView(view);
        return view;
    }

}
