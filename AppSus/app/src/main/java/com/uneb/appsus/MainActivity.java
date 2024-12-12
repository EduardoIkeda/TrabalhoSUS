package com.uneb.appsus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uneb.appsus.Activities.ConsultasActivity;
import com.uneb.appsus.Activities.RegisterUserActivity;

public class MainActivity extends AppCompatActivity {

    protected EditText susNumberText;
    protected EditText passwordText;
    protected Button registerButton;
    protected Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        registerButton = (Button) findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

        loginButton = (Button) findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                susNumberText = (EditText) findViewById(R.id.editTextNumeroCartaoSus);
                passwordText = (EditText) findViewById(R.id.editTextPassword);
                String susNumber = susNumberText.getText().toString();
                String password = passwordText.getText().toString();
                if (susNumber.equals("123456") && password.equals("123456")) {
                    Intent intent = new Intent(MainActivity.this, ConsultasActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}