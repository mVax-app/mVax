package mhealth.mvax.auth;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import mhealth.mvax.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApproveUsersFragment extends android.support.v4.app.Fragment {
    public static final String FIRST_COLUMN = "FULL_NAME";
    public static final String SECOND_COLUMN = "EMAIL";
    public static final String THIRD_COLUMN = "ROLE";

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
        ListView userRequests = (ListView) view.findViewById(R.id.approveUsersLV);

        userRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                decisionModal(view);
            }
        });
        ArrayList<HashMap<String, String>> requests = new ArrayList<HashMap<String, String>>();

        //TODO remove dummy data
        HashMap<String, String> testRequest = new HashMap<>();
        testRequest.put(FIRST_COLUMN, "Matthew Tribby");
        testRequest.put(SECOND_COLUMN, "tribby.matt5@gmail.com");
        testRequest.put(THIRD_COLUMN, "ADMIN");

        requests.add(testRequest);

        UserRegRequestsAdapter adapter = new UserRegRequestsAdapter(getActivity(), requests);

        userRequests.setAdapter(adapter);
    }

    private void decisionModal(View view){
        //Insert the decision modal code here
    }
}
