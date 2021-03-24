package com.imperiozipa.panaderiaapp.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imperiozipa.panaderiaapp.Modelos.ModeloInventario;
import com.imperiozipa.panaderiaapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductosAdaptador extends RecyclerView.Adapter<ProductosAdaptador.Vistaproductos>
    implements View.OnClickListener
{
    //Contructor con Eventlistener ??

    Context context;
    ArrayList<ModeloInventario> arrayListInvetario;
    private View.OnClickListener listener;
    View vista;
    DecimalFormat formateo;

    public ProductosAdaptador(Context context, ArrayList<ModeloInventario> arrayListInvetario) {
        this.context = context;
        this.arrayListInvetario = arrayListInvetario;
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }

    @NonNull
    @Override
    public Vistaproductos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        vista= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_productos,viewGroup,false);

        // Habia que configurar el andcho del Recycler aqui en el adaptador, ancho y alto.
        vista.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));

        vista.setOnClickListener(this);

        return new Vistaproductos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Vistaproductos vistaproductos, int position)
    {
        formateo = new DecimalFormat("###,###.##");
        int pventa = arrayListInvetario.get(position).getPrecio_venta();
        String pventaformat = formateo.format(pventa);

        int pprovedor = arrayListInvetario.get(position).getPrecio_compra();
        String pprovedorformat = formateo.format(pprovedor);

        vistaproductos.nombreitem.setText(arrayListInvetario.get(position).getNombre());
        vistaproductos.provedoritem.setText(arrayListInvetario.get(position).getProvedor());
        vistaproductos.cantitem.setText(String.valueOf(arrayListInvetario.get(position).getCantidad()));
        vistaproductos.precioitem.setText(pventaformat);
        vistaproductos.procioprovcedor.setText(pprovedorformat);

        Glide.with(context)
                .load(arrayListInvetario.get(position).getUrl_image())
                .into(vistaproductos.fotoitem);
    }

    @Override
    public int getItemCount()
    {
        return arrayListInvetario.size();
    }

    @Override
    public void onClick(View v)
    {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class Vistaproductos extends RecyclerView.ViewHolder
    {
        TextView nombreitem, provedoritem, cantitem, precioitem,procioprovcedor;
        ImageView fotoitem;
        public Vistaproductos(@NonNull View itemView)
        {
            super(itemView);
            nombreitem =itemView.findViewById(R.id.nombreitempro);
            procioprovcedor =itemView.findViewById(R.id.TVprecioprovedorproductos);
            provedoritem =itemView.findViewById(R.id.provedorcardpro);
            cantitem =itemView.findViewById(R.id.cantidadcardpro);
            precioitem =itemView.findViewById(R.id.preciocardpro);

            fotoitem = itemView.findViewById(R.id.imageitepro);

        }
    }
}
