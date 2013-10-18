package com.drod.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {
    static final String CALLER_READ="caller_read";
    static final String BODY_READ="body_read";
    static final String BLUETOOTH_AUTO="bluetooth_auto";
    static final String HEADSET_AUTO="headset_auto";

    private static SharedPreferences mPrefs = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
	}

    public void setContext(Context context) {
        if (context == null && mPrefs != null) {
            mPrefs = null;
        } else if (context != null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static boolean getCallerRead() {
        return mPrefs.getBoolean(CALLER_READ, true);
    }

    public static boolean getBodyRead() {
        return mPrefs.getBoolean(BODY_READ, true);
    }

    public static boolean getBTAuto() {
        return mPrefs.getBoolean(BLUETOOTH_AUTO, true);
    }

    public static boolean getHeadSetAuto() {
        return mPrefs.getBoolean(HEADSET_AUTO, true);
    }

}
