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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.HashMap;

import mhealth.mvax.R;
import mhealth.mvax.auth.utilities.AuthInputValidator;
import mhealth.mvax.model.user.User;
import mhealth.mvax.records.utilities.StringFetcher;

/**
 * @author Robert Steilberg
 * <p>
 * Displays a modal for requesting a new mVax account
 */
public class DenyUserModal extends CustomModal {

    private AlertDialog mBuilder;
    private User mRequest;

    private TextView mMessageView;

    private ProgressBar mSpinner;
    private Button mPositiveButton;
    private Button mNegativeButton;

    public DenyUserModal(View view, User request) {
        super(view);
        mRequest = request;
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
            mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE);
            mNegativeButton = mBuilder.getButton(AlertDialog.BUTTON_NEGATIVE);
            mSpinner = mBuilder.findViewById(R.id.spinner);
            mMessageView = mBuilder.findViewById(R.id.message);

            mPositiveButton.setOnClickListener(view -> deleteUserRequest());

        });



        return mBuilder;
    }

    private void deleteUserRequest() {
        toggleSpinner(true);

        deleteUser(mRequest.getUID()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                final String requestsTable = StringFetcher.fetchString(R.string.userRequestsTable);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child(requestsTable);
                ref.child(mRequest.getDatabaseKey()).setValue(null).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        mBuilder.dismiss();
                        Toast.makeText(getActivity(), R.string.deny_user_success, Toast.LENGTH_LONG).show();
                    } else {
                        toggleSpinner(false);
                        Toast.makeText(getActivity(), R.string.deny_user_fail, Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                toggleSpinner(false);
                Toast.makeText(getActivity(), R.string.deny_user_fail, Toast.LENGTH_LONG).show();
            }

        });
    }

    private Task<String> deleteUser(String uid) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("uid", uid);
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        return functions.getHttpsCallable("deleteAccount").call(args).continueWith(task -> null);
    }


    private void toggleSpinner(boolean showSpinner) {
        if (showSpinner) {
            mMessageView.setVisibility(View.INVISIBLE);
            mPositiveButton.setVisibility(View.INVISIBLE);
            mNegativeButton.setVisibility(View.INVISIBLE);
            mSpinner.setVisibility(View.VISIBLE);
        } else {
            mMessageView.setVisibility(View.VISIBLE);
            mPositiveButton.setVisibility(View.VISIBLE);
            mNegativeButton.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.INVISIBLE);
        }
    }

}
