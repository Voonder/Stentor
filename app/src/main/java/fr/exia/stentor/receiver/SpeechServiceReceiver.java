package fr.exia.stentor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.exia.stentor.speech.SpeechActivationService;
import fr.exia.stentor.util.AppUtils;

public class SpeechServiceReceiver extends BroadcastReceiver {
	public SpeechServiceReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			if (!AppUtils.isMyServiceRunning(SpeechActivationService.class, context)) {
				Intent i = SpeechActivationService.makeStartServiceIntent(context, "hello");
				context.startService(i);
			}
		}
	}
}
