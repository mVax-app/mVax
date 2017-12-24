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
package mhealth.mvax.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Vaccine;

public class DashboardFragment extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private Map<String, Vaccine> mVaccinationRecords;

    private VaccineCardAdapter mVaccinationCardAdapter;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVaccinationRecords = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // uncomment the below line to populate database with dummy data
        // NOTE: recommend you clear out the database beforehand
//        new DummyDataGenerator().generateDummyData();

        initDatabase(); // run this before touching mPatientRecords!

        mVaccinationCardAdapter = new VaccineCardAdapter(view.getContext(), mVaccinationRecords.values());

        renderListView(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = (Button)view.findViewById(R.id.button);

        Button formsSwitch = (Button) view.findViewById(R.id.forms_button);
        formsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFormsFragment();
            }
        });

    }

    private boolean initDatabase() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        // TODO handle auth fail
//        if (mFirebaseUser == null) {
////            Not logged in, launch the Log In activity
//        } else {
//            mUserId = mFirebaseUser.getUid();
//        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String masterTable = getResources().getString(R.string.dataTable);
        String vaccineTable = getResources().getString(R.string.vaccineTable);

        mDatabase.child(masterTable).child(vaccineTable).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Vaccine vaccine = dataSnapshot.getValue(Vaccine.class);
                mVaccinationRecords.put(vaccine.getName(), vaccine);
                mVaccinationCardAdapter.refresh(mVaccinationRecords.values());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                onChildAdded(dataSnapshot, prevChildKey);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Record record = dataSnapshot.getValue(Record.class);
//                mVaccinationRecords.remove(record.getDatabaseId());
                mVaccinationCardAdapter.refresh(mVaccinationRecords.values());
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return true;
    }

    private void renderListView(View view) {
        ListView vaccineListView = view.findViewById(R.id.vaccine_card_list_view);
        vaccineListView.setAdapter(mVaccinationCardAdapter);
        final DashboardFragment dashboardFragment = this;
        vaccineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recordId = mVaccinationCardAdapter.getVaccineNameFromDataSource(position);
            }
        });


    }

    private void switchToFormsFragment(){
        Fragment fragment = new FormsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}