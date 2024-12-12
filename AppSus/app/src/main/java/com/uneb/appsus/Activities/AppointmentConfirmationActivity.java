package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.MainActivity;
import com.uneb.appsus.R;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    private TextView textViewDoctorName;
    private TextView textViewHour;
    private TextView textViewHealthCenter;
    private Button confirmButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao);

        String doctorName = getIntent().getStringExtra("doctorName");
        String hour = getIntent().getStringExtra("hour");
        String healthCenter = getIntent().getStringExtra("healthCenter");

        textViewDoctorName = findViewById(R.id.textViewMedico);
        textViewHour = findViewById(R.id.textViewDataHora);
        textViewHealthCenter = findViewById(R.id.textViewPostoSaude);
        confirmButton = findViewById(R.id.buttonConfirm);

        textViewDoctorName.setText(doctorName);
        textViewHour.setText(hour);
        textViewHealthCenter.setText(healthCenter);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentConfirmationActivity.this, ConsultasActivity.class);
                startActivity(intent);
            }
        });



    }
}
