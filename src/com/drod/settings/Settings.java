package com.drod.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity {
    public static final String KEY_PREF_SYNC_CONN = "pref_syncConnectionType";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
	}
}
