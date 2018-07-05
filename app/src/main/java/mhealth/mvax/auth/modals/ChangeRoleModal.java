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
import mhealth.mvax.utilities.modals.CustomModal;

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
    public void createAndShow() {
        mDialog = new AlertDialog.Builder(mContext)
                .setView(mInflater.inflate(R.layout.modal_change_role, mParent, false))
                .setPositiveButton(R.string.submit, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        mDialog.setOnShowListener(dialog -> {
            mSpinner = mDialog.findViewById(R.id.spinner);

            mViews.add(mDialog.findViewById(R.id.change_role_modal_subtitle));

            final RadioGroup roleRadioGroup = mDialog.findViewById(R.id.role_radio_group);
            switch (mUser.getRole()) {
                case ADMIN:
                    roleRadioGroup.check(R.id.admin_radio_button);
                    break;
                case USER:
                    roleRadioGroup.check(R.id.reader_radio_button);
                    break;
                default:
                    break;
            }
            mViews.add(roleRadioGroup);

            final Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> {
                switch (roleRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.admin_radio_button:
                        changeRole(UserRole.ADMIN);
                        break;
                    case R.id.reader_radio_button:
                        changeRole(UserRole.USER);
                        break;
                    default:
                        break;
                }
            });
        });
        show();
    }

    private void changeRole(UserRole newRole) {
        showSpinner();

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currUser != null && currUser.getUid().equals(mUser.getUID())) {
            // trying to demote yourself
            hideSpinner();
            dismiss();
            Toast.makeText(mContext, R.string.change_role_denied, Toast.LENGTH_LONG).show();
            return;
        }
        if (newRole == mUser.getRole()) {
            // role was not changed so nothing needs to be done
            hideSpinner();
            dismiss();
            Toast.makeText(mContext, R.string.change_role_success, Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.user_table))
                .child(mUser.getUID())
                .child(getString(R.string.user_role));

        reference.setValue(newRole).addOnCompleteListener(roleChange -> {
            hideSpinner();
            if (roleChange.isSuccessful()) {
                Toast.makeText(mContext, R.string.change_role_success, Toast.LENGTH_LONG).show();
                dismiss();
            } else {
                Toast.makeText(mContext, R.string.change_role_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

}
