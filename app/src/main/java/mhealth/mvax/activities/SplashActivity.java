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
package mhealth.mvax.activities;

import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import mhealth.mvax.auth.AuthActivity;

/**
 * @author Steven Yang
 * <p>
 * Renders splash screen upon intial app start
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MUST BE CALLED BEFORE ANY OTHER FIREBASE API CALLS
        // enables offline data persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
