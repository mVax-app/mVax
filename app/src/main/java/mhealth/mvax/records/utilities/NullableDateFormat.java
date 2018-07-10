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

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Locale;

import mhealth.mvax.R;

/**
 * @author Robert Steilberg
 * <p>
 * Extension of SimpleDateFormat that handles formatting null dates and
 * initializes with default locale
 */
public class NullableDateFormat {

    /**
     * Converts a date into a string using a specified date pattern
     * @param pattern string representing the desired date pattern according
     *                to SimpleDateFormat conventions
     * @param date long representing the date (millis since Unix epoch)
     * @return string representation of the date
     */
    public static String getString(String pattern, Long date) {
        if (date != null) {
            final SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
            return formatter.format(date);
        } else {
            return "";
        }
    }

    /**
     * Converts a date into a string using the date pattern according to the
     * current language
     * @param date long representing the date (millis since Unix epoch)
     * @return string representation of the date
     */
    public static String getString(Context context, Long date) {
        final String pattern = context.getString(R.string.date_format);
        return getString(pattern, date);
    }

}
