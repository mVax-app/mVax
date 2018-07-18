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
package com.mvax.alerts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.mvax.R;
import com.mvax.model.immunization.DueDate;
import com.mvax.model.record.Patient;

public class AlertsFragment extends Fragment {

    private View mView;
    private MaterialCalendarView mCalendar;

    private AlertsAdapter mAdapter;
    private List<Patient> mPatients;

    public AlertsFragment() {
        mPatients = new ArrayList<>();
    }

    public static AlertsFragment newInstance() {
        return new AlertsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_alerts, container, false);
        mCalendar = mView.findViewById(R.id.calendar);

        render();
        downloadAlertsForToday();

        mCalendar.setOnDateChangedListener((widget, d, selected) -> {
            final long date = new LocalDate(d.getYear(), d.getMonth() + 1, d.getDay()).toDate().getTime();
            downloadAlertsForDate(date);
        });

        return mView;
    }

    private void downloadAlertsForToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        final Date date = new LocalDate(year, month, day).toDate();
        mCalendar.setDateSelected(date, true);
        downloadAlertsForDate(date.getTime());
    }

    private void downloadAlertsForDate(Long date) {
        mAdapter.clearResults();
        mView.findViewById(R.id.no_alerts).setVisibility(View.INVISIBLE);
        mView.findViewById(R.id.search_spinner).setVisibility(View.VISIBLE);

        final String masterTable = getString(R.string.data_table);
        final String dueDateTable = getString(R.string.due_date_table);
        final String dateField = getString(R.string.date);

        Query dueDateRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dueDateTable)
                .orderByChild(dateField)
                .equalTo(date);

        dueDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashSet<String> patientKeys = new HashSet<>();
                for (DataSnapshot dueDateSnap : dataSnapshot.getChildren()) {
                    DueDate dueDate = dueDateSnap.getValue(DueDate.class);
                    if (dueDate != null) {
                        patientKeys.add(dueDate.getPatientDatabaseKey());
                    }
                }
                if (patientKeys.size() == 0) {
                    refresh(); // no overdue patients
                } else {
                    downloadPatients(patientKeys);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mView.getContext(), R.string.alert_download_fail, Toast.LENGTH_LONG).show();
                mView.findViewById(R.id.search_spinner).setVisibility(View.INVISIBLE);
            }
        });
    }

    private void downloadPatients(Set<String> patientKeys) {
        final String masterTable = getString(R.string.data_table);
        final String patientTable = getString(R.string.patient_table);

        for (String patientKey : patientKeys) {
            Query patientRef = FirebaseDatabase.getInstance().getReference()
                    .child(masterTable)
                    .child(patientTable)
                    .orderByKey()
                    .equalTo(patientKey);

            patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot patientSnap : dataSnapshot.getChildren()) {
                        Patient patient = patientSnap.getValue(Patient.class);
                        if (patient != null) mPatients.add(patient);
                        if (mPatients.size() == patientKeys.size()) refresh();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mView.getContext(), R.string.alert_download_incomplete, Toast.LENGTH_LONG).show();
                    mView.findViewById(R.id.search_spinner).setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void render() {
        mCalendar.setTileWidthDp(ViewGroup.LayoutParams.MATCH_PARENT / 7);
        mCalendar.setTileHeightDp((int) getResources().getDimension(R.dimen.alert_calendar_tile_height));

        mCalendar.setTitleFormatter(day -> { // set calendar month to current language
            Locale locale = Locale.getDefault();
            DateFormat dateFormat = new SimpleDateFormat("LLLL yyyy", locale);
            return dateFormat.format(day.getDate());
        });

        final RecyclerView alertResults = mView.findViewById(R.id.alert_results);
        alertResults.setHasFixedSize(true);
        alertResults.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AlertsAdapter(mPatients, getActivity());
        alertResults.setAdapter(mAdapter);
    }

    private void refresh() {
        mView.findViewById(R.id.search_spinner).setVisibility(View.INVISIBLE);
        if (mPatients.size() == 0) {
            mView.findViewById(R.id.no_alerts).setVisibility(View.VISIBLE);
        } else {
            mView.findViewById(R.id.no_alerts).setVisibility(View.INVISIBLE);
        }
        mAdapter.refresh(mPatients);
    }

}
