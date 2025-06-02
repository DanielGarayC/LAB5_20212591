package com.example.lab5_20212591;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class ConfHabitoActivity extends AppCompatActivity {

    EditText etMensaje, etFrecuencia;
    Button btnGuardar;
    SharedPrefManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_habito);

        etMensaje = findViewById(R.id.etMensaje);
        etFrecuencia = findViewById(R.id.etFrecuencia);
        btnGuardar = findViewById(R.id.btnGuardarConfig);
        manager = new SharedPrefManager(this);

        etMensaje.setText(manager.obtenerMensaje());
        etFrecuencia.setText(String.valueOf(manager.obtenerFrecuenciaMotivacional()));

        btnGuardar.setOnClickListener(v -> {
            String mensaje = etMensaje.getText().toString();
            String frecuenciaStr = etFrecuencia.getText().toString();

            if (mensaje.isEmpty() || frecuenciaStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            int frecuencia;
            try {
                frecuencia = Integer.parseInt(frecuenciaStr);
                if (frecuencia <= 0) {
                    Toast.makeText(this, "La frecuencia debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Frecuencia inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            manager.guardarMensajeMotivacional(mensaje, frecuencia);
            Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show();

            // Datos al Worker
            Data data = new Data.Builder()
                    .putString("titulo", "Motivación diaria")
                    .putString("mensaje", mensaje)
                    .putString("canal", "Motivacional")
                    .build();

            PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(NotificationWorker.class, frecuencia, TimeUnit.HOURS)
                    .setInputData(data)
                    .build();

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    "MotivacionPeriodicWorker",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    request
            );

            finish();
        });
    }
}