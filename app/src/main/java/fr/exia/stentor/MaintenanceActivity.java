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

import fr.exia.stentor.adapter.MaintenanceAdapter;
import fr.exia.stentor.interfaces.OnClickOperationItem;
import fr.exia.stentor.model.Operation;
import fr.exia.stentor.model.Time;
import fr.exia.stentor.speech.SpeechActivationService;
import fr.exia.stentor.speech.SpeechUtils;
import fr.exia.stentor.util.AppUtils;

public class MaintenanceActivity extends AbstractActivity {

	private static final String TAG = "MaintenanceActivity";

	MenuItem speechOn;
	MenuItem speechOff;

	boolean firstPass = true;
	private List<Operation> operations = new ArrayList<>();

	OnClickOperationItem onClickOperationItem = new OnClickOperationItem() {
		@Override
		public void onClickOperationItem(Operation operation) {
			Intent intent = new Intent(getApplicationContext(), MaintenanceDetailActivity.class);
			intent.putExtra("PARAM_OPERATION", operation);
			startActivity(intent);
			finish();
		}
	};

	BroadcastReceiver maintenanceSpeech = new BroadcastReceiver() {
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
			} else if (intent.getAction().equals(SpeechUtils.OPERATION)) {
				loadOperation(intent.getExtras().getString("NAME"));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		checkTTS();

		List<String> steps = new ArrayList<>();
		steps.add("Open");
		steps.add("Tevmz");
		steps.add("Fnz, zltz");

		List<String> stepsFontaine = new ArrayList<>();
		stepsFontaine.add("Vérifier que la réserve est vide");
		stepsFontaine.add("Couper l’arrivée d’eau");
		stepsFontaine.add("Déclipser la réserve vide");
		stepsFontaine.add("Enlever le capuchon de la réserve pleine");
		stepsFontaine.add("Insérer la réserve pleine");
		stepsFontaine.add("Ouvrir l’arrivée d’eau");
		stepsFontaine.add("Tester la fontaine a eau");
		stepsFontaine.add("Fin de la maintenance");

		List<String> stepsExtincteurs = new ArrayList<>();
		stepsExtincteurs.add("vérifier que l’extincteur soit installé à un bon endroit");
		stepsExtincteurs.add("voir si le lieu d’installation de l’extincteur peut être vu et accédé facilement");
		stepsExtincteurs.add("vérifier qu'un procédé d’utilisation est affiché sur l’extincteur");
		stepsExtincteurs.add("s’assurer que le matériel n’est pas abîmé");
		stepsExtincteurs.add("voir si l’extincteur possède de l’aiguille qui affiche la pressionet de dernier se trouve dans l’espace en vert.");
		stepsExtincteurs.add("contrôler si l’extincteur est doté d’accroches de sécurité suffisantes et qui ne sont pas cassées");
		stepsExtincteurs.add("Fin de la maintenance");

		operations.add(new Operation("Test", "test description", new Time(0, 20, 30), steps));
		operations.add(new Operation("Fontaine", "L’entretien passe par un nettoyage relativement classique: chaque pièce de la fontaine à eau doit être au moins 2 fois par an désinfectée. Toutes les pièces en contact avec l’eau mais aussi l’extérieur des fontaines à eau.", new Time(0, 10, 0), stepsFontaine));
		operations.add(new Operation("Extincteur", "Un extincteur qui ne fait pas l’objet d’un entretien régulier ne peut pas fonctionner convenablement. Un incendie ne peut être lutté que grâce, uniquement à des extincteurs en parfait état. Pour savoir si un extincteur est en état de fonctionner, il est nécessaire d’effectuer régulièrement un contrôle extincteurs. En principe, un extincteur doit être contrôlé chaque année.", new Time(0, 35, 0), stepsExtincteurs));
		operations.add(new Operation("Test 3", "test description 3", new Time(2, 20, 0), steps));

		setContentView(R.layout.activity_maintenance);

		MaintenanceAdapter maintenanceAdapter = new MaintenanceAdapter(this, operations);
		maintenanceAdapter.onClickOperationItem = onClickOperationItem;

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

		RecyclerView recyclerMaintenance = (RecyclerView) findViewById(R.id.maintenance_list);
		recyclerMaintenance.setHasFixedSize(true);
		recyclerMaintenance.setAdapter(maintenanceAdapter);
		recyclerMaintenance.setLayoutManager(layoutManager);

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
		intentFilter.addAction(SpeechUtils.OPERATION);
		registerReceiver(maintenanceSpeech, intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(maintenanceSpeech);
		speaker.destroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_maintenance, menu);

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

	private void loadOperation(String operation) {
		if (firstPass) {
			Operation op = null;
			for (Operation s : operations) {
				if (s.getName().toLowerCase().equals(operation)) {
					op = s;
					break;
				}
			}

			if (op != null) {
				firstPass = false;
				Intent intent = new Intent(getApplicationContext(), MaintenanceDetailActivity.class);
				intent.putExtra("PARAM_OPERATION", op);
				startActivity(intent);
				finish();
			} else {
				speaker.speak(getString(R.string.tts_operation_unknown));
			}
		}
	}
}
