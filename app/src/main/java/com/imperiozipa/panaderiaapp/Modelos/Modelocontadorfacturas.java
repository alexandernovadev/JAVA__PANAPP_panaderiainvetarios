package com.imperiozipa.panaderiaapp.Modelos;

import java.io.Serializable;

public class Modelocontadorfacturas implements Serializable
{

    int contador ;

    public Modelocontadorfacturas()
    {
    }

    public Modelocontadorfacturas(int contador) {
        this.contador = contador;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }
}
