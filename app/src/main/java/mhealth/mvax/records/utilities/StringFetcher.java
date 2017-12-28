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
package mhealth.mvax.records.utilities;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * @author Robert Steilberg
 *         <p>
 *         Fetches Strings via their resource IDs; should be
 *         used when an Android Context object is not available
 *         in the current scope
 */
public class StringFetcher extends Application {

    // WeakReference prevents memory leaks
    private static WeakReference<Context> mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = new WeakReference<Context>(this);
    }

    /**
     * Fetch a String from resources
     *
     * @param resourceId the integer resource ID of the String
     * @return the String from resource file
     */
    public static String fetchString(int resourceId) {
        return mContext.get().getString(resourceId);
    }

}
