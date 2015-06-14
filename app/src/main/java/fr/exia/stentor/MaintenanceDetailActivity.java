package fr.exia.stentor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.exia.stentor.model.Operation;
import fr.exia.stentor.speech.SpeechActivationService;
import fr.exia.stentor.speech.SpeechUtils;

public class MaintenanceDetailActivity extends AbstractActivity {

	private static final String TAG = "MaintenanceDetail";
	MenuItem speechOn;
	MenuItem speechOff;

	boolean isStart = false;
	boolean firstPass = true;
	private TextView txtStep;
	private Operation operation;
	private ImageButton btnPrevious;
	private ImageButton btnStop;
	private ImageButton btnPlay;
	private ImageButton btnReplay;
	private ImageButton btnNext;
	private int positionStep = 0;

	View.OnClickListener onClickListenerPrevious = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			previous();
		}
	};

	View.OnClickListener onClickListenerStop = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			stop();
		}
	};

	View.OnClickListener onClickListenerPlay = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			play();
		}
	};

	View.OnClickListener onClickListenerReplay = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			repeat();
		}
	};

	View.OnClickListener onClickListenerNext = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			next();
		}
	};

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SpeechUtils.BACK)) {
				if (firstPass) {
					firstPass = false;
					speaker.speak(getString(R.string.tts_open_maintenance));
					startActivity(new Intent(getApplicationContext(), MaintenanceActivity.class));
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
			} else if (intent.getAction().equals(SpeechUtils.OP_PREVIOUS)) {
				if (isStart) {
					if (positionStep != 0) {
						previous();
					} else {
						speaker.speak(getString(R.string.tts_step_previous_error));
					}
				} else {
					speaker.speak(getString(R.string.tts_operation_not_start));
				}
			} else if (intent.getAction().equals(SpeechUtils.OP_STOP)) {
				isStart = false;
				stop();
			} else if (intent.getAction().equals(SpeechUtils.OP_LAUNCH)) {
				isStart = true;
				play();
			} else if (intent.getAction().equals(SpeechUtils.OP_REPEAT)) {
				if (isStart) {
					repeat();
				} else {
					speaker.speak(getString(R.string.tts_operation_not_start));
				}
			} else if (intent.getAction().equals(SpeechUtils.OP_NEXT)) {
				if (isStart) {
					if (positionStep != operation.getSteps().size() - 1) {
						next();
					} else {
						speaker.speak(getString(R.string.tts_step_next_error));
					}
				} else {
					speaker.speak(getString(R.string.tts_operation_not_start));
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintenance_detail);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		operation = (Operation) getIntent().getSerializableExtra("PARAM_OPERATION");

		RelativeLayout layoutBackground = (RelativeLayout) findViewById(R.id.maintenance_detail_background);
		TextView txtTitle = (TextView) findViewById(R.id.maintenance_detail_title);
		TextView txtDescription = (TextView) findViewById(R.id.maintenance_detail_description_plus);
		TextView txtDuration = (TextView) findViewById(R.id.maintenance_detail_duration_plus);
		TextView txtNbStep = (TextView) findViewById(R.id.maintenance_detail_nb_step_plus);
		txtStep = (TextView) findViewById(R.id.maintenance_detail_step_plus);
		btnPrevious = (ImageButton) findViewById(R.id.maintenance_detail_btn_previous);
		btnStop = (ImageButton) findViewById(R.id.maintenance_detail_btn_stop);
		btnPlay = (ImageButton) findViewById(R.id.maintenance_detail_btn_play);
		btnReplay = (ImageButton) findViewById(R.id.maintenance_detail_btn_replay);
		btnNext = (ImageButton) findViewById(R.id.maintenance_detail_btn_next);

		txtTitle.setText(operation.getName());
		txtDescription.setText(operation.getDescription());

		if (operation.getName().equals("Extincteur")) {
			layoutBackground.setBackground(getDrawable(R.drawable.photo_extincteurs));
		} else if (operation.getName().equals("Fontaine")) {
			layoutBackground.setBackground(getDrawable(R.drawable.photo_fontaines));
		}

		String time;
		if (operation.getDuration().getHour() == 0) {
			if (operation.getDuration().getMinute() == 0) {
				time = getString(R.string.maintenance_item_second, operation.getDuration().getSecond());
			} else {
				time = getString(R.string.maintenance_item_minute, operation.getDuration().getMinute(), operation.getDuration().getSecond());
			}
		} else {
			time = getString(R.string.maintenance_item_hour, operation.getDuration().getHour(), operation.getDuration().getMinute(), operation.getDuration().getSecond());
		}
		txtDuration.setText(time);

		txtNbStep.setText(String.valueOf(operation.getSteps().size()));
		txtStep.setText(getString(R.string.maintenance_detail_step_detail_none));

		btnPrevious.setOnClickListener(onClickListenerPrevious);
		btnStop.setOnClickListener(onClickListenerStop);
		btnPlay.setOnClickListener(onClickListenerPlay);
		btnReplay.setOnClickListener(onClickListenerReplay);
		btnNext.setOnClickListener(onClickListenerNext);

		changeColorItemButton(false, btnPrevious, ButtonPosition.PREVIOUS.id);
		changeColorItemButton(false, btnStop, ButtonPosition.STOP.id);
		changeColorItemButton(false, btnReplay, ButtonPosition.REPLAY.id);
		changeColorItemButton(false, btnNext, ButtonPosition.NEXT.id);

		checkTTS();
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SpeechUtils.CLOSE_APP);
		intentFilter.addAction(SpeechUtils.CLOSE_SERVICE);
		intentFilter.addAction(SpeechUtils.BACK);
		intentFilter.addAction(SpeechUtils.OPEN_HOME);
		intentFilter.addAction(SpeechUtils.OPEN_MAINTENANCE);
		intentFilter.addAction(SpeechUtils.OPEN_SETTINGS);
		intentFilter.addAction(SpeechUtils.OPEN_HELP_FEEDBACK);

		// Action operation
		intentFilter.addAction(SpeechUtils.OP_PREVIOUS);
		intentFilter.addAction(SpeechUtils.OP_STOP);
		intentFilter.addAction(SpeechUtils.OP_LAUNCH);
		intentFilter.addAction(SpeechUtils.OP_REPEAT);
		intentFilter.addAction(SpeechUtils.OP_NEXT);

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
		getMenuInflater().inflate(R.menu.menu_maintenance_detail, menu);

		checkServieRunning(menu);

		return true;
	}

	private void previous() {
		changeColorItemButton(true, btnNext, ButtonPosition.NEXT.id);

		positionStep--;
		txtStep.setText(operation.getSteps().get(positionStep));
		speaker.speak(operation.getSteps().get(positionStep));

		if (positionStep == 0) {
			changeColorItemButton(false, btnPrevious, ButtonPosition.PREVIOUS.id);
		} else {
			changeColorItemButton(true, btnPrevious, ButtonPosition.PREVIOUS.id);
		}
	}

	private void stop() {
		changeColorItemButton(false, btnPrevious, ButtonPosition.PREVIOUS.id);
		changeColorItemButton(false, btnStop, ButtonPosition.STOP.id);
		changeColorItemButton(true, btnPlay, ButtonPosition.PLAY.id);
		changeColorItemButton(false, btnReplay, ButtonPosition.REPLAY.id);
		changeColorItemButton(false, btnNext, ButtonPosition.NEXT.id);

		positionStep = 0;
		txtStep.setText(getString(R.string.maintenance_detail_step_detail_none));
		speaker.speak(getString(R.string.tts_step_stop));
	}

	private void play() {
		changeColorItemButton(false, btnPrevious, ButtonPosition.PREVIOUS.id);
		changeColorItemButton(true, btnStop, ButtonPosition.STOP.id);
		changeColorItemButton(false, btnPlay, ButtonPosition.PLAY.id);
		changeColorItemButton(true, btnReplay, ButtonPosition.REPLAY.id);

		txtStep.setText(operation.getSteps().get(positionStep));
		speaker.speak(operation.getSteps().get(positionStep));

		if (positionStep + 1 == operation.getSteps().size()) {
			changeColorItemButton(false, btnNext, ButtonPosition.NEXT.id);
		} else {
			changeColorItemButton(true, btnNext, ButtonPosition.NEXT.id);
		}
	}

	private void repeat() {
		speaker.speak(operation.getSteps().get(positionStep));
	}

	private void next() {
		changeColorItemButton(true, btnPrevious, ButtonPosition.PREVIOUS.id);

		positionStep++;
		txtStep.setText(operation.getSteps().get(positionStep));
		speaker.speak(operation.getSteps().get(positionStep));

		if (positionStep == operation.getSteps().size() - 1) {
			changeColorItemButton(false, btnNext, ButtonPosition.NEXT.id);
		} else {
			changeColorItemButton(true, btnNext, ButtonPosition.NEXT.id);
		}
	}

	private void changeColorItemButton(boolean isActive, ImageButton button, int position) {
		Drawable drawable = null;
		switch (ButtonPosition.values()[position]) {
			case PREVIOUS:
				drawable = getDrawable(isActive ? R.drawable.skip_previous : R.drawable.skip_previous_disabled);
				break;
			case STOP:
				drawable = getDrawable(isActive ? R.drawable.stop : R.drawable.stop_disabled);
				break;
			case PLAY:
				drawable = getDrawable(isActive ? R.drawable.play : R.drawable.play_disabled);
				break;
			case REPLAY:
				drawable = getDrawable(isActive ? R.drawable.replay : R.drawable.replay_disabled);
				break;
			case NEXT:
				drawable = getDrawable(isActive ? R.drawable.skip_next : R.drawable.skip_next_disabled);
				break;
		}

		button.setImageDrawable(drawable);
		button.setClickable(isActive);
		button.setEnabled(isActive);
	}

	private enum ButtonPosition {
		PREVIOUS(0),
		STOP(1),
		PLAY(2),
		REPLAY(3),
		NEXT(4);

		public int id;

		ButtonPosition(int id) {
			this.id = id;
		}
	}
}
