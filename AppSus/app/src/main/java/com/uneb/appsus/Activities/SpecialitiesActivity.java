package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.uneb.appsus.Client.SpecialitiesClient;
import com.uneb.appsus.DTO.SpecialitiesDTO;
import com.uneb.appsus.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpecialitiesActivity extends AppCompatActivity {

    protected LinearLayout layout;
    private ExecutorService executorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialidade);
        executorService = Executors.newSingleThreadExecutor();
        layout = findViewById(R.id.linearLayout);
        fetchAppointments();
    }

    private void fetchAppointments() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                    SpecialitiesClient specialitiesClient = new SpecialitiesClient (SpecialitiesActivity.this);
                List<SpecialitiesDTO> specialities = specialitiesClient.getSpecialities();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displaySpecialties(specialities);
                    }
                });
            }
        });
    }

    private void displaySpecialties(List<SpecialitiesDTO> specialities) {
        for (SpecialitiesDTO specialityDTO : specialities) {
            View view = LayoutInflater.from(this).inflate(R.layout.component_especialidade, layout, false);
            AppCompatButton button = view.findViewById(R.id.buttonEspecialidade);
            button.setText(specialityDTO.getName());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SpecialitiesActivity.this, HealthCentersActivity.class);
                    intent.putExtra("speciality", specialityDTO);
                    startActivity(intent);
                }
            });

            layout.addView(view);
        }
    }
}