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
package mhealth.mvax.auth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import mhealth.mvax.R;
import mhealth.mvax.model.user.UserWithUID;

/**
 * This fragment represents the page where administrators can approve or deny user requests to
 * gain access to the application. The fragment is built around a simple 4 columned-list view with
 * a modal that pops up for approval/denial
 * @author Matthew Tribby
 * November, 2017
 */
public class ApproveUsersFragment extends android.support.v4.app.Fragment {
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String UID = "UID";

    private ListView userRequests;
    private ArrayList<HashMap<String, String>> requests;
    private UserRegRequestsAdapter adapter;

    public ApproveUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_approve_users, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setInfoButton(view);
        setupUserRequestLV(view);


        super.onViewCreated(view, savedInstanceState);
    }

    private void setInfoButton(View view){
        ImageView info = (ImageView) view.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserRoleInfoBuilder(getActivity()).getBuilder().show();
            }
        });
    }

    private void setupUserRequestLV(View view){
        userRequests = (ListView) view.findViewById(R.id.approveUsersLV);

        requests = new ArrayList<HashMap<String, String>>();

        adapter = new UserRegRequestsAdapter(getActivity(), requests);

        userRequests.setAdapter(adapter);

        userRequests.setEmptyView(view.findViewById(R.id.empty_list));

        //Get data out of user requests
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(getResources().getString(R.string.userRequestsTable));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //help from: https://stackoverflow.com/questions/40366717/firebase-for-android-how-can-i-loop-through-a-child-for-each-child-x-do-y
                //requests = new ArrayList<HashMap<String, String>>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    UserWithUID user = snapshot.getValue(UserWithUID.class);
                    HashMap<String, String> userRequest = new HashMap<>();
                    userRequest.put(FIRST_NAME, user.getFirstName());
                    userRequest.put(LAST_NAME, user.getLastName());
                    userRequest.put(EMAIL, user.getEmail());
                    userRequest.put(UID, user.getUid());
                    requests.add(userRequest);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("databaseError", "Error in ApproveUsersFragment.java");
            }
        });

    }



    private void refresh(){
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        tr.replace(R.id.frame_layout, new ApproveUsersFragment());
        tr.commit();
    }
}
