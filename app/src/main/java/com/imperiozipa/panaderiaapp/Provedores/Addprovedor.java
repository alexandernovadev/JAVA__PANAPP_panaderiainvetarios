package com.imperiozipa.panaderiaapp.Provedores;

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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imperiozipa.panaderiaapp.Inventario.AddProducto;
import com.imperiozipa.panaderiaapp.Modelos.ModeloProvedor;
import com.imperiozipa.panaderiaapp.R;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Addprovedor extends AppCompatActivity implements View.OnClickListener
{

    String IDgenerado,marcaprovedor,nameprovedor,emailprovedor,tel1provedor,tel2provedor,nameimgprovedor;
    EditText marcaaddprovedor, nameaddprovedor,emailaddprovedor,tel1addprovedor,tel2addprovedor;
    TextView btnslectimgaddprovedor,validacionimgprovedor;
    ImageView imgaddproviewprovedor;
    LinearLayout linearbtnbackaddprovedor,linearbtnDoneaddprovedor;
    Uri imgaddprovedorURI;
    CheckBox visitalunes,visitamartes,visitamiercoles,visitajueves,visitaviernes,visitasabado
    ,entregalunes,entregamartes,entregamiercoles,entregajueves,entregaviernes,entregasabado;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    public ArrayList<ModeloProvedor> ALaddprovedor;
    private StorageReference refStorage;
    UploadTask tareaupdown;
    private static final int GALERIA_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_addprovedor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        marcaaddprovedor = findViewById(R.id.marcaaddprovedor);
        nameaddprovedor = findViewById(R.id.nombreaddprovedor);
        emailaddprovedor = findViewById(R.id.emailprovedor);
        tel1addprovedor = findViewById(R.id.tel1addprovedor);
        tel2addprovedor = findViewById(R.id.tel2addprovedor);
        imgaddproviewprovedor = findViewById(R.id.imagepreviewprovedor);
        validacionimgprovedor = findViewById(R.id.TVvalidaacionIMGprovedor);

        visitalunes = findViewById(R.id.CHprovevisitalunes);
        visitamartes = findViewById(R.id.CHprovevisitamartes);
        visitamiercoles = findViewById(R.id.CHprovevisitamiercoles);
        visitajueves = findViewById(R.id.CHprovevisitajueves);
        visitaviernes = findViewById(R.id.CHprovevisitaviernes);
        visitasabado = findViewById(R.id.CHprovevisitasabado);

        entregalunes = findViewById(R.id.CHproveentregalunes);
        entregamartes = findViewById(R.id.CHproveentregamartes);
        entregamiercoles = findViewById(R.id.CHproveentregamiercoles);
        entregajueves = findViewById(R.id.CHproveentregajueves);
        entregaviernes = findViewById(R.id.CHproveentregaviernes);
        entregasabado = findViewById(R.id.CHproveentregasabado);


        linearbtnbackaddprovedor = findViewById(R.id.linearbtnbackaddprovedor);
        linearbtnbackaddprovedor.setOnClickListener(this);

        linearbtnDoneaddprovedor = findViewById(R.id.linearbtnDoneaddprovedor);
        linearbtnDoneaddprovedor.setOnClickListener(this);

        btnslectimgaddprovedor = findViewById(R.id.TVbtn_addimgprovedorpreview);
        btnslectimgaddprovedor.setOnClickListener(this);

        refStorage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.linearbtnbackaddprovedor:
                cerraraddprovedor();
                break;
            case R.id.linearbtnDoneaddprovedor:
                enviardatos();
                break;
            case R.id.TVbtn_addimgprovedorpreview:
                selecionarimg();
                break;
        }
    }

    private void selecionarimg()
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
            imgaddprovedorURI = data.getData();
            imgaddproviewprovedor.setImageURI(imgaddprovedorURI);
            validacionimgprovedor.setVisibility(View.INVISIBLE);
        }
    }

    private void enviardatos()
    {
        if (marcaaddprovedor.getText().toString().equals(""))
        {
            marcaaddprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Marca Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if(nameaddprovedor.getText().toString().isEmpty())
        {
            nameaddprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Nombre Vacio"
                    , Toast.LENGTH_SHORT).show();;
        }
        else if (emailaddprovedor.getText().toString().isEmpty())
        {
            emailaddprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Email Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (tel1addprovedor.getText().toString().isEmpty())
        {
            tel1addprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Telefono 1 Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (tel2addprovedor.getText().toString().isEmpty())
        {
            tel2addprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Telefono 2 Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (imgaddprovedorURI == null){
            validacionimgprovedor.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No selecciono Imagen"
                    , Toast.LENGTH_SHORT).show();
        }
         else if (!visitalunes.isChecked() && !visitamartes.isChecked() && !visitamiercoles.isChecked()
                && !visitajueves.isChecked() && !visitaviernes.isChecked() && !visitasabado.isChecked())
        {
            Toast.makeText(this, "Debes Seleccionar un Dia/S de visita"
                    , Toast.LENGTH_SHORT).show();
        }
        else  if (!entregalunes.isChecked() && !entregamartes.isChecked() && !entregamiercoles.isChecked()
                && !entregajueves.isChecked() && !entregaviernes.isChecked() && !entregasabado.isChecked())
        {
            Toast.makeText(this, "Debes Seleccionar un Dia/S de entrega"
                    , Toast.LENGTH_SHORT).show();
        }
        else {

            ArrayList<String>diasvisita = new ArrayList<>();
            ArrayList<String>diasentrega = new ArrayList<>();

            if (visitalunes.isChecked())
            {
                diasvisita.add("Lunes");
            }
            if (visitamartes.isChecked())
            {
                diasvisita.add("Martes");
            }
            if (visitamiercoles.isChecked())
            {
                diasvisita.add("Miercoles");
            }
            if (visitajueves.isChecked())
            {
                diasvisita.add("Jueves");
            }
            if (visitaviernes.isChecked())
            {
                diasvisita.add("Viernes");
            }
            if (visitasabado.isChecked())
            {
                diasvisita.add("Sabado");
            }
            if (entregalunes.isChecked())
            {
                diasentrega.add("Lunes");
            }
            if (entregamartes.isChecked())
            {
                diasentrega.add("Martes");
            }
            if (entregamiercoles.isChecked())
            {
                diasentrega.add("Miercoles");
            }
            if (entregajueves.isChecked())
            {
                diasentrega.add("Jueves");
            }
            if (entregaviernes.isChecked())
            {
                diasentrega.add("Viernes");
            }
            if (entregasabado.isChecked())
            {
                diasentrega.add("Sabado");
            }

            IDgenerado = UUID.randomUUID().toString();
            marcaprovedor = marcaaddprovedor.getText().toString();
            nameprovedor = nameaddprovedor.getText().toString();
            emailprovedor = emailaddprovedor.getText().toString();
            tel1provedor = tel1addprovedor.getText().toString();
            tel2provedor = tel2addprovedor.getText().toString();
            nameimgprovedor = imgaddprovedorURI.getLastPathSegment();

            Map<String,String> camposvalidadosprovedor = new HashMap<String, String>();
            camposvalidadosprovedor.put("id",IDgenerado);
            camposvalidadosprovedor.put("marca",marcaprovedor);
            camposvalidadosprovedor.put("name",nameprovedor);
            camposvalidadosprovedor.put("email",emailprovedor);
            camposvalidadosprovedor.put("tel1",tel1provedor);
            camposvalidadosprovedor.put("tel2",tel2provedor);
            camposvalidadosprovedor.put("nameimg",nameimgprovedor);
            subirdatosvalidados(camposvalidadosprovedor,diasvisita,diasentrega);
        }
    }

    private void subirdatosvalidados(final Map<String,String> datosvalidos,
                                     final ArrayList<String>diasvisita, final ArrayList<String>diasentrega)
    {
        final StorageReference carpeta = refStorage.child("fotosprovedor")
                .child(datosvalidos.get("nameimg"));
        tareaupdown = carpeta.putFile(imgaddprovedorURI);
        tareaupdown.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int convertido = (int)progress;
                validacionimgprovedor.setText("Subiendo Imagen " +convertido +" %");
                validacionimgprovedor.setVisibility(View.VISIBLE);
                if (convertido == 100)
                {
                    validacionimgprovedor.setVisibility(View.INVISIBLE);
                    validacionimgprovedor.setText(R.string.validacionIMGproducto);
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

                            long telone = Long.parseLong(datosvalidos.get("tel1"));
                            long teltwo = Long.parseLong(datosvalidos.get("tel2"));


                            ModeloProvedor items = new ModeloProvedor(
                                    datosvalidos.get("id"),
                                    datosvalidos.get("name"),
                                    datosvalidos.get("marca"),
                                    datosvalidos.get("email"),
                                    telone,
                                    teltwo,
                                    datosvalidos.get("nameimg"),
                                    url_image,
                                    diasvisita,
                                    diasentrega
                            );

                            basedatos.collection("provedores").document(datosvalidos.get("id"))
                                    .set(items).addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Toast.makeText(Addprovedor.this,
                                            "Provedor Agregado", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                        else {/*ACA ERROR*/}
                    }
                });
    }

    private void cerraraddprovedor()
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
}
