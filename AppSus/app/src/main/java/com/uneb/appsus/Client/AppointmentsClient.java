package com.uneb.appsus.Client;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.uneb.appsus.DTO.AppointmentDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentsClient extends BaseClient {

    private static final String APPOINTMENTS_URL = "/appointments";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public AppointmentsClient(Context context) {
        super(context);
    }

    public List<AppointmentDTO> getAppointments() {
        Request request = this.baseRequest("/appointments");

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<AppointmentDTO>>() {}.getType();
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createAppointment(AppointmentDTO appointment){;
        Gson gson = new Gson();
        String json = gson.toJson(appointment);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = this.postRequest(body, APPOINTMENTS_URL);

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}