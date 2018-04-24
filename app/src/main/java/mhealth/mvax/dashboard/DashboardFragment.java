package mhealth.mvax.dashboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import mhealth.mvax.model.immunization.Vaccine;


public class DashboardFragment extends android.app.Fragment implements TabLayout.OnTabSelectedListener {

    //================================================================================
    // Properties
    //================================================================================

    private View mView;
    private ViewPager mViewPager;

    //================================================================================
    // Static methods
    //================================================================================

    public static DashboardFragment newInstance() {
        final DashboardFragment newInstance = new DashboardFragment();
//        final Bundle args = new Bundle();
//        args.putString("databaseKey", databaseKey);
//        newInstance.setArguments(args);
        return newInstance;
    }

    //================================================================================
    // Override methods
    //================================================================================

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initTabs();
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

    private void initTabs() {
        // set up tab layout
        final TabLayout tabLayout = mView.findViewById(R.id.dashboard_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Progress Bars"));
        tabLayout.addTab(tabLayout.newTab().setText("Charts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this); // enable swipe views

        // set up pager
        mViewPager = mView.findViewById(R.id.dashboard_viewpager);
        // change selected tab when swiped
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // initialize tab fragments
        final ProgressBarTab progressBarTab = ProgressBarTab.newInstance();
        final ChartTab chartTab = ChartTab.newInstance();

        // init adapter for pager
        final TabPageAdapter pager = new TabPageAdapter(getChildFragmentManager(), progressBarTab, chartTab);
        mViewPager.setAdapter(pager);
    }

}
