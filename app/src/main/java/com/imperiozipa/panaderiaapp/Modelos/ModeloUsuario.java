package com.imperiozipa.panaderiaapp.Modelos;

import java.io.Serializable;

public class ModeloUsuario implements Serializable
{
    String iduser;
    String nombreuser;
    String urlimguser;
    String nameimguser;
    String tipouser;
    String emailuser;
    long cedulauser;
    long celular;

    public ModeloUsuario() {}

    public ModeloUsuario(String iduser, String nombreuser, String urlimguser
            , String nameimguser, String tipouser
            , String emailuser, long cedulauser, long celular) {
        this.iduser = iduser;
        this.nombreuser = nombreuser;
        this.urlimguser = urlimguser;
        this.nameimguser = nameimguser;
        this.tipouser = tipouser;
        this.emailuser = emailuser;
        this.cedulauser = cedulauser;
        this.celular = celular;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getNombreuser() {
        return nombreuser;
    }

    public void setNombreuser(String nombreuser) {
        this.nombreuser = nombreuser;
    }

    public String getUrlimguser() {
        return urlimguser;
    }

    public void setUrlimguser(String urlimguser) {
        this.urlimguser = urlimguser;
    }

    public String getNameimguser() {
        return nameimguser;
    }

    public void setNameimguser(String nameimguser) {
        this.nameimguser = nameimguser;
    }

    public String getTipouser() {
        return tipouser;
    }

    public void setTipouser(String tipouser) {
        this.tipouser = tipouser;
    }

    public String getEmailuser() {
        return emailuser;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    public long getCedulauser() {
        return cedulauser;
    }

    public void setCedulauser(long cedulauser) {
        this.cedulauser = cedulauser;
    }

    public long getCelular() {
        return celular;
    }

    public void setCelular(long celular) {
        this.celular = celular;
    }
}
