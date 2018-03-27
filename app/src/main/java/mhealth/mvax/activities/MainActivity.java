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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.R;
import mhealth.mvax.alerts.AlertsFragment;
import mhealth.mvax.dashboard.DashboardFragment;
import mhealth.mvax.dashboard.FormsFragment;
import mhealth.mvax.records.search.SearchFragment;
import mhealth.mvax.settings.SettingsFragment;

public class MainActivity extends TimeoutActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = auth.getCurrentUser();


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference database = db.getReference();

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation_bar);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
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
                                selectedFragment = FormsFragment.newInstance();
                                break;
                            case R.id.nav_settings:
                                selectedFragment = SettingsFragment.newInstance();
                                break;
                            
                        }

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, SearchFragment.newInstance());
        transaction.commit();

        Log.d("Language", "Main Activity: " + getResources().getConfiguration().locale.toString());

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * Handler updating a patient record; calls super to pass this handler to fragments
     * at handling result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onUserLeaveHint() {

    }

    @Override
    public void onPause() {
        super.onPause();
        FirebaseAuth.getInstance().signOut();
        this.finish();
    }

}
