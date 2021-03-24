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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imperiozipa.panaderiaapp.Modelos.ModeloProvedor;
import com.imperiozipa.panaderiaapp.R;

import java.util.ArrayList;
import java.util.Iterator;

public class EditProvedorActivity extends AppCompatActivity implements View.OnClickListener
{

    ModeloProvedor item = null;
    String IDE,editmarcaprovedor,editnameprovedor,editemailprovedor,edittel1provedor
            ,edittel2provedor,imgoldproovedor,nameimgoldproovedor;
    EditText editmarcaaddprovedor, editnameaddprovedor,editemailaddprovedor,edittel1addprovedor,edittel2addprovedor;
    TextView editbtnslectimgaddprovedor,editvalidacionimgprovedor;
    CheckBox visitalunesedit,visitamartesedit,visitamiercolesedit,visitajuevesedit,visitaviernesedit,visitasabadoedit
            ,entregalunesedit,entregamartesedit,entregamiercolesedit,entregajuevesedit,entregaviernesedit,entregasabadoedit;
    ImageView editimgaddproviewprovedor;
    LinearLayout editlinearbtnbackeditprovedor,editlinearbtnDoneeditprovedor,editlinearbtnDeleteeditprovedor;
    Uri editimgaddprovedorURI;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    private StorageReference refStorage;
    UploadTask tareaupdown;
    private static final int GALERIA_INTENT = 1;
    ArrayList<String>diasvisita;
    ArrayList<String>diasentrega;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_provedor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        editmarcaaddprovedor = findViewById(R.id.marcaeditprovedor);
        editnameaddprovedor = findViewById(R.id.nombreeditprovedor);
        editemailaddprovedor = findViewById(R.id.emaileditprovedor);
        edittel1addprovedor = findViewById(R.id.tel1editprovedor);
        edittel2addprovedor = findViewById(R.id.tel2editprovedor);
        editimgaddproviewprovedor = findViewById(R.id.editimagepreviewprovedor);
        editvalidacionimgprovedor = findViewById(R.id.ValidaacioneditIMGprovedor);

        visitalunesedit = findViewById(R.id.CHprovevisitalunesEDIT);
        visitamartesedit = findViewById(R.id.CHprovevisitamartesEDIT);
        visitamiercolesedit = findViewById(R.id.CHprovevisitamiercolesEDIT);
        visitajuevesedit = findViewById(R.id.CHprovevisitajuevesEDIT);
        visitaviernesedit = findViewById(R.id.CHprovevisitaviernesEDIT);
        visitasabadoedit = findViewById(R.id.CHprovevisitasabadoEDIT);

        entregalunesedit = findViewById(R.id.CHproveentregalunesEDIT);
        entregamartesedit = findViewById(R.id.CHproveentregamartesEDIT);
        entregamiercolesedit = findViewById(R.id.CHproveentregamiercolesEDIT);
        entregajuevesedit = findViewById(R.id.CHproveentregajuevesEDIT);
        entregaviernesedit = findViewById(R.id.CHproveentregaviernesEDIT);
        entregasabadoedit = findViewById(R.id.CHproveentregasabadoEDIT);

        editlinearbtnbackeditprovedor = findViewById(R.id.linearbtnbackeditprovedor);
        editlinearbtnbackeditprovedor.setOnClickListener(this);

        editlinearbtnDeleteeditprovedor = findViewById(R.id.linearbtnDoneDeleteprovedor);
        editlinearbtnDeleteeditprovedor.setOnClickListener(this);

        editlinearbtnDoneeditprovedor = findViewById(R.id.linearbtnDoneEditprovedor);
        editlinearbtnDoneeditprovedor.setOnClickListener(this);

        editbtnslectimgaddprovedor = findViewById(R.id.TVbtn_editimgprovedorpreview);
        editbtnslectimgaddprovedor.setOnClickListener(this);

        refStorage = FirebaseStorage.getInstance().getReference();
        asignarnameprovedor();
    }

    private void asignarnameprovedor() 
    {
        final Bundle recibirdatos = getIntent().getExtras();
        if (recibirdatos != null) {
            item = (ModeloProvedor) recibirdatos.getSerializable("Provedor");

            editmarcaaddprovedor.setText(item.getMarca_provedor());
            editnameaddprovedor.setText(item.getNombre_provedor());
            editemailaddprovedor.setText(item.getEmailprovedor());
            edittel1addprovedor.setText(String.valueOf(item.getTelefono1()));
            edittel2addprovedor.setText(String.valueOf(item.getTelefono2()));

            Glide.with(this)
                    .load(item.getUrlimagen())
                    .into(editimgaddproviewprovedor);

            for (String diavisita:item.getDiasvisita())
            {
                if (diavisita.equals("Lunes"))
                {
                    visitalunesedit.setChecked(true);
                }
                if (diavisita.equals("Martes"))
                {
                    visitamartesedit.setChecked(true);
                }
                if (diavisita.equals("Miercoles"))
                {
                    visitamiercolesedit.setChecked(true);
                }
                if (diavisita.equals("Jueves"))
                {
                    visitajuevesedit.setChecked(true);
                }
                if (diavisita.equals("Viernes"))
                {
                    visitaviernesedit.setChecked(true);
                }
                if (diavisita.equals("Sabado"))
                {
                    visitasabadoedit.setChecked(true);
                }
            }

            for (String diaentrega:item.getDiasentrega())
            {
                if (diaentrega.equals("Lunes"))
                {
                    entregalunesedit.setChecked(true);
                }
                if (diaentrega.equals("Martes"))
                {
                    entregamartesedit.setChecked(true);
                }
                if (diaentrega.equals("Miercoles"))
                {
                    entregamiercolesedit.setChecked(true);
                }
                if (diaentrega.equals("Jueves"))
                {
                    entregajuevesedit.setChecked(true);
                }
                if (diaentrega.equals("Viernes"))
                {
                    entregaviernesedit.setChecked(true);
                }
                if (diaentrega.equals("Sabado"))
                {
                    entregasabadoedit.setChecked(true);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearbtnbackeditprovedor:
                cerrareditprovedor();
                break;
            case R.id.linearbtnDoneDeleteprovedor:
                deleteprovedor();
                break;
            case R.id.linearbtnDoneEditprovedor:
                doneeditporvedor();
                break;
            case R.id.TVbtn_editimgprovedorpreview:
                selectimg();
                break;
        }
    }

    private void selectimg()
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
            editimgaddprovedorURI = data.getData();
            editimgaddproviewprovedor.setImageURI(editimgaddprovedorURI);
        }
    }
    private void doneeditporvedor()
    {

        if ( editmarcaaddprovedor.getText().toString().isEmpty())
        {
            editmarcaaddprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Nombre Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if(editnameaddprovedor.getText().toString().isEmpty())
        {
            editnameaddprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Nombre Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if(editemailaddprovedor.getText().toString().isEmpty())
        {
            editemailaddprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Nombre Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if(edittel1addprovedor.getText().toString().isEmpty())
        {
            edittel1addprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Nombre Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if(edittel2addprovedor.getText().toString().isEmpty())
        {
            edittel2addprovedor.setError("Llena Este campo");
            Toast.makeText(this, "Campo Nombre Vacio"
                    , Toast.LENGTH_SHORT).show();
        }
        else if (!visitalunesedit.isChecked() && !visitamartesedit.isChecked() && !visitamiercolesedit.isChecked()
                && !visitajuevesedit.isChecked() && !visitaviernesedit.isChecked() && !visitasabadoedit.isChecked())
        {
            Toast.makeText(this, "Debes Seleccionar un Dia/S de visita"
                    , Toast.LENGTH_SHORT).show();
        }
        else  if (!entregalunesedit.isChecked() && !entregamartesedit.isChecked() && !entregamiercolesedit.isChecked()
                && !entregajuevesedit.isChecked() && !entregaviernesedit.isChecked() && !entregasabadoedit.isChecked())
        {
            Toast.makeText(this, "Debes Seleccionar un Dia/S de entrega"
                    , Toast.LENGTH_SHORT).show();
        }
        else if(editimgaddprovedorURI == null)
        {
            final Bundle recibirdatos = getIntent().getExtras();
            if (recibirdatos != null) {
                item = (ModeloProvedor) recibirdatos.getSerializable("Provedor");

                IDE = item.getIdprovedor();
            }

            editmarcaprovedor = editmarcaaddprovedor.getText().toString();
            editnameprovedor = editnameaddprovedor.getText().toString();
            editemailprovedor = editemailaddprovedor.getText().toString();
            edittel1provedor =edittel1addprovedor.getText().toString();
            edittel2provedor =  edittel2addprovedor.getText().toString();
            checks();

            basedatos
            .collection("provedores")
            .document(IDE)
            .update(
                    "emailprovedor",editemailprovedor
                    ,"marca_provedor",editmarcaprovedor
                    ,"nombre_provedor",editnameprovedor
                    ,"telefono1",Long.parseLong(edittel1provedor)
                    ,"telefono2",Long.parseLong(edittel2provedor)
                    ,"diasentrega",diasentrega
                    ,"diasvisita",diasvisita
            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EditProvedorActivity.this, "Se Actualizo"
                            , Toast.LENGTH_SHORT).show();
                }
            });


        }
        else
        {
            Log.i("Iruniiiiiinilll", "ito:  " + editimgaddprovedorURI);

            final Bundle recibirdatos = getIntent().getExtras();
            if (recibirdatos != null) {
                item = (ModeloProvedor) recibirdatos.getSerializable("Provedor");

                IDE = item.getIdprovedor();
                nameimgoldproovedor = item.getNameimgprovedor();
            }

            final StorageReference carpeta = refStorage.child("fotosprovedor")
                    .child(nameimgoldproovedor);

            carpeta.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    updatedatewithimagen(IDE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(EditProvedorActivity.this,
                            "ERROR DE FIREBASE", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void checks(){
        diasvisita = new ArrayList<>();
        diasentrega = new ArrayList<>();
        if (visitalunesedit.isChecked())
        {
            diasvisita.add("Lunes");
        }
        if (visitamartesedit.isChecked())
        {
            diasvisita.add("Martes");
        }
        if (visitamiercolesedit.isChecked())
        {
            diasvisita.add("Miercoles");
        }
        if (visitajuevesedit.isChecked())
        {
            diasvisita.add("Jueves");
        }
        if (visitaviernesedit.isChecked())
        {
            diasvisita.add("Viernes");
        }
        if (visitasabadoedit.isChecked())
        {
            diasvisita.add("Sabado");
        }
        if (entregalunesedit.isChecked())
        {
            diasentrega.add("Lunes");
        }
        if (entregamartesedit.isChecked())
        {
            diasentrega.add("Martes");
        }
        if (entregamiercolesedit.isChecked())
        {
            diasentrega.add("Miercoles");
        }
        if (entregajuevesedit.isChecked())
        {
            diasentrega.add("Jueves");
        }
        if (entregaviernesedit.isChecked())
        {
            diasentrega.add("Viernes");
        }
        if (entregasabadoedit.isChecked())
        {
            diasentrega.add("Sabado");
        }
    }

    private void updatedatewithimagen(final String IDE)
    {
        final StorageReference carpeta = refStorage.child("fotosprovedor")
                .child(editimgaddprovedorURI.getLastPathSegment());

        tareaupdown = carpeta.putFile(editimgaddprovedorURI);
        tareaupdown.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int convertido = (int)progress;
                editvalidacionimgprovedor.setText("Subiendo Imagen " +convertido +" %");
                editvalidacionimgprovedor.setVisibility(View.VISIBLE);
                if (convertido == 100)
                {
                    editvalidacionimgprovedor.setVisibility(View.INVISIBLE);
                    editvalidacionimgprovedor.setText(R.string.validacionIMGproducto);
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

                            editmarcaprovedor = editmarcaaddprovedor.getText().toString();
                            editnameprovedor = editnameaddprovedor.getText().toString();
                            editemailprovedor = editemailaddprovedor.getText().toString();
                            edittel1provedor =edittel1addprovedor.getText().toString();
                            edittel2provedor =  edittel2addprovedor.getText().toString();
                            checks();

                            basedatos
                                    .collection("provedores")
                                    .document(IDE)
                                    .update(
                                            "emailprovedor",editemailprovedor
                                            ,"marca_provedor",editmarcaprovedor
                                            ,"nombre_provedor",editnameprovedor
                                            ,"telefono1",Long.parseLong(edittel1provedor)
                                            ,"telefono2",Long.parseLong(edittel2provedor)
                                            ,"nameimgprovedor",editimgaddprovedorURI.getLastPathSegment()
                                            ,"urlimagen",url_image
                                            ,"diasentrega",diasentrega
                                            ,"diasvisita",diasvisita

                                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditProvedorActivity.this, "Se Actualizo Con Imagen"
                                            , Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {/*ACA ERROR*/}
                    }
                });
    }

    private void deleteprovedor()
    {
        final Bundle recibirdatos = getIntent().getExtras();
        if (recibirdatos != null) {
            item = (ModeloProvedor) recibirdatos.getSerializable("Provedor");
            IDE = item.getIdprovedor();
        }
        basedatos
        .collection("provedores")
        .document(IDE)
        .delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProvedorActivity.this
                        , "Se Elimino Provedor"
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {/*ERROR*/}
        });
    }

    private void cerrareditprovedor()
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
