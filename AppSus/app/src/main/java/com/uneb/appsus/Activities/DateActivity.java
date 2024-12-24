package com.uneb.appsus.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.DTO.DoctorDTO;
import com.uneb.appsus.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uneb.appsus.R.layout.activity_date);

        Button button = findViewById(R.id.nextButton);
        TextView dateText = findViewById(R.id.dateTextView);
        Button datebutton = findViewById(R.id.dateButton);

        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DateActivity.this, HourActivity.class);
                intent.putExtra("healthCenter", getIntent().getSerializableExtra("healthCenter"));
                intent.putExtra("speciality", getIntent().getSerializableExtra("speciality"));
                intent.putExtra("date", dateText.getText());

                startActivity(intent);
            }
        });
    }

    private void openDatePicker(){
        TextView textView = findViewById(R.id.dateTextView);

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                textView.setText(dateFormat.format(selectedDate.getTime()));
            }
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH));

        datepicker.getDatePicker().setMinDate(System.currentTimeMillis());

        datepicker.show();
    }
}
