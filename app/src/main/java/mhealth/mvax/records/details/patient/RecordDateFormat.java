package mhealth.mvax.records.details.patient;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Robert Steilberg
 *         <p>
 *         Implementation of SimpleDateFormat that handles formatting
 *         null dates
 */

public class RecordDateFormat extends SimpleDateFormat {

    public RecordDateFormat(String pattern) {
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
