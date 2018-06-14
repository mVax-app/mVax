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

import android.content.Context;
import android.support.annotation.NonNull;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import mhealth.mvax.R;
import mhealth.mvax.records.utilities.StringFetcher;

/**
 * @author Robert Steilberg
 * <p>
 * Encapsulates mVax-specific functionality for sending background mail via the
 * BackgroundMail package
 * <p>
 * TODO: error handling
 */
public class Mailer {

    private Context mContext;
    private String mRecipient;
    private String mSubject;
    private String mBody;
    private Boolean mProcessVisibility;

    public Mailer(Context context) {
        mContext = context;
    }

    public Mailer withMailTo(String email) {
        mRecipient = email;
        return this;
    }

    public Mailer withSubject(String subject) {
        mSubject = subject;
        return this;
    }

    public Mailer withBody(String body) {
        mBody = body;
        return this;
    }

    public Mailer withProcessVisibility(Boolean processVisibility) {
        mProcessVisibility = processVisibility;
        return this;
    }

    /**
     * Sends an email using credentials stored in the config table of the database;
     * uses the specified recipient email, subject, body, and process visibility values
     */
    public void send() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(StringFetcher.fetchString(R.string.configTable))
                .child(StringFetcher.fetchString(R.string.mail_table));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, String>> t = new GenericTypeIndicator<Map<String, String>>() {
                };
                Map<String, String> credentials = dataSnapshot.getValue(t);
                if (credentials != null) {
                    final String email = credentials.get(StringFetcher.fetchString(R.string.email_value));
                    final String password = credentials.get(StringFetcher.fetchString(R.string.password_value));
                    BackgroundMail.newBuilder(mContext)
                            .withUsername(email)
                            .withPassword(password)
                            .withMailto(mRecipient)
                            .withType(BackgroundMail.TYPE_PLAIN)
                            .withSubject(mSubject)
                            .withBody(mBody)
                            .withProcessVisibility(mProcessVisibility)
                            .send();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // don't display error since nothing happens anyway and it doesn't
                // affect the UX
            }
        });
    }
}
