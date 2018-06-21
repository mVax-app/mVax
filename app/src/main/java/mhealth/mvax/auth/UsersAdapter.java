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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.auth.modals.ChangeRoleModal;
import mhealth.mvax.auth.modals.DeleteUserModal;
import mhealth.mvax.auth.modals.RoleInfoModal;
import mhealth.mvax.model.user.User;

/**
 * @author Matthew Tribby, Robert Steilberg
 * <p>
 * Adapter for rendering current users for administrator management
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    final private List<User> mUsers;

    UsersAdapter() {
        mUsers = new ArrayList<>();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, email, role;
        final ImageView infoButton;
        final Button changeRoleButton, deleteButton;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.user_name);
            email = view.findViewById(R.id.user_email);
            role = view.findViewById(R.id.user_role);
            infoButton = view.findViewById(R.id.info_button);
            changeRoleButton = view.findViewById(R.id.change_role_button);
            deleteButton = view.findViewById(R.id.delete_button);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.name.setText(user.getDisplayName());
        holder.email.setText(user.getEmail());
        holder.role.setText(user.getRole().toString());
        holder.infoButton.setOnClickListener(v -> new RoleInfoModal(v).createAndShow());
        holder.changeRoleButton.setOnClickListener(v -> new ChangeRoleModal(v, user).createAndShow());
        holder.deleteButton.setOnClickListener(v -> new DeleteUserModal(v, user.getUID()).createAndShow());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    /**
     * Add a user to the RecyclerView
     *
     * @param newUser new user to be added to the
     *                adapter
     */
    public void addUser(User newUser) {
        mUsers.add(newUser);
        Collections.sort(mUsers);
        notifyDataSetChanged();
    }

    /**
     * Update an existing user; adds the user to
     * the RecyclerView if no existing request is
     * found
     *
     * @param newUser updated user
     */
    public void updateUser(User newUser) {
        removeUser(newUser);
        addUser(newUser);
        Collections.sort(mUsers);
        notifyDataSetChanged();
    }

    /**
     * Remove a user request from the RecyclerView; nothing
     * happens if the request isn't found
     *
     * @param user existing user to be removed from the
     *             adapter
     */
    public void removeUser(User user) {
        mUsers.removeIf(ur -> ur.getUID().equals(user.getUID()));
        Collections.sort(mUsers);
        notifyDataSetChanged();
    }

}
