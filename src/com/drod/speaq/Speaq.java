package com.drod.speaq;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.drod.settings.Settings;

public class Speaq extends Activity implements OnInitListener {
    private int MY_DATA_CHECK_CODE = 0;
    public static TextToSpeech textSpeech;
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private SpeaqSMS receiver;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent findIntent = new Intent();
        findIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(findIntent, MY_DATA_CHECK_CODE);

        // Load the default preference values
        PreferenceManager.setDefaultValues(this, R.xml.settings_menu, false);
    }

    public void onStart() {
        super.onStart();
    }

    public void onToggleClicked(View view) {
        boolean on = ((ToggleButton) view).isChecked();

        Intent startIntent = new Intent(Speaq.this, SpeaqService.class );
        Intent stopIntent = new Intent(Speaq.this, SpeaqService.class);

        if (on) {
            startService(startIntent);
        } else {
            stopService(stopIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (textSpeech == null) {
            stopService();
        }
        //stopService();
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

    private void startService() {
        receiver = new SpeaqSMS();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SMS_RECEIVED);
        registerReceiver(receiver, filter);
        Toast.makeText(getApplicationContext(), "Listening for incoming SMS",
                Toast.LENGTH_LONG).show();
        Log.i(SpeaqSMS.TAG, "SMS reading is activated");
    }

    private void stopService() {
        try {
            unregisterReceiver(receiver);
            Toast.makeText(getApplicationContext(),
                    "No longer listening for incoming SMS", Toast.LENGTH_LONG).show();
            Log.i(SpeaqSMS.TAG, "SMS reading is activated");
        } catch (Exception exe) {
            Toast.makeText(getApplicationContext(),
                    "sms listener is already unregistered", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
