package com.uneb.appsus.Client;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.uneb.appsus.DTO.AppointmentDTO;
import com.uneb.appsus.DTO.UserDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserClient extends BaseClient{

    private static final String USER_URL = "/users";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public UserClient(Context context) {
        super(context);
    }

    public String loginUser(UserDTO user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = this.postRequest(body, USER_URL + "/login");

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                return jsonObject.get("token").getAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String registerUser(UserDTO user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = this.postRequest(body, USER_URL + "/register");

        try(Response response = client.newCall(request).execute()){
            if (response.isSuccessful() && response.body() != null){
                String responseBody = response.body().string();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                return jsonObject.get("token").getAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
