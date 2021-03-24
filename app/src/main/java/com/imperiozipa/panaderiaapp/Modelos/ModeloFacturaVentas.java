package com.imperiozipa.panaderiaapp.Modelos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ModeloFacturaVentas implements Serializable {
    String facturaid;
    //String fecha_factura;
    String usuario;
    String name_factura;
    int total_factura;
    String estado;
    String nota;
    Date fecha_factura;
    int numerofactura;

    public ModeloFacturaVentas() {
    }

    public ModeloFacturaVentas(String facturaid, String usuario, String name_factura,
                               int total_factura, String estado, String nota,
                               Date fecha_factura, int numerofactura) {
        this.facturaid = facturaid;
        this.usuario = usuario;
        this.name_factura = name_factura;
        this.total_factura = total_factura;
        this.estado = estado;
        this.nota = nota;
        this.fecha_factura = fecha_factura;
        this.numerofactura = numerofactura;
    }

    public String getFacturaid() {
        return facturaid;
    }

    public void setFacturaid(String facturaid) {
        this.facturaid = facturaid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getName_factura() {
        return name_factura;
    }

    public void setName_factura(String name_factura) {
        this.name_factura = name_factura;
    }

    public int getTotal_factura() {
        return total_factura;
    }

    public void setTotal_factura(int total_factura) {
        this.total_factura = total_factura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Date getFecha_factura() {
        return fecha_factura;
    }

    public void setFecha_factura(Date fecha_factura) {
        this.fecha_factura = fecha_factura;
    }

    public int getNumerofactura() {
        return numerofactura;
    }

    public void setNumerofactura(int numerofactura) {
        this.numerofactura = numerofactura;
    }
}
