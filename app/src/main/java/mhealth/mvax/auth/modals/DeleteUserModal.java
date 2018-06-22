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
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.FirebaseUtilities;
import mhealth.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal for deleting an existing user permanently
 */
public class DeleteUserModal extends CustomModal {

    private String mUID;

    public DeleteUserModal(View view, String UID) {
        super(view);
        mUID = UID;
    }

    @Override
    public void createAndShow() {
        mDialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.delete_user_modal_title)
                .setView(mInflater.inflate(R.layout.modal_delete_user, mParent, false))
                .setPositiveButton(R.string.confirm, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        mDialog.setOnShowListener(dialog -> {
            mSpinner = mDialog.findViewById(R.id.spinner);

            mViews.add(mDialog.findViewById(R.id.dob));
            mViews.add(mDialog.getButton(AlertDialog.BUTTON_NEGATIVE));

            final Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> attemptUserDelete());
            mViews.add(mDialog.getButton(AlertDialog.BUTTON_POSITIVE));
        });
        mDialog.show();
    }

    private void attemptUserDelete() {
        showSpinner();
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currUser != null && currUser.getUid().equals(mUID)) {
            // user is trying to delete itself
            hideSpinner();
            dismiss();
            Toast.makeText(mContext, R.string.delete_user_denied, Toast.LENGTH_LONG).show();
            return;
        }
        // this user can be deleted, begin user deletion workflow
        deleteUserFromAuthTable();
    }

    private void deleteUserFromAuthTable() {
        FirebaseUtilities.deleteUser(mUID).addOnCompleteListener((Task<String> userDelete) -> {
            if (userDelete.isSuccessful()) {
                deleteUserFromDatabase();
            } else {
                hideSpinner();
                Toast.makeText(mContext, R.string.delete_user_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteUserFromDatabase() {
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.user_table))
                .child(mUID);

        userRef.setValue(null).addOnCompleteListener(userTableDelete -> {
            if (userTableDelete.isSuccessful()) {
                // user deletion workflow completed
                hideSpinner();
                dismiss();
                Toast.makeText(mContext, R.string.delete_user_success, Toast.LENGTH_LONG).show();
            } else {
                hideSpinner();
                Toast.makeText(mContext, R.string.delete_user_incomplete, Toast.LENGTH_LONG).show();
                // TODO implement better error handling in this case
            }
        });
    }

}
