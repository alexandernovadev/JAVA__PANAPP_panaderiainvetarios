package com.imperiozipa.panaderiaapp.Provedores;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.imperiozipa.panaderiaapp.Adaptadores.AdaptadorProvedores;
import com.imperiozipa.panaderiaapp.Adaptadores.ProductosAdaptador;
import com.imperiozipa.panaderiaapp.Inventario.EditarProducto;
import com.imperiozipa.panaderiaapp.Modelos.ModeloProvedor;
import com.imperiozipa.panaderiaapp.R;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nullable;

public class ProvedoresFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    FloatingActionButton fabprovedoradd;
    View vista;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    public ArrayList<ModeloProvedor> ALprovedor;
    AdaptadorProvedores adaptadorProvedores;
    private RecyclerView recyvclerviewprovedor;
    EditText buscarprovedor;

    public ProvedoresFragment(){}

    public static ProvedoresFragment newInstance(String param1, String param2)
    {
        ProvedoresFragment fragment = new ProvedoresFragment();
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

        vista = inflater.inflate(R.layout.fragment_provedores, container, false);

        fabprovedoradd = vista.findViewById(R.id.fabaddprpvedor);

        // ToDo Hacer una imagen que diga No hay facturas pendientes jdbsfuidsbg8
        fabprovedoradd
        .setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),Addprovedor.class);
                getActivity().startActivity(intent);
            }
        });

        recyvclerviewprovedor = (RecyclerView) vista.findViewById(R.id.recycleprovedor);
        recyvclerviewprovedor.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarProvedor();


        buscarprovedor = vista.findViewById(R.id.ET_buscarprovedor);
        buscarprovedor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                filterProvedor(s.toString());
            }
        });
        return vista;
    }

    public void  filterProvedor(String datoabucar)
    {
        final ArrayList<ModeloProvedor> filteredList = new ArrayList<>();
        for (ModeloProvedor item : ALprovedor)
        {
            if (item.getMarca_provedor().toLowerCase().contains(datoabucar.toLowerCase()))
            {filteredList.add(item);}
        }

        adaptadorProvedores = new AdaptadorProvedores(getContext(),(filteredList));
        adaptadorProvedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View posicion) {
                editprovedor(posicion, filteredList);
            }
        });

        recyvclerviewprovedor.setAdapter(adaptadorProvedores);
    }

    private void llenarProvedor()
    {
        basedatos
        .collection("provedores")
        .addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e)
            {
                if (e != null) { return; }

                ALprovedor = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloProvedor provedoritem = doc.toObject(ModeloProvedor.class);
                    ALprovedor.add(provedoritem);
                }

                adaptadorProvedores = new AdaptadorProvedores(getContext(),ALprovedor);

                adaptadorProvedores.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        editprovedor(v,ALprovedor);
                    }
                });

                recyvclerviewprovedor.setAdapter(adaptadorProvedores);
            }
        });

    }

    private void editprovedor(View posicion, ArrayList<ModeloProvedor> ALprovedorRECOBIDO)
    {
        Intent intent = new Intent(getContext(), EditProvedorActivity.class);

        ModeloProvedor item = ALprovedorRECOBIDO
                .get(recyvclerviewprovedor.getChildAdapterPosition(posicion));
        Bundle bundle=new Bundle();
        bundle.putSerializable("Provedor",item);
        intent.putExtras(bundle);
        startActivity(intent);
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
        } else
            {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() { super.onDetach();mListener = null; }
    public interface OnFragmentInteractionListener{void onFragmentInteraction(Uri uri);}
}
