// TokenManager.java
package com.uneb.appsus.Manager;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "token_prefs";
    private static final String TOKEN_KEY = "bearer_token";
    private static TokenManager instance;
    private final SharedPreferences sharedPreferences;

    private TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    public String getBearerToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public void setBearerToken(String bearerToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, bearerToken);
        editor.apply();
    }
}