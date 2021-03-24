package com.imperiozipa.panaderiaapp.Ventas;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.imperiozipa.panaderiaapp.Adaptadores.AdaptadorVentasProductos;
import com.imperiozipa.panaderiaapp.Adaptadores.ProductosAdaptador;
import com.imperiozipa.panaderiaapp.Inventario.EditarProducto;
import com.imperiozipa.panaderiaapp.Modelos.ModeloFacturaVentas;
import com.imperiozipa.panaderiaapp.Modelos.ModeloInventario;
import com.imperiozipa.panaderiaapp.Modelos.ModeloProductoFactura;
import com.imperiozipa.panaderiaapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static android.content.Context.VIBRATOR_SERVICE;

public class ProductosToBillsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Vibrator vibrator;
    private static MediaPlayer bipsound;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    View vista;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    public ArrayList<ModeloInventario> ALproductostobill;
    public AdaptadorVentasProductos adaptadorVentasProductos;
    private RecyclerView recyvclerviewProductostbill;

    EditText txtfilter;
    public String idbillEdit;


    public ProductosToBillsFragment(){}

    public static ProductosToBillsFragment newInstance(String param1, String param2) {
        ProductosToBillsFragment fragment = new ProductosToBillsFragment();
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
                             Bundle savedInstanceState)
    {
        vista = inflater.inflate(R.layout.fragment_productos_to_bills,
                container, false);

        recyvclerviewProductostbill = (RecyclerView) vista.findViewById(R.id.recyclerprocdutotobill);
        recyvclerviewProductostbill.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarproductos();

        txtfilter = vista.findViewById(R.id.EDsearchProdtobill);
        txtfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        idbillEdit = getArguments().getString("IdToProductotoBill");

        vibrator = (Vibrator) vista
                .getContext()
                .getApplicationContext()
                .getSystemService(Context.VIBRATOR_SERVICE);

        bipsound = MediaPlayer.create(getContext(), R.raw.bip);

        return vista;
    }


    private void filter(String text)
    {
        final ArrayList<ModeloInventario> filteredList = new ArrayList<>();
        for (ModeloInventario item : ALproductostobill)
        {
            if (item.getNombre().toLowerCase().contains(text.toLowerCase()))
            {filteredList.add(item);}
        }

        adaptadorVentasProductos = new AdaptadorVentasProductos((filteredList),idbillEdit);
        recyvclerviewProductostbill.setAdapter(adaptadorVentasProductos);
     }

    private void llenarproductos()
    {
        final CollectionReference docRef = basedatos
                .collection("productos");

        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Men:", "Listen failed.", e);
                    return;
                }
                ALproductostobill = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloInventario newdate = doc.toObject(ModeloInventario.class);
                    ALproductostobill.add(newdate);
                }
                adaptadorVentasProductos = new AdaptadorVentasProductos(ALproductostobill,idbillEdit);
                filter(txtfilter.getText().toString());
                recyvclerviewProductostbill.setAdapter(adaptadorVentasProductos);
            }
        });
    }

    public static void welldoneadd()
    {
        vibrator.vibrate(500);
        bipsound.start();
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) { mListener.onFragmentInteraction(uri); }
    }

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener { void onFragmentInteraction(Uri uri);}
}











