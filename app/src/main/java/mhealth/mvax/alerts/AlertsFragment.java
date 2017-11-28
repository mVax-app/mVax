package mhealth.mvax.alerts;

import android.app.ExpandableListActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import mhealth.mvax.R;
import android.support.v4.app.Fragment;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class AlertsFragment extends Fragment {

    FirebaseDatabase db = FirebaseDatabase.getInstance();


    public static AlertsFragment newInstance() {
//        AlertsFragment fragment = new AlertsFragment();
//        return fragment;
        return new AlertsFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alerts, null);
        ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.list);
        elv.setAdapter(new SavedTabsListAdapter());
        return v;
    }


    private boolean initDatabase(){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        String masterTable = getResources().getString(R.string.masterTable);

        DatabaseReference ref = mDatabase.child("mVax");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String value = dataSnapshot.getValue(String.class);
                    String key = dataSnapshot.getKey();
                    String totl = key + ": " + value;

                }
            }
        });
        return true;
    }






    public class SavedTabsListAdapter extends BaseExpandableListAdapter {


        private String[] groups = { "High Priority (> 2 Weeks Overdue)", "Medium Priority (1 - 2 Weeks Overdue)", "Low Priority (< 1 Week Overdue)"};

        private String[][] children = {
                { "Steilberg, Robert Hays II", "Bob, Muffin Lee IV", "Steilberg, Robert Hays", "Steilberg, Robert Hays" },
                { "Bob, Muffin Lee IV", "Bob, Muffin Lee VI"},
                { "Steilberg, Robert Hays III", "Steilberg, Robert Hays II" }
        };

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return children[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return groups[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return children[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(AlertsFragment.this.getActivity());
            textView.setText(getGroup(i).toString());
            textView.setTextSize(35);
            textView.setPadding(100,0,0,0);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(AlertsFragment.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            textView.setTextSize(25);
            textView.setPadding(100,0,0,0);
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

}