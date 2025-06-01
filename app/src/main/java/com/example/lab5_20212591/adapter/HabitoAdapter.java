package com.example.lab5_20212591.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_20212591.R;
import com.example.lab5_20212591.SharedPrefManager;
import com.example.lab5_20212591.model.Habito;

import java.util.ArrayList;

public class HabitoAdapter extends RecyclerView.Adapter<HabitoAdapter.ViewHolder> {
    private ArrayList<Habito> lista;
    private SharedPrefManager manager;

    public HabitoAdapter(ArrayList<Habito> lista, SharedPrefManager manager) {
        this.lista = lista;
        this.manager = manager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCategoria, tvFrecuencia, tvInicio;
        Button btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvFrecuencia = itemView.findViewById(R.id.tvFrecuencia);
            tvInicio = itemView.findViewById(R.id.tvInicio);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    @NonNull
    @Override
    public HabitoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habito, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitoAdapter.ViewHolder holder, int position) {
        Habito h = lista.get(position);
        holder.tvNombre.setText(h.getNombre());
        holder.tvCategoria.setText("Categoría: " + h.getCategoria());
        holder.tvFrecuencia.setText("Cada " + h.getFrecuenciaHoras() + " horas");
        holder.tvInicio.setText("Inicio: " + h.getFechaHoraInicio());

        holder.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Deseas eliminar este hábito?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        lista.remove(position);
                        notifyItemRemoved(position);
                        manager.guardarHabitos(lista);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
