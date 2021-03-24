package com.imperiozipa.panaderiaapp;


import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class PersistenciaFirebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //ToDo Esto servira ??? con cloud ??
        FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        basedatos.setFirestoreSettings(settings);

    }
}
