package com.smeagtechnology.diferencial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TemaEleccionActivity extends AppCompatActivity {
    private AdView mAdView;
    private RecyclerView recycler_parrafos;
    List<ParrafosTema> parrafosList;
    ProgressBar progressBar_parrafos;
    TextView textview_mensaje;
    Button button_entendido;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tema_eleccion);
        final String title_name = getIntent().getStringExtra("NombreTema");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(title_name);
        button_entendido = findViewById(R.id.button_entendido);
        textview_mensaje = findViewById(R.id.textview_mensaje);
        progressBar_parrafos = findViewById(R.id.progressBar_parrafos);
        recycler_parrafos = findViewById(R.id.recycler_parrafos);
        recycler_parrafos.setHasFixedSize(true);
        recycler_parrafos.setLayoutManager(new LinearLayoutManager(this));
        parrafosList = new ArrayList<>();
        mostrarTema();
        mAdView = findViewById(R.id.adViewTema);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        button_entendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int id_tema = getIntent().getIntExtra("IdTema", 0);
                Bundle extras = new Bundle();
                extras.putInt("IdTema" ,id_tema);
                extras.putString("NombreTema" ,title_name );
                Intent openActividad = new Intent(TemaEleccionActivity.this, ActividadActivity.class);
                openActividad.putExtras(extras);
                startActivity(openActividad);
                finish();
            }
        });

        progressBar_parrafos.setVisibility(View.GONE);
    }



    private void mostrarTema() {
        final int id_tema = getIntent().getIntExtra("IdTema", 0);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.smeag.online/ApiCalculoDiferencialApp/obtenerParrafos.php?tema="+id_tema,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length() == 0){
                                textview_mensaje.setVisibility(View.VISIBLE);
                                button_entendido.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Parrafos = array.getJSONObject(i);
                                parrafosList.add(new ParrafosTema(
                                        Parrafos.getInt("IdParrafo"),
                                        Parrafos.getString("ContenidoParrafo"),
                                        Parrafos.getString("ImagenParrafo")

                                ));
                            }
                            AdapterParrafosTema adapter = new AdapterParrafosTema(TemaEleccionActivity.this, parrafosList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(parrafosList.get(recycler_parrafos.getChildAdapterPosition(view)).getImagenparrafo().equals("")){
                                        }else{

                                        }
                                }
                            });
                            recycler_parrafos.setAdapter(adapter);
                            progressBar_parrafos.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_parrafos.setVisibility(View.GONE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(TemaEleccionActivity.this);
                Bien.setMessage("Parece que hay problemas en la conexión a internet.")
                        .setCancelable(false)
                        .setNegativeButton("Salir", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                parrafosList.clear();
                                progressBar_parrafos.setVisibility(View.VISIBLE);
                                mostrarTema();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Error de conexión");
                Titulo.show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }
}