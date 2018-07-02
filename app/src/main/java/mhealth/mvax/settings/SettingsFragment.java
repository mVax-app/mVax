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
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import mhealth.mvax.R;
import mhealth.mvax.main.MainActivity;
import mhealth.mvax.auth.UserRequestsFragment;
import mhealth.mvax.auth.ManageUsersFragment;
import mhealth.mvax.auth.modals.ChangeEmailModal;
import mhealth.mvax.auth.modals.ChangePasswordModal;
import mhealth.mvax.model.user.User;
import mhealth.mvax.utilities.modals.LoadingModal;

/**
 * @author Robert Steilberg
 * <p>
 * Fragment for application settings
 */
public class SettingsFragment extends Fragment {

    private View mView;
    private LayoutInflater mInflater;
    private ViewGroup mParent;
    private LoadingModal mLoadingModal;

    private static boolean GENERATE_DATA = false;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mParent = container;
        mView = inflater.inflate(R.layout.fragment_settings, container, false);

        mLoadingModal = new LoadingModal(mView);
        mLoadingModal.createAndShow();
        initAboutButton(GENERATE_DATA);
        initLanguageSwitch();
        downloadCurrentUser();

        return mView;
    }

    private void initLanguageSwitch() {
        Switch langSwitch = mView.findViewById(R.id.app_preferences_language_switch);

        String currLang = Locale.getDefault().getLanguage();
        if (currLang.equals(getString(R.string.spanish_code))) langSwitch.setChecked(true);

        langSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MainActivity main = (MainActivity) getActivity();
            String langCode;
            if (isChecked) {
                langCode = getString(R.string.spanish_code);
            } else {
                langCode = getString(R.string.english_code);
            }
            Locale.setDefault(new Locale(langCode));
            main.setLanguage(langCode);
            getActivity().recreate();
        });
    }

    private void downloadCurrentUser() {
        final String userTable = getResources().getString(R.string.user_table);
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currUser != null) {
            String uid = currUser.getUid();
            Query userQuery = FirebaseDatabase.getInstance().getReference()
                    .child(userTable)
                    .orderByKey()
                    .equalTo(uid);
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                        User currUser = userSnap.getValue(User.class);
                        if (currUser != null) {
                            initUserButtons();
                            if (currUser.isAdmin()) initAdminButtons();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.user_download_fail), Toast.LENGTH_LONG).show();
                        }
                        mLoadingModal.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mLoadingModal.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.user_download_fail), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            getActivity().finish();
        }
    }

    private void initUserButtons() {
        initUpdateEmailButton();
        initUpdatePasswordButton();
    }

    private void initAdminButtons() {
        mView.findViewById(R.id.admin_priv).setVisibility(View.VISIBLE);
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

}