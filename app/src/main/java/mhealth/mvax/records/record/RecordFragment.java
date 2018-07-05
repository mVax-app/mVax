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
package mhealth.mvax.records.record;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mhealth.mvax.R;
import mhealth.mvax.records.record.patient.view.PatientDetailsTab;
import mhealth.mvax.records.record.vaccine.VaccineScheduleTab;

/**
 * @author Robert Steilberg
 * <p>
 * Fragment for managing record data via a dual tab layout
 */
public class RecordFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private View mView;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    public static RecordFragment newInstance(String databaseKey) {
        final RecordFragment newInstance = new RecordFragment();
        final Bundle args = new Bundle();
        args.putString("databaseKey", databaseKey);
        newInstance.setArguments(args);
        return newInstance;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_record, container, false);
        initTabs(getArguments().getString("databaseKey"), inflater, container);
        return mView;
    }

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

    private void initTabs(String recordDatabaseKey, LayoutInflater inflater, ViewGroup parent) {
        // set up tab layout
        tabLayout = mView.findViewById(R.id.record_tabs);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_title_record_details)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_title_vaccine_schedule)));

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TextView tabView = (TextView) inflater.inflate(R.layout.tab, parent, false);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabView);
                ColorStateList textColor = tabLayout.getTabTextColors();
                tabView.setTextColor(textColor);
            }
        }

        tabLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.base));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this); // enable swipe views

        // set up pager
        mViewPager = mView.findViewById(R.id.pager);
        // change selected tab when swiped
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // initialize tab fragments
        final PatientDetailsTab patientDetailsTab = PatientDetailsTab.newInstance(recordDatabaseKey);
        final VaccineScheduleTab vaccineScheduleTab = VaccineScheduleTab.newInstance(recordDatabaseKey);

        // init adapter for pager
        final DualTabPagerAdapter pager = new DualTabPagerAdapter(getChildFragmentManager(), patientDetailsTab, vaccineScheduleTab);
        mViewPager.setAdapter(pager);
    }

}
