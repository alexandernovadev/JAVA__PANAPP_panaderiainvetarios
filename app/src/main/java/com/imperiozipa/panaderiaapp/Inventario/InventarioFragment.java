package com.imperiozipa.panaderiaapp.Inventario;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.imperiozipa.panaderiaapp.Adaptadores.ProductosAdaptador;
import com.imperiozipa.panaderiaapp.Modelos.ModeloInventario;
import com.imperiozipa.panaderiaapp.Modelos.ModeloProvedor;
import com.imperiozipa.panaderiaapp.R;

import java.util.ArrayList;
import java.util.List;

public class InventarioFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    View vista;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    public ArrayList<ModeloInventario> ALproductosinventario;
    public ProductosAdaptador productosAdaptador;
    private RecyclerView recyvclerviewInventario;

    EditText buscarproductoinventario;
    LinearLayout btnfilterproductos;

    FloatingActionMenu fabmenuInventario;
    FloatingActionButton fabinewcategoryinventario, fabinewproductosinventario;

    List<String> filterlistaProvedor;
    ArrayAdapter<String> filteradapterSpinnerProvedor;
    Spinner filterspSelectProvedor;

    Button btnfitteradd;

    LinearLayout btnclosefilterinventario;
    Dialog modalflterinventario;

    public InventarioFragment() {}

     public static InventarioFragment newInstance(String param1, String param2)
     {
        InventarioFragment fragment = new InventarioFragment();
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
        vista = inflater.inflate(R.layout.fragment_inventario,
                container, false);

        fabmenuInventario =  vista.findViewById(R.id.fabmenuinventario);
        fabinewcategoryinventario =  vista.findViewById(R.id.fabaddcategoriainventario);
        fabinewproductosinventario = vista.findViewById(R.id.fabaddproductoinventario);

//        filterspSelectProvedor = vista.findViewById(R.id.);


        btnfilterproductos = vista.findViewById(R.id.llbtnfilterproductos);
        btnfilterproductos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modalfilterproducto();

            }
        });


        fabinewcategoryinventario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabmenuInventario.close(true);
                Intent intent = new Intent(getActivity(), Categoriaproducto.class);
                getActivity().startActivity(intent);

            }
        });

        fabinewproductosinventario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabmenuInventario.close(true);
                Intent intent = new Intent(getActivity(), AddProducto.class);
                getActivity().startActivity(intent);
            }
        });

        recyvclerviewInventario = (RecyclerView) vista.findViewById(R.id.recycleinventario);
        recyvclerviewInventario.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarproductos();

        buscarproductoinventario = vista.findViewById(R.id.ET_buscarinventario);
        buscarproductoinventario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                filterInventario(s.toString());
            }
        });



        return vista;
    }

    public void filterInventario(String txt)
    {
        final ArrayList<ModeloInventario> filteredList = new ArrayList<>();
        for (ModeloInventario item : ALproductosinventario)
        {
            //TODO OJO CON ESTO JAJA
            if (item.getNombre().toLowerCase().contains(txt.toLowerCase())){filteredList.add(item);}
        }

        productosAdaptador = new ProductosAdaptador(getContext(),(filteredList));
        productosAdaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View posicion) {
                editproductosInventario(posicion, filteredList);
            }
        });

        recyvclerviewInventario.setAdapter(productosAdaptador);
    }

    private void modalfilterproducto()
    {
        modalflterinventario = new Dialog(getActivity());
        modalflterinventario.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modalflterinventario.setContentView(R.layout.modal_filtrarinventario);
        modalflterinventario.setCancelable(false);

        btnclosefilterinventario = modalflterinventario.findViewById(R.id.LLbtnclosefilterinventario);
        btnclosefilterinventario.setEnabled(true);

        btnclosefilterinventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalflterinventario.cancel();
            }
        });

        filterspSelectProvedor= modalflterinventario.findViewById(R.id.spinner_filterproducto);
        filterllenarspinnerprovedor();

        btnfitteradd = modalflterinventario.findViewById(R.id.BtnSavefilterinventario);
        btnfitteradd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext()
                        , (String) filterspSelectProvedor.getSelectedItem(), Toast.LENGTH_SHORT).show();

                filterTOProvedor((String) filterspSelectProvedor.getSelectedItem());
//        Log.w("Pruebaa", (String) filterspSelectProvedor.getSelectedItem());

            }
        });

        modalflterinventario.show();
    }

    public void filterTOProvedor(String txt)
    {
        final ArrayList<ModeloInventario> filteredList = new ArrayList<>();
        for (ModeloInventario item : ALproductosinventario)
        {
            if (item.getProvedor().toLowerCase().contains(txt.toLowerCase())){filteredList.add(item);}
        }

        productosAdaptador = new ProductosAdaptador(getContext(),(filteredList));
        productosAdaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View posicion) {
                editproductosInventario(posicion, filteredList);
            }
        });

        recyvclerviewInventario.setAdapter(productosAdaptador);
    }


    private void filterllenarspinnerprovedor()
    {
        basedatos
        .collection("provedores")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) { return; }

                filterlistaProvedor = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloProvedor newcatefory = doc.toObject(ModeloProvedor.class);
                    filterlistaProvedor.add(newcatefory.getMarca_provedor());
                }

                filteradapterSpinnerProvedor = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, filterlistaProvedor);
                filteradapterSpinnerProvedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                filterspSelectProvedor.setAdapter(filteradapterSpinnerProvedor);
            }
        });
    }

    private void llenarproductos()
    {
        basedatos
        .collection("productos")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) { return; }

                ALproductosinventario = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloInventario newbill = doc.toObject(ModeloInventario.class);
                    ALproductosinventario.add(newbill);
                }

                productosAdaptador = new ProductosAdaptador(getContext(),ALproductosinventario);

                productosAdaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        editproductosInventario(v,ALproductosinventario);
                    }
                });

                filterInventario(buscarproductoinventario.getText().toString());
                recyvclerviewInventario.setAdapter(productosAdaptador);
            }
        });
    }


    private void editproductosInventario(View posicion, ArrayList<ModeloInventario> filteredList)
    {
        Intent intent = new Intent(getContext(), EditarProducto.class);

        ModeloInventario item = filteredList
                .get(recyvclerviewInventario.getChildAdapterPosition(posicion));
        Bundle bundle=new Bundle();
        bundle.putSerializable("Producto",item);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) { mListener.onFragmentInteraction(uri);}
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {super.onDetach();mListener = null; }

    public interface OnFragmentInteractionListener {void onFragmentInteraction(Uri uri);}
}
