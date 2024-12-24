package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.uneb.appsus.DTO.DoctorDTO;
import com.uneb.appsus.R;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HourActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        LinearLayout layout = findViewById(R.id.linearLayout);

        List<DoctorDTO> doctors = getDoctors();

        for (DoctorDTO doctor : doctors) {
            View view = LayoutInflater.from(this).inflate(R.layout.component_horario, layout, false);
            TextView textViewSpecialty = view.findViewById(R.id.textViewSpecialty);
            LinearLayout hoursLayout = view.findViewById(R.id.hoursLayout);

            textViewSpecialty.setText("Dr." + doctor.getName() + " - " + doctor.getCrm());

            LocalTime start = doctor.getStartWork();
            LocalTime end = doctor.getEndWork();
            while (start.isBefore(end)) {
                AppCompatButton button = new AppCompatButton(this);
                button.setText(start.toString());
                button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                hoursLayout.addView(button);
                start = start.plusHours(1);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HourActivity.this, AppointmentConfirmationActivity.class);
                        String healthCenter = getIntent().getStringExtra("healthCenter");
                        intent.putExtra("healthCenter", healthCenter);
                        intent.putExtra("speciality", getIntent().getStringExtra("speciality"));
                        String date = getIntent().getStringExtra("date");

                        intent.putExtra("doctorName", doctor.getName());
                        intent.putExtra("hour", button.getText());
                        intent.putExtra("date", date);


                        startActivity(intent);
                    }
                });
            }

            layout.addView(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<DoctorDTO> getDoctors() {
        List<DoctorDTO> doctors = new ArrayList<>();

        DoctorDTO doctor1 = new DoctorDTO();
        doctor1.setId(1L);
        doctor1.setName("Pedro");
        doctor1.setCrm("CRM12345");
        doctor1.setStartWork(LocalTime.of(8, 0));
        doctor1.setEndWork(LocalTime.of(17, 0));
        Set<DayOfWeek> workingDays1 = new HashSet<>();
        workingDays1.add(DayOfWeek.MONDAY);
        workingDays1.add(DayOfWeek.WEDNESDAY);
        doctor1.setWorkingDays(workingDays1);

        DoctorDTO doctor2 = new DoctorDTO();
        doctor2.setId(2L);
        doctor2.setName("Mauro");
        doctor2.setCrm("CRM67890");
        doctor2.setStartWork(LocalTime.of(9, 0));
        doctor2.setEndWork(LocalTime.of(18, 0));
        Set<DayOfWeek> workingDays2 = new HashSet<>();
        workingDays2.add(DayOfWeek.TUESDAY);
        workingDays2.add(DayOfWeek.THURSDAY);
        doctor2.setWorkingDays(workingDays2);

        doctors.add(doctor1);
        doctors.add(doctor2);

        return doctors;
    }
}