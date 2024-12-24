package com.uneb.appsus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.Activities.ConsultasActivity;
import com.uneb.appsus.Activities.RegisterUserActivity;
import com.uneb.appsus.Client.UserClient;
import com.uneb.appsus.DTO.UserDTO;
import com.uneb.appsus.Manager.TokenManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    protected EditText susNumberText;
    protected EditText passwordText;
    protected Button registerButton;
    protected Button loginButton;
    private ExecutorService executorService;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        executorService = Executors.newSingleThreadExecutor();

        registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                susNumberText = findViewById(R.id.editTextNumeroCartaoSus);
                passwordText = findViewById(R.id.editTextPassword);
                String susNumber = susNumberText.getText().toString();
                String password = passwordText.getText().toString();

                UserDTO user = new UserDTO();
                user.setSusCardNumber(susNumber);
                user.setPassword(password);

                executorService.submit(() -> {
                    UserClient userClient = new UserClient(MainActivity.this);
                    String token = userClient.loginUser(user);

                    runOnUiThread(() -> {
                        if (token != null) {
                            TokenManager.getInstance(MainActivity.this).setBearerToken(token);
                            Intent intent = new Intent(MainActivity.this, ConsultasActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}