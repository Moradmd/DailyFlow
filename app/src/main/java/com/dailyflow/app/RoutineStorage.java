package com.dailyflow.app;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RoutineStorage {
    private static final String PREF_NAME = "dailyflow_routines";
    private static final String KEY_ROUTINES = "routines";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_STREAK = "streak";
    private static final String KEY_LAST_DATE = "last_date";
    private static final String KEY_TOTAL_COMPLETED = "total_completed";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_ONBOARDING_DONE = "onboarding_done";
    private static final String KEY_WEEKLY_DATA = "weekly_data";

    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public RoutineStorage(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveRoutines(List<Routine> routines) {
        String json = gson.toJson(routines);
        prefs.edit().putString(KEY_ROUTINES, json).apply();
    }

    public List<Routine> getRoutines() {
        String json = prefs.getString(KEY_ROUTINES, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<Routine>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveUserName(String name) {
        prefs.edit().putString(KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public boolean isOnboardingDone() {
        return prefs.getBoolean(KEY_ONBOARDING_DONE, false);
    }

    public void setOnboardingDone(boolean done) {
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, done).apply();
    }

    public int getStreak() {
        return prefs.getInt(KEY_STREAK, 0);
    }

    public void setStreak(int streak) {
        prefs.edit().putInt(KEY_STREAK, streak).apply();
    }

    public long getLastDate() {
        return prefs.getLong(KEY_LAST_DATE, 0);
    }

    public void setLastDate(long date) {
        prefs.edit().putLong(KEY_LAST_DATE, date).apply();
    }

    public int getTotalCompleted() {
        return prefs.getInt(KEY_TOTAL_COMPLETED, 0);
    }

    public void addTotalCompleted(int count) {
        prefs.edit().putInt(KEY_TOTAL_COMPLETED, getTotalCompleted() + count).apply();
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public void setDarkMode(boolean dark) {
        prefs.edit().putBoolean(KEY_DARK_MODE, dark).apply();
    }

    public boolean isNotificationsEnabled() {
        return prefs.getBoolean(KEY_NOTIFICATIONS, true);
    }

    public void setNotificationsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply();
    }

    public String getLanguage() {
        return prefs.getString(KEY_LANGUAGE, "en");
    }

    public void setLanguage(String lang) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply();
    }

    public void saveWeeklyData(int[] data) {
        String json = gson.toJson(data);
        prefs.edit().putString(KEY_WEEKLY_DATA, json).apply();
    }

    public int[] getWeeklyData() {
        String json = prefs.getString(KEY_WEEKLY_DATA, null);
        if (json == null) return new int[]{0,0,0,0,0,0,0};
        Type type = new TypeToken<int[]>() {}.getType();
        return gson.fromJson(json, type);
    }
}
