package mhealth.mvax.language;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import java.util.Locale;

/**
 * @author Matthew Tribby
 *         <p>
 *         Utility function to change locale
 */



public class LanguageUtillity {

    public static void changeLangauge(Resources resources, String langCode){

        Log.d("New Language Code", langCode);

        Locale locale = new Locale(langCode);

        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        Locale.setDefault(locale);

        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}

