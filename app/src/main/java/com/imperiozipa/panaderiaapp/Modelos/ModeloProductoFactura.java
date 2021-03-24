package com.imperiozipa.panaderiaapp.Modelos;

import java.io.Serializable;

public class ModeloProductoFactura implements Serializable
{
    String id;
    String name_producto;
    int precioUnidad;
    int precioTotal;
    int iva;
    int cantidad;

    public ModeloProductoFactura() {
    }

    public ModeloProductoFactura(String id, String name_producto,
                                 int precioUnidad, int precioTotal,
                                 int iva, int cantidad) {
        this.id = id;
        this.name_producto = name_producto;
        this.precioUnidad = precioUnidad;
        this.precioTotal = precioTotal;
        this.iva = iva;
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_producto() {
        return name_producto;
    }

    public void setName_producto(String name_producto) {
        this.name_producto = name_producto;
    }

    public int getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(int precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public int getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
