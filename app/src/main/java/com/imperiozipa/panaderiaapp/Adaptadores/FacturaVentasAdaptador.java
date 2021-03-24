package com.imperiozipa.panaderiaapp.Adaptadores;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imperiozipa.panaderiaapp.Modelos.ModeloFacturaVentas;
import com.imperiozipa.panaderiaapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FacturaVentasAdaptador extends RecyclerView.Adapter<FacturaVentasAdaptador.VistaFV>
    implements View.OnClickListener
{


    ArrayList<ModeloFacturaVentas> arrayListFacrutaventas;
    private View.OnClickListener listener;
    View vista;
    DecimalFormat formateo;

    public FacturaVentasAdaptador( ArrayList<ModeloFacturaVentas> arrayListFacrutaventas)
    {
        this.arrayListFacrutaventas = arrayListFacrutaventas;
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }

    @NonNull
    @Override
    public VistaFV onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {

        vista= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_bill_simple,viewGroup,false);

        // Habia que configurar el andcho del Recycler aqui en el adaptador, ancho y alto.
        vista.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));

        vista.setOnClickListener(this);

        return new VistaFV(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull VistaFV vistaFV, int i)
    {
        formateo = new DecimalFormat("###,###.##");
        int total = arrayListFacrutaventas.get(i).getTotal_factura();
        String totalFor = formateo.format(total);

        vistaFV.nameFVSIMPLE.setText(arrayListFacrutaventas.get(i).getName_factura());
        vistaFV.totalFCSIMPLE.setText(totalFor);
    }

    @Override
    public int getItemCount()
    {
        return arrayListFacrutaventas.size();
    }

    @Override
    public void onClick(View v)
    {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class VistaFV extends RecyclerView.ViewHolder
    {
        TextView nameFVSIMPLE,totalFCSIMPLE;
        public VistaFV(@NonNull View itemView)
        {
            super(itemView);

            nameFVSIMPLE =itemView.findViewById(R.id.namefacturabiilsimple);
            totalFCSIMPLE =itemView.findViewById(R.id.precioFVSIMPLE);

        }
    }
}
