package com.uneb.appsus.Client;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uneb.appsus.DTO.SpecialitiesDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class SpecialitiesClient extends BaseClient {
    private static final String SPECIALITIES_URL = "/specialties";

    public SpecialitiesClient(Context context) {
        super(context);
    }

    public List<SpecialitiesDTO> getSpecialities() {
        Request request = this.baseRequest(SPECIALITIES_URL);

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<SpecialitiesDTO>>() {
                }.getType();
                Log.d("SpecialitiesClient", "Response: " + json);
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
