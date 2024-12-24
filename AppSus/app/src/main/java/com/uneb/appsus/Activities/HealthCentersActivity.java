package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.DTO.HealthCenterDTO;
import com.uneb.appsus.R;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HealthCentersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posto_saude);

        LinearLayout layout = findViewById(R.id.linearLayout);

        List<HealthCenterDTO> healthCenters = getHealthCenters();

        for (HealthCenterDTO healthCenter : healthCenters) {
            View view = LayoutInflater.from(this).inflate(R.layout.component_posto_saude, layout, false);

            TextView textViewSpecialty = view.findViewById(R.id.textViewSpecialty);
            TextView textViewDate = view.findViewById(R.id.textViewDate);
            TextView textViewNumeroVagas = view.findViewById(R.id.textViewNumeroVagas);

            textViewSpecialty.setText(healthCenter.getName());
            textViewDate.setText(healthCenter.getAddress());
            textViewNumeroVagas.setText("50"); // Example value, replace with actual data if available

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HealthCentersActivity.this, DateActivity.class);
                    intent.putExtra("healthCenter", healthCenter);
                    intent.putExtra("speciality", getIntent().getSerializableExtra("speciality"));
                    startActivity(intent);
                }
            });
            layout.addView(view);
        }
    }

    private List<HealthCenterDTO> getHealthCenters() {
        // todo: receive health care list from API
        List<HealthCenterDTO> healthCenters = new ArrayList<>();

        HealthCenterDTO healthCenter1 = new HealthCenterDTO();
        healthCenter1.setId(1L);
        healthCenter1.setName("Health Center 1");
        healthCenter1.setAddress("123 Main St");
        healthCenter1.setOpeningHour(LocalTime.of(8, 0));
        healthCenter1.setClosingHour(LocalTime.of(17, 0));

        HealthCenterDTO healthCenter2 = new HealthCenterDTO();
        healthCenter2.setId(2L);
        healthCenter2.setName("Health Center 2");
        healthCenter2.setAddress("456 Elm St");
        healthCenter2.setOpeningHour(LocalTime.of(9, 0));
        healthCenter2.setClosingHour(LocalTime.of(18, 0));

        healthCenters.add(healthCenter1);
        healthCenters.add(healthCenter2);

        return healthCenters;
    }
}