package mhealth.mvax.search;


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
import mhealth.mvax.model.Record;

import android.widget.Toast;

/**
 * @author Robert Steilberg
 *         <p>
 *         A Fragment for managing mVax record details via a dual tab layout
 */

public class RecordFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    //================================================================================
    // Properties
    //================================================================================

    private ViewPager mViewPager;

    private RecordDetailsFragment mDetailsFragment;

    private VaccineHistoryFragment mVaccineHistoryFragment;

    private String mRecordDatabaseId;

    private ChildEventListener mDbListener;


    //================================================================================
    // Static methods
    //================================================================================

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }


    //================================================================================
    // Fragment override methods
    //================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_detail, container, false);

        // init dual tab layout
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab_title_record_details)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab_title_vaccine_history)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mDetailsFragment = RecordDetailsFragment.newInstance();
        mVaccineHistoryFragment = VaccineHistoryFragment.newInstance();

        // init pager to manage tabs
        DualTabPager customPager = new DualTabPager(getActivity().getSupportFragmentManager(), mDetailsFragment, mVaccineHistoryFragment);
        mViewPager = view.findViewById(R.id.pager);
        mViewPager.setAdapter(customPager);

        // enable swipe views
        tabLayout.addOnTabSelectedListener(this);
        // change selected tab when swiped
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getActivity().setTitle(getResources().getString(R.string.record_details));
        mRecordDatabaseId = getArguments().getString("recordId");
        initDatabase(mRecordDatabaseId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remove listener so it doesn't fire after the fragment is destroyed
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("patientRecords")
                .orderByChild("databaseId")
                .equalTo(mRecordDatabaseId)
                .removeEventListener(mDbListener);
    }


    //================================================================================
    // TabLayout override methods
    //================================================================================

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}



    //================================================================================
    // Private methods
    //================================================================================

    /**
     * Initializes the Firebase connection and sets up data listeners
     *
     * @return true if authentication and initialization was successful, false otherwise
     */
    private boolean initDatabase(String databaseId) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        // TODO authentication validation, throw back false if failed
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        mDbListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Record record = dataSnapshot.getValue(Record.class);
                mDetailsFragment.renderRecordDetails(record);
                mVaccineHistoryFragment.renderVaccineHistory(record);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Record record = dataSnapshot.getValue(Record.class);
                mDetailsFragment.updateRecordDetails(record);
                mVaccineHistoryFragment.renderVaccineHistory(record);

                Toast.makeText(getActivity(), R.string.successful_record_update, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // handled in SearchFragment
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.unsuccessful_record_update, Toast.LENGTH_SHORT).show();
            }
        };

        db.child("patientRecords").orderByChild("databaseId").equalTo(databaseId).addChildEventListener(mDbListener);
        return true;
    }

}
