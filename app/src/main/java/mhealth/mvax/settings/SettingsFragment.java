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
package mhealth.mvax.settings;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import mhealth.mvax.R;
import mhealth.mvax.auth.UserRequestsFragment;
import mhealth.mvax.auth.ManageUsersFragment;
import mhealth.mvax.auth.modals.ChangeEmailModal;
import mhealth.mvax.auth.modals.ChangePasswordModal;

/**
 */
public class SettingsFragment extends Fragment {

    private View mView;
    private LayoutInflater mInflater;
    private ViewGroup mParent;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mParent = container;
        mView = inflater.inflate(R.layout.fragment_settings, container, false);

        downloadCurrentUser();
        initButtons();

        return mView;
    }

    private void downloadCurrentUser() {

    }

    private void initButtons() {
        initAboutButton(false);
        initUpdateEmailButton();
        initUpdatePasswordButton();
        initApproveUsersButton();
        initManageUsersButton();
        initSignOutButton();
    }

    private void initAboutButton(boolean generate) {
        if (generate) {
            new DataGenerator(getContext()).generateData();
        } else {
            Button aboutButton = mView.findViewById(R.id.about);
            View aboutModalView = mInflater.inflate(R.layout.modal_about, mParent, false);
            AlertDialog aboutModal = new AlertDialog.Builder(mView.getContext())
                    .setView(aboutModalView)
                    .create();
            aboutButton.setOnClickListener(view -> aboutModal.show());
        }
    }

    private void initUpdateEmailButton() {
        TextView updateEmail = mView.findViewById(R.id.user_settings_email);
        updateEmail.setOnClickListener(v -> new ChangeEmailModal(v).createAndShow());
    }

    private void initUpdatePasswordButton() {
        TextView updatePassword = mView.findViewById(R.id.user_settings_password);
        updatePassword.setOnClickListener(v -> new ChangePasswordModal(v).createAndShow());
    }

    private void initApproveUsersButton() {
        TextView approveUsers = mView.findViewById(R.id.admin_priv_approve_users);
        approveUsers.setOnClickListener(v -> {
            UserRequestsFragment userRequestsFragment = UserRequestsFragment.newInstance();
            FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, userRequestsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void initManageUsersButton() {
        TextView approveUsers = mView.findViewById(R.id.admin_priv_manage_users);
        approveUsers.setOnClickListener(v -> {


            ManageUsersFragment manageUsersFragment = ManageUsersFragment.newInstance();
            FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, manageUsersFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void initSignOutButton() {
        Button signOutButton = mView.findViewById(R.id.sign_out);
        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
        });
    }




    // TODO only admin sees admin privileges
}