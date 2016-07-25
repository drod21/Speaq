package com.drod.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.drod.speaq.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_menu);
    }
}
