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
            int frecuencia = Integer.parseInt(etFrecuencia.getText().toString());

            //Validación owo
            if (mensaje.isEmpty() || etFrecuencia.getText().toString().isEmpty()) {
                Toast.makeText(this, "Completa todos los campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            manager.guardarMensajeMotivacional(mensaje, frecuencia);
            Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AlarmaReceiver.class);
            intent.putExtra("titulo", "Motivación diaria");
            intent.putExtra("mensaje", mensaje);
            intent.putExtra("canal", "Motivacional");
            intent.putExtra("tipo", "motivacional");

            long trigger = System.currentTimeMillis() + frecuencia * 3600000L;
            Notifications.programarNotificacion(this, intent, trigger);
        });
    }
}