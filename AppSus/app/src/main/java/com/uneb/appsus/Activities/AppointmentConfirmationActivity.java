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
import com.uneb.appsus.DTO.DoctorDTO;
import com.uneb.appsus.DTO.HealthCenterDTO;
import com.uneb.appsus.DTO.SpecialitiesDTO;
import com.uneb.appsus.MainActivity;
import com.uneb.appsus.R;
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
    private DoctorDTO doctor;
    private String date;
    private String hour;
    private String appointmentStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao);

        doctor = (DoctorDTO) getIntent().getSerializableExtra("doctorName");
        healthCenter = (HealthCenterDTO) getIntent().getSerializableExtra("healthCenter");
        speciality = (SpecialitiesDTO) getIntent().getSerializableExtra("speciality");
        date = getIntent().getStringExtra("date");
        hour = getIntent().getStringExtra("hour");
        appointmentStatus = AppointmentStatus.SCHEDULED.getValue();

        textViewDoctorName = findViewById(R.id.textViewMedico);
        textViewHour = findViewById(R.id.textViewDataHora);
        textViewHealthCenter = findViewById(R.id.textViewPostoSaude);
        textViewSpecialty = findViewById(R.id.textViewEspecialidade);
        confirmButton = findViewById(R.id.buttonConfirm);

        textViewDoctorName.setText(String.format("Dr. %s", doctor != null ? doctor.getName() : "Doutor"));
        textViewHour.setText(String.format("%s %s", date, hour));
        textViewHealthCenter.setText(healthCenter != null ? healthCenter.getName() : "PostoSaude");
        textViewSpecialty.setText(speciality != null ? speciality.getName() : "Especialidade");

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Future<AppointmentsClient.AppointmentResult> futureResult = getAppointmentResultFuture();

                try {
                    AppointmentsClient.AppointmentResult result = futureResult.get();
                    if (result.isSuccess()) {
                        Toast.makeText(AppointmentConfirmationActivity.this, "Consulta criada com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AppointmentConfirmationActivity.this, "Falha ao criar a Consulta!", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(AppointmentConfirmationActivity.this, ConsultasActivity.class);
                startActivity(intent);
            }

            private Future<AppointmentsClient.AppointmentResult> getAppointmentResultFuture() {
                AppointmentDTO newAppointment = new AppointmentDTO();
                newAppointment.setDoctorId(doctor.getId());
                newAppointment.setHealthCenterId(healthCenter.getId());
                newAppointment.setSpecialtyId(speciality.getId());
                newAppointment.setPatientId((long)1);
                newAppointment.setAppointmentStatus(appointmentStatus);

                // Formato aceito pelo backend no json é "20/08/2023 10:00"
                newAppointment.setAppointmentDateTime(date + " às " + hour);

                AppointmentsClient appointmentsClient = new AppointmentsClient(AppointmentConfirmationActivity.this);
                appointmentsClient.createAppointment(newAppointment);

                return appointmentsClient.createAppointment(newAppointment);
            }
        });



    }
}
