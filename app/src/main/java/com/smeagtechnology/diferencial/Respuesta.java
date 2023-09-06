package com.smeagtechnology.diferencial;

public class Respuesta {
    private int idrespuesta;
    private String nombrerespuesta;
    private String imagenrespuesta;
    private int idsolucionrta;


    public Respuesta(int idrespuesta, String nombrerespuesta, String imagenrespuesta, int idsolucionrta) {
        this.idrespuesta = idrespuesta;
        this.nombrerespuesta = nombrerespuesta;
        this.imagenrespuesta = imagenrespuesta;
        this.idsolucionrta = idsolucionrta;
    }

    public int getIdrespuesta() {
        return idrespuesta;
    }

    public void setIdrespuesta(int idrespuesta) {
        this.idrespuesta = idrespuesta;
    }

    public String getNombrerespuesta() {
        return nombrerespuesta;
    }

    public void setNombrerespuesta(String nombrerespuesta) {
        this.nombrerespuesta = nombrerespuesta;
    }

    public String getImagenrespuesta() {
        return imagenrespuesta;
    }

    public void setImagenrespuesta(String imagenrespuesta) {
        this.imagenrespuesta = imagenrespuesta;
    }

    public int getIdsolucionrta() {
        return idsolucionrta;
    }

    public void setIdsolucionrta(int idsolucionrta) {
        this.idsolucionrta = idsolucionrta;
    }



}
