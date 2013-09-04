package com.drod.speaq;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Speaq extends Activity implements OnInitListener {
	private int MY_DATA_CHECK_CODE = 0;
	public static TextToSpeech textSpeech;
	public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private SpeaqSMS receiver;
	private ToggleButton mVoiceToggle;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mVoiceToggle = (ToggleButton) findViewById(R.id.voice_read_toggle);
		
		Intent findIntent = new Intent();
		findIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(findIntent, MY_DATA_CHECK_CODE);
	}

	public void onToggleClicked(View view) {
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();
		if (on) {
			registerSMS();
		} else {
			unregisterSMS();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				textSpeech = new TextToSpeech(this, this);
			} else {
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			Toast.makeText(Speaq.this,
					"Sucessfull intialization of Text-To-Speech engine",
					Toast.LENGTH_LONG).show();
		} else if (status == TextToSpeech.ERROR) {
			Toast.makeText(Speaq.this,
					"Unable to initialize Text-To-Speech engine",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDestroy() {
		if (textSpeech != null) {
			textSpeech.stop();
			textSpeech.shutdown();
		}
		unregisterSMS();
		super.onDestroy();
	}

	public void registerSMS() {
		receiver = new SpeaqSMS();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SMS_RECEIVED);
		registerReceiver(receiver, filter);
		Toast.makeText(getApplicationContext(), "registered for incoming sms",
				Toast.LENGTH_LONG).show();
	}

	public void unregisterSMS() {
		try {
			unregisterReceiver(receiver);
			Toast.makeText(getApplicationContext(),
					"unregistered for listening sms", Toast.LENGTH_LONG).show();
		} catch (Exception exe) {
			Toast.makeText(getApplicationContext(),
					"sms listener is already unregistered", Toast.LENGTH_LONG)
					.show();
		}
	}
}