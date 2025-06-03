package com.example.lab5_20212591;
import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView tvSaludo, tvMotivacion;
    private ImageView ivImagen;
    private SharedPrefManager manager;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    guardarImagen(uri);
                    mostrarImagenGuardada();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pedirPermisoNotificaciones();
        Notifications.crearCanales(this);

        manager = new SharedPrefManager(this);


        tvSaludo = findViewById(R.id.tvSaludo);
        tvMotivacion = findViewById(R.id.tvMotivacion);
        ivImagen = findViewById(R.id.ivImagen);
        Button btnVerHabitos = findViewById(R.id.btnVerHabitos);
        Button btnConfiguraciones = findViewById(R.id.btnConfiguraciones);

        String nombre = "Daniel"; // Modificable (le pondré mi nombre :'v)
        String mensaje = manager.obtenerMensaje();

        tvSaludo.setText("¡Hola, " + nombre + "!");
        tvMotivacion.setText(mensaje);
        mostrarImagenGuardada();

        ivImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnVerHabitos.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ListaHabitosActivity.class))
        );

        btnConfiguraciones.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ConfHabitoActivity.class))
        );
    }

    private void pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    private void guardarImagen(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(), "foto_app.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarImagenGuardada() {
        File file = new File(getFilesDir(), "foto_app.jpg");
        if (file.exists()) {
            ivImagen.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String mensaje = manager.obtenerMensaje();
        tvMotivacion.setText(mensaje);
    }
}