package com.smeagtechnology.diferencial;

public class Apps {
    private int idaplicacion;
    private String nombreaplicacion;
    private String descripcionaplicacion;
    private String imagenaplicacion;
    private String linkaplicacion;


    public Apps(int idaplicacion, String nombreaplicacion, String descripcionaplicacion, String imagenaplicacion, String linkaplicacion) {
        this.idaplicacion = idaplicacion;
        this.nombreaplicacion = nombreaplicacion;
        this.descripcionaplicacion = descripcionaplicacion;
        this.imagenaplicacion = imagenaplicacion;
        this.linkaplicacion = linkaplicacion;
    }

    public int getIdaplicacion() {
        return idaplicacion;
    }

    public void setIdaplicacion(int idaplicacion) {
        this.idaplicacion = idaplicacion;
    }

    public String getNombreaplicacion() {
        return nombreaplicacion;
    }

    public void setNombreaplicacion(String nombreaplicacion) {
        this.nombreaplicacion = nombreaplicacion;
    }

    public String getDescripcionaplicacion() {
        return descripcionaplicacion;
    }

    public void setDescripcionaplicacion(String descripcionaplicacion) {
        this.descripcionaplicacion = descripcionaplicacion;
    }

    public String getImagenaplicacion() {
        return imagenaplicacion;
    }

    public void setImagenaplicacion(String imagenaplicacion) {
        this.imagenaplicacion = imagenaplicacion;
    }

    public String getLinkaplicacion() {
        return linkaplicacion;
    }

    public void setLinkaplicacion(String linkaplicacion) {
        this.linkaplicacion = linkaplicacion;
    }
}
