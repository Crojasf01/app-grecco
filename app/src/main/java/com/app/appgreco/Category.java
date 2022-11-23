package com.app.appgreco;

public class Category {

    private String IdEvento;
    private String id;
    private String imagen;
    private String desc;
    private String nombre;
    private String lugar;
    private String fecha;
    private Boolean status;
    private String categoria;
    private String telefono;
    private Double latitud;
    private Double longitud;
    private int fechaAprobacion;

    public Category() {
    }


    public Category(String id, String imagen, String desc, String nombre, String lugar, String fecha) {
        this.id = id;
        this.imagen = imagen;
        this.desc = desc;
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public Category(String id, String imagen, String desc, String nombre, String lugar, String fecha, Boolean status) {
        this.id = id;
        this.imagen = imagen;
        this.desc = desc;
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(int fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIdEvento() {
        return IdEvento;
    }

    public void setIdEvento(String idEvento) {
        IdEvento = idEvento;
    }

}
