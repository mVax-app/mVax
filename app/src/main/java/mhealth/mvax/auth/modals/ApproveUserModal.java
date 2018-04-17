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
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.FirebaseUtilities;
import mhealth.mvax.model.user.User;
import mhealth.mvax.model.user.UserRole;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for approving a new mVax user account
 */
public class ApproveUserModal extends CustomModal {

    private AlertDialog mBuilder;
    private User mRequest;

    private ProgressBar mSpinner;
    private List<View> mViews;

    public ApproveUserModal(View view, User request, UserRole role) {
        super(view);
        mRequest = request;
        mRequest.setRole(role); // do this here so modal can't be called without a role
        mViews = new ArrayList<>();
    }

    @Override
    AlertDialog createDialog() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.modal_approve_user_request_title))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_approve_user, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(getString(R.string.button_approve_user_confirm), null)
                .setNegativeButton(getString(R.string.button_approve_user_cancel), null)
                .create();

        mBuilder.setOnShowListener(dialogInterface -> {
            mSpinner = mBuilder.findViewById(R.id.spinner);

            final TextView confirmMessage = mBuilder.findViewById(R.id.message);
            final String userName = mRequest.getDisplayName();
            // interpolate user's name and role in display string
            final String text = String.format(getString(R.string.modal_approve_user_request_message),
                    userName,
                    mRequest.getRole().toString());
            confirmMessage.setText(text);
            mViews.add(confirmMessage);

            final Button positiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> activateUser());
            mViews.add(positiveButton);

            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE));
        });
        return mBuilder;
    }

    private void activateUser() {
        showSpinner(mSpinner, mViews);
        FirebaseUtilities.activateUser(mRequest).addOnCompleteListener(userActivate -> {
            if (userActivate.isSuccessful()) {
                updateUserTable();
            } else {
                Toast.makeText(getActivity(), R.string.approve_user_fail, Toast.LENGTH_LONG).show();
                hideSpinner(mSpinner, mViews);
            }
        });
    }

    private void updateUserTable() {
        final String userTable = getString(R.string.userTable);
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference()
                .child(userTable);

        usersRef.child(mRequest.getDatabaseKey()).setValue(mRequest).addOnCompleteListener(userTableAdd -> {
            if (userTableAdd.isSuccessful()) {
                updateRequestsTable();
            } else {
                Toast.makeText(getActivity(), R.string.approve_user_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateRequestsTable() {
        final String requestsTable = getString(R.string.userRequestsTable);
        final DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference()
                .child(requestsTable);

        requestsRef.child(mRequest.getDatabaseKey()).setValue(null).addOnCompleteListener(userRequestDelete -> {
            if (userRequestDelete.isSuccessful()) {
                // user activation workflow completed
                hideSpinner(mSpinner, mViews);
                Toast.makeText(getActivity(), R.string.approve_user_success, Toast.LENGTH_LONG).show();
                mBuilder.dismiss();
            } else {
                Toast.makeText(getActivity(), R.string.approve_user_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

}
