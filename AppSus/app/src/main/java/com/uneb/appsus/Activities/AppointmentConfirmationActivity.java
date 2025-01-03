package com.uneb.appsus.Activities;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.Client.AppointmentsClient;
import com.uneb.appsus.DTO.AppointmentDTO;
import com.uneb.appsus.DTO.DoctorAppointment;
import com.uneb.appsus.DTO.DoctorDTO;
import com.uneb.appsus.DTO.HealthCenterDTO;
import com.uneb.appsus.DTO.SpecialitiesDTO;
import com.uneb.appsus.MainActivity;
import com.uneb.appsus.R;
import com.uneb.appsus.Utility.ToolbarBuilder;
import com.uneb.appsus.enums.AppointmentStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    private TextView textViewDoctorName;
    private TextView textViewHour;
    private TextView textViewHealthCenter;
    private TextView textViewSpecialty;
    private Button confirmButton;

    private SpecialitiesDTO speciality;
    private HealthCenterDTO healthCenter;
    private String date;
    private String hour;

    private DoctorAppointment doctorAppointment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao);

        healthCenter = (HealthCenterDTO) getIntent().getSerializableExtra("healthCenter");
        speciality = (SpecialitiesDTO) getIntent().getSerializableExtra("speciality");
        date = getIntent().getStringExtra("date");
        hour = getIntent().getStringExtra("hour");
        doctorAppointment = (DoctorAppointment) getIntent().getSerializableExtra("appointment");

        if (doctorAppointment == null) {
            Toast.makeText(this, "Erro: Dados do agendamento não encontrados.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            // Debug log to check doctorAppointment values
            System.out.println("Doctor Name: " + doctorAppointment.getDoctorName());
            // Add more fields if necessary
        }

        textViewDoctorName = findViewById(R.id.textViewMedico);
        textViewHour = findViewById(R.id.textViewDataHora);
        textViewHealthCenter = findViewById(R.id.textViewPostoSaude);
        textViewSpecialty = findViewById(R.id.textViewEspecialidade);
        confirmButton = findViewById(R.id.buttonConfirm);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        new ToolbarBuilder(this, toolbar)
                .withTitle(getString(R.string.agendamento))
                .withReturnButton()
                .build();

        if (textViewDoctorName != null) {
            textViewDoctorName.setText(doctorAppointment.getDoctorName());
        }
        if (textViewHour != null) {
            textViewHour.setText(String.format("%s %s", date, hour));
        }
        if (textViewHealthCenter != null) {
            textViewHealthCenter.setText(healthCenter != null ? healthCenter.getName() : "PostoSaude");
        }
        if (textViewSpecialty != null) {
            textViewSpecialty.setText(speciality != null ? speciality.getName() : "Especialidade");
        }

         confirmButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Future<AppointmentsClient.AppointmentResult> futureResult = getAppointmentResultFuture(doctorAppointment);

                 try {
                     AppointmentsClient.AppointmentResult result = futureResult.get();
                     if (result.isSuccess()) {
                         Toast.makeText(AppointmentConfirmationActivity.this, "Consulta criada com sucesso!",
                                 Toast.LENGTH_SHORT).show();
                     } else {
                         Toast.makeText(AppointmentConfirmationActivity.this, "Falha ao criar a Consulta!",
                                 Toast.LENGTH_SHORT).show();
                     }
                 } catch (InterruptedException | ExecutionException e) {
                     e.printStackTrace();
                 }

                 Intent intent = new Intent(AppointmentConfirmationActivity.this, ConsultasActivity.class);
                 startActivity(intent);
             }
         });

    }

    private Future<AppointmentsClient.AppointmentResult> getAppointmentResultFuture(
            DoctorAppointment doctorAppointment) {
        AppointmentsClient appointmentsClient = new AppointmentsClient(AppointmentConfirmationActivity.this);
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(Long.parseLong(doctorAppointment.getId()));
        appointmentDTO.setAppointmentDateTime(doctorAppointment.getAppointmentDateTime());
        appointmentDTO.setAppointmentStatus(doctorAppointment.getAppointmentStatus());

        return appointmentsClient.createAppointment(appointmentDTO);
    }
}
