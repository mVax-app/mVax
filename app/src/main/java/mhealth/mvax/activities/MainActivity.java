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
package mhealth.mvax.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;

import mhealth.mvax.R;
import mhealth.mvax.alerts.AlertsFragment;
import mhealth.mvax.dashboard.DashboardFragment;
import mhealth.mvax.queries.QueryFragment;
import mhealth.mvax.records.search.SearchFragment;
import mhealth.mvax.settings.SettingsFragment;

/**
 * @author Robert Steilberg, Matthew Tribby
 * <p>
 * Main activity that initializes the bottom navigation bar, which is used to
 * navigate throughout the app. Handles all of the main fragments
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initNavBar();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, SearchFragment.newInstance());
            transaction.commit();
        }
    }

    private void initNavBar() {
        BottomNavigationView navBar = findViewById(R.id.navigation_bar);
        navBar.setOnNavigationItemSelectedListener
                (icon -> {
                    Fragment selectedFragment;
                    switch (icon.getItemId()) {
                        case R.id.nav_patients:
                            selectedFragment = SearchFragment.newInstance();
                            break;
                        case R.id.nav_overdue:
                            selectedFragment = AlertsFragment.newInstance();
                            break;
                        case R.id.nav_data:
                            selectedFragment = DashboardFragment.newInstance();
                            break;
                        case R.id.nav_forms:
                            selectedFragment = QueryFragment.newInstance();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = SettingsFragment.newInstance();
                            break;
                        default:
                            selectedFragment = null; // this should never happen
                            break;
                    }
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, selectedFragment);
                    transaction.commit();
                    return true;
                });
    }

}
