package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.uneb.appsus.Client.UserClient;
import com.uneb.appsus.DTO.UserDTO;
import com.uneb.appsus.Manager.TokenManager;
import com.uneb.appsus.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterUserActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextCPF;
    private EditText editTextPhone;
    private EditText editTextPasswordConfirmation;
    private EditText editTextSUSCard;
    private EditText editTextEmail;
    private Button button;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        executorService = Executors.newSingleThreadExecutor();

        this.editTextCPF = findViewById(R.id.editTextCPF);
        this.editTextName = findViewById(R.id.editTextName);
        this.editTextPhone = findViewById(R.id.editTextPhone);
        this.editTextPassword = findViewById(R.id.editTextPassword);
        this.editTextPasswordConfirmation = findViewById(R.id.editTextConfirmPassword);
        this.editTextSUSCard = findViewById(R.id.editTextNumeroCartaoSus);
        this.editTextEmail = findViewById(R.id.editTextEmail);
        this.button = findViewById(R.id.buttonRegister);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextPassword.getText().toString().equals(editTextPasswordConfirmation.getText().toString())){
                    Toast.makeText(RegisterUserActivity.this, "As senhas não conferem", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserDTO userDTO = new UserDTO();
                userDTO.setName(editTextName.getText().toString());
                userDTO.setPhone(editTextPhone.getText().toString());
                userDTO.setPassword(editTextPassword.getText().toString());
                userDTO.setSusCardNumber(editTextSUSCard.getText().toString());
                userDTO.setCpf(editTextCPF.getText().toString());
                userDTO.setEmail(editTextEmail.getText().toString());

                executorService.submit(() -> {
                    UserClient userClient = new UserClient(RegisterUserActivity.this);
                    String token = userClient.registerUser(userDTO);

                    runOnUiThread(() -> {
                        if (token != null) {
                            TokenManager.getInstance(RegisterUserActivity.this).setBearerToken(token);
                            Intent intent = new Intent(RegisterUserActivity.this, ConsultasActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(RegisterUserActivity.this, "Algo deu errado no cadastro", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
