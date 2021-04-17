package com.example.laspiedrasapp;

public class Product {

    //Estructura de datos

    public String id;
    public String nombre;
    public String descripcion;
    public double precio;

    //Para traer los datos de Firebase
    public Product(){
    }

    //Los Setter y Getter
    public Product(String id, String nombre, String descripcion, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    //Como muestro los datos en el Carrito
    public String toString(){
        return this.nombre+ " - $" + this.precio;
    }
}

