package mhealth.mvax.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mhealth.mvax.R;
import mhealth.mvax.model.user.User;

/**
 * Matthew Tribby
 */
public class CurrentUsersFragment extends android.support.v4.app.Fragment {

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

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.userTable));
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    users.add(child.getValue(User.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
