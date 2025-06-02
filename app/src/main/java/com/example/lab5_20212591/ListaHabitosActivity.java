package com.example.lab5_20212591;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_20212591.adapter.HabitoAdapter;
import com.example.lab5_20212591.model.Habito;
import com.example.lab5_20212591.SharedPrefManager;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class ListaHabitosActivity extends AppCompatActivity {
    RecyclerView rv;
    Button btnAgregar;
    HabitoAdapter adapter;
    SharedPrefManager manager;
    TextView msgVacio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_habitos);

        rv = findViewById(R.id.rvHabitos);
        btnAgregar = findViewById(R.id.btnAgregarHabito);
        manager = new SharedPrefManager(this);
        msgVacio = findViewById(R.id.tvVacio);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Botón atrás pal toolbar :D
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Comportamiento del botón :D
        toolbar.setNavigationOnClickListener(v -> finish());

        ArrayList<Habito> lista = manager.obtenerHabitos();

        //Mensaje que se muestra si está vacía la lista de hábitos :D
        if (lista.isEmpty()) {
            msgVacio.setVisibility(View.VISIBLE);
        } else {
            msgVacio.setVisibility(View.GONE);
        }

        adapter = new HabitoAdapter(lista, manager);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        btnAgregar.setOnClickListener(v ->
                startActivity(new Intent(this, CrearHabitoActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Habito> lista = manager.obtenerHabitos();
        msgVacio.setVisibility(lista.isEmpty() ? View.VISIBLE : View.GONE);
        adapter = new HabitoAdapter(lista, manager);
        rv.setAdapter(adapter);
    }
}
