package com.drod.settings;

import com.drod.speaq.R;
import com.drod.speaq.R.xml;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.settings_menu);
	    }
}
