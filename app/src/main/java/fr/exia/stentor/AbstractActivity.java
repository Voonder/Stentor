package fr.exia.stentor;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

import fr.exia.stentor.speech.Speaker;

public abstract class AbstractActivity extends Activity {

	Speaker speaker;
	private final int CHECK_CODE = 0x1;

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

	public void checkTTS() {
		Intent check = new Intent();
		check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(check, CHECK_CODE);
	}
}
