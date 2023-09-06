package com.smeagtechnology.diferencial;

public class ParrafosTema {
    private int idparrafo;
    private String contenidoparrafo;
    private String imagenparrafo;

    public ParrafosTema(int idparrafo, String contenidoparrafo, String imagenparrafo) {
        this.idparrafo = idparrafo;
        this.contenidoparrafo = contenidoparrafo;
        this.imagenparrafo = imagenparrafo;
    }

    public int getIdparrafo() {
        return idparrafo;
    }

    public void setIdparrafo(int idparrafo) {
        this.idparrafo = idparrafo;
    }

    public String getContenidoparrafo() {
        return contenidoparrafo;
    }

    public void setContenidoparrafo(String contenidoparrafo) {
        this.contenidoparrafo = contenidoparrafo;
    }

    public String getImagenparrafo() {
        return imagenparrafo;
    }

    public void setImagenparrafo(String imagenparrafo) {
        this.imagenparrafo = imagenparrafo;
    }
}
