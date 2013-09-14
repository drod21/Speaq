package com.drod.speaq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.Settings;
import android.telephony.SmsMessage;
import android.util.Log;
import android.speech.tts.TextToSpeech;

public class SpeaqSMS extends BroadcastReceiver {

	static final String TAG = "ReadSMS";
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		if (intent.getAction().equals(Speaq.SMS_RECEIVED)) {
			Bundle bundle = intent.getExtras();
			Log.i(TAG, "SMS received");
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

	public void speakSMS(String smsBodyStr, String phoneNoStr) {
		if (phoneNoStr != null) {
			String displayName = getContactName(phoneNoStr);
			if (displayName == null) {
				displayName = phoneNoStr;
			}
			if (smsBodyStr != null && smsBodyStr.length() > 0) {
				Speaq.textSpeech.speak("Text From " + displayName, TextToSpeech.QUEUE_ADD,
						null);
			}
		}
	}

	public String getContactName(String findName) {
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(findName));
		Cursor cr = mContext.getContentResolver().query(uri,
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
