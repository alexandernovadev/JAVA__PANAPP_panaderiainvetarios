package com.imperiozipa.panaderiaapp.Ventas;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.imperiozipa.panaderiaapp.Modelos.ModeloFacturaVentas;
import com.imperiozipa.panaderiaapp.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class VenderProductos extends AppCompatActivity
        implements VerFacturaFragment.OnFragmentInteractionListener,
                   ProductosToBillsFragment.OnFragmentInteractionListener,
        View.OnClickListener
{

    Context context;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;

    LinearLayout lineareditcliente, linearPayBill;
    ImageView btn_backVednProcutos;
    TextView totalbill,namecliente,calculototalbill,calculocambiobill,calculoefectivobill;
    ModeloFacturaVentas itembill = null;

    Dialog MyDialogInfoCliente,MyDialogPagarFactura;

    EditText EDnamecliente,EDnotecliente;

    Button btnEditclienteSave, btnAddcashCajaPaybill;

    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();

    public static String IDEFACTURA;

    public String EDITEnamecliente, EDITEnotecliente;
    public  int EDITEtotalbill;

    Vibrator vibrator;
    MediaPlayer bipsound;

    DecimalFormat formateador = new DecimalFormat("###,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_venderproductos);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        btn_backVednProcutos = findViewById(R.id.btnBack_venderproducts);
        btn_backVednProcutos.setOnClickListener(this);


        namecliente = findViewById(R.id.namecliente);

        linearPayBill = findViewById(R.id.linearpayBill);
        linearPayBill.setOnClickListener(this);

        lineareditcliente = findViewById(R.id.lineareditcliente);
        lineareditcliente.setOnClickListener(this);

        totalbill = findViewById(R.id.txtTotalBill);

        context = this;


        final Bundle recibirdatos = getIntent().getExtras();

        if (recibirdatos != null)
        {
            itembill = (ModeloFacturaVentas) recibirdatos.getSerializable("FacturaVenta");

            IDEFACTURA = itembill.getFacturaid();

            final DocumentReference docRef = basedatos
                    .collection("facturasventas")
                    .document(IDEFACTURA);

            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>()
            {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e)
                {
                    if (e != null) {return; }

                    if (snapshot != null && snapshot.exists())
                    {
                        ModeloFacturaVentas updatedaots= snapshot.toObject(ModeloFacturaVentas.class);

                        namecliente.setText(updatedaots.getName_factura());
                        EDITEnamecliente = updatedaots.getName_factura();
                        EDITEnotecliente = updatedaots.getNota();
                        EDITEtotalbill = updatedaots.getTotal_factura();

                        formateador =  new DecimalFormat("###,###.##");
                        int valor = updatedaots.getTotal_factura();
                        String valorST = formateador.format(valor);
                        totalbill.setText(valorST);
                    }
                    else {/*ERROR*/}
                }
            });
        }

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        bipsound = MediaPlayer.create(this, R.raw.registercash);
    }

    public void ocultarteclado()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnBack_venderproducts:
                ocultarteclado();
                finish();
                break;
            case R.id.lineareditcliente:
                ModalEditCliente();
                break;
            case R.id.linearpayBill:
                ModalPagarFactura();
                break;
        }
    }

    public void ModalEditCliente()
    {
        MyDialogInfoCliente = new Dialog(VenderProductos.this);
        MyDialogInfoCliente.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogInfoCliente.setContentView(R.layout.modal_infocliente);
        MyDialogInfoCliente.setCancelable(true);



        EDnamecliente = MyDialogInfoCliente.findViewById(R.id.ETnamecliente);
        EDnotecliente = MyDialogInfoCliente.findViewById(R.id.EDnotecliente);

        EDnamecliente.setText(EDITEnamecliente);
        EDnotecliente.setText(EDITEnotecliente);

        btnEditclienteSave = MyDialogInfoCliente.findViewById(R.id.btnsaveEditCliente);
        btnEditclienteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String EDITnamecliente = EDnamecliente.getText().toString();
                String EDITnotecliente = EDnotecliente.getText().toString();

                Map<String, Object> datos = new HashMap<>();
                datos.put("name_factura", EDITnamecliente);
                datos.put("nota", EDITnotecliente);

                basedatos.collection("facturasventas")
                        .document(itembill.getFacturaid())
                        .update(datos)
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(VenderProductos.this,
                                "Dato Existoso", Toast.LENGTH_SHORT).show();

                        MyDialogInfoCliente.cancel();

                    }
                });
            }
        });
        MyDialogInfoCliente.show();
    }

    public void ModalPagarFactura()
    {
        MyDialogPagarFactura = new Dialog(VenderProductos.this);
        MyDialogPagarFactura.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogPagarFactura.setContentView(R.layout.modal_pagarbilll);
        MyDialogPagarFactura.setCancelable(true);

        btnAddcashCajaPaybill = MyDialogPagarFactura.findViewById(R.id.btnParbillAddCaja);
        btnAddcashCajaPaybill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basedatos.collection("facturasventas")
                         .document(IDEFACTURA)
                         .update("estado","cancelada")
                         .addOnSuccessListener(new OnSuccessListener<Void>()
                          {
                            @Override
                            public void onSuccess(Void aVoid)
                             {
                                 vibrator.vibrate(500);
                                 bipsound.start();
                                 MyDialogPagarFactura.cancel();
                                finish();
                                                             }
                             });
            }
        });

        calculototalbill = MyDialogPagarFactura.findViewById(R.id.calculototalfactura);
        calculoefectivobill = MyDialogPagarFactura.findViewById(R.id.calculoefectivofactura);
        calculocambiobill = MyDialogPagarFactura.findViewById(R.id.calculocambiofactura);

        calculototalbill.setText(String.valueOf(EDITEtotalbill));

        calculoefectivobill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable valor)
            {
                int total = Integer.parseInt(calculototalbill.getText().toString());
                int efectivo = Integer.parseInt(calculoefectivobill.getText().toString());
                int cambio = efectivo - total;
                calculocambiobill.setText(String.valueOf(cambio));
            }
        });

        MyDialogPagarFactura.show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public static class PlaceholderFragment extends Fragment
    {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static Fragment newInstance(int sectionNumber) {
            Fragment fragment=null;

            switch (sectionNumber){
                case 1:
                    fragment= new VerFacturaFragment();
                    Bundle datoToVerFactura = new Bundle();
                    datoToVerFactura.putString("IdToVerFactura", IDEFACTURA);
                    fragment.setArguments(datoToVerFactura);
                    break;
                case 2:
                    fragment= new ProductosToBillsFragment();
                    Bundle datosProductoToBill = new Bundle();
                    datosProductoToBill.putString("IdToProductotoBill", IDEFACTURA);
                    fragment.setArguments(datosProductoToBill);
                    break;
            }

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ejemplo_tabs, container, false);

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) { super(fm); }

        @Override
        public Fragment getItem(int position)
        {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() { return 2; }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position) {
                case 0:
                    return "Factura";
                case 1:
                    return "Productos";
            }
            return null;
        }
    }
}
