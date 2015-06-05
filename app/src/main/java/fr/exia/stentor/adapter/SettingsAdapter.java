package fr.exia.stentor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.List;

import fr.exia.stentor.R;
import fr.exia.stentor.model.ui.SettingsItem;
import fr.exia.stentor.util.MyPrefs;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

	MyPrefs prefs;
	private List<SettingsItem> items;
	private Context context;
	private boolean isFirst = true;

	public SettingsAdapter(Context context, List<SettingsItem> items) {
		this.context = context;
		this.items = items;
		prefs = new MyPrefs(context, MyPrefs.SETTINGS_PREFS);
	}

	@Override
	public SettingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false);
		return new ViewHolder(rootView);
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
		viewHolder.txtName.setText(items.get(i).getName());
		viewHolder.txtDescription.setText(items.get(i).getDescription());

		switch (items.get(i).getTypeOfLanguage()) {
			case APP:
				String app = prefs.getLanguageApp();
				if (app.equals("")) {
					app = "EN";
				}
				viewHolder.spinLanguage.setSelection(getIndex(viewHolder.spinLanguage, app));
				break;
			case VOICE:
				String voice = prefs.getLanguageApp();
				if (voice.equals("")) {
					voice = "EN";
				}
				viewHolder.spinLanguage.setSelection(getIndex(viewHolder.spinLanguage, voice));
				break;
		}

		viewHolder.spinLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (SettingsItem.TypeOfLanguage.valueOf(items.get(i).getTypeOfLanguage().toString())) {
					case APP:
						prefs.saveLanguageApp(String.valueOf(viewHolder.spinLanguage.getItemAtPosition(position)));

						if (!isFirst) {
							SnackbarManager.show(Snackbar.with(context).text(R.string.settings_restart));
						} else {
							isFirst = false;
						}
						break;
					case VOICE:
						prefs.saveLanguageVoice(String.valueOf(viewHolder.spinLanguage.getItemAtPosition(position)));

						if (!isFirst) {
							SnackbarManager.show(Snackbar.with(context).text(R.string.settings_restart));
						} else {
							isFirst = false;
						}
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	private int getIndex(Spinner spinner, String myString) {
		int index = 0;

		for (int i = 0; i < spinner.getCount(); i++) {
			if (spinner.getItemAtPosition(i).equals(myString)) {
				index = i;
			}
		}

		return index;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView txtName;
		TextView txtDescription;
		Spinner spinLanguage;

		public ViewHolder(View itemView) {
			super(itemView);

			txtName = (TextView) itemView.findViewById(R.id.item_setting_name);
			txtDescription = (TextView) itemView.findViewById(R.id.item_setting_description);
			spinLanguage = (Spinner) itemView.findViewById(R.id.item_setting_spinner);
		}
	}
}
