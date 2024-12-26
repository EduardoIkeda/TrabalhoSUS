// TokenManager.java
package com.uneb.appsus.Manager;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "token_prefs";
    private static final String TOKEN_KEY = "bearer_token";
    private static final String USER_ID_KEY = "user_id";
    private static final String USER_NAME_KEY = "user_name";
    private static final String USER_AVATAR_URL_KEY = "user_avatar_url";
    private static final String USER_ROLE_KEY = "user_role";
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

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID_KEY, null);
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.remove(USER_ID_KEY);
        editor.remove(USER_NAME_KEY);
        editor.remove(USER_AVATAR_URL_KEY);
        editor.remove(USER_ROLE_KEY);
        editor.apply();
    }

    public void setUserInfo(String userId, String name, String avatarUrl, String role, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.putString(USER_NAME_KEY, name);
        editor.putString(USER_AVATAR_URL_KEY, avatarUrl);
        editor.putString(USER_ROLE_KEY, role);
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME_KEY, null);
    }

    public String getUserAvatarUrl() {
        return sharedPreferences.getString(USER_AVATAR_URL_KEY, null);
    }

    public String getUserRole() {
        return sharedPreferences.getString(USER_ROLE_KEY, null);
    }
}