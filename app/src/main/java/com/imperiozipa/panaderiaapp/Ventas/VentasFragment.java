package com.imperiozipa.panaderiaapp.Ventas;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.imperiozipa.panaderiaapp.Adaptadores.FacturaVentasAdaptador;
import com.imperiozipa.panaderiaapp.Modelos.ModeloFacturaVentas;
import com.imperiozipa.panaderiaapp.Modelos.ModeloInventario;
import com.imperiozipa.panaderiaapp.Modelos.Modelocontadorfacturas;
import com.imperiozipa.panaderiaapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;


public class VentasFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    FloatingActionButton fabi;
    View vista;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    public ArrayList<ModeloFacturaVentas> ALfacturastosell;
    public FacturaVentasAdaptador facturaVentasAdaptador;
    private RecyclerView recyvclerviewbillstosell;
    Vibrator vibrator;
    MediaPlayer bipsound;

    public VentasFragment(){}

    public static VentasFragment newInstance(String param1, String param2)
    {
        VentasFragment fragment = new VentasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        vista = inflater.inflate(R.layout.fragment_ventas,
                container, false);

        View view = vista.findFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) vista.getContext()
                    .getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        fabi = vista.findViewById(R.id.fabaddbill);
        bipsound = MediaPlayer.create(getContext(), R.raw.weldone);

        // ToDo Hacer una imagen que diga No hay facturas pendientes jdbsfuidsbg8


        //ToDo Arreglar esto del numero
        final int[] numerdfac = new int[1];
        basedatos.collection("contadorfacturasventas")
                .document("8jECL4xY8Xgcbt13URxA")
       .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Modelocontadorfacturas contadore = documentSnapshot.toObject(Modelocontadorfacturas.class);
                numerdfac[0] = contadore.getContador();
            }
        });

        final Date date = new Date();
        fabi
        .setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String IDgenerado = UUID.randomUUID().toString();
                ModeloFacturaVentas newbill = new ModeloFacturaVentas(
                    IDgenerado,
                    "UserAqui",
                    "cliente_45",
                    0,
                    "pendiente",
                    "",
                    date,
                    numerdfac[0]+1);

                basedatos
                .collection("facturasventas").document(IDgenerado).set(newbill)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "NuevaFactura", Toast.LENGTH_SHORT).show();
                    vibrator.vibrate(400);
                    bipsound.start();
                    }
                });
            }
        });

        recyvclerviewbillstosell = (RecyclerView) vista.findViewById(R.id.recyclerFacturaSimple);
        recyvclerviewbillstosell.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarFacturaSimple();

        vibrator = (Vibrator) vista
                .getContext()
                .getApplicationContext()
                .getSystemService(Context.VIBRATOR_SERVICE);
        return vista;
    }

    private void llenarFacturaSimple()
    {
         basedatos
         .collection("facturasventas").whereEqualTo("estado","pendiente")
         .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) { return; }
                ALfacturastosell = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloFacturaVentas newbill = doc.toObject(ModeloFacturaVentas.class);
                    ALfacturastosell.add(newbill);
                }
                facturaVentasAdaptador = new FacturaVentasAdaptador(ALfacturastosell);

                facturaVentasAdaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getContext(), VenderProductos.class);

                        ModeloFacturaVentas item = ALfacturastosell
                                .get(recyvclerviewbillstosell.getChildAdapterPosition(v));
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("FacturaVenta",item);
                        intent.putExtras(bundle);
                        startActivity(intent);
                     }
                });

                recyvclerviewbillstosell.setAdapter(facturaVentasAdaptador);
            }
        });

    }

    public void onButtonPressed(Uri uri){if (mListener != null){mListener.onFragmentInteraction(uri);}}

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach(){super.onDetach();mListener = null;}

    public interface OnFragmentInteractionListener{void onFragmentInteraction(Uri uri);}
}
