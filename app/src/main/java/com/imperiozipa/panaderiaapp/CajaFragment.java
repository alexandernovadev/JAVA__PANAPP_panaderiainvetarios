package com.imperiozipa.panaderiaapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.imperiozipa.panaderiaapp.Modelos.ModeloFacturaVentas;

import java.text.DecimalFormat;


public class CajaFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();

    private OnFragmentInteractionListener mListener;

    public CajaFragment()
    {

    }

    public static CajaFragment newInstance(String param1, String param2)
    {
        CajaFragment fragment = new CajaFragment();
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

        View vista = inflater.inflate(R.layout.fragment_caja, container, false);

        final TextView total = vista.findViewById(R.id.totalventass);

        basedatos.collection("facturasventas")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int totalevents = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ModeloFacturaVentas facts = document.toObject(ModeloFacturaVentas.class);
                        totalevents = totalevents +facts.getTotal_factura();
                    }
                    DecimalFormat formateo;
                    formateo = new DecimalFormat("###,###.##");
                    int pventa = totalevents;
                    String pventaformat = formateo.format(pventa);

                    total.setText(pventaformat);
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });

        return vista;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
