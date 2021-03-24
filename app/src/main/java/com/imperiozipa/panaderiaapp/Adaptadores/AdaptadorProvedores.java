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
import com.imperiozipa.panaderiaapp.Modelos.ModeloProvedor;
import com.imperiozipa.panaderiaapp.R;

import java.util.ArrayList;

public class AdaptadorProvedores extends RecyclerView.Adapter<AdaptadorProvedores.Vprovedores>
implements View.OnClickListener
{

    Context context;
    ArrayList<ModeloProvedor> arrayListProvedor;
    private View.OnClickListener listener;
    View vista;

    public AdaptadorProvedores() {}

    public AdaptadorProvedores(Context context, ArrayList<ModeloProvedor> arrayListProvedor) {
        this.context = context;
        this.arrayListProvedor = arrayListProvedor;
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }

    @NonNull
    @Override
    public AdaptadorProvedores.Vprovedores onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos)
    {
        vista  = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.itemprovedor,viewGroup,false);

        // Habia que configurar el andcho del Recycler aqui en el adaptador, ancho y alto.
        vista.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));

        vista.setOnClickListener(this);

        return new Vprovedores(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorProvedores.Vprovedores vprovedores, int pos)
    {
        vprovedores.marcaprovedor.setText(arrayListProvedor.get(pos).getMarca_provedor());
        vprovedores.nameprovedor.setText(arrayListProvedor.get(pos).getNombre_provedor());
        vprovedores.emailprovedor.setText(arrayListProvedor.get(pos).getEmailprovedor());
        vprovedores.diasvista.setText(arrayListProvedor.get(pos).getDiasvisita().toString());
        vprovedores.diasentrega.setText(arrayListProvedor.get(pos).getDiasentrega().toString());
        vprovedores.tel1provedor.setText(String.valueOf(arrayListProvedor.get(pos).getTelefono1()));
        vprovedores.tel2provedor.setText(String.valueOf(arrayListProvedor.get(pos).getTelefono2()));

        Glide.with(context)
                .load(arrayListProvedor.get(pos).getUrlimagen())
                .into(vprovedores.imgprovedor);
    }

    @Override
    public int getItemCount() {return arrayListProvedor.size(); }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class Vprovedores extends RecyclerView.ViewHolder
    {
        TextView marcaprovedor,nameprovedor,emailprovedor,tel1provedor,tel2provedor,diasvista,diasentrega;
        ImageView imgprovedor;
        public Vprovedores(@NonNull View itemView)
        {
            super(itemView);

            marcaprovedor = itemView.findViewById(R.id.TVmarcaprovedor);
            nameprovedor = itemView.findViewById(R.id.TVnameprovedor);
            emailprovedor = itemView.findViewById(R.id.Tvcorreoprovedor);
            tel1provedor = itemView.findViewById(R.id.TVtelefono1provedor);
            tel2provedor = itemView.findViewById(R.id.TVtelefono2provedor);
            diasvista = itemView.findViewById(R.id.TVdiasvisita);
            diasentrega = itemView.findViewById(R.id.TVdiasentrega);

            imgprovedor = itemView.findViewById(R.id.IMGprovedor);
        }
    }
}
