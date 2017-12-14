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
package mhealth.mvax;

import android.app.Activity;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

/**
 * Created by AlisonHuang on 12/12/17.
 */

public class MVaxActivityTestRule<A extends Activity> extends android.support.test.rule.ActivityTestRule<A> {

    public MVaxActivityTestRule(Class<A> activityClass) {
        super(activityClass);
    }
    @Override
    protected Intent getActivityIntent() {
        Log.e("MVaxActivityTestRule", "Prepare the activity's intent");
        return super.getActivityIntent();
    }

    @Override
    protected void beforeActivityLaunched() {
        Log.e("MVaxActivityTestRule", "Execute before the activity is launched");
        super.beforeActivityLaunched();
    }

    @Override
    protected void afterActivityLaunched() {
        Log.e("MVaxActivityTestRule", "Execute after the activity has been launched");
        super.afterActivityLaunched();
    }

    @Override
    protected void afterActivityFinished() {
        Log.e("MVaxActivityTestRule", "Cleanup after it has finished");
        super.afterActivityFinished();
    }

    @Override
    public A launchActivity(Intent startIntent) {
        Log.e("MVaxActivityTestRule", "Launching the activity");
        return super.launchActivity(startIntent);
    }
}
