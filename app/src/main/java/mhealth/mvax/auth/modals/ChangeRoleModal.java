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
package mhealth.mvax.auth.modals;

import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.model.user.User;
import mhealth.mvax.model.user.UserRole;

/**
 * @author Robert Steilberg, Juliana Costa
 * <p>
 * Modal for changing an existing user's role
 */
public class ChangeRoleModal extends CustomModal {

    private User mUser;

    public ChangeRoleModal(View view, User user) {
        super(view);
        mUser = user;
    }

    @Override
    AlertDialog createDialog() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.change_role_modal_title)
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_change_role, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(R.string.change_role_modal_submit, null)
                .setNegativeButton(R.string.change_role_modal_cancel, null)
                .create();

        mBuilder.setOnShowListener(dialog -> {
            mSpinner = mBuilder.findViewById(R.id.role_spinner);

            mViews.add(mBuilder.findViewById(R.id.change_role_modal_subtitle));
            mViews.add(mBuilder.findViewById(R.id.role_radio_group));
            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE));

            RadioGroup roleRadioGroup = mBuilder.findViewById(R.id.role_radio_group);

            if (mUser.getRole() == UserRole.ADMIN) {
                roleRadioGroup.check(R.id.admin_radio_button);
            } else if (mUser.getRole() == UserRole.READER) {
                roleRadioGroup.check(R.id.reader_radio_button);
            }
            mViews.add(roleRadioGroup);

            final Button positiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> {

                final int chosenButtonId = roleRadioGroup.getCheckedRadioButtonId();
                final int chosenIdx = roleRadioGroup.indexOfChild(roleRadioGroup.findViewById(chosenButtonId));
                switch (chosenIdx) {
                    case 0: // admin (first item) chosen
                        changeRole(UserRole.ADMIN);
                        break;
                    case 1: // reader (second item) chosen
                        changeRole(UserRole.READER);
                        break;
                    default: // nothing chosen
                        break;
                }
            });
            mViews.add(positiveButton);
        });

        return mBuilder;
    }

    private void changeRole(UserRole newRole) {
        showSpinner();

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
            return;
        }
        if (currUser.getUid().equals(mUser.getUID())) {
            // trying to demote yourself
            hideSpinner();
            mBuilder.dismiss();
            Toast.makeText(getActivity(), R.string.change_role_denied, Toast.LENGTH_LONG).show();
            return;
        }

        if (mUser.getRole() == newRole) {
            // role was not changed
            hideSpinner();
            mBuilder.dismiss();
            Toast.makeText(getActivity(), R.string.change_role_success, Toast.LENGTH_LONG).show();
            return;
        }

        mUser.setRole(newRole);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.userTable))
                .child(mUser.getUID());

        reference.setValue(mUser).addOnCompleteListener(roleChange -> {
            hideSpinner();
            if (roleChange.isSuccessful()) {
                Toast.makeText(getActivity(), R.string.change_role_success, Toast.LENGTH_LONG).show();
                mBuilder.dismiss();
            } else {
                Toast.makeText(getActivity(), R.string.change_role_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

}
