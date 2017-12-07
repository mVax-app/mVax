package mhealth.mvax.auth;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import mhealth.mvax.R;
import mhealth.mvax.model.user.User;
import mhealth.mvax.model.user.UserRequest;

/**
 * This fragment represents the page where administrators can approve or deny user requests to
 * gain access to the application. The fragment is built around a simple 4 columned-list view with
 * a modal that pops up for approval/denial
 * @author Matthew Tribby
 * November, 2017
 */
public class ApproveUsersFragment extends android.support.v4.app.Fragment {
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String ROLE = "ROLE";
    public static final String UID = "UID";

    private ListView userRequests;
    private ArrayList<HashMap<String, String>> requests;
    private UserRegRequestsAdapter adapter;

    public ApproveUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_approve_users, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupUserRequestLV(view);

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupUserRequestLV(View view){
        userRequests = (ListView) view.findViewById(R.id.approveUsersLV);

        userRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                decisionModal(view, i);
            }
        });
        requests = new ArrayList<HashMap<String, String>>();

        adapter = new UserRegRequestsAdapter(getActivity(), requests);

        userRequests.setAdapter(adapter);

        //Get data out of user requests
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(getResources().getString(R.string.userRequestsTable));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //help from: https://stackoverflow.com/questions/40366717/firebase-for-android-how-can-i-loop-through-a-child-for-each-child-x-do-y
                //requests = new ArrayList<HashMap<String, String>>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    UserRequest user = snapshot.getValue(UserRequest.class);
                    HashMap<String, String> userRequest = new HashMap<>();
                    userRequest.put(FIRST_NAME, user.getFirstName());
                    userRequest.put(LAST_NAME, user.getLastName());
                    userRequest.put(EMAIL, user.getEmail());
                    userRequest.put(ROLE, user.getRole());
                    userRequest.put(UID, user.getUid());
                    requests.add(userRequest);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("databaseError", "Error in ApproveUsersFragment.java");
            }
        });

    }

    private void decisionModal(View view, int i){
        final int index = i;
        //builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.modal_user_decision_title));

        //https://stackoverflow.com/questions/18371883/how-to-create-modal-dialog-box-in-android
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.modal_user_request_decision, null);
        builder.setView(dialogView);

        final HashMap<String, String> data = (HashMap<String, String>) userRequests.getAdapter().getItem(i);

        TextView name = (TextView) dialogView.findViewById(R.id.name);
        name.setText(data.get(FIRST_NAME) + " " + data.get(LAST_NAME));

        TextView email = (TextView) dialogView.findViewById(R.id.email);
        email.setText(data.get(EMAIL));

        TextView role = (TextView) dialogView.findViewById(R.id.role);
        role.setText(data.get(ROLE));

        // This stack overflow post helped for querying https://stackoverflow.com/questions/39135619/java-firebase-search-by-child-value
        builder.setPositiveButton(getResources().getString(R.string.approve_user), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Delete Entry
                deleteUserRequest(data);

                //Make a user in the user table
                User newUser = new User(data.get(FIRST_NAME), data.get(LAST_NAME), data.get(EMAIL), data.get(ROLE));

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref = ref.child(getResources().getString(R.string.userTable)).child(data.get(UID));
                ref.setValue(newUser);

                requests.remove(index);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.deny_user), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUserRequest(data);

                requests.remove(index);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteUserRequest(final HashMap<String, String> data){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference account = ref.child(getResources().getString(R.string.userRequestsTable)).child(data.get(UID));
        account.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("databaseError", "Error in ApproveUsersFragment");
            }
        });
    }

    private void refresh(){
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        tr.replace(R.id.frame_layout, new ApproveUsersFragment());
        tr.commit();
    }
}
