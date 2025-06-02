package com.example.lab5_20212591;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notifications {
    public static void crearCanales(Context context) {
        //Considere el mensaje motivacional como una categoria más :c
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String[] categorias = {"Ejercicio", "Alimentación", "Sueño", "Lectura","Motivacional"};
            for (String categoria : categorias) {
                NotificationChannel channel = new NotificationChannel(
                        categoria, "Canal " + categoria, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Recordatorios para " + categoria);
                channel.enableVibration(true);
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void mostrarNotificacion(Context context, String titulo, String mensaje, String canalId) {

        int icono = R.drawable.ic_notification; //icono por default :D (una campana)

        // Switch para colocar un icono por canal (categoría)
        switch (canalId) {
            case "Ejercicio":
                icono = R.drawable.ic_ejercicio;
                break;
            case "Alimentación":
                icono = R.drawable.ic_alimentacion;
                break;
            case "Sueño":
                icono = R.drawable.ic_mimir;
                break;
            case "Lectura":
                icono = R.drawable.ic_lectura;
                break;
            default:
                icono = R.drawable.ic_notification;
                break;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, canalId)
                .setContentTitle(titulo)
                .setSmallIcon(icono)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
    }


}
