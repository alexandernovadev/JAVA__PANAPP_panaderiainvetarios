package com.imperiozipa.panaderiaapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.imperiozipa.panaderiaapp.Inventario.InventarioFragment;
import com.imperiozipa.panaderiaapp.Modelos.ModeloUsuario;
import com.imperiozipa.panaderiaapp.Provedores.ProvedoresFragment;
import com.imperiozipa.panaderiaapp.Ventas.VentasFragment;

import javax.annotation.Nullable;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        VentasFragment.OnFragmentInteractionListener,
        InventarioFragment.OnFragmentInteractionListener,
        PedidosFragment.OnFragmentInteractionListener,
        FacturasFragment.OnFragmentInteractionListener,
        GastosFragment.OnFragmentInteractionListener,
        PerfilFragment.OnFragmentInteractionListener,
        CajaFragment.OnFragmentInteractionListener,
        ProvedoresFragment.OnFragmentInteractionListener,
        UsuariosFragment.OnFragmentInteractionListener,
        PrestamosFragment.OnFragmentInteractionListener,
        PerdidasFragment.OnFragmentInteractionListener,
        ListapanaderoFragment.OnFragmentInteractionListener{

    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    VentasFragment ventasFragment;
    InventarioFragment inventarioFragment;
    PedidosFragment pedidosFragment;
    FacturasFragment facturasFragment;
    GastosFragment gastosFragment;
    PerfilFragment perfilFragment;
    CajaFragment cajaFragment;
    UsuariosFragment usuariosFragment;
    ProvedoresFragment provedoresFragment;
    PrestamosFragment prestamosFragment;
    PerdidasFragment perdidasFragment;
    ListapanaderoFragment listapanaderoFragment;

    TextView usernombree,useremaaail;
    ImageView userimage;
    FirebaseUser user;
    View navrefhead;
    Menu menudrawer;
    String ideUser;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        verificarUser();
        ventasFragment = new VentasFragment();
        inventarioFragment = new InventarioFragment();
        pedidosFragment = new PedidosFragment();
        facturasFragment = new FacturasFragment();
        gastosFragment = new GastosFragment();
        perfilFragment = new PerfilFragment();
        cajaFragment = new CajaFragment();
        provedoresFragment= new ProvedoresFragment();
        usuariosFragment = new UsuariosFragment();
        prestamosFragment = new PrestamosFragment();
        perdidasFragment = new PerdidasFragment();
        listapanaderoFragment = new ListapanaderoFragment();


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_home,inventarioFragment).commit();

    }

    private void verificarUser()
    {
        navrefhead = navigationView.getHeaderView(0);
            usernombree = navrefhead.findViewById(R.id.user_name);
            useremaaail = navrefhead.findViewById(R.id.user_email);
            userimage = navrefhead.findViewById(R.id.userimage);

        menudrawer = navigationView.getMenu();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user !=  null)
        {
            ideUser = user.getUid();

            basedatos
            .collection("usuarios")
            .document(ideUser)
            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                    @Nullable FirebaseFirestoreException e)
                {
                    if (documentSnapshot != null && documentSnapshot.exists())
                    {
                        ModeloUsuario usernew = documentSnapshot.toObject(ModeloUsuario.class);

                        usernombree.setText(usernew.getNombreuser());
                        useremaaail.setText(usernew.getEmailuser());
                        Glide.with(Home.this)
                                .load(usernew.getUrlimguser())
                                .into(userimage);


                        Log.w("Pruebaa", usernew.getTipouser());
                        if (usernew.getTipouser().equals("Tendero"))
                        {
                            menudrawer.findItem(R.id.nav_caja).setVisible(false);
                            menudrawer.findItem(R.id.nav_inventario).setVisible(false);
                            menudrawer.findItem(R.id.nav_pedidos).setVisible(false);
                            menudrawer.findItem(R.id.nav_facturas).setVisible(false);
                            menudrawer.findItem(R.id.nav_gastos).setVisible(false);
                            menudrawer.findItem(R.id.nav_caja).setVisible(false);
                            menudrawer.findItem(R.id.nav_provedores).setVisible(false);
                            menudrawer.findItem(R.id.nav_perdidas).setVisible(false);
                            menudrawer.findItem(R.id.nav_listapanadero).setVisible(false);
                            menudrawer.findItem(R.id.nav_usarios).setVisible(false);

                        }
                        if (usernew.getTipouser().equals("Panadero"))
                        {
                            menudrawer.findItem(R.id.nav_caja).setVisible(false);
                            menudrawer.findItem(R.id.nav_inventario).setVisible(false);
                            menudrawer.findItem(R.id.nav_pedidos).setVisible(false);
                            menudrawer.findItem(R.id.nav_facturas).setVisible(false);
                            menudrawer.findItem(R.id.nav_gastos).setVisible(false);
                            menudrawer.findItem(R.id.nav_caja).setVisible(false);
                            menudrawer.findItem(R.id.nav_provedores).setVisible(false);
                            menudrawer.findItem(R.id.nav_perdidas).setVisible(false);
                            menudrawer.findItem(R.id.nav_usarios).setVisible(false);
                        }

                    }
                    else {Log.d("FIREUSER",  " data: null"); }
                }
            });

        }
    }

    @Override
    public void onBackPressed()
    {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        //Ref User Info

        int id = item.getItemId();
        Fragment miFragment=null;
        boolean fragmentSeleccionado=false;

        switch (id)
        {
            case R.id.nav_ventas:
                miFragment=new VentasFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_inventario:
                miFragment=new InventarioFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_pedidos:
                miFragment=new PedidosFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_facturas:
                miFragment=new FacturasFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_gastos:
                miFragment=new GastosFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_caja:
                miFragment=new CajaFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_provedores:
                miFragment=new ProvedoresFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_usarios:
                miFragment=new UsuariosFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_prestamos:
                miFragment=new PrestamosFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_perdidas:
                miFragment=new PerdidasFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_listapanadero:
                miFragment=new ListapanaderoFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_perfil:
                miFragment=new PerfilFragment();
                fragmentSeleccionado=true;
                break;

            case R.id.nav_outsesion:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this,Login.class));
                break;
        }

        if (fragmentSeleccionado==true)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home,miFragment).commit();
        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
