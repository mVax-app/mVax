package mhealth.mvax.dashboard;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mhealth.mvax.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormsFragment extends android.support.v4.app.Fragment {

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    public FormsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forms2, container, false);
    }

}
