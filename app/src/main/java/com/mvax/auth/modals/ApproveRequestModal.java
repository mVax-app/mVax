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
package com.mvax.auth.modals;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mvax.R;
import com.mvax.auth.utilities.FirebaseUtilities;
import com.mvax.auth.utilities.Mailer;
import com.mvax.model.user.User;
import com.mvax.model.user.UserRole;
import com.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for approving a new mVax user account
 */
public class ApproveRequestModal extends CustomModal {

    private User mRequest;

    public ApproveRequestModal(View view, User request, UserRole role) {
        super(view);
        mRequest = request;
        mRequest.setRole(role); // do this here so modal can't be called without a role
    }

    @Override
    public void createAndShow() {
        mDialog = new AlertDialog.Builder(mContext)
                .setView(mInflater.inflate(R.layout.modal_approve_request, mParent, false))
                .setPositiveButton(R.string.confirm, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        mDialog.setOnShowListener(dialogInterface -> {
            mSpinner = mDialog.findViewById(R.id.spinner);

            final TextView title = mDialog.findViewById(R.id.title);
            final String text = String.format(getString(R.string.approve_user_subtitle),
                    mRequest.getDisplayName(),
                    mRequest.getRole().toString());
            title.setText(text);
            mViews.add(title);

            final Button positiveButton = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> activateUser());
        });
        show();
    }

    private void activateUser() {
        showSpinner();
        FirebaseUtilities.activateUser(mRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateUserTable();
            } else {
                Toast.makeText(mContext, R.string.approve_user_fail, Toast.LENGTH_LONG).show();
                hideSpinner();
            }
        });
    }

    private void updateUserTable() {
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.user_table))
                .child(mRequest.getUID());

        usersRef.setValue(mRequest).addOnCompleteListener(userTableAdd -> {
            if (userTableAdd.isSuccessful()) {
                updateRequestsTable();
            } else {
                Toast.makeText(mContext, R.string.approve_user_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateRequestsTable() {
        final DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.user_requests_table))
                .child(mRequest.getUID());

        requestsRef.setValue(null).addOnCompleteListener(userRequestDelete -> {
            if (userRequestDelete.isSuccessful()) {
                // user activation workflow completed
                hideSpinner();
                sendWelcomeEmail();
                dismiss();
                Toast.makeText(mContext, R.string.approve_user_success, Toast.LENGTH_LONG).show();
            } else {
                hideSpinner();
                FirebaseUtilities.disableUser(mRequest); // try to undo activation
                Toast.makeText(mContext, R.string.approve_user_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendWelcomeEmail() {
        final String subject = getString(R.string.welcome_email_subject);
        final String body = String.format(getString(R.string.welcome_email_body),
                mRequest.getDisplayName(),
                mRequest.getRole().toString());
        new Mailer(mContext)
                .withMailTo(mRequest.getEmail())
                .withSubject(subject)
                .withBody(body)
                .withProcessVisibility(false)
                .send();
    }

}
