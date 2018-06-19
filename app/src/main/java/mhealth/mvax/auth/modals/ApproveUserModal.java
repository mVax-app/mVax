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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.FirebaseUtilities;
import mhealth.mvax.auth.utilities.Mailer;
import mhealth.mvax.model.user.User;
import mhealth.mvax.model.user.UserRole;
import mhealth.mvax.utilities.StringFetcher;
import mhealth.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for approving a new mVax user account
 */
public class ApproveUserModal extends CustomModal {

    private User mRequest;

    public ApproveUserModal(View view, User request, UserRole role) {
        super(view);
        mRequest = request;
        mRequest.setRole(role); // do this here so modal can't be called without a role
    }

    @Override
    AlertDialog createDialog() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.approve_user_modal_title))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_approve_user, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(getString(R.string.confirm), null)
                .setNegativeButton(getString(R.string.cancel), null)
                .create();

        mBuilder.setOnShowListener(dialogInterface -> {
            mSpinner = mBuilder.findViewById(R.id.spinner);

            final TextView subtitle = mBuilder.findViewById(R.id.dob);
            final String text = String.format(getString(R.string.approve_user_subtitle),
                    mRequest.getDisplayName(),
                    mRequest.getRole().toString());
            subtitle.setText(text);
            mViews.add(subtitle);

            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE));

            final Button positiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> activateUser());
            mViews.add(positiveButton);
        });
        return mBuilder;
    }

    private void activateUser() {
        showSpinner();
        FirebaseUtilities.activateUser(mRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateUserTable();
            } else {
                Toast.makeText(getActivity(), R.string.approve_user_fail, Toast.LENGTH_LONG).show();
                hideSpinner();
            }
        });
    }

    private void updateUserTable() {
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.userTable))
                .child(mRequest.getUID());

        usersRef.setValue(mRequest).addOnCompleteListener(userTableAdd -> {
            if (userTableAdd.isSuccessful()) {
                updateRequestsTable();
            } else {
                Toast.makeText(getActivity(), R.string.approve_user_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateRequestsTable() {
        final DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.userRequestsTable))
                .child(mRequest.getUID());

        requestsRef.setValue(null).addOnCompleteListener(userRequestDelete -> {
            if (userRequestDelete.isSuccessful()) {
                // user activation workflow completed
                hideSpinner();
                sendWelcomeEmail();
                mBuilder.dismiss();
                Toast.makeText(getActivity(), R.string.approve_user_success, Toast.LENGTH_LONG).show();
            } else {
                hideSpinner();
                FirebaseUtilities.disableUser(mRequest); // try to undo activation
                Toast.makeText(getActivity(), R.string.approve_user_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendWelcomeEmail() {
        final String subject = getString(R.string.welcome_email_subject);
        final String body = String.format(StringFetcher.fetchString(R.string.welcome_email_body),
                mRequest.getDisplayName(),
                mRequest.getRole().toString());
        new Mailer(getContext())
                .withMailTo(mRequest.getEmail())
                .withSubject(subject)
                .withBody(body)
                .withProcessVisibility(false)
                .send();
    }

}
