package com.imperiozipa.panaderiaapp.Ventas;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.imperiozipa.panaderiaapp.Adaptadores.AdaptadorVentasProductos;
import com.imperiozipa.panaderiaapp.Adaptadores.AdaptadorveritemsFacturas;
import com.imperiozipa.panaderiaapp.Modelos.ModeloFacturaVentas;
import com.imperiozipa.panaderiaapp.Modelos.ModeloInventario;
import com.imperiozipa.panaderiaapp.Modelos.ModeloProductoFactura;
import com.imperiozipa.panaderiaapp.R;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;

import java.util.ArrayList;
import java.util.List;


public class VerFacturaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View vista;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    public ArrayList<ModeloProductoFactura> ALprocductosFactura;
    public AdaptadorveritemsFacturas adaptadorveritemsFacturas;
    private RecyclerView recyvclerviewProductosbill;

    public String ideFacturayoEdit;

    Vibrator vibrator;
    MediaPlayer bipsound;

    public VerFacturaFragment(){}

    public static VerFacturaFragment newInstance(String param1, String param2) {
        VerFacturaFragment fragment = new VerFacturaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_ver_factura, container, false);
        ideFacturayoEdit =  getArguments().getString("IdToVerFactura");

        recyvclerviewProductosbill = (RecyclerView) vista.findViewById(R.id.recycleritemproductofactura);
        recyvclerviewProductosbill.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarproductosFactura(ideFacturayoEdit);

        vibrator = (Vibrator) vista
                .getContext()
                .getApplicationContext()
                .getSystemService(Context.VIBRATOR_SERVICE);

        bipsound = MediaPlayer.create(getContext(), R.raw.gota);

        return vista;
    }

    private void llenarproductosFactura(final String idFacToedit) {
        final CollectionReference docRef = basedatos
                .collection("facturasventas")
                .document(idFacToedit)
                .collection("productosfactura");

        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Men:", "Listen failed.", e);
                    return;
                }
                ALprocductosFactura = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                 ModeloProductoFactura newdate = doc.toObject(ModeloProductoFactura.class);
                 ALprocductosFactura.add(newdate);
                }
                adaptadorveritemsFacturas = new AdaptadorveritemsFacturas(ALprocductosFactura);
                adaptadorveritemsFacturas.setOnClickListener(new DoubleClick(new DoubleClickListener() {
                    @Override
                    public void onSingleClick(View view)
                    {
                        final ModeloProductoFactura item = ALprocductosFactura
                                .get(recyvclerviewProductosbill
                                        .getChildAdapterPosition(view));

                        /*Snackbar.make(view, item.getName_producto()+" Eliminada", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();*/
                    }

                    @Override
                    public void onDoubleClick(View view)
                    {
                        removeproductofactura(view,ALprocductosFactura);
                    }
                }));

                recyvclerviewProductosbill.setAdapter(adaptadorveritemsFacturas);
            }


        });
    }

    private void removeproductofactura(View posicion, ArrayList<ModeloProductoFactura> ALrecibido)
    {

        final ModeloProductoFactura item = ALprocductosFactura
                .get(recyvclerviewProductosbill
                        .getChildAdapterPosition(posicion));

        final DocumentReference Refproductotobill = basedatos
                .collection("facturasventas")
                .document(ideFacturayoEdit)
                .collection("productosfactura")
                .document(item.getId());

        final DocumentReference RefBill = basedatos
                .collection("facturasventas")
                .document(ideFacturayoEdit);

        Refproductotobill.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        ModeloProductoFactura dateproductobillold = document.
                                toObject(ModeloProductoFactura.class);


                        if (dateproductobillold.getCantidad() == 1)
                        {
                            Refproductotobill.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                //Colocar vibracion y sonido
                                    Toast.makeText(getContext(), "REMOVE"
                                            , Toast.LENGTH_SHORT).show();
                                    updatebill(RefBill, item.getPrecioUnidad());
                                }
                            });
                        }
                        else
                        {
                            int nuevacantidad = dateproductobillold.getCantidad() - 1;
                            int preciototal = dateproductobillold.getPrecioTotal()
                                    - dateproductobillold.getPrecioUnidad();

                            Refproductotobill.update("cantidad" , nuevacantidad,
                                    "precioTotal",preciototal)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "REMOVE UPDATE"
                                                    , Toast.LENGTH_SHORT).show();
                                            updatebill(RefBill, item.getPrecioUnidad());
                                        }
                                    });

                        }
                    }
                    else {/*DOCUEMNTO NO EXISTE*/}
                }
                else {/*ERROR DE FIREBASEQUERY*/}
            }
        });
    }

    private void updatebill(final DocumentReference RefBill, final int precioproducto)
    {
        vibrator.vibrate(500);
        bipsound.start();
        RefBill.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            ModeloFacturaVentas billtoedit = documentSnapshot.toObject(ModeloFacturaVentas.class);
            RefBill.update("total_factura", billtoedit.getTotal_factura()- precioproducto);
            }
        });
    }
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) { mListener.onFragmentInteraction(uri);}
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
