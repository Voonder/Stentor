package fr.exia.stentor.speech;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

import fr.exia.stentor.util.MyPrefs;

public class Speaker implements TextToSpeech.OnInitListener {

	private boolean ready = false;
	private TextToSpeech tts;
	private Context context;

	public Speaker(Context context) {
		this.context = context;
		tts = new TextToSpeech(context, this);
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			MyPrefs prefs = new MyPrefs(context, MyPrefs.SETTINGS_PREFS);

			if (prefs.getLanguageVoice().equals("EN")) {
				tts.setLanguage(Locale.ENGLISH);
			} else {
				tts.setLanguage(Locale.FRENCH);
			}
			ready = true;
		} else {
			ready = false;
		}
	}

	public void speak(String text) {
		// Speak only if the TTS is ready and the user has allowed speech
		if (ready) {
			Bundle bundle = new Bundle();
			bundle.putString(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_NOTIFICATION));
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, bundle, "TTS");
		}
	}

	public void pause(int duration) {
		tts.playSilentUtterance(duration, TextToSpeech.QUEUE_ADD, null);
	}

	public void destroy() {
		tts.shutdown();
	}
}
