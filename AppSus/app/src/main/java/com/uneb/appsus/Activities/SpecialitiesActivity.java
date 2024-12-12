package com.uneb.appsus.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.uneb.appsus.DTO.SpecialitiesDTO;
import com.uneb.appsus.R;

import java.util.ArrayList;
import java.util.List;

public class SpecialitiesActivity extends AppCompatActivity {

    protected LinearLayout layout;
    protected ArrayAdapter<SpecialitiesDTO> adapter;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialidade);

        SpecialitiesDTO speciality = new SpecialitiesDTO();
        speciality.setId(Long.valueOf(1));
        speciality.setName("Nova especialidade1 ");
        speciality.setDescription("Nova descrição1");
        SpecialitiesDTO speciality2 = new SpecialitiesDTO();
        speciality2.setId(Long.valueOf(2));
        speciality2.setName("Nova especialidade2 ");
        speciality2.setDescription("Nova descrição2");


        ArrayList<SpecialitiesDTO> specialities = (ArrayList<SpecialitiesDTO>) new ArrayList();
        specialities.add(speciality);
        specialities.add(speciality2);

        adapter = new ArrayAdapter<SpecialitiesDTO>(this, android.R.layout.simple_list_item_1, specialities);
        layout = (LinearLayout) findViewById(R.id.linearLayout);

        for (SpecialitiesDTO specialityDTO : specialities) {
            AppCompatButton button = new AppCompatButton(this);
            button.setText(specialityDTO.getName());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SpecialitiesActivity.this, HealthCentersActivity.class);
                    intent.putExtra("speciality", specialityDTO.getId().toString());
                    startActivity(intent);
                }
            });

            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            layout.addView(button);

        }


    }
}
