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

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Robert Steilberg
 * <p>
 * Extension of SimpleDateFormat that handles formatting
 * null dates and initializes with default locale
 */

public class NullableDateFormat extends SimpleDateFormat {

    public NullableDateFormat(String pattern) {
        super(pattern, Locale.getDefault());
    }

    public String getString(Long date) {
        if (date != null) {
            return format(date);
        } else {
            return "";
        }
    }

}
