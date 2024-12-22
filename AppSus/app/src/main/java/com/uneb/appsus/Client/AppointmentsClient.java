package com.uneb.appsus.Client;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

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
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public AppointmentsClient(Context context) {
        super(context);
    }

    public class AppointmentResult {
        private final boolean success;
        private final String response;
        private final Exception exception;

        public AppointmentResult(boolean success, String response, Exception exception) {
            this.success = success;
            this.response = response;
            this.exception = exception;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getResponse() {
            return response;
        }

        public Exception getException() {
            return exception;
        }
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

    public Future<AppointmentResult> createAppointment(AppointmentDTO appointment) {
        return executorService.submit(new Callable<AppointmentResult>() {
            @Override
            public AppointmentResult call() {
                Gson gson = new Gson();
                String json = gson.toJson(appointment);
                RequestBody body = RequestBody.create(json, JSON);
                Request request = postRequest(body, APPOINTMENTS_URL);

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseBody = response.body().string();
                        return new AppointmentResult(true, responseBody, null);
                    } else {
                        return new AppointmentResult(false, null, new IOException("Unexpected code " + response));
                    }
                } catch (IOException e) {
                    return new AppointmentResult(false, null, e);
                }
            }
        });
    }
}
