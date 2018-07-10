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
package mhealth.mvax.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import mhealth.mvax.R;
import mhealth.mvax.alerts.AlertsFragment;
import mhealth.mvax.stats.StatsFragment;
import mhealth.mvax.reports.ReportsFragment;
import mhealth.mvax.records.search.SearchFragment;
import mhealth.mvax.settings.SettingsFragment;
import mhealth.mvax.utilities.LanguageChanger;

/**
 * @author Robert Steilberg, Matthew Tribby
 * <p>
 * Main activity that initializes the bottom navigation bar, which is used to
 * navigate throughout the app. Handles all of the main fragments
 */
public class MainActivity extends FragmentActivity {

    private int mCurrentTab;
    private String mCurrentLanguage;

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

    public void setLanguage(String langCode) {
        mCurrentLanguage = langCode;
    }

    private void initNavBar() {
        BottomNavigationView navBar = findViewById(R.id.navigation_bar);
        navBar.setSelectedItemId(mCurrentTab);

        navBar.setOnNavigationItemSelectedListener(icon -> {
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
                selectedFragment = AlertsFragment.newInstance();
                break;
            case R.id.nav_stats:
                selectedFragment = StatsFragment.newInstance();
                break;
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

}
