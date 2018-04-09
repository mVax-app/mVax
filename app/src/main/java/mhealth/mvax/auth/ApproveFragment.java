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
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.model.user.User;

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
                User request = dataSnapshot.getValue(User.class);
                mAdapter.addRequest(request);
                mNoRequestText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // this should never happen
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User request = dataSnapshot.getValue(User.class);
                mAdapter.removeRequest(request);
                if (mAdapter.getItemCount() == 0) mNoRequestText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // this should never happen
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_user_request_download, Toast.LENGTH_SHORT).show();
            }
        };

        mRef.addChildEventListener(mListener);

    }
}
