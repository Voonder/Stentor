package fr.exia.stentor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import fr.exia.stentor.adapter.SettingsAdapter;
import fr.exia.stentor.model.ui.SettingsItem;
import fr.exia.stentor.speech.SpeechActivationService;
import fr.exia.stentor.speech.SpeechUtils;
import fr.exia.stentor.util.AppUtils;
import fr.exia.stentor.util.MyPrefs;

public class SettingsActivity extends AbstractActivity {

	MyPrefs prefs;
	MenuItem speechOn;
	MenuItem speechOff;
	boolean firstPass = true;
	private List<SettingsItem> items = new ArrayList<>();

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SpeechUtils.BACK)) {
				if (firstPass) {
					firstPass = false;
					speaker.speak(getString(R.string.tts_open_home));
					startActivity(new Intent(getApplicationContext(), HomeActivity.class));
					finish();
				}
			} else if (intent.getAction().equals(SpeechUtils.CLOSE_APP)) {
				if (firstPass) {
					firstPass = false;
					finish();
				}
			} else if (intent.getAction().equals(SpeechUtils.CLOSE_SERVICE)) {
				if (firstPass) {
					firstPass = false;
					startService(SpeechActivationService.makeServiceStopIntent(getApplicationContext()));
					speechOn.setVisible(true);
					speechOff.setVisible(false);
					speaker.speak(getString(R.string.tts_stop_service));
				}
			} else if (intent.getAction().equals(SpeechUtils.OPEN_HOME)) {
				if (firstPass) {
					firstPass = false;
					speaker.speak(getString(R.string.tts_open_home));
					startActivity(new Intent(getApplicationContext(), HomeActivity.class));
					finish();
				}
			} else if (intent.getAction().equals(SpeechUtils.OPEN_MAINTENANCE)) {
				if (firstPass) {
					firstPass = false;
					speaker.speak(getString(R.string.tts_open_maintenance));
					startActivity(new Intent(getApplicationContext(), MaintenanceActivity.class));
					finish();
				}
			} else if (intent.getAction().equals(SpeechUtils.OPEN_SETTINGS)) {
				if (firstPass) {
					firstPass = false;
					speaker.speak(getString(R.string.tts_open_settings));
					startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
					finish();
				}
			} else if (intent.getAction().equals(SpeechUtils.OPEN_HELP_FEEDBACK)) {
				if (firstPass) {
					firstPass = false;
					speaker.speak(getString(R.string.tts_open_help_feedback));
					startActivity(new Intent(getApplicationContext(), HelpFeedbackActivity.class));
					finish();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		prefs = new MyPrefs(this, MyPrefs.SETTINGS_PREFS);

		String app = prefs.getLanguageApp();
		if (app.equals("")) {
			app = "EN";
		}

		String voice = prefs.getLanguageApp();
		if (voice.equals("")) {
			voice = "EN";
		}

		items.add(new SettingsItem(SettingsItem.TypeOfLanguage.APP,
				getString(R.string.settings_language_app),
				getString(R.string.settings_language_app_info),
				app));

		items.add(new SettingsItem(SettingsItem.TypeOfLanguage.VOICE,
				getString(R.string.settings_language_voice),
				getString(R.string.settings_language_voice_info),
				voice));

		setContentView(R.layout.activity_settings);

		SettingsAdapter settingsAdapter = new SettingsAdapter(this, items);

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

		RecyclerView recyclerSettings = (RecyclerView) findViewById(R.id.settings_list);
		recyclerSettings.setHasFixedSize(true);
		recyclerSettings.setAdapter(settingsAdapter);
		recyclerSettings.setLayoutManager(layoutManager);

		checkTTS();
	}

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SpeechUtils.CLOSE_APP);
		intentFilter.addAction(SpeechUtils.CLOSE_SERVICE);
		intentFilter.addAction(SpeechUtils.BACK);
		intentFilter.addAction(SpeechUtils.OPEN_HOME);
		intentFilter.addAction(SpeechUtils.OPEN_MAINTENANCE);
		intentFilter.addAction(SpeechUtils.OPEN_SETTINGS);
		intentFilter.addAction(SpeechUtils.OPEN_HELP_FEEDBACK);
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
		speaker.destroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_settings, menu);

		speechOn = menu.findItem(R.id.action_speech_on);
		speechOff = menu.findItem(R.id.action_speech_off);

		if (AppUtils.isMyServiceRunning(SpeechActivationService.class, this)) {
			speechOn.setVisible(false);
			speechOff.setVisible(true);
		} else {
			speechOn.setVisible(true);
			speechOff.setVisible(false);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case R.id.action_speech_on:
				startService(SpeechActivationService.makeStartServiceIntent(this, "hello"));
				speechOn.setVisible(false);
				speechOff.setVisible(true);
				break;
			case R.id.action_speech_off:
				startService(SpeechActivationService.makeServiceStopIntent(this));
				speechOn.setVisible(true);
				speechOff.setVisible(false);
				break;
			case R.id.action_home:
				startActivity(new Intent(this, HomeActivity.class));
				finish();
				break;
			case R.id.action_maintenance:
				startActivity(new Intent(this, MaintenanceActivity.class));
				finish();
				break;
			case R.id.action_help_feedback:
				startActivity(new Intent(this, HelpFeedbackActivity.class));
				finish();
				break;
		}

		return super.onOptionsItemSelected(item);
	}
}
