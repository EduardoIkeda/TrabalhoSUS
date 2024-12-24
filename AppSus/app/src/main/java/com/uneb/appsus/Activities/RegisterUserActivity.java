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
import com.uneb.appsus.Utility.Tuple;

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
                String password = editTextPassword.getText().toString();
                String name = editTextName.getText().toString();
                String phone = editTextPhone.getText().toString();
                String cpf = editTextCPF.getText().toString();
                String susCard = editTextSUSCard.getText().toString();
                String email = editTextEmail.getText().toString();

                if(!isNameValid()){
                    Toast.makeText(RegisterUserActivity.this, "O nome deve ter entre 6 e 100 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isPhoneValid()){
                    Toast.makeText(RegisterUserActivity.this, "O telefone deve ter entre 10 e 13 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isPasswordValid() ){
                    Toast.makeText(RegisterUserActivity.this, "Senha inválida", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isCPFValid()){
                    Toast.makeText(RegisterUserActivity.this, "CPF inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isSUSCardValid()){
                    Toast.makeText(RegisterUserActivity.this, "Cartão SUS inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isEmailValid() ){
                    Toast.makeText(RegisterUserActivity.this, "Email inválido", Toast.LENGTH_SHORT).show();
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

    private boolean isNameValid(){
        String name = editTextName.getText().toString();
        if(!isTextMinSize(6, name)){
            return false;
        }

        if(!isTextMaxSize(100, name)){
            return false;
        }

        return true;
    }

    private boolean isPhoneValid(){
        String phone = editTextPhone.getText().toString();

        if(!isTextMinSize(10, phone)){
            return false;
        }

        if(!isTextMaxSize(13, phone)){
            return false;
        }

        return true;
    }

    private boolean isPasswordValid() {
        String password = editTextPassword.getText().toString();
        String passwordConfirmation = editTextPasswordConfirmation.getText().toString();

        if (!password.equals(passwordConfirmation)) {
            return false;
        }

        if (!isTextMinSize(6, password)) {
            return false;
        }

        if (!isTextMaxSize(100, password)) {
            return false;
        }

        return true;
    }

    private boolean isCPFValid(){
        String cpf = editTextCPF.getText().toString();

        if(cpf.length() != 11){
            return false;
        }

        return true;
    }

    private boolean isSUSCardValid(){
        String susCard = editTextSUSCard.getText().toString();

        if(susCard.length() != 15){
            return false;
        }

        return true;
    }

    private boolean isEmailValid(){
        String email = editTextEmail.getText().toString();

        if(!isTextMinSize(6, email)){
            return false;
        }

        if(!isTextMaxSize(100, email)){
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isTextMinSize(int min, String text){
        return text.length() >= min;
    }

    private boolean isTextMaxSize(int max, String text){
        return text.length() <= max;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
