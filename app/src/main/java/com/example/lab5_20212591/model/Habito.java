package com.example.lab5_20212591.model;

public class Habito {
    private String nombre;
    private String categoria;
    private int frecuenciaHoras;
    private String fechaHoraInicio;

    public Habito(String nombre, String categoria, int frecuenciaHoras, String fechaHoraInicio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.frecuenciaHoras = frecuenciaHoras;
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public int getFrecuenciaHoras() { return frecuenciaHoras; }
    public String getFechaHoraInicio() { return fechaHoraInicio; }
}