package mhealth.mvax.activities;

import android.app.Activity;
import android.support.design.widget.BottomNavigationView;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import mhealth.mvax.MVaxActivityTestRule;
import mhealth.mvax.R;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by AlisonHuang on 12/12/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public MVaxActivityTestRule<MainActivity> mainActivityMVaxActivityTestRule = new MVaxActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testUINotNull() {
        Activity activity = mainActivityMVaxActivityTestRule.getActivity();
        assertNotNull(activity.findViewById(R.id.frame_layout));
        assertNotNull(activity.findViewById(R.id.navigation_bar));
//        TextView helloView = (TextView) activity.findViewById(R.id.text_hello);
//        assertTrue(helloView.isShown());
//        assertEquals("Hello World!", helloView.getText());
//        assertEquals(InstrumentationRegistry.getTargetContext().getString(R.string.hello_world), helloView.getText());
//        assertNull(activity.findViewById(android.R.id.button1));
    }

    public void testNavBar() {
        Activity activity = mainActivityMVaxActivityTestRule.getActivity();
        BottomNavigationView navigationView = (BottomNavigationView)activity.findViewById(R.id.navigation_bar);
        assertNotNull(navigationView.getMenu());
        assertEquals(4, navigationView.getMenu().size());
    }
}