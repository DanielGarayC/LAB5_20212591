package com.example.lab5_20212591;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import com.example.lab5_20212591.model.Habito;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CrearHabitoActivity extends AppCompatActivity {

    EditText etNombre, etFrecuencia, etFechaHora;
    Button btnGuardar;
    Calendar calendar;
    SharedPrefManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_habito);

        etNombre = findViewById(R.id.etNombreHabito);
        etFrecuencia = findViewById(R.id.etFrecuencia);
        etFechaHora = findViewById(R.id.etFechaHora);
        AutoCompleteTextView autoCategoria = findViewById(R.id.autoCategoria);
        btnGuardar = findViewById(R.id.btnGuardarHabito);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Botón atrás pal toolbar :D
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Comportamiento del botón :D
        toolbar.setNavigationOnClickListener(v -> finish());

        manager = new SharedPrefManager(this);
        calendar = Calendar.getInstance();

        String[] categorias = {"Ejercicio", "Alimentación", "Sueño", "Lectura"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categorias);
        autoCategoria.setAdapter(adapter);

        etFechaHora.setOnClickListener(v -> mostrarDateTimePicker());

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String categoria = autoCategoria.getText().toString();
            String strFrecuencia = etFrecuencia.getText().toString();
            String fechaHora = etFechaHora.getText().toString();

            if (nombre.isEmpty() || strFrecuencia.isEmpty() || fechaHora.isEmpty() || categoria.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int frecuencia;
            try {
                frecuencia = Integer.parseInt(strFrecuencia);
                if (frecuencia <= 0) {
                    Toast.makeText(this, "La frecuencia debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Frecuencia inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guarda hábito
            Habito habito = new Habito(nombre, categoria, frecuencia, fechaHora);
            ArrayList<Habito> lista = manager.obtenerHabitos();
            lista.add(habito);
            manager.guardarHabitos(lista);

            // Mensaje sugerido :D
            String mensajeSugerido;
            switch (categoria) {
                case "Ejercicio":
                    mensajeSugerido = "Hora de realizar actividad física: " + nombre;
                    break;
                case "Alimentación":
                    mensajeSugerido = "Hora de que te alimentes: " + nombre;
                    break;
                case "Sueño":
                    mensajeSugerido = "Hora de descansar: " + nombre;
                    break;
                case "Lectura":
                    mensajeSugerido = "Hora de lectura: " + nombre;
                    break;
                default:
                    mensajeSugerido = "Recordatorio: " + nombre;
                    break;
            }


            // Datos al Worker
            Data data = new Data.Builder()
                    .putString("titulo", "Recordatorio de hábito")
                    .putString("mensaje", mensajeSugerido)
                    .putString("canal", categoria)
                    .build();

            long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
            if (delay < 0) delay = 0;

            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, frecuencia, TimeUnit.HOURS)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .build();

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    "Habito_" + nombre + "_" + System.currentTimeMillis(), // identificador único por hábito
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
            );

            Toast.makeText(this, "Hábito guardado", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void mostrarDateTimePicker() {
        Calendar now = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            new TimePickerDialog(this, (view1, hour, minute) -> {
                calendar.set(year, month, dayOfMonth, hour, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                etFechaHora.setText(sdf.format(calendar.getTime()));
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show();
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
    }
}