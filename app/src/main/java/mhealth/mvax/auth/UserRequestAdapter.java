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

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.UtilityEmailer;
import mhealth.mvax.model.user.User;
import mhealth.mvax.model.user.UserRequest;
import mhealth.mvax.model.user.UserRole;

/**
 * This Adapter is a custom one that is for the ApproveFragment
 * There are three columsn, name, email, role
 * <p>
 * Made with help from this tutorial: http://techlovejump.com/android-multicolumn-listview/
 *
 * @author Matthew Tribby
 * Created on 11/18/17
 */
public class UserRequestAdapter extends RecyclerView.Adapter<UserRequestAdapter.ViewHolder> {

    private List<UserRequest> mUserRequests;

    UserRequestAdapter() {
        mUserRequests = new ArrayList<>();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public RadioGroup roles;
        public Button approve, deny;
        public ImageView info;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.user_name);
            email = view.findViewById(R.id.user_email);
            roles = view.findViewById(R.id.role_radio_group);
            approve = view.findViewById(R.id.approve_button);
            deny = view.findViewById(R.id.deny_button);
            info = view.findViewById(R.id.info_button);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user_request, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserRequest request = mUserRequests.get(position);
        holder.name.setText(request.getName());
        holder.email.setText(request.getEmail());


        holder.info.setOnClickListener(v ->
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.role_info_title)
                        .setMessage(R.string.role_info_desc)
                        .setPositiveButton(R.string.ok, null)
                        .show());

        holder.approve.setOnClickListener(v -> {

            int selectedId = holder.roles.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(v.getContext(), R.string.no_role_selected, Toast.LENGTH_SHORT).show();
            } else if (selectedId == R.id.admin_radio_button) {
                attemptApprove(request, UserRole.ADMIN);
            } else if (selectedId == R.id.reader_radio_button) {
                attemptApprove(request, UserRole.READER);
            }

        });

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptDeny(request);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUserRequests.size();
    }

    public void addRequest(UserRequest newRequest) {
        mUserRequests.add(newRequest);
        notifyDataSetChanged();
    }

    public void removeRequest(UserRequest request) {
        mUserRequests.removeIf(ur -> ur.getDatabaseKey().equals(request.getDatabaseKey()));
        notifyDataSetChanged();
    }

    private void attemptApprove(UserRequest request, UserRole role) {

    }

    private void attemptDeny(UserRequest request) {

    }

}
