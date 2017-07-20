package com.sungjae.app.showmethemoney.activity.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.sungjae.com.app.showmethemoney.R;

import static com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants.syncSettingsToDataMap;

public class SettingsActiviy extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String SETTING_HEADER = "SETTING_";

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        update(getPreferenceScreen());
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
        ListView v = getListView();
        Button btn = new Button(this);
        btn.setText("back");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        v.addFooterView(btn);

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
