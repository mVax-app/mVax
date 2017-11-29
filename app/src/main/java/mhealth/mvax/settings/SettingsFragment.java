package mhealth.mvax.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
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

import java.util.Locale;

import mhealth.mvax.R;
import mhealth.mvax.auth.ApproveUsersFragment;
import mhealth.mvax.auth.LoginActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SettingsFragment extends Fragment {
    private Switch languageSwitch;


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        Button changeEmail = (Button) v.findViewById(R.id.changeEmail);
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmail(view);
            }
        });

        Button resetPassword = (Button) v.findViewById(R.id.changePassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword(view);
            }
        });

        Button about = (Button) v.findViewById(R.id.aboutButton);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAboutModal();
            }
        });

//        Button signOut = (Button) v.findViewById(R.id.signOut);
//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//            }
//        });

        createApproveUsersButton(v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        languageSwitch = (Switch) view.findViewById(R.id.spanish);

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b && !getResources().getConfiguration().getLocales().toString().equals(getResources().getString(R.string.spanishLocaleCode))){
                    setLocale(getResources().getString(R.string.spanishCode));
                }
                else if(!b && !getResources().getConfiguration().getLocales().toString().equals(getResources().getString(R.string.usLocaleCode))) {
                    setLocale(getResources().getString(R.string.englishCode));
                }
            }
        });


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //Ensures that the button is correctly selected if in spanish
        languageSwitch.setChecked(getResources().getConfiguration().getLocales().toString().equals(getResources().getString(R.string.spanishLocaleCode)));
    }


    //Stack overflow: https://stackoverflow.com/questions/45584865/change-default-locale-language-android
    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Locale.setDefault(locale);
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        //Deprecated in API 25, minSDK for this project is 24
        res.updateConfiguration(config, dm);

        //TODO save instance state beforehand

        //getActivity().recreate();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

    }

    public void updateEmail(View v){

        //builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.modal_update_title));

        //https://stackoverflow.com/questions/18371883/how-to-create-modal-dialog-box-in-android
        LayoutInflater inflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.modal_update_email, null);
        builder.setView(dialogView);

        final TextView address = (TextView) dialogView.findViewById(R.id.emailReset);

        builder.setPositiveButton(getResources().getString(R.string.update_email), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (LoginActivity.isEmailValid(address.getText().toString())){
                    FirebaseAuth.getInstance().getCurrentUser().updateEmail(address.getText().toString());
                    dialog.dismiss();
                    Toast.makeText(getActivity(), R.string.update_email_success, Toast.LENGTH_LONG).show();
                 }
                 else{
                    Toast.makeText(getActivity(), R.string.error_invalid_email, Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.show();
    }

    public void resetPassword(View v){

            //builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.modal_reset_title));

            //https://stackoverflow.com/questions/18371883/how-to-create-modal-dialog-box-in-android
            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

            final View dialogView = inflater.inflate(R.layout.modal_reset_password_confirm, null);
            builder.setView(dialogView);

            builder.setPositiveButton(getResources().getString(R.string.reset_password_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    Toast.makeText(getActivity(), getResources().getString(R.string.reset_email_confirm) + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });

            builder.show();
    }

    public void signOut(){
        getActivity().finish();
        FirebaseAuth.getInstance().signOut();
    }

    private void createApproveUsersButton(View view){
        final Button approveUsers = (Button) view.findViewById(R.id.approveUsers);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(getResources().getString(R.string.userTable)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getResources().getString(R.string.role));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                if(dataSnapshot.getValue().equals(UserRole.ADMIN.toString())){
//                    approveUsers.setVisibility(View.VISIBLE);
//                }
//                else{
//                    approveUsers.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_user_role), Toast.LENGTH_LONG);
            }
        });


        approveUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               switchToApproveUsersFragment(view);
            }
        });
    }


    private void switchToApproveUsersFragment(View view){
        ApproveUsersFragment approveUsers = new ApproveUsersFragment();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(getId(), this).addToBackStack(null); // so that back button works
        transaction.replace(R.id.frame_layout, approveUsers);
        transaction.commit();
    }

    private void createAboutModal(){
        //builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.about));

        //https://stackoverflow.com/questions/18371883/how-to-create-modal-dialog-box-in-android
        LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.modal_about, null);
        builder.setView(dialogView);

        builder.show();
    }
}