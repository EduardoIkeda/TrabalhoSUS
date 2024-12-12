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

public class SpecialitiesActivity extends AppCompatActivity {

    protected LinearLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialidade);

        SpecialitiesClient specialitiesClient = new SpecialitiesClient(this);
        List<SpecialitiesDTO> specialities = specialitiesClient.GetSpecialities();

        layout = findViewById(R.id.linearLayout);

        for (SpecialitiesDTO specialityDTO : specialities) {
            View view = LayoutInflater.from(this).inflate(R.layout.component_especialidade, layout, false);
            AppCompatButton button = view.findViewById(R.id.buttonEspecialidade);
            button.setText(specialityDTO.getName());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SpecialitiesActivity.this, HealthCentersActivity.class);
                    intent.putExtra("speciality", specialityDTO.getName());
                    startActivity(intent);
                }
            });

            layout.addView(view);
        }
    }
}