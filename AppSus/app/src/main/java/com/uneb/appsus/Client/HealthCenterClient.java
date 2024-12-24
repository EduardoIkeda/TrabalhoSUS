package com.uneb.appsus.Client;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uneb.appsus.DTO.HealthCenterDTO;
import com.uneb.appsus.DTO.SpecialitiesDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class HealthCenterClient extends BaseClient {
    private static final String HEALTH_CENTER_URL = "/health-centers";

    public HealthCenterClient(Context context) {
        super(context);
    }

    public List<HealthCenterDTO> getHealthCenterBySpeciality(Long id) {
        Request request = this.baseRequest(HEALTH_CENTER_URL + "/by-specialty/" + id);

        Log.d("HealthCenterClient", "Request: " + request.toString());
        Log.d("HealthCenterClient", "speciality id: " + id);

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<HealthCenterDTO>>() {
                }.getType();
                Log.d("HealthCenterClient", "Response: " + json);
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
