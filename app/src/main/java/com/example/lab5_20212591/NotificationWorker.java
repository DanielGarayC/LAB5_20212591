package com.example.lab5_20212591;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String titulo = getInputData().getString("titulo");
        String mensaje = getInputData().getString("mensaje");
        String canal = getInputData().getString("canal");

        Notifications.mostrarNotificacion(getApplicationContext(), titulo, mensaje, canal);

        return Result.success();
    }
}