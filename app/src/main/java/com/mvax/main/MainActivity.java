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
package com.mvax.main;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimerTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mvax.R;
import com.mvax.alerts.AlertsFragment;
import com.mvax.model.immunization.DueDate;
import com.mvax.reports.ReportsFragment;
import com.mvax.records.search.SearchFragment;
import com.mvax.settings.SettingsFragment;
import com.mvax.utilities.LanguageChanger;

import org.joda.time.LocalDate;

/**
 * @author Robert Steilberg, Matthew Tribby
 * <p>
 * Main activity that initializes the bottom navigation bar, which is used to
 * navigate throughout the app. Handles all of the main fragments
 */
public class MainActivity extends FragmentActivity {

    private BottomNavigationView mNavBar;
    private int mCurrentTab;
    private Long mOverdueDate;
    private String mCurrentLanguage;
    private InactivityTimer mTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mCurrentLanguage = Locale.getDefault().getLanguage();

            setContentView(R.layout.activity_main);
            initNavBar();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, SearchFragment.newInstance());
            transaction.commit();

            checkForAlerts();
        } else {
            mCurrentLanguage = savedInstanceState.getString("langCode");
            mCurrentTab = savedInstanceState.getInt("mCurrentTab");
            LanguageChanger.changeLanguage(mCurrentLanguage, getResources());

            setContentView(R.layout.activity_main);
            initNavBar();
            Fragment chosenTab = chooseTab(mCurrentTab);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, chosenTab);
            transaction.commit();
        }
        setInactivityTimer();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LanguageChanger.changeLanguage(mCurrentLanguage, getResources());
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("langCode", mCurrentLanguage);
        outState.putInt("mCurrentTab", mCurrentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onUserInteraction() {
        if (mTimer.isRunning()) mTimer.cancel();
        setInactivityTimer(); // reset inactivity timeout
    }

    @Override
    public void onDestroy() {
        if (mTimer.isRunning()) mTimer.cancel();
        super.onDestroy();
    }

    private void setInactivityTimer() {
        // get stored timeout pref from device
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE);
        String timeoutKey = getString(R.string.timeout_pref_key);
        final int timeoutDefault = getResources().getInteger(R.integer.timeout_default);
        final int timeoutHours = prefs.getInt(timeoutKey, timeoutDefault);
        final long timeoutMillis = timeoutHours * 3600000L; // 3600000 millis = 1 hour

        mTimer = new InactivityTimer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        };
        mTimer.schedule(task, timeoutMillis);
    }

    public void setLanguage(String langCode) {
        mCurrentLanguage = langCode;
    }

    private void initNavBar() {
        mNavBar = findViewById(R.id.navigation_bar);
        mNavBar.setSelectedItemId(mCurrentTab);
        mNavBar.setOnNavigationItemSelectedListener(icon -> {
            Fragment chosenFragment = chooseTab(icon.getItemId());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, chosenFragment);
            transaction.commit();
            return true;
        });
    }

    private Fragment chooseTab(int tabId) {
        mCurrentTab = tabId;
        Fragment selectedFragment;
        switch (tabId) {
            case R.id.nav_patients:
                selectedFragment = SearchFragment.newInstance();
                break;
            case R.id.nav_alerts:
                // TODO clean up this logic
                if (mOverdueDate != null) {
                    selectedFragment = AlertsFragment.newInstance(mOverdueDate);
                    mOverdueDate = null;
                } else {
                    selectedFragment = AlertsFragment.newInstance();
                }
                break;
//            case R.id.nav_stats:
//                selectedFragment = StatsFragment.newInstance();
//                break;
            case R.id.nav_reports:
                selectedFragment = ReportsFragment.newInstance();
                break;
            case R.id.nav_settings:
                selectedFragment = SettingsFragment.newInstance();
                break;
            default:
                selectedFragment = null; // should never happen
                break;
        }
        return selectedFragment;
    }

    private void checkForAlerts() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH) + 1; // for next day
        final Date date = new LocalDate(year, month, day).toDate();

        final String masterTable = getString(R.string.data_table);
        final String dueDateTable = getString(R.string.due_date_table);
        final String dateField = getString(R.string.date);

        Query dueDateRef = FirebaseDatabase.getInstance().getReference()
                .child(masterTable)
                .child(dueDateTable)
                .orderByChild(dateField)
                .equalTo(date.getTime());

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
                if (!patientKeys.isEmpty())
                    notifyForOverduePatients(patientKeys.size(), date.getTime());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, R.string.alert_check_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void notifyForOverduePatients(int numOverduePatients, long date) {
        final Runnable goToAlerts = () -> {
            mOverdueDate = date;
            mNavBar.setSelectedItemId(R.id.nav_alerts); // triggers listener
        };
        new OverdueAlertModal(findViewById(R.id.frame), numOverduePatients, goToAlerts).createAndShow();
    }

}
