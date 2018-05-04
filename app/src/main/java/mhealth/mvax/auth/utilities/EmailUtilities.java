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
package mhealth.mvax.auth.utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import mhealth.mvax.R;

/**
 * @author Matthew Tribby, Robert Steilberg
 * <p>
 * Utilities for sending emails to mVax users
 */

public class EmailUtilities {

    public static void sendEmail(Activity activity, String to, String subject, String body) {

        to = "rsteilberg@gmail.com";
        subject="This is a test!";
        body="Hello Robert!\nThis is a friendly test.\n\nBest,\nRob";

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
