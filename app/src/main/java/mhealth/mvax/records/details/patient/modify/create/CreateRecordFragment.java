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
package mhealth.mvax.records.details.patient.modify.create;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Guardian;
import mhealth.mvax.model.record.Patient;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.model.record.Vaccine;
import mhealth.mvax.records.details.patient.modify.ModifiableRecordFragment;
import mhealth.mvax.records.search.SearchFragment;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for creating patient data for a new record
 */

public class CreateRecordFragment extends ModifiableRecordFragment {

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
        View view = inflater.inflate(R.layout.tab_record_details, container, false);
        mInflater = inflater;


        TextView recordName = view.findViewById(R.id.record_details_title);
        recordName.setText(R.string.new_record_title);

        mPatient = new Patient(mPatientDatabaseRef.push().getKey());
        mGuardian = new Guardian(mGuardianDatabaseRef.push().getKey());

        mPatient.setGuardianDatabaseID(mGuardian.getDatabaseKey());
        mGuardian.addDependent(mPatient.getDatabaseKey());

        renderListView(view);
        return view;
    }

    @Override
    public void onBack() {
        // add "-> Search" to back stack
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new SearchFragment());
        transaction.commit();
    }

}
