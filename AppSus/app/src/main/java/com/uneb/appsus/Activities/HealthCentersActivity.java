package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.Client.HealthCenterClient;
import com.uneb.appsus.DTO.HealthCenterDTO;
import com.uneb.appsus.DTO.SpecialitiesDTO;
import com.uneb.appsus.R;
import com.uneb.appsus.Utility.ToolbarBuilder;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HealthCentersActivity extends AppCompatActivity {

    private SpecialitiesDTO speciality;
    LinearLayout layout;
    private ExecutorService executorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posto_saude);
        speciality = (SpecialitiesDTO) getIntent().getSerializableExtra("speciality");
        executorService = Executors.newSingleThreadExecutor();
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        new ToolbarBuilder(this, toolbar)
                .withTitle(getString(R.string.agendamento))
                .withReturnButton()
                .build();

        layout = findViewById(R.id.linearLayout);
        layout.removeAllViews();

        fetchHealthCenters();
    }

    private void fetchHealthCenters() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HealthCenterClient client = new HealthCenterClient(HealthCentersActivity.this);
                List<HealthCenterDTO> healthCenters = client.getHealthCenterBySpeciality(speciality.getId());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayHealthCenters(healthCenters, layout);
                    }
                });
            }
        });
    }

    private void displayHealthCenters(List<HealthCenterDTO> healthCenters, LinearLayout layout) {
        for (HealthCenterDTO healthCenter : healthCenters) {
            View view = LayoutInflater.from(HealthCentersActivity.this).inflate(R.layout.component_posto_saude, layout, false);

            TextView textViewSpecialty = view.findViewById(R.id.textViewSpecialty);
            TextView textViewDate = view.findViewById(R.id.textViewDate);
            TextView textViewNumeroVagas = view.findViewById(R.id.textViewNumeroVagas);

            textViewSpecialty.setText(healthCenter.getName());
            textViewDate.setText(healthCenter.getAddress());
            textViewNumeroVagas.setText(String.valueOf(healthCenter.getAvailableAppointmentsCount()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HealthCentersActivity.this, DateActivity.class);
                    intent.putExtra("healthCenter", healthCenter);
                    intent.putExtra("speciality", speciality);
                    startActivity(intent);
                }
            });
            layout.addView(view);
        }
    }
}