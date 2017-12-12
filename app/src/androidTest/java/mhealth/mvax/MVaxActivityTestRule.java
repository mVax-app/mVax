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
