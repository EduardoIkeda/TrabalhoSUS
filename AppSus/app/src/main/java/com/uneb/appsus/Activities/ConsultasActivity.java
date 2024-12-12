package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.R;

public class ConsultasActivity extends AppCompatActivity {

    private Button bookingButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        bookingButton = (Button) findViewById(R.id.bookingButton);

        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultasActivity.this, SpecialitiesActivity.class);
                startActivity(intent);
            }
        });
    }
}
