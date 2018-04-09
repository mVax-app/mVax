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

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.UserRecoverableException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import mhealth.mvax.R;
import mhealth.mvax.model.user.User;
import mhealth.mvax.model.user.UserRequest;
import mhealth.mvax.model.user.UserWithUID;

/**
 * Supports a fragment that allows an administrator to approve or deny requests
 * for new mVax accounts
 *
 * @author Matthew Tribby, Robert Steilberg
 */
public class ApproveFragment extends Fragment {


    private RecyclerView mUserRequestList;
    private LinearLayoutManager mLayoutManager;
    private UserRequestAdapter mAdapter;
    private DatabaseReference mRef;
    private ChildEventListener mListener;
    private TextView mNoRequestText;


    public static ApproveFragment newInstance() {
        return new ApproveFragment();
    }

    // ondestroy

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approve_users, container, false);


        initDatabase();

        mNoRequestText = view.findViewById(R.id.no_user_requests);


        mUserRequestList = view.findViewById(R.id.user_request_list);
        mUserRequestList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mUserRequestList.setLayoutManager(mLayoutManager);
        mAdapter = new UserRequestAdapter();
        mUserRequestList.setAdapter(mAdapter);
        mUserRequestList.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void onDestroyView() {
        mRef.removeEventListener(mListener);
        super.onDestroyView();
    }

    private void initDatabase() {

        final String requestTable = getResources().getString(R.string.userRequestsTable);
        mRef = FirebaseDatabase.getInstance().getReference()
                .child(requestTable);
        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserRequest request = dataSnapshot.getValue(UserRequest.class);
                mAdapter.addRequest(request);
                mNoRequestText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // this should not ever happen
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserRequest request = dataSnapshot.getValue(UserRequest.class);
                mAdapter.removeRequest(request);
                if (mAdapter.getItemCount() == 0) mNoRequestText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // this should not ever happen
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_user_request_download, Toast.LENGTH_SHORT).show();
            }
        };

        mRef.addChildEventListener(mListener);

    }






























//    private void setInfoButton(View view) {
//        ImageView info = view.findViewById(R.id.info);
//        info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new UserRoleInfoBuilder(getActivity()).getBuilder().show();
//            }
//        });
//    }
//
//    private void setupUserRequestLV(View view) {
//        userRequests = (ListView) view.findViewById(R.id.approveUsersLV);
//
//        requests = new ArrayList<HashMap<String, String>>();
//
//        adapter = new UserRegRequestsAdapter(getActivity(), requests);
//
//        userRequests.setAdapter(adapter);
//
//        userRequests.setEmptyView(view.findViewById(R.id.empty_list));
//
//        //Get data out of user requests
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref = ref.child(getResources().getString(R.string.userRequestsTable));
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //help from: https://stackoverflow.com/questions/40366717/firebase-for-android-how-can-i-loop-through-a-child-for-each-child-x-do-y
//                //requests = new ArrayList<HashMap<String, String>>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                    UserWithUID user = snapshot.getValue(UserWithUID.class);
//                    HashMap<String, String> userRequest = new HashMap<>();
//                    userRequest.put(FIRST_NAME, user.getFirstName());
//                    userRequest.put(LAST_NAME, user.getLastName());
//                    userRequest.put(EMAIL, user.getEmail());
//                    userRequest.put(UID, user.getUid());
//                    requests.add(userRequest);
//                    adapter.notifyDataSetChanged();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("databaseError", "Error in ApproveFragment.java");
//            }
//        });
//
//    }
//
//
//    private void refresh() {
//        FragmentTransaction tr = getFragmentManager().beginTransaction();
//        tr.replace(R.id.frame_layout, new ApproveFragment());
//        tr.commit();
//    }
}
