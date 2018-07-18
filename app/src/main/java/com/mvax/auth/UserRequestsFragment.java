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
package com.mvax.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.mvax.R;
import com.mvax.model.user.User;

/**
 * @author Matthew Tribby, Robert Steilberg
 * <p>
 * Fragment for approving or denying requests for new mVax accounts
 */
public class UserRequestsFragment extends Fragment {

    private UserRequestsAdapter mAdapter;
    private DatabaseReference mRequestsRef;
    private ChildEventListener mListener;
    private TextView mNoRequestsTextView;
    private ProgressBar mSpinner;

    public static UserRequestsFragment newInstance() {
        return new UserRequestsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_requests, container, false);
        mSpinner = view.findViewById(R.id.search_spinner);

        initDatabase();

        mNoRequestsTextView = view.findViewById(R.id.no_user_requests);

        RecyclerView userRequestList = view.findViewById(R.id.user_request_list);
        userRequestList.setHasFixedSize(true);
        userRequestList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mAdapter = new UserRequestsAdapter();
        userRequestList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        mRequestsRef.removeEventListener(mListener);
        super.onDestroyView();
    }

    private void initDatabase() {
        mRequestsRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.user_requests_table));

        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                mSpinner.setVisibility(View.GONE);
                mNoRequestsTextView.setVisibility(View.INVISIBLE);
                User request = dataSnapshot.getValue(User.class);
                mAdapter.addRequest(request);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                User request = dataSnapshot.getValue(User.class);
                mAdapter.updateRequest(request);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                User request = dataSnapshot.getValue(User.class);
                mAdapter.removeRequest(request);
                if (mAdapter.getItemCount() == 0) mNoRequestsTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // should never happen
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.user_request_download_fail, Toast.LENGTH_SHORT).show();
            }
        };

        mRequestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    mSpinner.setVisibility(View.GONE);
                    mNoRequestsTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRequestsRef.addChildEventListener(mListener);
    }

}
