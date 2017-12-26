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
package mhealth.mvax.records.details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author Robert Steilberg
 */

class DualTabPagerAdapter extends FragmentStatePagerAdapter {

    //================================================================================
    // Properties
    //================================================================================

    private static final int TAB_COUNT = 2;
    private Fragment mFirstTab;
    private Fragment mSecondTab;


    //================================================================================
    // Constructors
    //================================================================================

     DualTabPagerAdapter(FragmentManager fm, Fragment firstTab, Fragment secondTab) {
        super(fm);
        mFirstTab = firstTab;
        mSecondTab = secondTab;
    }


    //================================================================================
    // Override methods
    //================================================================================

    /**
     * Return the tab at the specified position
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
