package mhealth.mvax.auth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import mhealth.mvax.R;

/**
 * @author Matthew Tribby
 *         Goal of this class is to refactor out email functions into a single helper class
 *         Using JavaMail API
 */

public class UtilityEmailer {

    public static void sendEmail(Activity activity, String to, String subject, String body){

        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setType("message/rfc822");

        Uri emailUri = Uri.parse("mailto:" + to + "?subject=" + subject + "&body=" + body);
        email.setData(emailUri);

        try {
            activity.startActivityForResult(Intent.createChooser(email, activity.getResources().getString(R.string.send_email)), 2);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_LONG).show();
        }
    }

}
