package fr.exia.stentor;

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

public class LicenceActivity extends AbstractActivity {

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

		checkServieRunning(menu);

		return true;
	}
}
