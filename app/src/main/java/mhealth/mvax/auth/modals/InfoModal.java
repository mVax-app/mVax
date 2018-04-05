///*
//Copyright (C) 2018 Duke University
//
//This file is part of mVax.
//
//mVax is free software: you can redistribute it and/or
//modify it under the terms of the GNU Affero General Public License
//as published by the Free Software Foundation, either version 3,
//or (at your option) any later version.
//
//mVax is distributed in the hope that it will be useful, but
//WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//GNU Affero General Public License for more details.
//
//You should have received a copy of the GNU General Public
//License along with mVax; see the file LICENSE. If not, see
//<http://www.gnu.org/licenses/>.
//*/
//package mhealth.mvax.auth.modals;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.res.Resources;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import mhealth.mvax.R;
//import mhealth.mvax.auth.utilities.AuthInputValidator;
//import mhealth.mvax.model.user.UserRequest;
//
///**
// * @author Robert Steilberg
// * <p>
// * Displays a modal for requesting a new mVax account
// */
//public class RegisterModal extends AlertDialog.Builder {
//
//    private Activity mActivity;
//    private View mView;
//    private Resources mResources;
//    private AlertDialog mBuilder;
//
//    private TextView mSubtitleView;
//    private TextView mNameView;
//    private TextView mEmailView;
//    private TextView mConfirmEmailView;
//    private ProgressBar mSpinner;
//    private Button mPositiveButton;
//    private Button mNegativeButton;
//
//    public RegisterModal(View view) {
//        super(view.getContext());
//        mActivity = (Activity) view.getContext();
//        mView = view;
//        mResources = view.getResources();
//    }
//
//    /**
//     * Build and display the modal to request a new user account
//     */
//    @Override
//    public AlertDialog show() {
//        initBuilder().show();
//        return null;
//    }
//
//    private AlertDialog initBuilder() {
//        mBuilder = new AlertDialog.Builder(mActivity)
//                .setTitle(mResources.getString(R.string.modal_info_title))
//                .setView(mActivity.getLayoutInflater().inflate(R.layout.modal_info, (ViewGroup) mView.getParent(), false))
//                .setPositiveButton(mResources.getString(R.string.ok), null)
//                .create();
//        return mBuilder;
//    }
//
//}
