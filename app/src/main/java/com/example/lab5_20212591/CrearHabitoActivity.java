package com.example.lab5_20212591;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab5_20212591.model.Habito;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CrearHabitoActivity extends AppCompatActivity {

    EditText etNombre, etFrecuencia, etFechaHora;
    Spinner spinnerCategoria;
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
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnGuardar = findViewById(R.id.btnGuardarHabito);

        manager = new SharedPrefManager(this);
        calendar = Calendar.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Ejercicio", "Alimentación", "Sueño", "Lectura"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        etFechaHora.setOnClickListener(v -> mostrarDateTimePicker());

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String categoria = spinnerCategoria.getSelectedItem().toString();
            int frecuencia = Integer.parseInt(etFrecuencia.getText().toString());
            String fechaHora = etFechaHora.getText().toString();

            Habito habito = new Habito(nombre, categoria, frecuencia, fechaHora);
            ArrayList<Habito> lista = manager.obtenerHabitos();
            lista.add(habito);
            manager.guardarHabitos(lista);

            // Recordatorio del hábito
            Intent intent = new Intent(this, AlarmaReceiver.class);
            intent.putExtra("titulo", "Recordatorio de hábito");
            //Sección para el mensaje sugerido :D
            String mensajeSugerido = "";

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

            }

            intent.putExtra("mensaje", mensajeSugerido);
            intent.putExtra("canal", categoria);
            Notifications.programarNotificacion(this, intent, calendar.getTimeInMillis());

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