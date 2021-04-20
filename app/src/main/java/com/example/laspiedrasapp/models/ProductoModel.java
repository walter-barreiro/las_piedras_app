package com.example.laspiedrasapp.models;

public class ProductoModel {
    private Integer uid;
    private String nombre;
    private Integer precio;
    private Integer cantidad;
    private String categoria;
    private String image;

    public ProductoModel(){}

    public ProductoModel(Integer uid, String nombre, Integer precio, Integer cantidad, String categoria, String image) {
        this.uid = uid;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.image = image;
    }


    public Integer getUid() {return uid;}
    public void setUid(Integer uid) {this.uid = uid;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public Integer getPrecio() {return precio;}
    public void setPrecio(Integer precio) {this.precio = precio;}

    public Integer getCantidad() {return cantidad;}
    public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}

    public String getCategoria() {return categoria;}
    public void setCategoria(String categoria) {this.categoria = categoria;}

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image; }

}