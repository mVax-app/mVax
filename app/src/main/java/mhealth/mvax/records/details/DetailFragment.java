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
import mhealth.mvax.records.details.record.view.RecordDetailsTab;
import mhealth.mvax.records.details.vaccine.VaccineHistoryTab;

import android.widget.Toast;

/**
 * @author Robert Steilberg
 *         <p>
 *         A Fragment for managing mVax record details via a dual tab layout
 */

public class DetailFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    //================================================================================
    // Properties
    //================================================================================

    private ViewPager mViewPager;

    private RecordDetailsTab mRecordDetailsTab;

    private VaccineHistoryTab mVaccineHistoryTab;

    private String mRecordDatabaseId;

    private ChildEventListener mDbListener;

    private View mView;

    //================================================================================
    // Static methods
    //================================================================================

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }


    //================================================================================
    // Fragment override methods
    //================================================================================

    // TODO we can probably get rid of this override
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPager = new DualTabPager(getChildFragmentManager());

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_record_detail, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecordDatabaseId = getArguments().getString("recordId");
        initDatabase(mRecordDatabaseId);
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
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }


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

        // init dual tab layout
        TabLayout tabLayout = mView.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab_title_record_details)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab_title_vaccine_history)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // enable swipe views
        tabLayout.addOnTabSelectedListener(this);


        mViewPager = mView.findViewById(R.id.pager);

        // change selected tab when swiped
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        mDbListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Record record = dataSnapshot.getValue(Record.class);

                mRecordDetailsTab = RecordDetailsTab.newInstance();
                mVaccineHistoryTab = VaccineHistoryTab.newInstance();

                Bundle args = new Bundle();
                args.putSerializable("record", record);
                mRecordDetailsTab.setArguments(args);
                mVaccineHistoryTab.setArguments(args);

                // init pager to manage tabs
                DualTabPager pager = new DualTabPager(getChildFragmentManager(), mRecordDetailsTab, mVaccineHistoryTab);
                mViewPager.setAdapter(pager);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Record record = dataSnapshot.getValue(Record.class);
                // TODO change full re-render to update
                mRecordDetailsTab.updateRecordDetails(record);
                mVaccineHistoryTab.renderVaccineHistory(record);

                Toast.makeText(getActivity(), R.string.successful_record_update, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // handled in SearchFragment
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.unsuccessful_record_update, Toast.LENGTH_SHORT).show();
            }
        };

        String masterTable = getResources().getString(R.string.masterTable);
        String recordTable = getResources().getString(R.string.recordTable);
        String databaseIdField = getResources().getString(R.string.databaseId);
        db.child(masterTable).child(recordTable).orderByChild(databaseIdField).equalTo(databaseId).addChildEventListener(mDbListener);
        return true;
    }

}
