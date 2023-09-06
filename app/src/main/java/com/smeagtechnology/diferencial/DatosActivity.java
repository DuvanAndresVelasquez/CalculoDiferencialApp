package com.smeagtechnology.diferencial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class DatosActivity extends AppCompatActivity {
    TextView textview_nombreusuario, textview_correousuario;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Tus datos");
        textview_nombreusuario = findViewById(R.id.textview_nombreusuario);
        textview_correousuario = findViewById(R.id.textview_correousuario);
        dates();
    }

    private void dates(){
        SharedPreferences preferencesId = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
        final String NameUserR = preferencesId.getString("UserName","Not Result");
        SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
        final String Email = preferencesEmail.getString("UserEmail","Not Result");
        textview_nombreusuario.setText(NameUserR);
        textview_correousuario.setText(Email);
    }
}