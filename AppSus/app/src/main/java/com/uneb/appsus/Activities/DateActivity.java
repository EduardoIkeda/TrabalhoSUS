package com.uneb.appsus.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.Client.AppointmentsClient;
import com.uneb.appsus.DTO.AppointmentByDateDTO;
import com.uneb.appsus.DTO.HealthCenterDTO;
import com.uneb.appsus.DTO.SpecialitiesDTO;
import com.uneb.appsus.R;
import com.uneb.appsus.Utility.ToolbarBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private AppointmentsClient appointmentsClient;
    private SpecialitiesDTO speciality;
    private HealthCenterDTO healthCenter;
    private String[] dates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uneb.appsus.R.layout.activity_date);

        Button button = findViewById(R.id.nextButton);

        TextView dateText = findViewById(R.id.dateTextView);
        Button dateButton = findViewById(R.id.dateButton);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        // Configurar Toolbar
        new ToolbarBuilder(this, toolbar)
                .withTitle(getString(R.string.agendamento))
                .withReturnButton()
                .build();

        speciality = (SpecialitiesDTO) getIntent().getSerializableExtra("speciality");
        healthCenter = (HealthCenterDTO) getIntent().getSerializableExtra("healthCenter");

        executorService = Executors.newSingleThreadExecutor();
        appointmentsClient = new AppointmentsClient(DateActivity.this);

        button.setEnabled(false);

        executorService.execute(() -> {
            List<AppointmentByDateDTO> appointments = appointmentsClient
                    .getAppointmentsBySpecialtyAndHealthCenter(speciality.getId(), healthCenter.getId());
            dates = new String[appointments.size()];

            for (int i = 0; i < appointments.size(); i++) {
                dates[i] = appointments.get(i).getDate();
                Log.d("DateActivity", "Date: " + dates[i]);
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(button);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DateActivity.this, HourActivity.class);
                intent.putExtra("healthCenter", healthCenter);
                intent.putExtra("speciality", speciality);
                intent.putExtra("date", dateText.getText());

                startActivity(intent);
            }
        });
    }

    private void openDatePicker(Button button) {
        TextView textView = findViewById(R.id.dateTextView);

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date = dateFormat.format(selectedDate.getTime());
                textView.setText(date);

                // Verificar disponibilidade da data
                if (isDateAvailable(date)) {
                    button.setEnabled(true); // Habilita o botão se a data for válida
                    Toast.makeText(DateActivity.this, "Data disponível: " + date, Toast.LENGTH_SHORT).show();
                } else {
                    button.setEnabled(false); // Desabilita o botão se a data não for válida
                    Toast.makeText(DateActivity.this, "Data não disponível: " + date, Toast.LENGTH_SHORT).show();
                }
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datepicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datepicker.show();
    }

    private boolean isDateAvailable(String date) {
        for (String availableDate : dates) {
            Log.d("DateActivity", "Available Date: " + availableDate + " Date: " + date);
            if (availableDate.equals(date)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
