package com.imperiozipa.panaderiaapp.Inventario;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.imperiozipa.panaderiaapp.Adaptadores.Adaptadorcategoriasproductos;
import com.imperiozipa.panaderiaapp.Modelos.ModeloCateoriaproductos;
import com.imperiozipa.panaderiaapp.R;

import java.util.ArrayList;
import java.util.UUID;

public class Categoriaproducto extends AppCompatActivity implements View.OnClickListener {

    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    public ArrayList<ModeloCateoriaproductos> Alcategoriaproductos;
    public Adaptadorcategoriasproductos categoriaAdaptador;
    private RecyclerView recyvclerviewCateforiaproductos;
    LinearLayout LinearBntbackcategory,LinearBntAddcategory,btnclosecategoryinventario;
    Dialog MyDialogAddcategory;
    Button BtnSavaAddcategory;
    EditText ETnameCateoriaproducto, EDdescategoriaproducto,ETbuscarcategoria;

    View vistaparateclado;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_categoriaproducto);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LinearBntbackcategory = findViewById(R.id.linearbtnbackcategoriaproductos);
        LinearBntbackcategory.setOnClickListener(this);

        LinearBntAddcategory = findViewById(R.id.linearbtnADDcategoriaproductos);
        LinearBntAddcategory.setOnClickListener(this);

        ETbuscarcategoria = findViewById(R.id.ET_buscarcategoria);
        ETbuscarcategoria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                filterCategoria(s.toString());
            }
        });

        recyvclerviewCateforiaproductos = (RecyclerView) findViewById(R.id.recyclerItemCategoriaproductos);
        recyvclerviewCateforiaproductos.setLayoutManager(new LinearLayoutManager(this));
        llenarcategorias();
        vistaparateclado = this.getCurrentFocus();

    }

    private void filterCategoria(String s) {
        final ArrayList<ModeloCateoriaproductos> filteredList = new ArrayList<>();
        for (ModeloCateoriaproductos item : Alcategoriaproductos)
        {
            if (item.getNombrecategoria().toLowerCase().contains(s.toLowerCase())){filteredList.add(item);}
        }
        categoriaAdaptador = new Adaptadorcategoriasproductos(this,(filteredList));
        recyvclerviewCateforiaproductos.setAdapter(categoriaAdaptador);
    }

    private void llenarcategorias()
    {
        basedatos
        .collection("category_product")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) { return; }
                Alcategoriaproductos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloCateoriaproductos newcategoryprodu = doc.toObject(ModeloCateoriaproductos.class);
                    Alcategoriaproductos.add(newcategoryprodu);
                }
                categoriaAdaptador = new Adaptadorcategoriasproductos(getApplicationContext(),Alcategoriaproductos);
                recyvclerviewCateforiaproductos.setAdapter(categoriaAdaptador);
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.linearbtnbackcategoriaproductos:
                cerrarcategory();
                break;

            case R.id.linearbtnADDcategoriaproductos:
                agregarcategoriaproducto();
                break;
        }
    }

    
    private void agregarcategoriaproducto()
    {
        MyDialogAddcategory = new Dialog(Categoriaproducto.this);
        MyDialogAddcategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogAddcategory.setContentView(R.layout.modaladdcategoriaproducto);
        MyDialogAddcategory.setCancelable(false);

        btnclosecategoryinventario = MyDialogAddcategory.findViewById(R.id.LLbtnclosecategoryinventario);
        btnclosecategoryinventario.setEnabled(true);

        btnclosecategoryinventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogAddcategory.cancel();
            }
        });

        ETnameCateoriaproducto = MyDialogAddcategory.findViewById(R.id.ETaddnameCateoriaproducto);
        EDdescategoriaproducto= MyDialogAddcategory.findViewById(R.id.EDadddescripcategoriaproducto);

        BtnSavaAddcategory = MyDialogAddcategory.findViewById(R.id.BtnSavaAddcategory);
        BtnSavaAddcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                String name = ETnameCateoriaproducto.getText().toString();
                String decripcion = EDdescategoriaproducto.getText().toString();

                ModeloCateoriaproductos items = new ModeloCateoriaproductos(
                        id,
                        name,
                        decripcion);

                basedatos.collection("category_product").document(id)
                .set(items).addOnSuccessListener(new OnSuccessListener<Void>()
                 {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(Categoriaproducto.this,
                        "Se agrego Categoriaa", Toast.LENGTH_SHORT).show();
                        MyDialogAddcategory.cancel();
                    }
                });


            }
        });
        MyDialogAddcategory.show();
    }


    private void cerrarcategory()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //Oculta el teclado
        finish();
    }
}
