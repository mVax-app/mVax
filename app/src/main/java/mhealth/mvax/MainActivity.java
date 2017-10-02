package mhealth.mvax;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        Log.d("locale", getResources().getConfiguration().getLocales().toString());

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_search:
                                selectedFragment = SearchFragment.newInstance();
                                break;
                            case R.id.navigation_alerts:
                                selectedFragment = AlertsFragment.newInstance();
                                break;
                            case R.id.navigation_forms:
                                selectedFragment = FormsFragment.newInstance();
                                break;
                            case R.id.navigation_settings:
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

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }


    //    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationSelectionListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment selected_fragment = null;
//            switch (item.getItemId()) {
//                case R.id.navigation_alerts:
//                    selected_fragment = AlertsFragment.newInstance();
//                    break;
//                case R.id.navigation_forms:
//                    selected_fragment = FormsFragment.newInstance();
//                    break;
//                case R.id.navigation_settings:
//                    selected_fragment = SettingsFragment.newInstance();
//                    break;
//                case R.id.navigation_search:
//                    selected_fragment = SearchFragment.newInstance();
//                    break;
//            }
//            fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame_layout)).commit();
//            fragmentTransaction.add(R.id.frame_layout, selected_fragment);
//            fragmentTransaction.commit();
//            return true;
//        }
//
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(onNavigationSelectionListener);
//        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.frame_layout, SearchFragment.newInstance());
//        fragmentTransaction.commit();
//    }

}
