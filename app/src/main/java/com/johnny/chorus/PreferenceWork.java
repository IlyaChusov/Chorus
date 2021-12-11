package com.johnny.chorus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceWork {

    private static final String PREF_SAVED_APP_VERSION = "savedAppVersion";
    private static final String PREF_TONE_ID = "defaultToneId";
    private static final String PREF_REP_MODE = "defaultRepeatingMode";
    private static SharedPreferences preferences;
    private static int defaultToneId;
    private static boolean defaultRepeatingMode;

    private PreferenceWork () {}

    public static void setContext(Context context) {
        preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        defaultToneId = preferences.getInt(PREF_TONE_ID,  0); // Бас по умолчанию
        defaultRepeatingMode = preferences.getBoolean(PREF_REP_MODE, false); // Настройка повтора песни по умолчанию
    }

    public static String getSavedAppVersion() {
        if (preferences != null)
            return preferences.getString(PREF_SAVED_APP_VERSION, "");
        return "";
    }

    public static void setSavedAppVersion(String savedAppVersion) {
        if (preferences != null)
            preferences.edit().putString(PREF_SAVED_APP_VERSION, savedAppVersion).apply();
    }

    public static int getDefaultToneId() {
        if (preferences != null)
            return defaultToneId;
        return -1;
    }

    public static void setDefaultTone(int newDefaultToneId) {
        if (preferences != null) {
            preferences.edit().putInt(PREF_TONE_ID, newDefaultToneId).apply();
            defaultToneId = newDefaultToneId;
        }
    }

    public static boolean getDefaultRepeatingMode() {
        if (preferences != null)
            return defaultRepeatingMode;
        return false;
    }

    public static void setDefaultRepeatingMode(boolean isRepeating) {
        if (preferences != null) {
            preferences.edit().putBoolean(PREF_REP_MODE, isRepeating).apply();
            defaultRepeatingMode = isRepeating;
        }
    }
}