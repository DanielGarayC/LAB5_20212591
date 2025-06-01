package com.example.lab5_20212591;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import com.example.lab5_20212591.model.Habito;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPrefManager {
    private static final String PREF_NAME = "HabitosPrefs";
    private static final String KEY_HABITOS = "habitos";
    private static final String KEY_MENSAJE = "mensaje_motivacional";
    private static final String KEY_FRECUENCIA_MOTIVACIONAL = "frecuencia_motivacional";

    private SharedPreferences preferences;
    private Gson gson;

    public SharedPrefManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void guardarHabitos(ArrayList<Habito> lista) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_HABITOS, gson.toJson(lista));
        editor.apply();
    }

    public ArrayList<Habito> obtenerHabitos() {
        String json = preferences.getString(KEY_HABITOS, null);
        Type type = new TypeToken<ArrayList<Habito>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    public void guardarMensajeMotivacional(String mensaje, int frecuencia) {
        preferences.edit()
                .putString(KEY_MENSAJE, mensaje)
                .putInt(KEY_FRECUENCIA_MOTIVACIONAL, frecuencia)
                .apply();
    }


    public String obtenerMensaje() {
        return preferences.getString(KEY_MENSAJE, "Sigue avanzando 💪");
    }

    public int obtenerFrecuenciaMotivacional() {
        return preferences.getInt(KEY_FRECUENCIA_MOTIVACIONAL, 6);
    }
}