package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.uneb.appsus.Client.AppointmentsClient;
import com.uneb.appsus.DTO.AppointmentByDateDTO;
import com.uneb.appsus.DTO.DoctorAppointment;
import com.uneb.appsus.DTO.DoctorDTO;
import com.uneb.appsus.DTO.HealthCenterDTO;
import com.uneb.appsus.DTO.SpecialitiesDTO;
import com.uneb.appsus.R;
import com.uneb.appsus.Utility.ToolbarBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HourActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private AppointmentsClient appointmentsClient;
    private SpecialitiesDTO speciality;
    private HealthCenterDTO healthCenter;
    private String date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        LinearLayout layout = findViewById(R.id.linearLayout);
        executorService = Executors.newSingleThreadExecutor();
        appointmentsClient = new AppointmentsClient(HourActivity.this);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        new ToolbarBuilder(this, toolbar)
                .withTitle(getString(R.string.agendamento))
                .withReturnButton()
                .build();

        speciality = (SpecialitiesDTO) getIntent().getSerializableExtra("speciality");
        healthCenter = (HealthCenterDTO) getIntent().getSerializableExtra("healthCenter");
        date = getIntent().getStringExtra("date");

        executorService.execute(() -> {
            List<AppointmentByDateDTO> appointments = appointmentsClient
                    .getAppointmentsBySpecialtyAndHealthCenter(speciality.getId(), healthCenter.getId());
            runOnUiThread(() -> displayAppointments(appointments, layout));
        });
    }

    private void displayAppointments(List<AppointmentByDateDTO> appointments, LinearLayout layout) {
        AppointmentByDateDTO appointment = getAppointmentFromDate(appointments, date);
        if (appointment == null) {
            return;
        }

        List<DoctorDTO> doctors = appointment.getDoctorList();

        for (DoctorDTO doctorDTO : doctors) {
            List<DoctorAppointment> doctorAppointments = doctorDTO.getAppointments();

            if (doctorAppointments == null || doctorAppointments.isEmpty()) {
                continue;
            }

            View view = LayoutInflater.from(this).inflate(R.layout.component_horario, layout, false);
            TextView doctorTextView = view.findViewById(R.id.doctorTextView);
            LinearLayout hoursLayout = view.findViewById(R.id.hoursLayout);

            for (DoctorAppointment doctorAppointment : doctorAppointments) {
                doctorTextView.setText(String.format("Dr. %s", doctorAppointment.getDoctorName()));
                String time = doctorAppointment.getTime();

                if (!isDoctorTimeValid(doctorDTO, time)) {
                    continue;
                }

                AppCompatButton button = new AppCompatButton(this);

                button.setBackground( ResourcesCompat.getDrawable( getResources(), R.drawable.rounded_background, null));
                button.setBackgroundColor(getResources().getColor(R.color.white));
                button.setPadding(2, 0, 2, 0);
                button.setTextSize(16);

                button.setText(time);
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                hoursLayout.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HourActivity.this, AppointmentConfirmationActivity.class);
                        intent.putExtra("healthCenter", getIntent().getSerializableExtra("healthCenter"));
                        intent.putExtra("speciality", getIntent().getSerializableExtra("speciality"));
                        intent.putExtra("date", date);
                        intent.putExtra("appointment", doctorAppointment);

                        intent.putExtra("hour", button.getText());

                        startActivity(intent);
                    }
                });
            }
            layout.addView(view);
        }
    }

    private boolean isDoctorTimeValid(DoctorDTO doctorDTO, String hour) {
        String startTime = doctorDTO.getStartWork();
        String endTime = doctorDTO.getEndWork();
        Log.d("HourActivity", "start: " + startTime + "end: " + endTime + "hour: " + hour);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            Date appointmentTime = sdf.parse(hour);
    
            if (appointmentTime == null || start == null || end == null || appointmentTime.before(start) || appointmentTime.after(end)) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    
        return true;
    }

    private AppointmentByDateDTO getAppointmentFromDate(List<AppointmentByDateDTO> appointments, String date) {
        for (AppointmentByDateDTO appointment : appointments) {
            if (appointment.getDate().equals(date)) {
                return appointment;
            }
        }
        return null;
    }
}