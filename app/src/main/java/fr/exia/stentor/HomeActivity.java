/**************************************************************************************************
 * The MIT License (MIT)                                                                          *
 *                                                                                                *
 * Copyright (c) 2015 - Julien Normand                                                            *
 *                                                                                                *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software  *
 * and associated documentation files (the "Software"),  to deal in the Software without          *
 * restriction, including without limitation  the  rights to use, copy, modify, merge, publish,   *
 * distribute, sublicense, and/or  sell copies of the Software, and to permit persons to whom the *
 * Software is furnished to do so, subject to the following conditions:                           *
 *                                                                                                *
 * The above copyright notice and this permission notice shall be included in all copies or       *
 * substantial portions of the Software.                                                          *
 *                                                                                                *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING  *
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND     *
 * NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,  *
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING       *
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.  *
 **************************************************************************************************/

package fr.exia.stentor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;

import fr.exia.stentor.speech.SpeechActivationService;
import fr.exia.stentor.speech.SpeechUtils;
import fr.exia.stentor.util.AppUtils;

public class HomeActivity extends AbstractActivity {
    boolean firstPass = true;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SpeechUtils.BACK)) {
                if (firstPass) {
                    firstPass = false;
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
        setContentView(R.layout.activity_home);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!AppUtils.isMyServiceRunning(SpeechActivationService.class, this)) {
            Intent i = SpeechActivationService.makeStartServiceIntent(this, "hello");
            startService(i);
        }

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
        getMenuInflater().inflate(R.menu.menu_home, menu);

        checkServieRunning(menu);

        return true;
    }
}
