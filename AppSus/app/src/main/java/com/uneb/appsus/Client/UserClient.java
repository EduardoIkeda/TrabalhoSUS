package com.uneb.appsus.Client;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.uneb.appsus.DTO.AuthResponseDTO;
import com.uneb.appsus.DTO.UserDTO;
import com.uneb.appsus.DTO.UserPartialDTO;
import com.uneb.appsus.Manager.TokenManager;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserClient extends BaseClient {

    private static final String USER_URL = "/users";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String TAG = "UserClient";

    public UserClient(Context context) {
        super(context);
    }

    private AuthResponseDTO handleAuthResponse(Response response) throws IOException {
        if (response.isSuccessful() && response.body() != null) {
            String responseBody = response.body().string();
            Gson gson = new Gson();
            AuthResponseDTO authResponse = gson.fromJson(responseBody, AuthResponseDTO.class);
            TokenManager.getInstance(context).setUserInfo(
                String.valueOf(authResponse.getId()),
                authResponse.getName(),
                authResponse.getAvatarUrl(),
                authResponse.getRole(),
                authResponse.getToken()
            );
            return authResponse;
        } else {
            Log.d(TAG, "Auth failed: " + response.message());
        }
        return null;
    }

    public AuthResponseDTO loginUser(UserDTO user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = this.postRequest(body, USER_URL + "/login");

        try (Response response = client.newCall(request).execute()) {
            return handleAuthResponse(response);
        } catch (IOException e) {
            Log.d(TAG, "Login error: ", e);
        }
        return null;
    }

    public AuthResponseDTO registerUser(UserDTO user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = this.postRequest(body, USER_URL + "/register");

        try (Response response = client.newCall(request).execute()) {
            return handleAuthResponse(response);
        } catch (IOException e) {
            Log.d(TAG, "Registration error: ", e);
        }
        return null;
    }

    public UserDTO getUser() {
        Request request = this.baseRequest(USER_URL + "/" + TokenManager.getInstance(context).getUserId());

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                Gson gson = new Gson();
                return gson.fromJson(responseBody, UserDTO.class);
            } else {
                Log.d(TAG, "Get user failed: " + response.message());
            }
        } catch (IOException e) {
            Log.d(TAG, "Get user error: ", e);
        }
        return null;
    }

    public boolean updateUser(UserPartialDTO user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = this.patchRequest(body, USER_URL + "/" + TokenManager.getInstance(context).getUserId());

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                Log.d(TAG, "Update user failed: " + response.message());
            }
        } catch (IOException e) {
            Log.d(TAG, "Update user error: ", e);
        }
        return false;
    }

}
