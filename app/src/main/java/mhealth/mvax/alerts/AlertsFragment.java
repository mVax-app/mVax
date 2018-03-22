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
package mhealth.mvax.alerts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import mhealth.mvax.R;
import android.support.v4.app.Fragment;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * @author Steven Yang
 *        Displaying Overdue Patients
 */

public class AlertsFragment extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    ChildEventListener mPatientListener;


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
        View v = inflater.inflate(R.layout.fragment_overdue, null);
        ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.list);
        elv.setAdapter(new SavedTabsListAdapter());
        return v;
    }


    private boolean initDatabase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String masterTable = getResources().getString(R.string.dataTable);
        //get overdue table
        String vaccineTable = getResources().getString(R.string.vaccineTable);

        mPatientListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        return true;
    }


    public class SavedTabsListAdapter extends BaseExpandableListAdapter {


        private String[] groups = { getResources().getString(R.string.high_priority), getResources().getString(R.string.medium_priority), getResources().getString(R.string.low_priority)};

        private String[][] children = {
                { "Steilberg, Robert Hays II", "Bob, Muffin Lee IV"},
                { "Bob, Muffin Lee IV"},
                { "Steilberg, Robert Hays II"}
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
            //Temporary PlaceHolder for patient contact info - Demo purpose
            if(getChild(i, i1).toString().equals("Steilberg, Robert Hays II")){
                textView.setText(getChild(i, i1).toString() + "\nPhone Number: 8046904814" + "\nCommunity: Alspaugh" + "\nAddress: 9014 Tarrytown Drive, Richmind, VA 23229");
            }
            else{
                textView.setText(getChild(i, i1).toString() + "\nPhone Number: 3840185960" + "\nCommunity: Roatan" + "\nAddress: 1 Muffin Ln, Atlanta, GA");
            }
            //textView.setText(getChild(i, i1).toString() + " Phone Number: " + "8046904814");
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
