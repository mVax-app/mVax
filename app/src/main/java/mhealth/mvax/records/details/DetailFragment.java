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

import mhealth.mvax.R;
import mhealth.mvax.records.details.patient.view.PatientDataTab;
import mhealth.mvax.records.details.vaccine.VaccineScheduleTab;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fragment for managing record data via a dual tab layout
 */

public class DetailFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;
    private ViewPager mViewPager;

    //================================================================================
    // Static methods
    //================================================================================

    public static DetailFragment newInstance(String databaseKey) {
        final DetailFragment newInstance = new DetailFragment();
        final Bundle args = new Bundle();
        args.putString("databaseKey", databaseKey);
        newInstance.setArguments(args);
        return newInstance;
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_record_detail, container, false);
        initTabs(getArguments().getString("databaseKey"));
        return mView;
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

    private void initTabs(String recordDatabaseKey) {
        // set up tab layout
        final TabLayout tabLayout = mView.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_title_record_details)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_title_vaccine_history)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this); // enable swipe views

        // set up pager
        mViewPager = mView.findViewById(R.id.pager);
        // change selected tab when swiped
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // initialize tab fragments
        final PatientDataTab patientDataTab = PatientDataTab.newInstance(recordDatabaseKey);
        final VaccineScheduleTab vaccineScheduleTab = VaccineScheduleTab.newInstance(recordDatabaseKey);

        // init adapter for pager
        final DualTabPagerAdapter pager = new DualTabPagerAdapter(getChildFragmentManager(), patientDataTab, vaccineScheduleTab);
        mViewPager.setAdapter(pager);
    }

}
