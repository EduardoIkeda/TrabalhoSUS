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
import com.uneb.appsus.DTO.AppointmentByDateDTO;
import com.uneb.appsus.DTO.AppointmentDTO;
import com.uneb.appsus.DTO.AppointmentDisplayDTO;
import com.uneb.appsus.DTO.DoctorAppointment;
import com.uneb.appsus.Manager.TokenManager;

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

    public List<AppointmentByDateDTO> getAppointmentsBySpecialtyAndHealthCenter(Long specialtyId, Long healthCenterId) {
        Request request = this.baseRequest(APPOINTMENTS_URL +
                "/group?healthCenterId=" + healthCenterId +
                "&specialtyId=" + specialtyId);
    
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Log.d("AppointmentsClient", "Response: " + json); // Adicione este log
                Gson gson = new Gson();
                Type listType = new TypeToken<List<AppointmentByDateDTO>>() {
                }.getType();
                return gson.fromJson(json, listType);
            } else {
                Log.e("AppointmentsClient", "Failed to get appointments: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AppointmentDisplayDTO> getAppointments() {
        String userId = TokenManager.getInstance(context).getUserId();
        Log.d("AppointmentsClient", "User ID: " + userId);
        Request request = this.baseRequest(APPOINTMENTS_URL + "/by-user/" + userId);

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<AppointmentDisplayDTO>>() {
                }.getType();
                Log.d("AppointmentsClient", "Response: " + json);
                return gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Future<AppointmentResult> createAppointment(DoctorAppointment appointment) {
        appointment.setPatientId(TokenManager.getInstance(context).getUserId());
        return executorService.submit(new Callable<AppointmentResult>() {
            @Override
            public AppointmentResult call() {
                Gson gson = new Gson();
                String json = gson.toJson(appointment);
                RequestBody body = RequestBody.create(json, JSON);
                Request request = putRequest(body, APPOINTMENTS_URL + "/schedule/" + appointment.getId());

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

    public boolean cancelAppointment(int appointmentId) {
        Request request = this.deleteRequest( APPOINTMENTS_URL + "/" + appointmentId);
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Log.d("AppointmentsClient", "Appointment canceled successfully");
                return true;
            } else {
                Log.e("AppointmentsClient", "Failed to cancel appointment: " + response.code());
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean scheduleAppointment(DoctorAppointment doctorAppointment) {
        doctorAppointment.setPatientId( TokenManager.getInstance(context).getUserId());

        Gson gson = new Gson();
        String json = gson.toJson(doctorAppointment);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = putRequest(body, APPOINTMENTS_URL + "/schedule/" + doctorAppointment.getId());

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Log.d("AppointmentsClient", "Appointment scheduled successfully");
                return true;
            } else {
                Log.e("AppointmentsClient", "Failed to schedule appointment: " + response.code());
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
