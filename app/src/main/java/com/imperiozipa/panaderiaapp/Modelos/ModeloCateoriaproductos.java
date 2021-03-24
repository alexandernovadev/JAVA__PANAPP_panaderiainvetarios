package com.imperiozipa.panaderiaapp.Modelos;

public class ModeloCateoriaproductos
{
    String idcategoria;
    String nombrecategoria;
    String descripcioncategoria;


    public ModeloCateoriaproductos(){}

    public ModeloCateoriaproductos(String idcategoria,
                                   String nombrecategoria,
                                   String descripcioncategoria)
    {
        this.idcategoria = idcategoria;
        this.nombrecategoria = nombrecategoria;
        this.descripcioncategoria = descripcioncategoria;
    }

    public String getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(String idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getNombrecategoria() {
        return nombrecategoria;
    }

    public void setNombrecategoria(String nombrecategoria) {
        this.nombrecategoria = nombrecategoria;
    }

    public String getDescripcioncategoria() {
        return descripcioncategoria;
    }

    public void setDescripcioncategoria(String descripcioncategoria) {
        this.descripcioncategoria = descripcioncategoria;
    }
}
