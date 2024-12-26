package com.uneb.appsus.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.uneb.appsus.MainActivity;
import com.uneb.appsus.Manager.TokenManager;
import com.uneb.appsus.R;
import com.uneb.appsus.Utility.ToolbarBuilder;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    boolean isSpinnerInitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button logoutButton = findViewById(R.id.logoutButton);
        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitch);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        Spinner spinner = findViewById(R.id.languageSpinner);

        new ToolbarBuilder(this, toolbar)
                .withTitle(getString(R.string.configuracoes))
                .withReturnButton()
                .build();

        configureNotificationSwitch(notificationSwitch);
        ConfigureLogoutButton(logoutButton);
        ConfigureSpinnerItems(spinner);
    }

    private void configureNotificationSwitch(SwitchCompat notificationSwitch) {
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, getString(R.string.notificacoes_ativadas), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.notificacoes_desativadas), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ConfigureLogoutButton(Button logoutButton) {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDialog(getString(R.string.deseja_realmente_sair_da_conta), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
            }
        });
    }

    private void ConfigureSpinnerItems(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items_idioma, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        isSpinnerInitial = true;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeLanguage(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void changeLanguage(AdapterView<?> parent, int position) {
        if (isSpinnerInitial) {
            isSpinnerInitial = false;
            return;
        }

        String selectedItem = parent.getItemAtPosition(position).toString();
        String currentLocale = getResources().getConfiguration().locale.getLanguage();

        if (selectedItem.equals("English") && !currentLocale.equals("en")) {
            setLocale(SettingsActivity.this, "en");
        } else if (selectedItem.equals("Português") && !currentLocale.equals("pt-rBR")) {
            setLocale(SettingsActivity.this, "pt");
        }
    }

    public void setLocale(Context context, String languageCode) {
        Toast.makeText(context, "Language changed to " + languageCode, Toast.LENGTH_SHORT).show();
        //TODO Implementar a mudança de idioma independente do idioma do SO
    }

    private void showBookingDialog(String message, DialogInterface.OnClickListener confirmAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(getString(R.string.confirmar), confirmAction)
                .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void logout() {
        TokenManager.getInstance(this).clearToken();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}