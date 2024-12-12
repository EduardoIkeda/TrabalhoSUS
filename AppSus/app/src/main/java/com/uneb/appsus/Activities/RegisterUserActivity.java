package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.uneb.appsus.DTO.UserDTO;
import com.uneb.appsus.R;

public class RegisterUserActivity extends AppCompatActivity {
    private EditText editTextAddress;
    private EditText editTextPassword;
    private EditText editTextCPF;
    private EditText editTextPasswordConfirmation;
    private EditText editTextSUSCard;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.editTextCPF = findViewById(R.id.editTextCPF);
        this.editTextAddress = findViewById(R.id.editTextAddress);
        this.editTextPassword = findViewById(R.id.editTextPassword);
        this.editTextPasswordConfirmation = findViewById(R.id.editTextConfirmPassword);
        this.editTextSUSCard = findViewById(R.id.editTextNumeroCartaoSus);
        this.button = findViewById(R.id.buttonRegister);

        Intent intent = getIntent();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextPassword.getText().toString().equals(editTextPasswordConfirmation.getText().toString())){
                    Toast.makeText(RegisterUserActivity.this, "As senhas não conferem", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserDTO userDTO = new UserDTO();
                userDTO.setAddress(editTextAddress.getText().toString());
                userDTO.setPassword(editTextPassword.getText().toString());
                userDTO.setSUSCard(editTextSUSCard.getText().toString());
                userDTO.setCPF(editTextCPF.getText().toString());

                //todo: implementar a chamada para o serviço de cadastro de usuário
                finish();
            }
        });
    }
}
