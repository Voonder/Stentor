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
