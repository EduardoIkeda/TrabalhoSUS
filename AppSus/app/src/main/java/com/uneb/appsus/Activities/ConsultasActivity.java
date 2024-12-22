package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.Client.AppointmentsClient;
import com.uneb.appsus.DTO.AppointmentDTO;
import com.uneb.appsus.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConsultasActivity extends AppCompatActivity {

    private Button bookingButton;
    private LinearLayout appointmentsContainer;
    private ExecutorService executorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        bookingButton = findViewById(R.id.bookingButton);
        appointmentsContainer = findViewById(R.id.appointmentContainer);
        executorService = Executors.newSingleThreadExecutor();

        appointmentsContainer.removeAllViews();

        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultasActivity.this, SpecialitiesActivity.class);
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
                final List<AppointmentDTO> appointments = client.getAppointments();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayAppointments(appointments);
                    }
                });
            }
        });
    }

    private void displayAppointments(List<AppointmentDTO> appointments) {
        LayoutInflater inflater = LayoutInflater.from(this);
        if (appointments == null) {
            return;
        }
        for (AppointmentDTO appointment : appointments) {
            View appointmentView = inflater.inflate(R.layout.component_consulta, appointmentsContainer, false);

            TextView textViewSpecialty = appointmentView.findViewById(R.id.textViewSpecialty);
            TextView textViewDate = appointmentView.findViewById(R.id.textViewDate);

            textViewSpecialty.setText(appointment.getAppointmentStatus() + " " + appointment.getId()); // Replace with actual specialty name
            textViewDate.setText(appointment.getAppointmentDateTime());

            appointmentsContainer.addView(appointmentView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}