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
package mhealth.mvax.records.details;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.model.record.Record;
import mhealth.mvax.records.details.patient.view.PatientDataTab;
import mhealth.mvax.records.details.vaccine.VaccineScheduleTab;

import android.widget.Toast;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for managing mVax record details via a dual tab layout
 */

public class DetailFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;
    private ViewPager mViewPager;
    private PatientDataTab mPatientDataTab;
    private VaccineScheduleTab mVaccineScheduleTab;
    private String mRecordDatabaseId;
    private ChildEventListener mDatabaseListener;

    //================================================================================
    // Static methods
    //================================================================================

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }


    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_record_detail, container, false);
        mRecordDatabaseId = getArguments().getString("recordId");
        initTabs();
        initDatabase(mRecordDatabaseId);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remove listener so it doesn't fire after the fragment is destroyed
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        String databaseIdField = getResources().getString(R.string.databaseId);
        db.child(masterTable)
                .child(recordTable)
                .orderByChild(databaseIdField)
                .equalTo(mRecordDatabaseId)
                .removeEventListener(mDatabaseListener);
    }


    //================================================================================
    // TabLayout override methods
    //================================================================================

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }


    //================================================================================
    // Private methods
    //================================================================================

    private void initTabs() {
        TabLayout tabLayout = mView.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_title_record_details)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_title_vaccine_history)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this); // enable swipe views

        mViewPager = mView.findViewById(R.id.pager);
        // change selected tab when swiped
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initTabViews() {
        // init pager to manage tabs
        DualTabPager pager = new DualTabPager(getChildFragmentManager(), mPatientDataTab, mVaccineScheduleTab);
        mViewPager.setAdapter(pager);
    }

    /**
     * Initializes the Firebase connection and sets up data listeners
     *
     * @return true if authentication and initialization was successful,
     * false otherwise
     */
    private boolean initDatabase(String databaseId) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        // TODO authentication validation, throw back false if failed
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        mDatabaseListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                mPatientDataTab = PatientDataTab.newInstance();
                mVaccineScheduleTab = VaccineScheduleTab.newInstance();

                Record record = dataSnapshot.getValue(Record.class);
                Bundle args = new Bundle();
                args.putSerializable("record", record);
                mPatientDataTab.setArguments(args);
                mVaccineScheduleTab.setArguments(args);

                initTabViews();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Record record = dataSnapshot.getValue(Record.class);
                mPatientDataTab.update(record);
                mVaccineScheduleTab.update(record);
                Toast.makeText(getActivity(), R.string.successful_record_update, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                getActivity().onBackPressed(); // transition back to search
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.unsuccessful_record_update, Toast.LENGTH_SHORT).show();
            }
        };

        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        String databaseIdField = getResources().getString(R.string.databaseId);
        db.child(masterTable).child(recordTable)
                .orderByChild(databaseIdField)
                .equalTo(databaseId)
                .addChildEventListener(mDatabaseListener);
        return true;
    }

}
