package com.uneb.appsus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.uneb.appsus.Activities.ConsultasActivity;
import com.uneb.appsus.Activities.RegisterUserActivity;
import com.uneb.appsus.Client.UserClient;
import com.uneb.appsus.DTO.UserDTO;
import com.uneb.appsus.Manager.TokenManager;
import com.uneb.appsus.Utility.ToolbarBuilder;
import com.uneb.appsus.Utility.Tuple;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    protected TextView forgotPasswordText;
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
        forgotPasswordText = findViewById(R.id.textViewForgotPassword);
        registerButton = findViewById(R.id.buttonRegister);
        susNumberText = findViewById(R.id.editTextNumeroCartaoSus);
        passwordText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        new ToolbarBuilder(this, toolbar)
                .withTitle(getString(R.string.app_name))
                .build();

        configureForgotPassword();
        configureRegisterButton();
        configureLoginButton();
    }

    private void configureForgotPassword() {
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.google.com.br"));
            startActivity(browserIntent);
            }
        });
    }

    private void configureRegisterButton() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void configureLoginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String susNumber = susNumberText.getText().toString();
                String password = passwordText.getText().toString();

                UserDTO user = new UserDTO();
                user.setSusCardNumber(susNumber);
                user.setPassword(password);

                executorService.submit(() -> {
                    UserClient userClient = new UserClient(MainActivity.this);
                    Tuple<String,String> tokenAndUserId = userClient.loginUser(user);
                    String token = tokenAndUserId.first;
                    String userId = tokenAndUserId.second;

                    runOnUiThread(() -> {
                        if (token != null) {
                            TokenManager.getInstance(MainActivity.this).setBearerToken(token);
                            TokenManager.getInstance(MainActivity.this).setUserId(userId);
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