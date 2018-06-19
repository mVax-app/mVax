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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.FirebaseUtilities;
import mhealth.mvax.auth.utilities.Mailer;
import mhealth.mvax.model.user.User;
import mhealth.mvax.utilities.StringFetcher;
import mhealth.mvax.utilities.modals.CustomModal;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for denying an mVax user account request
 */
public class DenyUserModal extends CustomModal {

    private User mRequest;

    public DenyUserModal(View view, User request) {
        super(view);
        mRequest = request;
    }

    @Override
    public AlertDialog create() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.deny_user_modal_title))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_deny_user, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(getString(R.string.confirm), null)
                .setNegativeButton(getString(R.string.cancel), null)
                .create();

        mBuilder.setOnShowListener(dialogInterface -> {
            mSpinner = mBuilder.findViewById(R.id.spinner);

            mViews.add(mBuilder.findViewById(R.id.dob));
            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE));

            final Button positiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> deleteUser());
            mViews.add(positiveButton);
        });
        return mBuilder;
    }

    private void deleteUser() {
        showSpinner();
        FirebaseUtilities.deleteUser(mRequest.getUID()).addOnCompleteListener(userDelete -> {
            if (userDelete.isSuccessful()) {
                deleteUserRequest();
            } else {
                hideSpinner();
                Toast.makeText(getActivity(), R.string.deny_user_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteUserRequest() {
        final DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.userRequestsTable))
                .child(mRequest.getUID());

        requestsRef.setValue(null).addOnCompleteListener(userRequestDelete -> {
            if (userRequestDelete.isSuccessful()) {
                // user request denial workflow completed
                hideSpinner();
                sendDenialEmail();
                mBuilder.dismiss();
                Toast.makeText(getActivity(), R.string.deny_user_success, Toast.LENGTH_LONG).show();
            } else {
                hideSpinner();
                Toast.makeText(getActivity(), R.string.deny_user_incomplete, Toast.LENGTH_LONG).show();
                // TODO implement better error handling in this case
            }
        });
    }

    private void sendDenialEmail() {
        final String subject = getString(R.string.deny_email_subject);
        final String body = String.format(StringFetcher.fetchString(R.string.deny_email_body),
                mRequest.getDisplayName());
        new Mailer(getContext())
                .withMailTo(mRequest.getEmail())
                .withSubject(subject)
                .withBody(body)
                .withProcessVisibility(false)
                .send();
    }

}
