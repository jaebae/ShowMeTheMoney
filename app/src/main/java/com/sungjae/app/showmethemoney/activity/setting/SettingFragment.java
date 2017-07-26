package com.sungjae.app.showmethemoney.activity.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.annotation.Nullable;

import com.sungjae.com.app.showmethemoney.R;

import static com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants.syncSettingsToDataMap;


public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String SETTING_HEADER = "SETTING_";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        update(getPreferenceScreen());
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    private void update(Preference pref) {
        if (pref instanceof PreferenceGroup) {
            for (int i = 0; i < ((PreferenceGroup) pref).getPreferenceCount(); i++) {
                update(((PreferenceGroup) pref).getPreference(i));
            }
        } else if (pref instanceof EditTextPreference) {
            pref.setSummary(((EditTextPreference) pref).getText());
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference pref = findPreference(s);
        update(pref);
        syncSettingsToDataMap();
    }
}
