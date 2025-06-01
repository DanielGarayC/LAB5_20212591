package com.example.lab5_20212591;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String titulo = intent.getStringExtra("titulo");
        String mensaje = intent.getStringExtra("mensaje");
        String canal = intent.getStringExtra("canal");
        Notifications.mostrarNotificacion(context, titulo, mensaje, canal);


    }
}
