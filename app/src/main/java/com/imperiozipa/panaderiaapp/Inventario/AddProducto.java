package com.imperiozipa.panaderiaapp.Inventario;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddProducto extends AppCompatActivity
    implements  View.OnClickListener
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
    //Textos
    TextView TVbtn_selectimage,ValidacionIMGproducto;
    EditText etFechaentradaproducto,etFechavenciproducto, edNombreprodu,edCantiProdu,
             edPVprodu,edPCprodu,edCodigoprodu,edIvaprodu,edPesoprodu;
    ImageView ibObtenerFechaentradaprodu,ibObtenerFechavencidaprodu,
              imagepreview;
    Spinner spSelectcategoria,spSelecttipo, spSelectProvedor;
    Uri urImagendegaleria;

    LinearLayout linearbtnbackaddproductos,linearbtnDoneaddproductos;
    String IDgenerado,nombre,categoria,tipo,provedor,cantidadST,preciocompraST
            ,precioventaST,codigo_producto,name_image,ivaSt,fecha_vencimiento,fecha_entrada
            ,peso_unidad;
    //FireCloudDatabase
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    public ArrayList<ModeloInventario> arrayupdate;
    private StorageReference refStorage;
    UploadTask tareaupdown;

    List<String> categorialist;
    ArrayAdapter<String> adpaptercategoria;
    List<String> provedorlist;
    ArrayAdapter<String> adpapterprovedor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_add_producto);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        imagepreview = findViewById(R.id.imagepreviewproducto);
        ValidacionIMGproducto = findViewById(R.id.TVvalidaacionIMGproducto);
        //DETALLES
        edNombreprodu = findViewById(R.id.nombreaddproducto);
        spSelectcategoria =findViewById(R.id.spinner_categoriaproducto);
        spSelecttipo = findViewById(R.id.spinner_tipoproducto);
        spSelectProvedor = findViewById(R.id.spinner_provedor);
        edCantiProdu = findViewById(R.id.cantidaditempro);
        edPCprodu = findViewById(R.id.preciocompraproducto);
        edPVprodu = findViewById(R.id.precioventaproducto);
        edCodigoprodu = findViewById(R.id.codigoproducto);
        edIvaprodu = findViewById(R.id.ivaproducto);
        edPesoprodu = findViewById(R.id.pesoproducto);
        etFechaentradaproducto = findViewById(R.id.et_mostrar_fechaentrada_producto);
        etFechavenciproducto = findViewById(R.id.et_mostrar_fechavencimiento_producto);
        //BOTONESS
        linearbtnbackaddproductos = findViewById(R.id.linearbtnbackaddproductos);
        linearbtnbackaddproductos.setOnClickListener(this);
        linearbtnDoneaddproductos = findViewById(R.id.linearbtnbackDoneaddproductos);
        linearbtnDoneaddproductos.setOnClickListener(this);
        TVbtn_selectimage = findViewById(R.id.TVbtn_addimgproductopreview);
        TVbtn_selectimage.setOnClickListener(this);
        ibObtenerFechaentradaprodu = findViewById(R.id.ib_obtener_fecha_entrada_producto);
        ibObtenerFechaentradaprodu.setOnClickListener(this);
        ibObtenerFechavencidaprodu = findViewById(R.id.ib_obtener_fecha_vencimiento_producto);
        ibObtenerFechavencidaprodu.setOnClickListener(this);
        refStorage = FirebaseStorage.getInstance().getReference();


        llenarspinnercategoria();
        llenarspinnerprovedor();
  }

    private void llenarspinnerprovedor()
    {
        basedatos
        .collection("provedores")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) { return; }

                provedorlist = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloProvedor newcatefory = doc.toObject(ModeloProvedor.class);
                    provedorlist.add(newcatefory.getMarca_provedor());
                }

                adpapterprovedor = new ArrayAdapter<String>(AddProducto.this,
                        android.R.layout.simple_spinner_item, provedorlist);
                adpapterprovedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSelectProvedor.setAdapter(adpapterprovedor);
            }
        });
    }

    private void llenarspinnercategoria()
    {
        basedatos
        .collection("category_product")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) { return; }

                categorialist = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                {
                    ModeloCateoriaproductos newcatefory = doc.toObject(ModeloCateoriaproductos.class);
                    categorialist.add(newcatefory.getNombrecategoria());
                }

                adpaptercategoria = new ArrayAdapter<String>(AddProducto.this,
                        android.R.layout.simple_spinner_item, categorialist);
                adpaptercategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSelectcategoria.setAdapter(adpaptercategoria);
            }
        });
    }

    //Eventos Botonera
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_obtener_fecha_entrada_producto:
                obtenerFecha(etFechaentradaproducto);
                break;
            case  R.id.ib_obtener_fecha_vencimiento_producto:
                obtenerFecha(etFechavenciproducto);
                break;

            case R.id.linearbtnbackaddproductos:
                cerraraddprodcutos();
                break;

            case R.id.TVbtn_addimgproductopreview:
                selecionarimage();
                break;

            case R.id.linearbtnbackDoneaddproductos:
                agregasproductosfirebase();
                break;
        }
    }

    private void agregasproductosfirebase()
    {
        if (edNombreprodu.getText().toString().equals(""))
        {
            edNombreprodu.setError("Llena Este campo");
            Toast.makeText(this, "Campo Nombre Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if(edCantiProdu.getText().toString().isEmpty())
        {
            edCantiProdu.setError("Llena Este campo");
            Toast.makeText(this, "Campo Cantidad Vacio"
                    , Toast.LENGTH_SHORT).show();;
        }
        else if (edPCprodu.getText().toString().isEmpty())
        {
            edPCprodu.setError("Llena Este campo");
            Toast.makeText(this, "Campo Precio Compra Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (edPVprodu.getText().toString().isEmpty())
        {
            edPVprodu.setError("Llena Este campo");
            Toast.makeText(this, "Campo Precio Venta Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (edCodigoprodu.getText().toString().isEmpty())
        {
            edCodigoprodu.setError("Llena Este campo");
            Toast.makeText(this, "Campo Codigo Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (edIvaprodu.getText().toString().isEmpty())
        {
            edIvaprodu.setError("Llena Este campo");
            Toast.makeText(this, "Campo Iva Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (etFechavenciproducto.getText().toString().isEmpty())
        {
            etFechavenciproducto.setError("Llena Este campo");
            Toast.makeText(this, "Campo Fecha Vencimineto Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (etFechaentradaproducto.getText().toString().isEmpty())
        {
            etFechaentradaproducto.setError("Llena Este campo");
            Toast.makeText(this, "Campo Fecha Entrada Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (edPesoprodu.getText().toString().isEmpty())
        {
            edPesoprodu.setError("Llena Este campo");
            Toast.makeText(this, "Campo Peso Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (urImagendegaleria == null){
            ValidacionIMGproducto.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No selecciono Imagen"
                    , Toast.LENGTH_SHORT).show();
        }
        else
        {
            IDgenerado = UUID.randomUUID().toString();
            nombre = edNombreprodu.getText().toString();
            categoria = spSelectcategoria.getSelectedItem().toString();
            tipo = spSelecttipo.getSelectedItem().toString();
            provedor = spSelectProvedor.getSelectedItem().toString();
            cantidadST = edCantiProdu.getText().toString();
            preciocompraST = edPCprodu.getText().toString();
            precioventaST = edPVprodu.getText().toString();
            codigo_producto = edCodigoprodu.getText().toString();
            ivaSt = edIvaprodu.getText().toString();
            fecha_vencimiento = etFechavenciproducto.getText().toString();
            fecha_entrada = etFechaentradaproducto.getText().toString();
            peso_unidad = edPesoprodu.getText().toString();
            name_image = urImagendegaleria.getLastPathSegment();

            Map<String,String> camposvalidados = new HashMap<String, String>();
            camposvalidados.put("id",IDgenerado);
            camposvalidados.put("nombre",nombre);
            camposvalidados.put("categoria",categoria);
            camposvalidados.put("tipo",tipo);
            camposvalidados.put("provedor",provedor);
            camposvalidados.put("cantidad",cantidadST);
            camposvalidados.put("preciocompra",preciocompraST);
            camposvalidados.put("precioventa",precioventaST);
            camposvalidados.put("caodigoproducto",codigo_producto);
            camposvalidados.put("iva",ivaSt);
            camposvalidados.put("fechaV",fecha_vencimiento);
            camposvalidados.put("fechaE",fecha_entrada);
            camposvalidados.put("peso",peso_unidad);
            camposvalidados.put("nameimage",name_image);
            subirdatosvalidados(camposvalidados);
        }
    }

    public void subirdatosvalidados(final Map<String,String> datosvalidos)
    {
        final StorageReference carpeta = refStorage.child("fotosinventario")
                .child(datosvalidos.get("nameimage"));
        tareaupdown = carpeta.putFile(urImagendegaleria);
        tareaupdown.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int convertido = (int)progress;
                ValidacionIMGproducto.setText("Subiendo Imagen " +convertido +" %");
                ValidacionIMGproducto.setVisibility(View.VISIBLE);
                if (convertido == 100)
                {
                    ValidacionIMGproducto.setVisibility(View.INVISIBLE);
                    ValidacionIMGproducto.setText(R.string.validacionIMGproducto);
                }
            }
        });
        Task<Uri> urlTask = tareaupdown
        .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {if (!task.isSuccessful()) { throw task.getException();}return carpeta.getDownloadUrl();}
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

                    ModeloInventario items = new ModeloInventario(
                            datosvalidos.get("id"),
                            datosvalidos.get("nombre"),
                            Integer.parseInt(datosvalidos.get("cantidad")),
                            datosvalidos.get("categoria"),
                            datosvalidos.get("tipo"),
                            Integer.parseInt(datosvalidos.get("preciocompra")),
                            Integer.parseInt(datosvalidos.get("precioventa")),
                            datosvalidos.get("caodigoproducto"),
                            datosvalidos.get("nameimage"),
                            url_image,
                            Integer.parseInt(datosvalidos.get("iva")),
                            datosvalidos.get("fechaV"),
                            datosvalidos.get("fechaE"),
                            datosvalidos.get("peso"),
                            datosvalidos.get("provedor")
                    );

                    basedatos.collection("productos").document(datosvalidos.get("id"))
                    .set(items).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(AddProducto.this,
                                    "Producto Agregado", Toast.LENGTH_SHORT).show();
//                            finish();
                        }
                    });
                }
                else {/*ACA ERROR*/}
        }
        });
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

        if(resultCode == RESULT_OK && requestCode == GALERIA_INTENT) {
            //Obtiene la imagen
            urImagendegaleria = data.getData();
            imagepreview.setImageURI(urImagendegaleria);
            ValidacionIMGproducto.setVisibility(View.INVISIBLE);
        }
    }

    private void cerraraddprodcutos()
    {
        //TODO Hacer comprobaciones de "Esta seguro que quiere descartar cambios ?"
        //Oculta el teclado
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //Oculta el teclado
        finish();
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
}
