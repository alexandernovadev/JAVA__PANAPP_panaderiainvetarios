package com.imperiozipa.panaderiaapp.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imperiozipa.panaderiaapp.Modelos.ModeloFacturaVentas;
import com.imperiozipa.panaderiaapp.Modelos.ModeloInventario;
import com.imperiozipa.panaderiaapp.Modelos.ModeloProductoFactura;
import com.imperiozipa.panaderiaapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AdaptadorveritemsFacturas extends RecyclerView.Adapter<AdaptadorveritemsFacturas.VistaseeitemsFactura>
        implements View.OnClickListener
{
    ArrayList<ModeloProductoFactura> ALveritemsfacturas;
    private View.OnClickListener listener;
    View vista;
    DecimalFormat formateo;

    public AdaptadorveritemsFacturas(ArrayList<ModeloProductoFactura> ALveritemsfacturas)
    {
        this.ALveritemsfacturas = ALveritemsfacturas;
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }

    @NonNull
    @Override
    public VistaseeitemsFactura onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {

        vista= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.veritemfactura_layout,viewGroup,false);

        // Habia que configurar el andcho del Recycler aqui en el adaptador, ancho y alto.
        vista.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));

        vista.setOnClickListener(this);

        return new VistaseeitemsFactura(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull VistaseeitemsFactura vistaseeitemsFactura, int i)
    {
        vistaseeitemsFactura.veritemfactura_name.setText(ALveritemsfacturas.get(i).getName_producto());

        formateo = new DecimalFormat("###,###.##");
        int preciouni = ALveritemsfacturas.get(i).getPrecioUnidad();
        String preciouniForm = formateo.format(preciouni);

        int preciototal = ALveritemsfacturas.get(i).getPrecioTotal();
        String preciototalForm = formateo.format(preciototal);

        vistaseeitemsFactura.veritemfactura_precioU
                .setText(preciouniForm);
        vistaseeitemsFactura.veritemfactura_precioT
                .setText(preciototalForm);

        vistaseeitemsFactura.veritemfactura_cantidad.setText(String.valueOf(ALveritemsfacturas.get(i).getCantidad()));
    }

    @Override
    public int getItemCount()
    {
        return ALveritemsfacturas.size();
    }

    @Override
    public void onClick(View v)
    {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class VistaseeitemsFactura extends RecyclerView.ViewHolder
    {
        TextView veritemfactura_name,veritemfactura_precioU,veritemfactura_precioT,veritemfactura_cantidad;
        public VistaseeitemsFactura(@NonNull View itemView)
        {
            super(itemView);

            veritemfactura_name =itemView.findViewById(R.id.veritemfactura_name);
            veritemfactura_precioU =itemView.findViewById(R.id.veritemfactura_precioU);
            veritemfactura_precioT =itemView.findViewById(R.id.veritemfactura_precioTotal);
            veritemfactura_cantidad =itemView.findViewById(R.id.veritemfactura_cantidad);


        }
    }
}