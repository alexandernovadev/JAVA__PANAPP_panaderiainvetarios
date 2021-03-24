package com.imperiozipa.panaderiaapp.Inventario;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imperiozipa.panaderiaapp.Modelos.ModeloCateoriaproductos;
import com.imperiozipa.panaderiaapp.Modelos.ModeloInventario;
import com.imperiozipa.panaderiaapp.Modelos.ModeloProvedor;
import com.imperiozipa.panaderiaapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EditarProducto extends AppCompatActivity implements  View.OnClickListener
{
    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    private static final int GALERIA_INTENT = 1;

    TextView btnslectediimageproducto,msgvalidaimgeditproducto ;

    EditText etFechaentradaproductoEDIT,etFechavenciproductoEDIT, edNombreproduEDIT,edCantiProduEDIT,
            edPVproduEDIT,edPCproduEDIT,edCodigoproduEDIT,edIvaproduEDIT,edPesoproduEDIT;

    ImageView BTNEDITFechaentradaprodu,BTNEDITFechavencidaprodu, EDITimagepreview;

    LinearLayout EditLLbtndoneproducto,EditLLbtndeleteproducto,EditLLbtnbackproducto;

    Spinner spSelectcategoriaEDIT,spSelecttipoEDIT, spSelectProvedorEDIT;

    Uri urImagendegaleriaEDIT;

    UploadTask tareaupdown;

    //FireCloudDatabase
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    public ArrayList<ModeloInventario> arrayupdateEDIT;


    private StorageReference refStorage;

    ModeloInventario item = null;

    List<String> editlistaCategoria;
    ArrayAdapter<String> editadapterSpinnerCategoria;

    List<String> editlistaProvedor;
    ArrayAdapter<String> editadapterSpinnerProvedor;

    Bundle recibirdatos;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_editar_producto);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        EDITimagepreview = findViewById(R.id.imageprevieweditproducto);

        msgvalidaimgeditproducto = findViewById(R.id.TVeditvalidaacionIMGproducto);

        //DETALLES

        edNombreproduEDIT = findViewById(R.id.nombreeditproducto);
        spSelectcategoriaEDIT =findViewById(R.id.spinner_edicategoriaproducto);
        spSelecttipoEDIT = findViewById(R.id.spinner_editipoproducto);
        spSelectProvedorEDIT = findViewById(R.id.spinner_ediprovedor);
        edCantiProduEDIT = findViewById(R.id.edicantidaditempro);
        edPCproduEDIT = findViewById(R.id.edipreciocompraproducto);
        edPVproduEDIT = findViewById(R.id.ediprecioventaproducto);
        edCodigoproduEDIT = findViewById(R.id.edicodigoproducto);
        edIvaproduEDIT = findViewById(R.id.ediivaproducto);
        edPesoproduEDIT = findViewById(R.id.edipesoproducto);
        etFechaentradaproductoEDIT = findViewById(R.id.edifechaentradaproducto);
        etFechavenciproductoEDIT = findViewById(R.id.edifechavenciientoproducto);

        //BOTONESS

        btnslectediimageproducto = findViewById(R.id.TVbtn_editimgproductopreview);
        btnslectediimageproducto.setOnClickListener(this);

        EditLLbtnbackproducto = findViewById(R.id.linearbtnbackeditproducto);
        EditLLbtnbackproducto.setOnClickListener(this);

        EditLLbtndoneproducto = findViewById(R.id.linearbtnbackDoneeditproductos);
        EditLLbtndoneproducto.setOnClickListener(this);

        EditLLbtndeleteproducto = findViewById(R.id.linearbtnbackdeleteeditproductos);
        EditLLbtndeleteproducto.setOnClickListener(this);

        BTNEDITFechaentradaprodu = findViewById(R.id.ib_EDITobtener_fecha_entrada_producto);
        BTNEDITFechaentradaprodu.setOnClickListener(this);

        BTNEDITFechavencidaprodu = findViewById(R.id.ib_EDITobtener_fecha_venciiento_producto);
        BTNEDITFechavencidaprodu.setOnClickListener(this);

        refStorage = FirebaseStorage.getInstance().getReference();

        editllenarspinnerprovedor();
        editllenarspinnercategoria();
        recibirdatos = getIntent().getExtras();
    }

    private void editllenarspinnercategoria()
    {
        basedatos
        .collection("category_product")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) { return; }

                editlistaCategoria = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloCateoriaproductos newcatefory = doc.toObject(ModeloCateoriaproductos.class);
                    editlistaCategoria.add(newcatefory.getNombrecategoria());
                }

                editadapterSpinnerCategoria = new ArrayAdapter<String>(EditarProducto.this,
                        android.R.layout.simple_spinner_item, editlistaCategoria);
                editadapterSpinnerCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSelectcategoriaEDIT.setAdapter(editadapterSpinnerCategoria);
                asingarnombres();
            }
        });
    }

    private void editllenarspinnerprovedor()
    {
        basedatos
        .collection("provedores")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) { return; }

                editlistaProvedor = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloProvedor newcatefory = doc.toObject(ModeloProvedor.class);
                    editlistaProvedor.add(newcatefory.getMarca_provedor());
                }

                editadapterSpinnerProvedor = new ArrayAdapter<String>(EditarProducto.this,
                        android.R.layout.simple_spinner_item, editlistaProvedor);
                editadapterSpinnerProvedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSelectProvedorEDIT.setAdapter(editadapterSpinnerProvedor);
                asingarnombres();
            }
        });
    }
    private void asingarnombres()
    {

        if (recibirdatos != null)
        {
            item = (ModeloInventario) recibirdatos.getSerializable("Producto");
            edNombreproduEDIT.setText(item.getNombre());
            spSelectcategoriaEDIT.setSelection(obtenerPosicionItem(spSelectcategoriaEDIT,
                    item.getCategoria()));
            spSelecttipoEDIT.setSelection(obtenerPosicionItem(spSelecttipoEDIT, item.getTipo()));
            spSelectProvedorEDIT.setSelection(obtenerPosicionItem(spSelectProvedorEDIT,
                    item.getProvedor()));
            edCantiProduEDIT.setText(String.valueOf(item.getCantidad()));
            edPVproduEDIT.setText(String.valueOf(item.getPrecio_venta()));
            edPCproduEDIT.setText(String.valueOf(item.getPrecio_compra()));
            edCodigoproduEDIT.setText(item.getCodigo_producto());
            edIvaproduEDIT.setText(String.valueOf(item.getIva()));
            edPesoproduEDIT.setText(item.getPeso_unidad());
            etFechaentradaproductoEDIT.setText(item.getFecha_entrada());
            etFechavenciproductoEDIT.setText(item.getFecha_vencimiento());
            String urlpagina= item.getUrl_image();
            Glide.with(this).load(urlpagina).into(EDITimagepreview);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_EDITobtener_fecha_entrada_producto:
                obtenerFecha(etFechaentradaproductoEDIT);
                break;
            case  R.id.ib_EDITobtener_fecha_venciiento_producto:
                obtenerFecha(etFechavenciproductoEDIT);
                break;
            case  R.id.TVbtn_editimgproductopreview:
                selecionarimage();
                break;
            case R.id.linearbtnbackeditproducto:
                salirEdit(); //Hacer comprobaciones
                break;
            case  R.id.linearbtnbackdeleteeditproductos:
                eliminarprducto();
                break;
            case R.id.linearbtnbackDoneeditproductos:
                editarproducto();
                break;
        }
    }

    private void editarproducto()
    {
        if (edNombreproduEDIT.getText().toString().equals(""))
        {
            edNombreproduEDIT.setError("Llena Este campo");
            Toast.makeText(this, "Campo Nombre Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if(edCantiProduEDIT.getText().toString().isEmpty())
        {
            edCantiProduEDIT.setError("Llena Este campo");
            Toast.makeText(this, "Campo Cantidad Vacio"
                    , Toast.LENGTH_SHORT).show();;
        }
        else if (edPCproduEDIT.getText().toString().isEmpty())
        {
            edPCproduEDIT.setError("Llena Este campo");
            Toast.makeText(this, "Campo Precio Compra Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (edPVproduEDIT.getText().toString().isEmpty())
        {
            edPVproduEDIT.setError("Llena Este campo");
            Toast.makeText(this, "Campo Precio Venta Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (edCodigoproduEDIT.getText().toString().isEmpty())
        {
            edCodigoproduEDIT.setError("Llena Este campo");
            Toast.makeText(this, "Campo Codigo Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (edIvaproduEDIT.getText().toString().isEmpty())
        {
            edIvaproduEDIT.setError("Llena Este campo");
            Toast.makeText(this, "Campo Iva Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (etFechavenciproductoEDIT.getText().toString().isEmpty())
        {
            etFechavenciproductoEDIT.setError("Llena Este campo");
            Toast.makeText(this, "Campo Fecha Vencimineto Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (etFechaentradaproductoEDIT.getText().toString().isEmpty())
        {
            etFechaentradaproductoEDIT.setError("Llena Este campo");
            Toast.makeText(this, "Campo Fecha Entrada Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (edPesoproduEDIT.getText().toString().isEmpty())
        {
            edPesoproduEDIT.setError("Llena Este campo");
            Toast.makeText(this, "Campo Peso Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (urImagendegaleriaEDIT == null){
            if (recibirdatos != null)
            {
                item = (ModeloInventario) recibirdatos.getSerializable("Producto");

                basedatos
                        .collection("productos")
                        .document(item.getIdgen())
                        .update(
                        "tipo",spSelecttipoEDIT.getSelectedItem().toString()
                        ,"provedor",spSelectProvedorEDIT.getSelectedItem().toString()
                        ,"precio_venta",Integer.parseInt(edPVproduEDIT.getText().toString())
                        ,"precio_compra",Integer.parseInt(edPCproduEDIT.getText().toString())
                        ,"peso_unidad",edPesoproduEDIT.getText().toString()
                        ,"nombre",edNombreproduEDIT.getText().toString()
                        ,"iva",Integer.parseInt(edIvaproduEDIT.getText().toString())
                        ,"fecha_vencimiento",etFechavenciproductoEDIT.getText().toString()
                        ,"fecha_entrada",etFechaentradaproductoEDIT.getText().toString()
                        ,"codigo_producto",edCodigoproduEDIT.getText().toString()
                        ,"categoria",spSelectcategoriaEDIT.getSelectedItem().toString()
                        ,"cantidad",Integer.parseInt(edCantiProduEDIT.getText().toString())
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditarProducto.this, "Se Actualizo"
                                , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else
        {
            if (recibirdatos != null) {
                item = (ModeloInventario) recibirdatos.getSerializable("Producto");
            }

            final StorageReference carpeta = refStorage.child("fotosinventario")
                    .child(item.getName_image());

            carpeta.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    updatedatewithimagen(item.getIdgen());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(EditarProducto.this,
                            "ERROR DE FIREBASE", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updatedatewithimagen(final String idgen)
    {
        final StorageReference carpeta = refStorage.child("fotosinventario")
                .child(urImagendegaleriaEDIT.getLastPathSegment());

        tareaupdown = carpeta.putFile(urImagendegaleriaEDIT);
        tareaupdown.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int convertido = (int)progress;
                msgvalidaimgeditproducto.setText("Subiendo Imagen " +convertido +" %");
                msgvalidaimgeditproducto.setVisibility(View.VISIBLE);
                if (convertido == 100)
                {
                    msgvalidaimgeditproducto.setVisibility(View.INVISIBLE);
                    msgvalidaimgeditproducto.setText(R.string.validacionIMGproducto);
                }
            }
        });
        Task<Uri> urlTask = tareaupdown
        .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
        @Override
        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
        {if (!task.isSuccessful())
            { throw task.getException();}return carpeta.getDownloadUrl();}
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>()
            {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
            if (task.isSuccessful())
            {
            Uri downloadUri = task.getResult();
            String url_image = downloadUri.toString();

            basedatos
            .collection("productos")
            .document(idgen)
            .update(
            "tipo",spSelecttipoEDIT.getSelectedItem().toString()
            ,"provedor",spSelectProvedorEDIT.getSelectedItem().toString()
            ,"precio_venta",Integer.parseInt(edPVproduEDIT.getText().toString())
            ,"precio_compra",Integer.parseInt(edPCproduEDIT.getText().toString())
            ,"peso_unidad",edPesoproduEDIT.getText().toString()
            ,"nombre",edNombreproduEDIT.getText().toString()
            ,"iva",Integer.parseInt(edIvaproduEDIT.getText().toString())
            ,"fecha_vencimiento",etFechavenciproductoEDIT.getText().toString()
            ,"fecha_entrada",etFechaentradaproductoEDIT.getText().toString()
            ,"codigo_producto",edCodigoproduEDIT.getText().toString()
            ,"categoria",spSelectcategoriaEDIT.getSelectedItem().toString()
            ,"cantidad",Integer.parseInt(edCantiProduEDIT.getText().toString())
            ,"name_image",urImagendegaleriaEDIT.getLastPathSegment()
            ,"url_image",url_image

            ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            Toast.makeText(EditarProducto.this, "Se Actualizo Con Imagen"
            , Toast.LENGTH_SHORT).show();
            }
            });
            }
            else {/*ACA ERROR*/}
        }
        });
    }

    private void eliminarprducto()
    {
        final Bundle recibirdatos = getIntent().getExtras();
        if (recibirdatos != null)
        {
            item = (ModeloInventario) recibirdatos.getSerializable("Producto");
            String ide = item.getIdgen();
            basedatos.collection("productos").document(ide)
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid)
                {
                    finish();
                    // TODO La imagen Hay que borrarla
                    Toast.makeText(EditarProducto.this, "Se elimino", Toast.LENGTH_SHORT).show();

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e){/*ERROR*/}
            });
        }
    }

    private void salirEdit()
    {
        // TODO Las validaciones de EDITAR
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //Oculta el teclado
        finish();
    }

    private void selecionarimage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALERIA_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == GALERIA_INTENT)
        {
            urImagendegaleriaEDIT = data.getData();
            EDITimagepreview.setImageURI(urImagendegaleriaEDIT);
        }
    }

    private void obtenerFecha(final EditText edtexto)
    {
        DatePickerDialog recogerFecha = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        //Esta variable lo que realiza es aumentar en uno el mes ya que
                        // comienza desde 0 = enero
                        final int mesActual = month + 1;

                        //Formateo el día obtenido: antepone el 0 si son menores de 10
                        String diaFormateado = (dayOfMonth < 10)? CERO + String
                                .valueOf(dayOfMonth):String.valueOf(dayOfMonth);

                        //Formateo el mes obtenido: antepone el 0 si son menores de 10
                        String mesFormateado = (mesActual < 10)? CERO + String
                                .valueOf(mesActual):String.valueOf(mesActual);

                        //Muestro la fecha con el formato deseado
                        // Ese texto se envia de acuerdo al parametro, hay para Fecha Vencimiento y Llegada
                        edtexto.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


                    }
                    //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                    /**
                     *También puede cargar los valores que usted desee
                     */
                },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

    public static int obtenerPosicionItem(Spinner spinner, String fruta) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(fruta)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;

    }
}
