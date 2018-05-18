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
package mhealth.mvax.calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mhealth.mvax.R;
import mhealth.mvax.model.immunization.DueDate;
import mhealth.mvax.records.utilities.NullableDateFormat;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * @author Steven Yang
 *        Displaying Overdue Patients
 */

public class CalendarFragment extends Fragment {

    //================================================================================
    // Properties
    //================================================================================

    CalendarView calendar;

    private View mView;

    private FirebaseAuth mAuth;

    private HashMap<String, DueDate> mDueDates;
    private DatabaseReference mDueDateRef;
    private ChildEventListener mDueDateListener;

    private SelectedDateAdapter mSelectedDateAdapter;
    private OverdueAdapter mOverdueAdapter;

    private String mSelectedDateString;
    private Long mSelectedDateLong;
    private NullableDateFormat mDateFormat;

    private TextView mSelectedDateTV;
    private ListView mSelectedDateLV;
    private ListView mOverdueLV;




    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        mDateFormat = new NullableDateFormat(getContext().getString(R.string.date_format));
        initDueDateListener();
        initCalendar(mView);
        renderLV();
        return mView;
    }

    private void initDueDateListener() {
        final String masterTable = getResources().getString(R.string.dataTable);
        final String dueDateTable = getResources().getString(R.string.dueDatesTable);
        mDueDateRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dueDateTable);

        mDueDateListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final DueDate addedDueDate = dataSnapshot.getValue(DueDate.class);
                if (addedDueDate != null) {
                    mDueDates.put(addedDueDate.getVaccineDatabaseKey(), addedDueDate);
                    //refresh();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevKey) {
                onChildAdded(dataSnapshot, prevKey);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final DueDate removedDueDate = dataSnapshot.getValue(DueDate.class);
                if (removedDueDate != null) {
                    mDueDates.remove(removedDueDate.getVaccineDatabaseKey());
                    //refresh();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), R.string.failure_due_dates_download, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initCalendar(View view) {
        calendar = (CalendarView) view.findViewById(R.id.calendar);

        setTitle(view, calendar);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendar, int year, int month, int day) {
                changeTitle(month, day, year);
                refreshSelectedDayPatients();
                refreshOverduePatients();
            }
        });
    }

    private void setTitle(View view, CalendarView calendar) {
        long rawDate = calendar.getDate();
        mSelectedDateString = mDateFormat.getString(rawDate);

        mSelectedDateTV = (TextView) view.findViewById(R.id.title_selected_day_patients);
        mSelectedDateTV.setText(getString(R.string.title_selected_day_patients) + " " + mSelectedDateString);

        TextView overdueTitle = (TextView) view.findViewById(R.id.title_overdue_patients);
        overdueTitle.setText(getString(R.string.title_overdue_patients));

    }

    private void changeTitle(int month, int day, int year) {
        try {
            String selectedDateString = "" + Integer.toString(month+1) + "/" + day + "/" + year;
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date selectedDate = sdf.parse(selectedDateString);
            mSelectedDateLong = selectedDate.getTime();
        } catch (ParseException e) {
            System.out.println("PRINT: ParseException - unable to parse selected date");
        }

        mSelectedDateString = mDateFormat.getString(mSelectedDateLong);

        mSelectedDateTV.setText(getString(R.string.title_selected_day_patients) + " " + mSelectedDateString);

    }

    private void refreshSelectedDayPatients() {
        // TODO
    }

    private void refreshOverduePatients() {

    }

    private void renderLV() {
        mSelectedDateLV = (ListView) mView.findViewById(R.id.selected_day_patients_LV);
        mOverdueLV = (ListView) mView.findViewById(R.id.overdue_patients_LV);

        mSelectedDateAdapter = new SelectedDateAdapter();
        mOverdueAdapter = new OverdueAdapter();

        mSelectedDateLV.setAdapter(mSelectedDateAdapter);
        mOverdueLV.setAdapter(mOverdueAdapter);

    }

//    public void refresh() {
//        mAdapter.refresh(mVaccines, mDates);
//    }

}