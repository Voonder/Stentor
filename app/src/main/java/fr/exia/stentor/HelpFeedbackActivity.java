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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import fr.exia.stentor.adapter.HelpFeedbackAdapter;
import fr.exia.stentor.interfaces.OnClickLicenseItem;
import fr.exia.stentor.model.ui.HelpFeedbackItem;
import fr.exia.stentor.speech.SpeechActivationService;
import fr.exia.stentor.speech.SpeechUtils;

public class HelpFeedbackActivity extends AbstractActivity {

    boolean firstPass = true;
    private List<HelpFeedbackItem> items = new ArrayList<>();

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

    OnClickLicenseItem onClickLicenseItem = new OnClickLicenseItem() {
        @Override
        public void onClickLicenseItem() {
            Intent intent = new Intent(getApplicationContext(), LicenceActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        items.add(new HelpFeedbackItem(getResources().getDrawable(R.drawable.library, null), getString(R.string.help_feedback_libraries)));
        items.add(new HelpFeedbackItem(getResources().getDrawable(R.drawable.message_alert, null), getString(R.string.help_feedback_feedback)));

        setContentView(R.layout.activity_help_feedback);

        HelpFeedbackAdapter helpFeedbackAdapter = new HelpFeedbackAdapter(this, items);
        helpFeedbackAdapter.onClickLicenseItem = onClickLicenseItem;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerHelpFeedback = (RecyclerView) findViewById(R.id.help_feedback_list);
        recyclerHelpFeedback.setHasFixedSize(true);
        recyclerHelpFeedback.setAdapter(helpFeedbackAdapter);
        recyclerHelpFeedback.setLayoutManager(layoutManager);

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
        getMenuInflater().inflate(R.menu.menu_help_feedback, menu);

        checkServieRunning(menu);

        return true;
    }
}
