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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.FirebaseUtilities;
import mhealth.mvax.model.user.User;
import mhealth.mvax.records.utilities.StringFetcher;

/**
 * @author Robert Steilberg
 * <p>
 * Modal and functionality for denying an mVax user account request
 */
public class DenyUserModal extends CustomModal {

    private AlertDialog mBuilder;
    private User mRequest;

    private ProgressBar mSpinner;
    private List<View> mViews;

    public DenyUserModal(View view, User request) {
        super(view);
        mRequest = request;
        mViews = new ArrayList<>();
    }

    @Override
    AlertDialog createDialog() {
        mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.modal_deny_user_request_title))
                .setView(getActivity().getLayoutInflater().inflate(R.layout.modal_deny_user, (ViewGroup) getView().getParent(), false))
                .setPositiveButton(getString(R.string.ok), null)
                .setNegativeButton(getString(R.string.button_reset_password_cancel), null)
                .create();

        mBuilder.setOnShowListener(dialogInterface -> {
            mSpinner = mBuilder.findViewById(R.id.spinner);

            final Button positiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> deleteUser());
            mViews.add(positiveButton);

            mViews.add(mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE));
            mViews.add(mBuilder.findViewById(R.id.message));
        });
        return mBuilder;
    }

    private void deleteUser() {
        showSpinner(mSpinner, mViews);

        FirebaseUtilities.deleteUser(mRequest.getUID()).addOnCompleteListener(userDelete -> {
            if (userDelete.isSuccessful()) {
                deleteUserRequest();
            } else {
                hideSpinner(mSpinner, mViews);
                Toast.makeText(getActivity(), R.string.deny_user_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteUserRequest() {
        final String requestsTable = StringFetcher.fetchString(R.string.userRequestsTable);
        final DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference()
                .child(requestsTable);

        requestsRef.child(mRequest.getDatabaseKey()).setValue(null).addOnCompleteListener(userRequestDelete -> {
            if (userRequestDelete.isSuccessful()) {
                // user request denial workflow completed
                hideSpinner(mSpinner, mViews);
                mBuilder.dismiss();
                Toast.makeText(getActivity(), R.string.deny_user_success, Toast.LENGTH_LONG).show();
            } else {
                hideSpinner(mSpinner, mViews);
                Toast.makeText(getActivity(), R.string.deny_user_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

}
