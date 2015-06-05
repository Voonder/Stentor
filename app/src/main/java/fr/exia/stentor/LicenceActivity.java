package fr.exia.stentor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import fr.exia.stentor.adapter.LicenseAdapter;
import fr.exia.stentor.model.ui.LicenseItem;
import fr.exia.stentor.speech.SpeechActivationService;
import fr.exia.stentor.util.AppUtils;

public class LicenceActivity extends Activity {

	MenuItem speechOn;
	MenuItem speechOff;
	private List<LicenseItem> licenseItems = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		List<String> librariesMIT = new ArrayList<>();
		librariesMIT.add(getString(R.string.license_mit_snackbar));

		List<String> librariesApache = new ArrayList<>();
		librariesApache.add(getString(R.string.license_apache_recycler));
		librariesApache.add(getString(R.string.license_apache_cardview));

		licenseItems.add(new LicenseItem(getString(R.string.license_master_mit), librariesMIT));
		licenseItems.add(new LicenseItem(getString(R.string.license_master_apache), librariesApache));

		setContentView(R.layout.activity_licence);

		LicenseAdapter licenseAdapter = new LicenseAdapter(this, licenseItems);

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

		RecyclerView recyclerLicense = (RecyclerView) findViewById(R.id.license_list);
		recyclerLicense.setHasFixedSize(true);
		recyclerLicense.setAdapter(licenseAdapter);
		recyclerLicense.setLayoutManager(layoutManager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_licence, menu);

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
}
