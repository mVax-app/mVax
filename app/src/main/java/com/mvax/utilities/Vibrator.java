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
package com.mvax.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;

import com.mvax.R;

/**
 * @author Robert Steilberg
 * <p>
 * Triggers a device vibrate
 */
public class Vibrator {

    public static void vibrate(Activity activity) {
        final int duration = activity.getResources().getInteger(R.integer.vibrate_duration);
        android.os.Vibrator v = (android.os.Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(duration); // deprecated in API 26
            }
        }
    }

}
