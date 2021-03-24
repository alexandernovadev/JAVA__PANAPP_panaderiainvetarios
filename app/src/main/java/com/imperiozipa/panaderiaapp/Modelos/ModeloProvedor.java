package com.imperiozipa.panaderiaapp.Modelos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

public class ModeloProvedor implements Serializable {
    String idprovedor;
    String nombre_provedor;
    String marca_provedor;
    String emailprovedor;
    long telefono1;
    long telefono2;
    String nameimgprovedor;
    String urlimagen;
    ArrayList<String> diasvisita;
    ArrayList<String> diasentrega;

    public ModeloProvedor() {}

    public ModeloProvedor(String idprovedor, String nombre_provedor, String marca_provedor, String emailprovedor, long telefono1, long telefono2,
                          String nameimgprovedor, String urlimagen, ArrayList<String> diasvisita, ArrayList<String> diasentrega) {
        this.idprovedor = idprovedor;
        this.nombre_provedor = nombre_provedor;
        this.marca_provedor = marca_provedor;
        this.emailprovedor = emailprovedor;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.nameimgprovedor = nameimgprovedor;
        this.urlimagen = urlimagen;
        this.diasvisita = diasvisita;
        this.diasentrega = diasentrega;
    }

    public String getIdprovedor() {
        return idprovedor;
    }

    public void setIdprovedor(String idprovedor) {
        this.idprovedor = idprovedor;
    }

    public String getNombre_provedor() {
        return nombre_provedor;
    }

    public void setNombre_provedor(String nombre_provedor) {
        this.nombre_provedor = nombre_provedor;
    }

    public String getMarca_provedor() {
        return marca_provedor;
    }

    public void setMarca_provedor(String marca_provedor) {
        this.marca_provedor = marca_provedor;
    }

    public String getEmailprovedor() {
        return emailprovedor;
    }

    public void setEmailprovedor(String emailprovedor) {
        this.emailprovedor = emailprovedor;
    }

    public long getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(long telefono1) {
        this.telefono1 = telefono1;
    }

    public long getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(long telefono2) {
        this.telefono2 = telefono2;
    }

    public String getNameimgprovedor() {
        return nameimgprovedor;
    }

    public void setNameimgprovedor(String nameimgprovedor) {
        this.nameimgprovedor = nameimgprovedor;
    }

    public String getUrlimagen() {
        return urlimagen;
    }

    public void setUrlimagen(String urlimagen) {
        this.urlimagen = urlimagen;
    }

    public ArrayList<String> getDiasvisita() {
        return diasvisita;
    }

    public void setDiasvisita(ArrayList<String> diasvisita) {
        this.diasvisita = diasvisita;
    }

    public ArrayList<String> getDiasentrega() {
        return diasentrega;
    }

    public void setDiasentrega(ArrayList<String> diasentrega) {
        this.diasentrega = diasentrega;
    }
}
