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
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.FirebaseUtilities;
import mhealth.mvax.model.user.User;
import mhealth.mvax.model.user.UserRole;
import mhealth.mvax.records.utilities.StringFetcher;

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
        final String userTable = getString(R.string.userTable);
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference()
                .child(userTable)
                .child(mRequest.getUID());

        usersRef.setValue(mRequest).addOnCompleteListener(userTableAdd -> {
            if (userTableAdd.isSuccessful()) {
                updateRequestsTable();
                sendWelcomeEmail(); // requests table update doesn't depend on email success
            } else {
                Toast.makeText(getActivity(), R.string.approve_user_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateRequestsTable() {
        final String requestsTable = getString(R.string.userRequestsTable);
        final DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference()
                .child(requestsTable)
                .child(mRequest.getUID());

        requestsRef.setValue(null).addOnCompleteListener(userRequestDelete -> {
            hideSpinner();
            if (userRequestDelete.isSuccessful()) {
                // user activation workflow completed
                Toast.makeText(getActivity(), R.string.approve_user_success, Toast.LENGTH_LONG).show();
                mBuilder.dismiss();
            } else {
                Toast.makeText(getActivity(), R.string.approve_user_incomplete, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendWelcomeEmail() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.configTable))
                .child(getString(R.string.mail_table));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            @SuppressWarnings(value = "unchecked")
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final HashMap<String, String> credentials = (HashMap<String, String>) dataSnapshot.getValue();
                assert credentials != null;
                final String email = credentials.get(getString(R.string.email_value));
                final String password = credentials.get(getString(R.string.password_value));

                final String subject = getString(R.string.welcome_email_subject);
                final String body = String.format(StringFetcher.fetchString(R.string.welcome_email_body),
                        mRequest.getDisplayName(),
                        mRequest.getRole().toString());

                BackgroundMail.newBuilder(getContext())
                        .withUsername(email)
                        .withPassword(password)
                        .withMailto(mRequest.getEmail())
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject(subject)
                        .withBody(body)
                        .withProcessVisibility(false)
                        .withOnFailCallback(() -> Toast.makeText(getActivity(), R.string.welcome_email_error, Toast.LENGTH_LONG).show())
                        .send();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.welcome_email_error, Toast.LENGTH_LONG).show();
            }
        });
    }

}
