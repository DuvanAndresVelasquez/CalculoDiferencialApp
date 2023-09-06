package com.smeagtechnology.diferencial;

public class Actividad {
    private int idactividad;
    private String enunciadotema;
    private String imagenenunciado;

    public Actividad(int idactividad, String enunciadotema, String imagenenunciado) {
        this.idactividad = idactividad;
        this.enunciadotema = enunciadotema;
        this.imagenenunciado = imagenenunciado;
    }

    public int getIdactividad() {
        return idactividad;
    }

    public void setIdactividad(int idactividad) {
        this.idactividad = idactividad;
    }

    public String getEnunciadotema() {
        return enunciadotema;
    }

    public void setEnunciadotema(String enunciadotema) {
        this.enunciadotema = enunciadotema;
    }

    public String getImagenenunciado() {
        return imagenenunciado;
    }

    public void setImagenenunciado(String imagenenunciado) {
        this.imagenenunciado = imagenenunciado;
    }
}
