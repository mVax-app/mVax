package mhealth.mvax;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Locale;


public class SettingsFragment extends Fragment {
    private Switch languageSwitch;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setLocale("es");
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        languageSwitch = (Switch) view.findViewById(R.id.spanish);

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b && !getResources().getConfiguration().getLocales().toString().equals("[es]")){
                    setLocale("es");
                    return;
                }
                else if(!b && !getResources().getConfiguration().getLocales().toString().equals("[en_US]")) {
                    setLocale("en");
                }
            }
        });

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        languageSwitch.setChecked(getResources().getConfiguration().getLocales().toString().equals("[es]"));

    }


    //Stack overflow: https://stackoverflow.com/questions/45584865/change-default-locale-language-android
    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Locale.setDefault(locale);
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        //Deprecated in API 25, minSDK for this project is 24
        res.updateConfiguration(config, dm);

        getActivity().recreate();

    }
}