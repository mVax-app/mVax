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
import mhealth.mvax.R;
import mhealth.mvax.model.immunization.DueDate;
import mhealth.mvax.records.utilities.NullableDateFormat;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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

    private DueDate mDueDate;
    private DatabaseReference mDueDateRef;
    private ChildEventListener mDueDateListener;

    private String mDateSelectedString;
    private NullableDateFormat mDateFormat;

    private TextView mSelectedDayTV;



    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        mDateFormat = new NullableDateFormat(getContext().getString(R.string.date_format));
        initCalendar(mView);
        renderPatientLV();
        return mView;
    }

    public void initCalendar(View view) {
        calendar = (CalendarView) view.findViewById(R.id.calendar);

        setTitle(view, calendar);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendar, int year, int month, int day) {
                Long selectedDateLong = 0L;
                try {
                    String selectedDateString = "" + Integer.toString(month+1) + "/" + day + "/" + year;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date selectedDate = sdf.parse(selectedDateString);
                    selectedDateLong = selectedDate.getTime();
                } catch (ParseException e) {
                    System.out.println("PRINT: ParseException - unable to parse selected date");
                }

                mDateSelectedString = mDateFormat.getString(selectedDateLong);

                mSelectedDayTV.setText(getString(R.string.title_selected_day_patients) + " " + mDateSelectedString);
                System.out.println("PRINT: day change selected: "+year+"/"+month+"/"+day);
            }
        });
    }

    private void setTitle(View view, CalendarView calendar) {
        long rawDate = calendar.getDate();
        mDateSelectedString = mDateFormat.getString(rawDate);

        mSelectedDayTV = (TextView) view.findViewById(R.id.title_selected_day_patients);
        mSelectedDayTV.setText(getString(R.string.title_selected_day_patients) + " " + mDateSelectedString);

        TextView overdueTitle = (TextView) view.findViewById(R.id.title_overdue_patients);
        overdueTitle.setText(getString(R.string.title_overdue_patients));

    }

    private void renderPatientLV() {
        ListView patientLV = (ListView) mView.findViewById(R.id.selected_day_patients_LV);
    }

}