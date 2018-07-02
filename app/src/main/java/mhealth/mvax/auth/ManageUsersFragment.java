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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mhealth.mvax.R;
import mhealth.mvax.model.user.User;

/**
 * @author Matthew Tribby, Robert Steilberg
 * <p>
 * Fragment for managing current mVax users
 */
public class ManageUsersFragment extends Fragment {

    private ManageUsersAdapter mAdapter;
    private DatabaseReference mUsersRef;
    private ChildEventListener mListener;
    private ProgressBar mSpinner;

    public static ManageUsersFragment newInstance() {
        return new ManageUsersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_manage_users, container, false);
        mSpinner = view.findViewById(R.id.spinner);

        initDatabase();

        RecyclerView usersList = view.findViewById(R.id.user_list);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mAdapter = new ManageUsersAdapter();
        usersList.setAdapter(mAdapter);
        usersList.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void onDestroyView() {
        mUsersRef.removeEventListener(mListener);
        super.onDestroyView();
    }

    private void initDatabase() {
        mUsersRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.user_table));

        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                mAdapter.addUser(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                mAdapter.updateUser(user);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mAdapter.removeUser(user);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // this should never happen
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.user_download_fail, Toast.LENGTH_SHORT).show();
            }
        };

        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mUsersRef.addChildEventListener(mListener);
    }

}
