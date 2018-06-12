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
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.FirebaseUtilities;

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
    AlertDialog createDialog() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_user_modal_title)
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_delete_user, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(R.string.delete_user_submit, null)
                .setNegativeButton(R.string.delete_user_cancel, null)
                .create();

        mBuilder.setOnShowListener(dialog -> {
            mSpinner = mBuilder.findViewById(R.id.spinner);

            mViews.add(mBuilder.findViewById(R.id.delete_user_modal_subtitle));
            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE));

            final Button positiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> attemptUserDelete());
            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_POSITIVE));
        });
        return mBuilder;
    }

    private void attemptUserDelete() {
        showSpinner();

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
            return;
        }
        if (currUser.getUid().equals(mUID)) {
            // user is trying to delete itself
            hideSpinner();
            mBuilder.dismiss();
            Toast.makeText(getActivity(), R.string.delete_user_denied, Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), R.string.delete_user_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteUserFromDatabase() {
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.userTable))
                .child(mUID);

        userRef.setValue(null).addOnCompleteListener(userTableDelete -> {
            if (userTableDelete.isSuccessful()) {
                // user deletion workflow completed
                hideSpinner();
                mBuilder.dismiss();
                Toast.makeText(getActivity(), R.string.delete_user_success, Toast.LENGTH_LONG).show();
            } else {
                hideSpinner();
                Toast.makeText(getActivity(), R.string.delete_user_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

}
