package fr.exia.stentor.speech;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import fr.exia.stentor.R;
import fr.exia.stentor.util.MyPrefs;

/**
 * Persistently run a speech activator in the background.
 * Use {@link Intent}s to start and stop it
 */
public class SpeechActivationService extends Service {

	private static final String TAG = "SpeechActivationService";
	/**
	 * send this when external code wants the Service to stop
	 */
	public static final String ACTIVATION_STOP_INTENT_KEY = "ACTIVATION_STOP_INTENT_KEY";
	public static final String NOTIFICATION_ICON_RESOURCE_INTENT_KEY = "NOTIFICATION_ICON_RESOURCE_INTENT_KEY";
	public static final String ACTIVATION_TYPE_INTENT_KEY = "ACTIVATION_TYPE_INTENT_KEY";
	public static final String ACTIVATION_RESULT_INTENT_KEY = "ACTIVATION_RESULT_INTENT_KEY";
	public static final String ACTIVATION_RESULT_BROADCAST_NAME = "ACTIVATION_RESULT_BROADCAST_NAME";

	public static final int NOTIFICATION_ID = 10298;
	private boolean isStarted;
	private SpeechRecognizer recognizer;

	RecognitionListener recognitionListener = new RecognitionListener() {
		@Override
		public void onReadyForSpeech(Bundle params) {
			Log.d(TAG, "ready for speech " + params);
		}

		@Override
		public void onBeginningOfSpeech() {
			Log.i(TAG, "onBeginningOfSpeech");
		}

		@Override
		public void onRmsChanged(float rmsdB) {
		}

		@Override
		public void onBufferReceived(byte[] buffer) {
		}

		@Override
		public void onEndOfSpeech() {
			Log.i(TAG, "onEndOfSpeech");
		}

		@Override
		public void onError(int error) {
			if ((error == SpeechRecognizer.ERROR_NO_MATCH) || (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)) {
				Log.d(TAG, "didn't recognize anything");
				// keep going
				recognizeSpeechDirectly();
			} else {
				Log.d(TAG, "FAILED " + diagnoseErrorCode(error));
			}
		}

		@Override
		public void onResults(Bundle results) {
			Log.d(TAG, "full results");
			receiveResults(results);
		}

		@Override
		public void onPartialResults(Bundle partialResults) {
			Log.d(TAG, "partial results");
			receiveResults(partialResults);
		}

		@Override
		public void onEvent(int eventType, Bundle params) {

		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		isStarted = false;
	}

	/**
	 * stop or start an activator based on the activator type and if an activator is currently running
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			if (intent.hasExtra(ACTIVATION_STOP_INTENT_KEY)) {
				Log.d(TAG, "stop service intent");
				activated(false);
			} else {
				if (isStarted) {
					// the activator is currently started if the intent is requesting a new activator stop the current activator and start the new one
					stopActivator();
					startDetecting(intent);
				} else {
					// activator not started, start it
					startDetecting(intent);
				}
			}
		}

		// restart in case the Service gets canceled
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "On destroy");
		super.onDestroy();
		stopActivator();
		stopForeground(true);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static Intent makeStartServiceIntent(Context context, String activationType) {
		Intent i = new Intent(context, SpeechActivationService.class);
		i.putExtra(ACTIVATION_TYPE_INTENT_KEY, activationType);
		return i;
	}

	public static Intent makeServiceStopIntent(Context context) {
		Intent i = new Intent(context, SpeechActivationService.class);
		i.putExtra(ACTIVATION_STOP_INTENT_KEY, true);
		return i;
	}

	public void activated(boolean success) {
		// make sure the activator is stopped before doing anything else
		stopActivator();

		// broadcast result
		Intent intent = new Intent(ACTIVATION_RESULT_BROADCAST_NAME);
		intent.putExtra(ACTIVATION_RESULT_INTENT_KEY, success);
		sendBroadcast(intent);

		// always stop after receive an activation
		stopSelf();
	}

	public void detectActivation() {
		recognizeSpeechDirectly();
	}

	public void stop() {
		if (getSpeechRecognizer() != null) {
			getSpeechRecognizer().stopListening();
			getSpeechRecognizer().cancel();
			getSpeechRecognizer().destroy();
		}
	}

	private void startDetecting(Intent intent) {
		Log.d(TAG, "extras: " + intent.getExtras().toString());
		Log.d(TAG, "started");
		isStarted = true;
		detectActivation();
		startForeground(NOTIFICATION_ID, getNotification(intent));
	}

	private void stopActivator() {
		Log.d(TAG, "stopped");
		stop();
		isStarted = false;
	}

	@SuppressLint("NewApi")
	private Notification getNotification(Intent intent) {
		// determine label based on the class
		String message = getString(R.string.speech_notification_listening);
		String title = getString(R.string.speech_notification_title);

		PendingIntent pi = PendingIntent.getService(this, 0, makeServiceStopIntent(this), 0);

		int icon = intent.getIntExtra(NOTIFICATION_ICON_RESOURCE_INTENT_KEY, R.drawable.music_note);

		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(icon)
				.setWhen(System.currentTimeMillis()).setTicker(message)
				.setContentTitle(title).setContentText(message)
				.addAction(R.drawable.close, getString(R.string.speech_notification_close), pi);
		return builder.build();
	}

	private void recognizeSpeechDirectly() {
		Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		MyPrefs prefs = new MyPrefs(getApplicationContext(), MyPrefs.SETTINGS_PREFS);
		if (prefs.getLanguageVoice().equals("EN")) {
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
		} else {
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRENCH.toString());
		}

		recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
		recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
		recognizeSpeechDirectly(recognizerIntent, recognitionListener, getSpeechRecognizer());
	}

	/**
	 * common method to process any results bundle from {@link SpeechRecognizer}
	 */
	private void receiveResults(Bundle results) {
		if ((results != null) && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
			List<String> heard = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			for (String s : heard) {
				Log.e(TAG, s);
				sendBroadcastReceiver(s);
			}
			recognizeSpeechDirectly();
		} else {
			Log.d(TAG, "no results");
		}
	}

	private void sendBroadcastReceiver(String s) {
		if (s.contains("opération ")) {
			Intent intent = new Intent(SpeechUtils.OPERATION);
			intent.putExtra("NAME", s.replace("opération ", ""));
			sendBroadcast(intent);
		} else {
			switch (s) {
				case "ouvrir application":
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setComponent(new ComponentName("fr.exia.stentor", "fr.exia.stentor.HomeActivity"));
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					break;
				case "fermer application":
					sendBroadcast(new Intent(SpeechUtils.CLOSE_APP));
					break;
				case "fermer service":
					sendBroadcast(new Intent(SpeechUtils.CLOSE_SERVICE));
					break;
				case "retour":
					sendBroadcast(new Intent(SpeechUtils.BACK));
					break;
				case "accueil":
					sendBroadcast(new Intent(SpeechUtils.OPEN_HOME));
					break;
				case "maintenance":
					sendBroadcast(new Intent(SpeechUtils.OPEN_MAINTENANCE));
					break;
				case "paramètres":
					sendBroadcast(new Intent(SpeechUtils.OPEN_SETTINGS));
					break;
				case "aide et commentaires":
					sendBroadcast(new Intent(SpeechUtils.OPEN_HELP_FEEDBACK));
					break;
				case "démarrer":
					sendBroadcast(new Intent(SpeechUtils.OP_LAUNCH));
					break;
				case "précédent":
					sendBroadcast(new Intent(SpeechUtils.OP_PREVIOUS));
					break;
				case "suivant":
					sendBroadcast(new Intent(SpeechUtils.OP_NEXT));
					break;
				case "répéter":
					sendBroadcast(new Intent(SpeechUtils.OP_REPEAT));
					break;
				case "stop":
					sendBroadcast(new Intent(SpeechUtils.OP_STOP));
			}
		}
	}

	private void recognizeSpeechDirectly(Intent recognizerIntent, RecognitionListener listener, SpeechRecognizer recognizer) {
		//need to have a calling package for it to work
		if (!recognizerIntent.hasExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE)) {
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.dummy");
		}

		recognizer.setRecognitionListener(listener);
		recognizer.startListening(recognizerIntent);
	}

	private String diagnoseErrorCode(int errorCode) {
		String message;
		switch (errorCode) {
			case SpeechRecognizer.ERROR_AUDIO:
				message = "Audio recording error";
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				message = "Client side error";
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				message = "Insufficient permissions";
				break;
			case SpeechRecognizer.ERROR_NETWORK:
				message = "Network error";
				break;
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				message = "Network timeout";
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				message = "No match";
				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				message = "RecognitionService busy";
				break;
			case SpeechRecognizer.ERROR_SERVER:
				message = "error from server";
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				message = "No speech input";
				break;
			default:
				message = "Didn't understand, please try again.";
				break;
		}
		return message;
	}

	private SpeechRecognizer getSpeechRecognizer() {
		if (recognizer == null) {
			recognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
		}
		return recognizer;
	}
}
