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
//package mhealth.mvax.auth;
//
//import android.app.Activity;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import mhealth.mvax.R;
//import mhealth.mvax.auth.utilities.UtilityEmailer;
//import mhealth.mvax.model.user.User;
//import mhealth.mvax.model.user.UserRole;
//
///**
// * This Adapter is a custom one that is for the ApproveFragment
// * There are three columsn, name, email, role
// *
// * Made with help from this tutorial: http://techlovejump.com/android-multicolumn-listview/
// *
// * @author Matthew Tribby
// * Created on 11/18/17
// */
//public class UserRegRequestsAdapter extends BaseAdapter{
//
//    private Activity activity;
//    private ArrayList<HashMap<String, String>> requests;
//    private TextView name;
//    private TextView email;
//    private Spinner roleSpinner;
//    private ImageView approve;
//    private ImageView deny;
//
//
//    public UserRegRequestsAdapter(Activity activity, ArrayList<HashMap<String, String>> list){
//        super();
//        this.activity=activity;
//        this.requests=list;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        LayoutInflater inflater=activity.getLayoutInflater();
//
//        if(view == null){
//
//            view=inflater.inflate(R.layout.user_request_row, null);
//
//            name = (TextView) view.findViewById(R.id.name);
//            email = (TextView) view.findViewById(R.id.edittext_email);
//            roleSpinner = (Spinner) view.findViewById(R.id.role_spinner);
//            approve = (ImageView) view.findViewById(R.id.approve_icon);
//            deny = (ImageView) view.findViewById(R.id.deny_icon);
//
//
//        }
//
//        HashMap<String, String> map=requests.get(i);
//        name.setText(map.get(ApproveFragment.FIRST_NAME) + " " + map.get(ApproveFragment.LAST_NAME));
//        email.setText(map.get(ApproveFragment.EMAIL));
//      //  role.setText(map.get(ApproveFragment.ROLE));
//        createSpinner();
//        setDecisionButtons(i);
//
//        return view;
//    }
//
//    private void createSpinner(){
//        List<String> userRoles = new ArrayList<>();
//        userRoles.add(activity.getResources().getString(R.string.select_user_role));
//        userRoles.addAll(UserRole.getRoles());
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
//                android.R.layout.simple_spinner_item, userRoles);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        roleSpinner.setAdapter(dataAdapter);
//    }
//
//    private void setDecisionButtons(final int index){
//        approve.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(roleSpinner.getSelectedItemPosition() != 0){
//                    addUserToDB(index);
//                }
//                else{
//                    Toast.makeText(activity, activity.getResources().getString(R.string.error_select_role), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        deny.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                removeUserRequestFromDB(index, false);
//            }
//        });
//    }
//
//    private void addUserToDB(int index){
//        Map<String, String> data = requests.get(index);
//        User newUser = new User(data.get(ApproveFragment.FIRST_NAME), data.get(ApproveFragment.LAST_NAME), data.get(ApproveFragment.EMAIL), roleSpinner.getSelectedItem().toString());
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref = ref.child(activity.getResources().getString(R.string.userTable)).child(data.get(ApproveFragment.UID));
//        ref.setValue(newUser);
//
//        Log.d("Remove", "remove request submitted: "+ index);
//        removeUserRequestFromDB(index, true);
//    }
//
//    private void sendAcceptanceEmail(String email){
//        UtilityEmailer.sendEmail(activity, email, activity.getResources().getString(R.string.acceptance_email_subject), activity.getResources().getString(R.string.acceptance_email_body));
//    }
//
//    private void sendRejectionEmail(String email){
//        UtilityEmailer.sendEmail(activity, email, activity.getResources().getString(R.string.rejection_email_subject), activity.getResources().getString(R.string.rejection_email_body));
//    }
//
//    private void removeUserRequestFromDB(int index, final boolean successful){
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference account = ref.child(activity.getResources().getString(R.string.userRequestsTable)).child(requests.get(index).get(ApproveFragment.UID));
//        final String userEmail = requests.get(index).get(ApproveFragment.EMAIL);
//        requests.remove(index);
//
//        account.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                dataSnapshot.getRef().removeValue();
//                if(successful){
//                    sendAcceptanceEmail(userEmail);
//                }
//                else{
//                    sendRejectionEmail(userEmail);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("databaseError", "Error in ApproveFragment");
//            }});
//
//        notifyDataSetChanged();
//    }
//
//
//    @Override
//    public int getCount() {
//        return requests.size();
//    }
//
//    @Override
//    public HashMap<String, String> getItem(int i) {
//        return requests.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    public void refresh(ArrayList<HashMap<String, String>> list){
//        this.requests = list;
//        notifyDataSetChanged();
//    }
//}
