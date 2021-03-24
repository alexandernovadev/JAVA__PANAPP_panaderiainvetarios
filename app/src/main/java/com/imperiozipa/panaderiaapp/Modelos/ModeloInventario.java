package com.imperiozipa.panaderiaapp.Modelos;

import java.io.Serializable;

public class ModeloInventario implements Serializable {
    private String idgen;
    private String nombre;
    private int cantidad;
    private String categoria;
    private String tipo;
    private int precio_compra;
    private int precio_venta;
    private String codigo_producto;
    private String name_image;
    private String url_image;

    private int iva;
    private String fecha_vencimiento;
    private String fecha_entrada;
    private String peso_unidad;
    private String provedor;


    public ModeloInventario() {
        //public no-arg constructor needed
    }

    public ModeloInventario(String idgen, String nombre, int cantidad,
                            String categoria, String tipo, int precio_compra,
                            int precio_venta, String codigo_producto, String name_image,
                            String url_image, int iva, String fecha_vencimiento,
                            String fecha_entrada, String peso_unidad, String provedor) {
        this.idgen = idgen;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.tipo = tipo;
        this.precio_compra = precio_compra;
        this.precio_venta = precio_venta;
        this.codigo_producto = codigo_producto;
        this.name_image = name_image;
        this.url_image = url_image;
        this.iva = iva;
        this.fecha_vencimiento = fecha_vencimiento;
        this.fecha_entrada = fecha_entrada;
        this.peso_unidad = peso_unidad;
        this.provedor = provedor;
    }

    public String getIdgen() {
        return idgen;
    }

    public void setIdgen(String idgen) {
        this.idgen = idgen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(int precio_compra) {
        this.precio_compra = precio_compra;
    }

    public int getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(int precio_venta) {
        this.precio_venta = precio_venta;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getName_image() {
        return name_image;
    }

    public void setName_image(String name_image) {
        this.name_image = name_image;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(String fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }

    public String getPeso_unidad() {
        return peso_unidad;
    }

    public void setPeso_unidad(String peso_unidad) {
        this.peso_unidad = peso_unidad;
    }

    public String getProvedor() {
        return provedor;
    }

    public void setProvedor(String provedor) {
        this.provedor = provedor;
    }
}
