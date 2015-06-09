package fr.exia.stentor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;

import fr.exia.stentor.speech.Speaker;
import fr.exia.stentor.speech.SpeechActivationService;
import fr.exia.stentor.speech.SpeechUtils;
import fr.exia.stentor.util.AppUtils;

public abstract class AbstractActivity extends Activity {

	private final int CHECK_CODE = 0x1;
	Speaker speaker;
	MenuItem speechOn;
	MenuItem speechOff;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				speaker = new Speaker(this);
			} else {
				Intent install = new Intent();
				install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(install);
			}
		}
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
			case R.id.action_list_command:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.speech_list_command_title)
						.setItems(R.array.speech_list_command, null)
						.create()
						.show();
				break;
			case R.id.action_home:
				startActivity(new Intent(this, HomeActivity.class));
				finish();
				break;
			case R.id.action_maintenance:
				startActivity(new Intent(this, MaintenanceActivity.class));
				finish();
				break;
			case R.id.action_settings:
				startActivity(new Intent(this, SettingsActivity.class));
				finish();
				break;
			case R.id.action_help_feedback:
				startActivity(new Intent(this, HelpFeedbackActivity.class));
				finish();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void checkTTS() {
		Intent check = new Intent();
		check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(check, CHECK_CODE);
	}

	public void checkServieRunning(Menu menu) {
		speechOn = menu.findItem(R.id.action_speech_on);
		speechOff = menu.findItem(R.id.action_speech_off);

		if (AppUtils.isMyServiceRunning(SpeechActivationService.class, this)) {
			speechOn.setVisible(false);
			speechOff.setVisible(true);
		} else {
			speechOn.setVisible(true);
			speechOff.setVisible(false);
		}

	}
}
