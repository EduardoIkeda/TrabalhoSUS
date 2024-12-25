package com.uneb.appsus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uneb.appsus.Client.UserClient;
import com.uneb.appsus.DTO.UserDTO;
import com.uneb.appsus.Manager.TokenManager;
import com.uneb.appsus.R;
import com.uneb.appsus.Utility.ToolbarBuilder;
import com.uneb.appsus.Utility.Tuple;
import com.uneb.appsus.Utility.Validator;

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
        executorService = Executors.newSingleThreadExecutor();

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        new ToolbarBuilder(this, toolbar)
                .withTitle(getString(R.string.cadastro))
                .withReturnButton()
                .build();

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
                String password = editTextPassword.getText().toString();
                String passwordConfirmation = editTextPasswordConfirmation.getText().toString();
                String name = editTextName.getText().toString();
                String phone = editTextPhone.getText().toString();
                String cpf = editTextCPF.getText().toString();
                String susCard = editTextSUSCard.getText().toString();
                String email = editTextEmail.getText().toString();

                if(!Validator.isNameValid(name)){
                    Toast.makeText(RegisterUserActivity.this, getString(R.string.nome_validacao), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Validator.isPhoneValid(phone)){
                    Toast.makeText(RegisterUserActivity.this, getString(R.string.phone_validacao), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Validator.isPasswordValid(password, passwordConfirmation) ){
                    Toast.makeText(RegisterUserActivity.this, getString(R.string.senha_validacao), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Validator.isCPFValid(cpf)){
                    Toast.makeText(RegisterUserActivity.this, getString(R.string.cpf_validacao), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Validator.isSUSCardValid(susCard)){
                    Toast.makeText(RegisterUserActivity.this, getString(R.string.sus_validacao), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Validator.isEmailValid(email) ){
                    Toast.makeText(RegisterUserActivity.this, getString(R.string.email_validacao), Toast.LENGTH_SHORT).show();
                    return;
                }

                UserDTO userDTO = new UserDTO();
                userDTO.setName(name);
                userDTO.setPhone(phone);
                userDTO.setPassword(password);
                userDTO.setSusCardNumber(susCard);
                userDTO.setCpf(cpf);
                userDTO.setEmail(email);

                executorService.submit(() -> {
                    UserClient userClient = new UserClient(RegisterUserActivity.this);
                    Tuple<String,String> tokenAndUserId = userClient.registerUser(userDTO);
                    String token = tokenAndUserId.first;
                    String userId = tokenAndUserId.second;

                    runOnUiThread(() -> {
                        if (token != null) {
                            TokenManager.getInstance(RegisterUserActivity.this).setBearerToken(token);
                            TokenManager.getInstance(RegisterUserActivity.this).setUserId(userId);
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
