package mhealth.mvax.auth;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.user.User;
import mhealth.mvax.model.user.UserRole;

/**
 * Matthew Tribby
 */
public class CurrentUsersFragment extends Fragment {

    private ListView currentUsersLV;
    private List<User> users;
    private CurrentUsersAdapter adapter;

    public CurrentUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CurrentUsersFragment.
     */
    public static CurrentUsersFragment newInstance() {
        CurrentUsersFragment fragment = new CurrentUsersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_users, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setUpLV(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setUpLV(View view){
        currentUsersLV = (ListView) view.findViewById(R.id.currentUsersLV);

        users = new ArrayList<User>();
        adapter = new CurrentUsersAdapter(getActivity(), users);

        currentUsersLV.setAdapter(adapter);

        currentUsersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                createModalForEditingUser(i);
            }
        });


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.userTable));
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    User user = child.getValue(User.class);
//                    users.add(new UserWithUID(child.getKey(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole()));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void createModalForEditingUser(int row){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.modal_current_user_title));

        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.modal_current_user, null);
        builder.setView(dialogView);

        final User user = (User) currentUsersLV.getAdapter().getItem(row);

        setModalText(dialogView,  user);
        setInfoButton(dialogView);

//        final Spinner roleSpinner = createSpinner(dialogView, user.getRole());
//
//        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if(!((String) roleSpinner.getSelectedItem()).equals(user.getRole())){
//                    Log.d("CHANGE", "Change role to: " + roleSpinner.getSelectedItem());
//
//                    //Update Database
//                    User newUserAttributes = new User(user.getFirstName(), user.getLastName(), user.getEmail(), roleSpinner.getSelectedItem().toString());
//                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.userTable)).child(user.getUid());
//                    userRef.setValue(newUserAttributes);
//
//                    //Update Adapter
//                    user.setRole(roleSpinner.getSelectedItem().toString());
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });


        builder.show();

    }

    private void setModalText(View dialogView, User user){

        TextView name = (TextView) dialogView.findViewById(R.id.name);
        name.setText(user.getDisplayName());

        TextView email = (TextView) dialogView.findViewById(R.id.edittext_email);
        email.setText(user.getEmail());
    }

    private void setInfoButton(View dialogView){
        ImageView info = (ImageView) dialogView.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserRoleInfoBuilder(getActivity()).getBuilder().show();
            }
        });
    }


    private Spinner createSpinner(View dialogView, String spinnerRole){

        final Spinner roleSpinner = (Spinner) dialogView.findViewById(R.id.role_spinner);
        List<String> rolesList = new ArrayList<>();
//        rolesList.addAll(UserRole.getRoles());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, rolesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(dataAdapter);

        roleSpinner.setSelection(rolesList.indexOf(spinnerRole));

        return roleSpinner;
    }
}
