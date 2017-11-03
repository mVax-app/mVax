package mhealth.mvax.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Robert Steilberg
 */

public class RecordDateFormat extends SimpleDateFormat {

    public RecordDateFormat(String pattern) {
        super(pattern, Locale.getDefault());
    }

    public String getString(Long date) {
        if (date != null) {
            return super.format(date);
        } else {
            return "";
        }
    }

}
