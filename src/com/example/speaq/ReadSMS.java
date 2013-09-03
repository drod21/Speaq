package com.example.speaq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.Settings;
import android.telephony.SmsMessage;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.speech.tts.TextToSpeech;

public class ReadSMS extends BroadcastReceiver {

	static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "ReadSMS";
	public static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
	private SharedPreferences preferences;
	private Context mContext;
	public static boolean readTexts;
	public static String body;
	public static String findName;
	public static String caller;
	private Context mcontext;

	public ReadSMS() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		this.mcontext = context;
		if (intent.getAction().equals(SMS_RECEIVED)) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				readSMS(bundle);
			}
		}
	}

	public void readSMS(Bundle bundle) {
		SmsMessage[] msgs = null;
		try {
			Object[] pdus = (Object[]) bundle.get("pdus");
			if (pdus != null) {
				msgs = new SmsMessage[pdus.length];
				String smsBodyStr = null;
				String phoneNoStr = null;
				for (int k = 0; k < msgs.length; k++) {
					msgs[k] = SmsMessage.createFromPdu((byte[]) pdus[k]);
					smsBodyStr = msgs[k].getMessageBody().trim();
					phoneNoStr = msgs[k].getOriginatingAddress().trim();
					speakSMS(smsBodyStr, phoneNoStr);
				}
			}
		} catch (Exception exe) {
			exe.printStackTrace();
		}
	}

	public void speakSMS(String body, String caller) {
		if (caller != null) {
			String displayName = getContactName(caller);
			if (displayName == null) {
				displayName = caller;
			}
			if (body != null && body.length() > 0) {
				Speaq.textSpeech.speak("Text From " + displayName, TextToSpeech.QUEUE_ADD,
						null);
			}
		}
	}

	public String getContactName(String findName) {
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(findName));
		Cursor cr = mcontext.getContentResolver().query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		String displayName = null;
		if (cr.getCount() > 0) {
			cr.moveToFirst();
			displayName = cr.getString(cr
					.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}
		return displayName;
	}
}
