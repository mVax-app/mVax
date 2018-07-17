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
package com.mvax.records.record.patient.modify.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvax.R;
import com.mvax.model.record.Patient;
import com.mvax.records.record.patient.modify.ModifiablePatientFragment;
import com.mvax.records.utilities.AlgoliaUtilities;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        setTitle(R.string.new_record_title);

        if (savedInstanceState == null) {
            // create new Patient
            mPatient = new Patient(mPatientRef.push().getKey());
            // TODO put search engine in here
        } else {
            // resuming from environment change
            mPatient = (Patient) savedInstanceState.getSerializable("patient");
        }

        mSearchEngine = new AlgoliaUtilities(getActivity(), initSuccessful -> {
            mLoadingModal.dismiss();
            if (initSuccessful) initSaveButton(view.findViewById(R.id.header_button));
        });
        renderListView(view.findViewById(R.id.details_list));
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("patient", mPatient);
    }

}
