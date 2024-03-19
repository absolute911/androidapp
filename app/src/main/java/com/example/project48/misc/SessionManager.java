package com.example.project48.misc;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createLoginSession(String username ) {
        editor.putBoolean(KEY_IS_LOGGEDIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public void clearLoginSession() {
        // Clearing all data from SharedPreferences
        editor.clear();
        editor.commit();

        // After clearing the session, you might want to redirect the user to the login screen
        // Intent intent = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Starting Login Activity
        // context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }
}

