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
import com.google.firebase.functions.FirebaseFunctions;

import java.util.HashMap;

import mhealth.mvax.R;
import mhealth.mvax.model.user.User;
import mhealth.mvax.model.user.UserRole;

/**
 * @author Robert Steilberg
 * <p>
 * Modal for approving a new mVax user account
 */
public class ApproveUserModal extends CustomModal {

    private AlertDialog mBuilder;
    private User mRequest;
    private UserRole mRole;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private ProgressBar mSpinner;
    private TextView mConfirmMessage;

    public ApproveUserModal(View view, User request, UserRole role) {
        super(view);
        mRequest = request;
        mRequest.setRole(role);
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

            mConfirmMessage = mBuilder.findViewById(R.id.approve_user_message);
            String userName = mRequest.getDisplayName();
            String text = String.format(getString(R.string.modal_approve_user_request_message), userName, mRequest.getRole().toString());
            mConfirmMessage.setText(text);


            // get generated views

            mSpinner = mBuilder.findViewById(R.id.approve_spinner);
            mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            mNegativeButton = mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE);

            mPositiveButton.setOnClickListener(view -> approveRequest());

        });
        return mBuilder;
    }

    private Task<String> activateUser(String uid) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("uid", uid);
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        return functions.getHttpsCallable("activateAccount").call(args).continueWith(task -> (String) task.getResult().getData());
    }


    private void approveRequest() {
        toggleSpinner(true);
        activateUser(mRequest.getUID()).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                final String requestsTable = getString(R.string.userRequestsTable);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child(requestsTable);


                ref.child(mRequest.getDatabaseKey()).setValue(null).addOnCompleteListener(task1 -> {

                    final String userTable = getString(R.string.userTable);
                    DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference()
                            .child(userTable);
                    userTableRef.child(mRequest.getDatabaseKey()).setValue(mRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            toggleSpinner(false);
                            mBuilder.dismiss();
                            Toast.makeText(getActivity(), R.string.approve_user_success, Toast.LENGTH_LONG).show();

                        }
                    });

                });

            } else {
                Toast.makeText(getActivity(), R.string.approve_user_fail, Toast.LENGTH_LONG).show();
                toggleSpinner(false);
            }


        });
    }

    private void toggleSpinner(boolean showSpinner) {
        if (showSpinner) {
            mConfirmMessage.setVisibility(View.INVISIBLE);
            mPositiveButton.setVisibility(View.INVISIBLE);
            mNegativeButton.setVisibility(View.INVISIBLE);
            mSpinner.setVisibility(View.VISIBLE);
        } else {
            mConfirmMessage.setVisibility(View.VISIBLE);
            mPositiveButton.setVisibility(View.VISIBLE);
            mNegativeButton.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.INVISIBLE);
        }
    }


}
