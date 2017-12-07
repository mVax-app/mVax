package mhealth.mvax.auth;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mhealth.mvax.R;

/**
 * This Adapter is a custom one that is for the ApproveUsersFragment
 * There are three columsn, name, email, role
 *
 * Made with help from this tutorial: http://techlovejump.com/android-multicolumn-listview/
 *
 * @author Matthew Tribby
 * Created on 11/18/17
 */
public class UserRegRequestsAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<HashMap<String, String>> requests;
    private TextView name;
    private TextView email;
    private TextView role;


    public UserRegRequestsAdapter(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.requests=list;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=activity.getLayoutInflater();

        if(view == null){

            view=inflater.inflate(R.layout.user_request_row, null);

            name = (TextView) view.findViewById(R.id.name);
            email = (TextView) view.findViewById(R.id.email);
            role = (TextView) view.findViewById(R.id.role);


        }

        //TODO checking for .get()
        HashMap<String, String> map=requests.get(i);
        name.setText(map.get(ApproveUsersFragment.FIRST_NAME) + " " + map.get(ApproveUsersFragment.LAST_NAME));
        email.setText(map.get(ApproveUsersFragment.EMAIL));
        role.setText(map.get(ApproveUsersFragment.ROLE));

        return view;
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public HashMap<String, String> getItem(int i) {
        return requests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void refresh(ArrayList<HashMap<String, String>> list){
        this.requests = list;
        notifyDataSetChanged();
    }
}
