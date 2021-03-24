package com.imperiozipa.panaderiaapp.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.imperiozipa.panaderiaapp.Inventario.Categoriaproducto;
import com.imperiozipa.panaderiaapp.Modelos.ModeloCateoriaproductos;

import com.imperiozipa.panaderiaapp.R;

import java.util.ArrayList;

public class Adaptadorcategoriasproductos extends RecyclerView
        .Adapter<Adaptadorcategoriasproductos.Vcategoriaproducto>
implements View.OnClickListener
{

    Context context;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    ArrayList<ModeloCateoriaproductos> ALcategoriaproductos;
    private View.OnClickListener listener;
    View vista;

    public Adaptadorcategoriasproductos(){}

    public Adaptadorcategoriasproductos(Context context, ArrayList<ModeloCateoriaproductos> ALcategoriaproductos) {
        this.context = context;
        this.ALcategoriaproductos = ALcategoriaproductos;
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }
    @NonNull
    @Override
    public Vcategoriaproducto onCreateViewHolder(@NonNull ViewGroup viewGroup, int posicion)
    {
        vista= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.itemcategoriaproductos,viewGroup,false);

        // Habia que configurar el andcho del Recycler aqui en el adaptador, ancho y alto.
        vista.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));

        vista.setOnClickListener(this);

        return new Vcategoriaproducto(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final Vcategoriaproducto vcategoriaproducto, final int posicion)
    {
        vcategoriaproducto.namecategoria.setText(ALcategoriaproductos.get(posicion).getNombrecategoria());
        vcategoriaproducto.descripcioncategoria.setText(ALcategoriaproductos.get(posicion).getDescripcioncategoria());

        vcategoriaproducto.btneditcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basedatos.collection("category_product").document(ALcategoriaproductos.get(posicion).getIdcategoria())
                        .update("nombrecategoria",vcategoriaproducto.namecategoria.getText().toString()
                        ,"descripcioncategoria",vcategoriaproducto.descripcioncategoria.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(context,
                                "Se modifico ", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        vcategoriaproducto.btndeletecategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basedatos.collection("category_product").document(ALcategoriaproductos.get(posicion).getIdcategoria())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,
                                "Se Elimino ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount(){return ALcategoriaproductos.size();}

    @Override
    public void onClick(View v){}

    public class Vcategoriaproducto extends RecyclerView.ViewHolder
    {
        EditText namecategoria, descripcioncategoria;
        LinearLayout btneditcategory,btndeletecategory;

        public Vcategoriaproducto(@NonNull View itemView)
        {
            super(itemView);

            namecategoria = itemView.findViewById(R.id.ETnamecategoriaproducto);
            descripcioncategoria = itemView.findViewById(R.id.EDdescategoriaproducto);
            btneditcategory = itemView.findViewById(R.id.LLbtneditcategory);
            btndeletecategory = itemView.findViewById(R.id.LLbrndeletecategory);
        }
    }
}
