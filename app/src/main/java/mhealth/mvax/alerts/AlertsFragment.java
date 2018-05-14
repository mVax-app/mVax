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
package mhealth.mvax.alerts;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import mhealth.mvax.R;
import mhealth.mvax.model.immunization.Date;
import mhealth.mvax.model.immunization.DueDate;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * @author Steven Yang
 *        Displaying Overdue Patients
 */

public class AlertsFragment extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    CalendarView calendar;

    private View mView;

    private FirebaseAuth mAuth;

    private DueDate mDueDate;
    private DatabaseReference mDueDateRef;
    private ChildEventListener mDueDateListener;

    TextView selectedTV;


    public static AlertsFragment newInstance() {
        AlertsFragment fragment = new AlertsFragment();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        initializeCalendar(view);
        return view;
    }

    public void initializeCalendar(View view) {
        calendar = (CalendarView) view.findViewById(R.id.calendar);
        selectedTV = (TextView) view.findViewById(R.id.calendar_selected_text);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                selectedTV.setText(String.valueOf(month + 1) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
                System.out.println("PRINT: day change selected");
            }
        });
    }


}