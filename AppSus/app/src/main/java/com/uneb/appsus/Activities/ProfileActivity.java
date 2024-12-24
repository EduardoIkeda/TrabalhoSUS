package com.uneb.appsus.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.w3c.dom.Text;

import com.uneb.appsus.Client.UserClient;
import com.uneb.appsus.DTO.UserDTO;
import com.uneb.appsus.DTO.UserPartialDTO;
import com.uneb.appsus.R;
import com.uneb.appsus.Utility.Validator;

public class ProfileActivity extends AppCompatActivity {

    private ExecutorService executorService;

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextCPF;
    private EditText editTextPhone;
    private EditText editTextSUSCard;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirmation;

    private Button buttonConfirm;

    private UserClient userClient;
    private UserDTO user;
    private UserPartialDTO userPartial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        executorService = Executors.newSingleThreadExecutor();

        editTextCPF = findViewById(R.id.editTextCPF);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSUSCard = findViewById(R.id.editTextNumeroCartaoSus);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirmation = findViewById(R.id.editTextConfirmPassword);

        buttonConfirm = findViewById(R.id.buttonConfirm);

        userClient = new UserClient(ProfileActivity.this);
        userPartial = new UserPartialDTO();

        fetchProfile();

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordConfirmation = editTextPasswordConfirmation.getText().toString();

                if (!Validator.isEmailValid(email)) {
                    Toast.makeText(ProfileActivity.this, "Email inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Validator.isPhoneValid(phone)) {
                    Toast.makeText(ProfileActivity.this, "Telefone inválido", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(password.isEmpty() && passwordConfirmation.isEmpty())) {
                    if (!Validator.isPasswordValid(password, passwordConfirmation)) {
                        Toast.makeText(ProfileActivity.this, "Senha inválida", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    userPartial.setPassword(password);
                }

                userPartial.setPhone(phone);
                userPartial.setEmail(email);

                if(hasNothingChanged(userPartial))
                {
                    Toast.makeText(ProfileActivity.this, "Nenhuma alteração realizada", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                updateUserProfile();
            }
        });        
    }

    private boolean hasNothingChanged(UserPartialDTO userPartial) {
        return  Objects.equals(user.getPhone(), userPartial.getPhone()) &&
                Objects.equals(user.getEmail(), userPartial.getEmail()) &&
                userPartial.getPassword().isEmpty();
    }

    private void updateUserProfile() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userClient.updateUser(userPartial);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ProfileActivity.this, "Perfil atualizado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    private void fetchProfile() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                user = userClient.getUser();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayProfile(user);
                    }
                });
            }
        });
    }

    private void displayProfile(UserDTO user) {
        editTextCPF.setText(user.getCpf());
        editTextName.setText(user.getName());
        editTextPhone.setText(user.getPhone());
        editTextEmail.setText(user.getEmail());
        editTextSUSCard.setText(user.getSusCardNumber());
    }
}
