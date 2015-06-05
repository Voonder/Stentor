package fr.exia.stentor.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefs {
	public static final String SETTINGS_PREFS = "SETTINGS_PREFS";
	public static final String LANGUAGE_APP = "LANGUAGE_APP";
	public static final String LANGUAGE_VOICE = "LANGUAGE_VOICE";

	private SharedPreferences sharedPreferences;

	public MyPrefs(Context context, String filePref) {
		sharedPreferences = context.getSharedPreferences(filePref, Context.MODE_PRIVATE);
	}

	// --------------------------------------------------------------------------------------------
	// SETTINGS_PREFS
	// --------------------------------------------------------------------------------------------
	public void saveLanguageApp(String value) {
		sharedPreferences.edit().putString(LANGUAGE_APP, value).apply();
	}

	public void removeLanguageApp() {
		sharedPreferences.edit().remove(LANGUAGE_APP).apply();
	}

	public void saveLanguageVoice(String value) {
		sharedPreferences.edit().putString(LANGUAGE_VOICE, value).apply();
	}

	public void removeLanguageVoice() {
		sharedPreferences.edit().remove(LANGUAGE_VOICE).apply();
	}

	public void clear() {
		sharedPreferences.edit().clear().apply();
	}

	public String getLanguageApp() {
		return sharedPreferences.getString(LANGUAGE_APP, "");
	}

	public String getLanguageVoice() {
		return sharedPreferences.getString(LANGUAGE_VOICE, "");
	}
}
