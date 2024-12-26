package com.uneb.appsus.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.uneb.appsus.Client.AppointmentsClient;
import com.uneb.appsus.DTO.AppointmentDisplayDTO;
import com.uneb.appsus.Manager.TokenManager;
import com.uneb.appsus.R;
import com.uneb.appsus.Utility.ToolbarBuilder;
import com.uneb.appsus.enums.AppointmentStatus;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsultasActivity extends AppCompatActivity {

    private Button bookingButton;
    private LinearLayout appointmentsContainer;
    private ExecutorService executorService;
    private ImageView imageView;
    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView usernameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        bookingButton = findViewById(R.id.bookingButton);
        appointmentsContainer = findViewById(R.id.appointmentContainer);
        imageView = findViewById(R.id.imageView);
        toolbar = findViewById(R.id.toolbar);
        usernameTextView = findViewById(R.id.textViewName);

        executorService = Executors.newSingleThreadExecutor();

        new ToolbarBuilder(this, toolbar)
                .withTitle(getString(R.string.app_name))
                .withBurgerButton(SettingsActivity.class)
                .build();

        imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.profile, null));

        configureBookingButton();
        configureProfileImage();

        usernameTextView.setText(TokenManager.getInstance(this).getUserName());

        fetchAppointments();
    }

    private void configureProfileImage() {

        String avatarUrl = TokenManager.getInstance(this).getUserAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = new java.net.URL(avatarUrl).openStream();
                        final Drawable drawable = Drawable.createFromStream(inputStream, "src");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageDrawable(drawable);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultasActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void configureBookingButton() {
        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultasActivity.this, SpecialitiesActivity.class);
                startActivity(intent);
            }
        });
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
    
    private void fetchAppointments() {
        appointmentsContainer.removeAllViews();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                AppointmentsClient client = new AppointmentsClient(ConsultasActivity.this);
                final List<AppointmentDisplayDTO> appointments = client.getAppointments();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayAppointments(appointments, client);
                    }
                });
            }
        });
    }

    private void displayAppointments(List<AppointmentDisplayDTO> appointments, AppointmentsClient client) {
        LayoutInflater inflater = LayoutInflater.from(this);
        if (appointments == null) {
            return;
        }
        appointmentsContainer.removeAllViews();

        for (AppointmentDisplayDTO appointment : appointments) {
            View appointmentView = inflater.inflate(R.layout.component_consulta, appointmentsContainer, false);

            TextView textViewSpecialty = appointmentView.findViewById(R.id.textViewSpecialty);
            TextView textViewDate = appointmentView.findViewById(R.id.textViewDate);
            ImageButton cancelButton = appointmentView.findViewById(R.id.buttonCancel);
            ImageButton rescheduleButton = appointmentView.findViewById(R.id.buttonReschedule);

            textViewSpecialty.setText(appointment.getSpecialtyName());
            textViewDate.setText(appointment.getAppointmentDateTime());

            if (isAppointmentCanceledOrMissed(appointment)) {
                cancelButton.setAlpha(.5f);
                rescheduleButton.setAlpha(.5f);
            } else {
                configureCancelButton(client, appointment, cancelButton);
                configureScheduleButton(client, appointment, rescheduleButton);
            }

            appointmentsContainer.addView(appointmentView);
        }
    }

    private void configureScheduleButton(AppointmentsClient client, AppointmentDisplayDTO appointment, ImageButton rescheduleButton) {
        rescheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBookingDialog("Deseja remarcar a consulta?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(ConsultasActivity.this, "Consulta remarcada com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void configureCancelButton(AppointmentsClient client, AppointmentDisplayDTO appointment, ImageButton cancelButton) {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBookingDialog("Deseja cancelar a consulta?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                boolean success = client.cancelAppointment(appointment.getId());
                                if (success) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            fetchAppointments();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private boolean isAppointmentCanceledOrMissed(AppointmentDisplayDTO appointment) {
        return Objects.equals(appointment.getAppointmentStatus(), AppointmentStatus.CANCELED.getValue()) ||
                Objects.equals(appointment.getAppointmentStatus(), AppointmentStatus.MISSED.getValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}