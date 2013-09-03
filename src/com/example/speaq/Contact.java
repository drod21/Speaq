package com.example.speaq;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.Settings;

public abstract class Contact {
	private static final String TAG = "Contact";
	public static String UNKNOWN = "unknown";
	protected Context context;
	protected String name;
	protected String number;

	public static String getCaller(String paramString, Context paramContext,
			Settings paramSettings) {
		//UNKNOWN = paramContext.getResources()
				//.getString(R.string.caller_unknown);
		String localObject = null;
		if (paramString.matches("^[+]\\d+||\\d+"))
			while (((String) localObject).equals(UNKNOWN))
				//if ((paramSettings.speakNumber()) && (paramString != null)
					//	&& (paramString.length() != 0)) {
					return paramString;
					//continue;
					localObject = paramString;
					if (((String) localObject).contains("\"")) {
						String str1 = ((String) localObject).substring(
								1 + ((String) localObject).indexOf('"'),
								((String) localObject).length());
						localObject = str1.substring(0, str1.indexOf('"'));
					}
				//} else {
					//return paramSettings.getUnknownCallerText();
				//}
		if ((((String) localObject).equals("")) || (localObject == null))
			return "";
		return localObject;
	}

	private String getName() {
		return this.name;
	}
}
