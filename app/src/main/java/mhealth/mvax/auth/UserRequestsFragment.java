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
import android.support.annotation.NonNull;
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
 * @author Matthew Tribby, Robert Steilberg
 * <p>
 * Fragment for approving or denying requests for new mVax accounts
 */
public class UserRequestsFragment extends Fragment {

    private UserRequestsAdapter mAdapter;
    private DatabaseReference mRequestsRef;
    private ChildEventListener mListener;
    private TextView mNoRequestsTextView;

    public static UserRequestsFragment newInstance() {
        return new UserRequestsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_approve_users, container, false);

        initDatabase();

        mNoRequestsTextView = view.findViewById(R.id.no_user_requests);

        RecyclerView userRequestList = view.findViewById(R.id.user_request_list);
        userRequestList.setHasFixedSize(true);
        userRequestList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mAdapter = new UserRequestsAdapter();
        userRequestList.setAdapter(mAdapter);
        userRequestList.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void onDestroyView() {
        mRequestsRef.removeEventListener(mListener);
        super.onDestroyView();
    }

    private void initDatabase() {
        mRequestsRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.userRequestsTable));

        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                User request = dataSnapshot.getValue(User.class);
                mAdapter.addRequest(request);
                mNoRequestsTextView.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                User request = dataSnapshot.getValue(User.class);
                mAdapter.updateRequest(request);
                mNoRequestsTextView.setVisibility(View.GONE);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                User request = dataSnapshot.getValue(User.class);
                mAdapter.removeRequest(request);
                if (mAdapter.getItemCount() == 0) mNoRequestsTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // this should never happen
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.user_request_download_fail, Toast.LENGTH_SHORT).show();
            }
        };
        mRequestsRef.addChildEventListener(mListener);
    }

}
