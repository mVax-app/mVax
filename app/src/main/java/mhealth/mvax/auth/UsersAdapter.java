package mhealth.mvax.auth;

import android.app.AlertDialog;
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
        final Button changeRoleButton, deleteButton;
        final ImageView infoButton;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.user_name);
            email = view.findViewById(R.id.user_email);
            role = view.findViewById(R.id.user_role);
            changeRoleButton = view.findViewById(R.id.change_role_button);
            deleteButton = view.findViewById(R.id.delete_button);
            infoButton = view.findViewById(R.id.info_button);
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

        holder.infoButton.setOnClickListener(v ->
                new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.role_info_title)
                        .setMessage(R.string.role_info_desc)
                        .setPositiveButton(R.string.ok, null)
                        .show());

        holder.role.setText(user.getRole().toString());

        holder.changeRoleButton.setOnClickListener(v -> {
            new ChangeRoleModal(v, user).show();
        });

        holder.deleteButton.setOnClickListener(v -> {
            new DeleteUserModal(v, user.getUID()).show();
        });
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
