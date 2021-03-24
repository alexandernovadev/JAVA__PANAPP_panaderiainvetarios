package com.imperiozipa.panaderiaapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity
{

    private static final String TAG = "MensajeFirebase";
    EditText userpan,passpan;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();

        userpan = findViewById(R.id.login_user);
        passpan = findViewById(R.id.login_pass);

    }


    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser)
    {
        if (currentUser != null)
        {
            startActivity(new Intent(Login.this,Home.class));
        }
    }


    public void btn_login(View view)
    {
        //TODO Hacer validacion de campos User y password
       login_firebase();
    }


    private void login_firebase()
    {
        String email = userpan.getText().toString();
        String passw = passpan.getText().toString();

        mAuth.signInWithEmailAndPassword(email, passw)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Log.d(TAG, "Bienvenido");
                    startActivity(new Intent(Login.this,Home.class));
                } else {

                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(Login.this, "Fallo al entrar",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
