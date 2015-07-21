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
