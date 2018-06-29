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
package mhealth.mvax.settings;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mhealth.mvax.R;
import mhealth.mvax.auth.UserRequestsFragment;
import mhealth.mvax.auth.UsersFragment;
import mhealth.mvax.auth.modals.ChangeEmailModal;
import mhealth.mvax.auth.modals.ChangePasswordModal;
import mhealth.mvax.language.LanguageUtillity;

/**
 */
public class SettingsFragment extends Fragment {

    private LayoutInflater mInflater;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        View aboutModal = inflater.inflate(R.layout.modal_about, container, false);
        Button aboutButton = v.findViewById(R.id.about_button);
        AlertDialog ab = new AlertDialog.Builder(v.getContext())
                .setView(aboutModal)
                .create();
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ab.show();
            }
        });

        TextView dummyData = v.findViewById(R.id.admin_priv_dummy_data);
        dummyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDummyData();
            }
        });

        TextView signOut = v.findViewById(R.id.sign_out_button);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();

            }
        });

        return v;
    }

    private void generateDummyData() {
        //builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Generate Dummy Data");


        final View dialogView = mInflater.inflate(R.layout.modal_generate_dummy_data, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataGenerator generator = new DataGenerator(getContext());
                generator.generateData();
            }
        });

        builder.show();
    }
}