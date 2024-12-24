package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.Client.AppointmentsClient;
import com.uneb.appsus.DTO.AppointmentDTO;
import com.uneb.appsus.DTO.AppointmentDisplayDTO;
import com.uneb.appsus.R;
import com.uneb.appsus.enums.AppointmentStatus;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConsultasActivity extends AppCompatActivity {

    private Button bookingButton;
    private LinearLayout appointmentsContainer;
    private ExecutorService executorService;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        bookingButton = findViewById(R.id.bookingButton);
        appointmentsContainer = findViewById(R.id.appointmentContainer);
        executorService = Executors.newSingleThreadExecutor();
        ImageView imageView = findViewById(R.id.imageView);

        appointmentsContainer.removeAllViews();

        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultasActivity.this, SpecialitiesActivity.class);
                startActivity(intent);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultasActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        fetchAppointments();
    }

    private void fetchAppointments() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                AppointmentsClient client = new AppointmentsClient(ConsultasActivity.this);
                final List<AppointmentDisplayDTO> appointments = client.getAppointments();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayAppointments(appointments, client);
                    }
                });
            }
        });
    }

    private void displayAppointments(List<AppointmentDisplayDTO> appointments, AppointmentsClient client) {
        LayoutInflater inflater = LayoutInflater.from(this);
        if (appointments == null) {
            return;
        }
        appointmentsContainer.removeAllViews();
        Log.d("ConsultaRetornada", "Appointments size: " + appointments.size());
        for (AppointmentDisplayDTO appointment : appointments) {
            Log.d("ConsultaRetornada", "Specialty: " + appointment.getSpecialtyName());
            View appointmentView = inflater.inflate(R.layout.component_consulta, appointmentsContainer, false);

            TextView textViewSpecialty = appointmentView.findViewById(R.id.textViewSpecialty);
            TextView textViewDate = appointmentView.findViewById(R.id.textViewDate);
            ImageButton cancelButton = appointmentView.findViewById(R.id.buttonCancel);
            ImageButton rescheduleButton = appointmentView.findViewById(R.id.buttonReschedule);

            textViewSpecialty.setText(appointment.getSpecialtyName());
            textViewDate.setText(appointment.getAppointmentDateTime());

            if (isAppointmentCanceledOrMissed(appointment)) {
                cancelButton.setAlpha(.5f);
                rescheduleButton.setAlpha(.5f);
            } else {
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                boolean success = client.cancelAppointment(appointment.getId());
                                if (success) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            fetchAppointments();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }

            appointmentsContainer.addView(appointmentView);
        }
    }

    private boolean isAppointmentCanceledOrMissed(AppointmentDisplayDTO appointment) {
        return Objects.equals(appointment.getAppointmentStatus(), AppointmentStatus.CANCELED.getValue()) ||
                Objects.equals(appointment.getAppointmentStatus(), AppointmentStatus.MISSED.getValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}