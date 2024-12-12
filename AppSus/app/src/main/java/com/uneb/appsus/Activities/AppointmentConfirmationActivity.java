package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.DTO.AppointmentDTO;
import com.uneb.appsus.MainActivity;
import com.uneb.appsus.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    private TextView textViewDoctorName;
    private TextView textViewHour;
    private TextView textViewHealthCenter;
    private TextView textViewSpecialty;
    private Button confirmButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao);

        String doctorName = getIntent().getStringExtra("doctorName");
        String hour = getIntent().getStringExtra("hour");
        String healthCenter = getIntent().getStringExtra("healthCenter");
        String speciality = getIntent().getStringExtra("speciality");

        textViewDoctorName = findViewById(R.id.textViewMedico);
        textViewHour = findViewById(R.id.textViewDataHora);
        textViewHealthCenter = findViewById(R.id.textViewPostoSaude);
        textViewSpecialty = findViewById(R.id.textViewEspecialidade);
        confirmButton = findViewById(R.id.buttonConfirm);

        textViewDoctorName.setText(doctorName);
        textViewHour.setText(hour);
        textViewHealthCenter.setText(healthCenter);
        textViewSpecialty.setText(speciality);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentDTO newAppointment = new AppointmentDTO();
                newAppointment.setAppointmentStatus("scheduled");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String formattedDate = formatter.format(date);
                newAppointment.setAppointmentDateTime(formattedDate + hour);
                Intent intent = new Intent(AppointmentConfirmationActivity.this, ConsultasActivity.class);
                startActivity(intent);
            }
        });



    }
}
