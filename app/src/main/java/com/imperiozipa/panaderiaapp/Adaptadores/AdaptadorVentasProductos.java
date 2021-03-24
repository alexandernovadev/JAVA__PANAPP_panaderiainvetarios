package com.imperiozipa.panaderiaapp.Adaptadores;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.imperiozipa.panaderiaapp.Modelos.ModeloFacturaVentas;
import com.imperiozipa.panaderiaapp.Modelos.ModeloInventario;
import com.imperiozipa.panaderiaapp.Modelos.ModeloProductoFactura;
import com.imperiozipa.panaderiaapp.R;
import com.imperiozipa.panaderiaapp.Ventas.ProductosToBillsFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptadorVentasProductos extends RecyclerView.Adapter<AdaptadorVentasProductos.VistaVentasProductos>
        implements View.OnClickListener, ProductosToBillsFragment.OnFragmentInteractionListener
{
    ArrayList<ModeloInventario> arrayListInvetarioVentas;
    private View.OnClickListener listener;
    View vista;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    ProductosToBillsFragment productosToBillsFragment;
    DecimalFormat formateador;

    String IDEbill;

    public AdaptadorVentasProductos(ArrayList<ModeloInventario> arrayListInvetarioVentas, String IDEbill) {
        this.arrayListInvetarioVentas = arrayListInvetarioVentas;
        this.IDEbill = IDEbill;
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }

    @NonNull
    @Override
    public AdaptadorVentasProductos.VistaVentasProductos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        vista= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.itemproscutostobill,viewGroup,false);

        // Habia que configurar el andcho del Recycler aqui en el adaptador, ancho y alto.
        vista.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));

        productosToBillsFragment = new ProductosToBillsFragment();
        vista.setOnClickListener(this);

        return new VistaVentasProductos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorVentasProductos.VistaVentasProductos vistaVentasProductos, int i)
    {
        formateador = new DecimalFormat("###,###.##");
        int precioventaa =  arrayListInvetarioVentas.get(i).getPrecio_venta();

        String valorformat = formateador.format(precioventaa);

        vistaVentasProductos.nameprodutobiil
                .setText(arrayListInvetarioVentas.get(i).getNombre());
        vistaVentasProductos.precioprodutobill
                .setText(valorformat);

        final int cantactual = arrayListInvetarioVentas.get(i).getCantidad();
        final String ideproductobill = arrayListInvetarioVentas.get(i).getIdgen();
        final String name = arrayListInvetarioVentas.get(i).getNombre();
        final int precioU = arrayListInvetarioVentas.get(i).getPrecio_venta();
        final int iva = arrayListInvetarioVentas.get(i).getIva();

        if (cantactual == 0)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.VISIBLE);
            vistaVentasProductos.btnone.setVisibility(View.GONE);
            vistaVentasProductos.btndos.setVisibility(View.GONE);
            vistaVentasProductos.btntres.setVisibility(View.GONE);
            vistaVentasProductos.btncuatro.setVisibility(View.GONE);
            vistaVentasProductos.btnfive.setVisibility(View.GONE);
            vistaVentasProductos.btnseis.setVisibility(View.GONE);
            vistaVentasProductos.btnsiete.setVisibility(View.GONE);
            vistaVentasProductos.btnocho.setVisibility(View.GONE);
            vistaVentasProductos.btnnueve.setVisibility(View.GONE);
            vistaVentasProductos.btnten.setVisibility(View.GONE);
        }

        if (cantactual > 0)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btnone.setVisibility(View.VISIBLE);
            vistaVentasProductos.btnone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 1);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*1);

                    int newcantidainventario = cantactual - 1;
                    updatebilleinventario(ideproductobill,newproductobill,newcantidainventario);
                }
            });
        }

        if (cantactual >= 2)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btndos.setVisibility(View.VISIBLE);
            vistaVentasProductos.btndos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 2);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*2);

                    int newcantidainventario = cantactual - 2;
                    updatebilleinventario(ideproductobill,newproductobill,newcantidainventario);
                }
            });
        }

        if (cantactual >= 3)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btntres.setVisibility(View.VISIBLE);
            vistaVentasProductos.btntres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 3);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*3);

                    int newcantidainventario = cantactual - 3;
                    updatebilleinventario(ideproductobill,newproductobill,newcantidainventario);
                }
            });
        }

        if (cantactual >= 4)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btncuatro.setVisibility(View.VISIBLE);
            vistaVentasProductos.btncuatro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 4);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*4);

                    int newcantidainventario = cantactual - 4;
                    updatebilleinventario(ideproductobill,newproductobill,newcantidainventario);
                }
            });
        }


        if (cantactual >= 5)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btnfive.setVisibility(View.VISIBLE);
            vistaVentasProductos.btnfive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 5);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*5);

                    int newcantidainventario = cantactual - 5;
                    updatebilleinventario(ideproductobill,newproductobill, newcantidainventario);
                }
            });
        }

        if (cantactual >= 6)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btnseis.setVisibility(View.VISIBLE);
            vistaVentasProductos.btnseis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 6);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*6);

                    int newcantidainventario = cantactual - 6;
                    updatebilleinventario(ideproductobill,newproductobill,newcantidainventario);
                }
            });
        }

        if (cantactual >= 7)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btnsiete.setVisibility(View.VISIBLE);
            vistaVentasProductos.btnsiete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 7);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*7);

                    int newcantidainventario = cantactual - 7;
                    updatebilleinventario(ideproductobill,newproductobill,newcantidainventario);
                }
            });
        }

        if (cantactual >= 8)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btnocho.setVisibility(View.VISIBLE);
            vistaVentasProductos.btnocho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 8);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*8);

                    int newcantidainventario = cantactual - 8;
                    updatebilleinventario(ideproductobill,newproductobill,newcantidainventario);
                }
            });
        }

        if (cantactual >= 9)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btnnueve.setVisibility(View.VISIBLE);
            vistaVentasProductos.btnnueve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 9);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*9);

                    int newcantidainventario = cantactual - 9;
                    updatebilleinventario(ideproductobill,newproductobill,newcantidainventario);
                }
            });
        }

        if (cantactual >= 10)
        {
            vistaVentasProductos.msgagotado.setVisibility(View.INVISIBLE);
            vistaVentasProductos.btnten.setVisibility(View.VISIBLE);
            vistaVentasProductos.btnten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> newproductobill = new HashMap<>();
                    newproductobill.put("id", ideproductobill);
                    newproductobill.put("name_producto", name);
                    newproductobill.put("cantidad", 10);
                    newproductobill.put("precioUnidad", precioU);
                    newproductobill.put("iva",iva);
                    newproductobill.put("precioTotal",precioU*10);

                    int newcantidainventario = cantactual - 10;
                    updatebilleinventario(ideproductobill,newproductobill,newcantidainventario);
                }
            });
        }

    }

    public void updatebilleinventario (final String ideproductofactura, final Map<String,Object> producto,final int cantinventario)
    {
        final DocumentReference Refproductotobill = basedatos
                .collection("facturasventas")
                .document(IDEbill)
                .collection("productosfactura")
                .document(ideproductofactura);

        final DocumentReference RefBill = basedatos
                .collection("facturasventas")
                .document(IDEbill);

        final Map<String,Object> updateinventario = new HashMap<>();
        updateinventario.put("id" , producto.get("id"));
        updateinventario.put("cantidadnew" , cantinventario);



        Refproductotobill.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        ModeloProductoFactura dateproductobillold = document.
                        toObject(ModeloProductoFactura.class);


                        String cantidad = producto.get("cantidad").toString();
                        int cantidaconvert = Integer.parseInt(cantidad);

                        String prociotoal = producto.get("precioUnidad").toString();
                        int precioTconvert = Integer.parseInt(prociotoal);

                        final int totalmul = cantidaconvert * precioTconvert;

                        int nuevacantidad = dateproductobillold.getCantidad() + cantidaconvert;
                        int preciototal = dateproductobillold.getPrecioTotal() + totalmul;

                        Refproductotobill
                        .update(
                        "cantidad" , nuevacantidad,
                        "precioTotal",preciototal
                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                updatebill(RefBill, totalmul,updateinventario);
                            }
                        });
                    }
                    else
                    {
                        Refproductotobill
                        .set(producto)
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                String precioUst = producto.get("precioTotal").toString();
                                int procionew = Integer.parseInt(precioUst);
                                updatebill(RefBill, procionew,updateinventario);
                            }
                        });
                    }

                }
                else {/*ERROR DE FIREBASEQUERY*/}
            }
        });
    }

    public void updatebill(final DocumentReference ref, final int precioventa, final Map<String,Object> datosinventario)
    {
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ModeloFacturaVentas billtoedit = documentSnapshot.toObject(ModeloFacturaVentas.class);

                ref.update("total_factura", precioventa + billtoedit.getTotal_factura());

                basedatos.collection("productos")
                        .document((String) datosinventario.get("id"))
                        .update("cantidad",datosinventario.get("cantidadnew"));
                ProductosToBillsFragment.welldoneadd();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListInvetarioVentas.size();
    }


    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class VistaVentasProductos extends RecyclerView.ViewHolder
    {
        TextView nameprodutobiil, precioprodutobill,msgagotado;
        LinearLayout btnone,btndos,btntres,btncuatro,btnfive,btnseis,btnsiete,btnocho,btnnueve,btnten;

        public VistaVentasProductos(@NonNull View itemView)
        {
            super(itemView);
            nameprodutobiil = itemView.findViewById(R.id.nameproductotobill);
            precioprodutobill = itemView.findViewById(R.id.precioproductotobill);
            msgagotado = itemView.findViewById(R.id.msgproductoagotado);
            btnone = itemView.findViewById(R.id.linearbtnone);
            btndos = itemView.findViewById(R.id.linearbtndos);
            btntres = itemView.findViewById(R.id.linearbtntres);
            btncuatro = itemView.findViewById(R.id.linearbtncuatro);
            btnfive = itemView.findViewById(R.id.linearbtncinco);
            btnseis = itemView.findViewById(R.id.linearseis);
            btnsiete = itemView.findViewById(R.id.linearsiete);
            btnocho = itemView.findViewById(R.id.linearocho);
            btnnueve = itemView.findViewById(R.id.linearnueve);
            btnten = itemView.findViewById(R.id.linearbtndiez);


            btnone.setVisibility(View.INVISIBLE);
            btndos.setVisibility(View.INVISIBLE);
            btntres.setVisibility(View.INVISIBLE);
            btncuatro.setVisibility(View.INVISIBLE);
            btnfive.setVisibility(View.INVISIBLE);
            btnseis.setVisibility(View.INVISIBLE);
            btnsiete.setVisibility(View.INVISIBLE);
            btnocho.setVisibility(View.INVISIBLE);
            btnnueve.setVisibility(View.INVISIBLE);
            btnten.setVisibility(View.INVISIBLE);
            msgagotado.setVisibility(View.INVISIBLE);

        }
    }
}
