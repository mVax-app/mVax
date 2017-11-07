package mhealth.mvax.records.details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author Robert Steilberg
 */

class DualTabPager extends FragmentStatePagerAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private final int TAB_COUNT = 2;

    private Fragment mFirstTab;

    private Fragment mSecondTab;


    //================================================================================
    // Constructors
    //================================================================================

    DualTabPager(FragmentManager fm, Fragment firstTab, Fragment secondTab) {
        super(fm);
        mFirstTab = firstTab;
        mSecondTab = secondTab;
    }


    //================================================================================
    // Override methods
    //================================================================================

    /**
     * Return the tab at the specified positon
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mFirstTab;
            case 1:
                return mSecondTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

}
