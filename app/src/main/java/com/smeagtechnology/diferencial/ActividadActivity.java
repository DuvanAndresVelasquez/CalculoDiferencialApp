package com.smeagtechnology.diferencial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActividadActivity extends AppCompatActivity implements RewardedVideoAdListener{
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    private RecyclerView recycler_actividad, recycler_seleccion_respuesta;
    List<Actividad> actividadList;
    List<Respuesta> rtaList;
    ProgressBar progressBar_actividad;
    TextView textview_mensaje_actividad;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1778393545986901/2739999128");
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {


            public void onAdLoaded(){
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

        });
        final String title_name = getIntent().getStringExtra("NombreTema");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Ejercicio de " + title_name);
        progressBar_actividad = findViewById(R.id.progressBar_actividad);

        //MobileAds.initialize(getApplicationContext(),"ca-app-pub-2554259609577130/8107678786");

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);


        recycler_actividad = findViewById(R.id.recycler_actividad);
        recycler_actividad.setHasFixedSize(true);
        recycler_actividad.setLayoutManager(new LinearLayoutManager(this));
        actividadList = new ArrayList<>();
        textview_mensaje_actividad = findViewById(R.id.textview_mensaje_actividad);
        recycler_seleccion_respuesta = findViewById(R.id.recycler_seleccion_respuesta);
        recycler_seleccion_respuesta.setHasFixedSize(true);
        recycler_seleccion_respuesta.setLayoutManager(new LinearLayoutManager(this));
        rtaList = new ArrayList<>();

        mostrarActividad();
    }

    private void loadRewardedVideoAd(){
        if(!mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.loadAd("ca-app-pub-1778393545986901/1235345765",  new AdRequest.Builder().build());
        }
    }



    private void mostrarActividad() {
        final int id_tema = getIntent().getIntExtra("IdTema", 0);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.smeag.online/ApiCalculoDiferencialApp/obtenerActividad.php?tema=" + id_tema,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length() == 0){
                              textview_mensaje_actividad.setVisibility(View.VISIBLE);
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                actividadList.add(new Actividad(
                                        Temas.getInt("IdActividad"),
                                        Temas.getString("EnunciadoActividad"),
                                        Temas.getString("ImagenEnunciado")

                                ));
                            }
                            AdapterActividad adapter = new AdapterActividad(ActividadActivity.this, actividadList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    progressBar_actividad.setVisibility(View.VISIBLE);


                                    rtaList.clear();

                                    int id_actividad = actividadList.get(recycler_actividad.getChildAdapterPosition(view)).getIdactividad();
                                    mostrarRespuestas(id_actividad);


                                }
                            });
                            recycler_actividad.setAdapter(adapter);
                            progressBar_actividad.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_actividad.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(ActividadActivity.this);
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
                                actividadList.clear();
                                progressBar_actividad.setVisibility(View.VISIBLE);
                                mostrarActividad();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Error de conexión");
                Titulo.show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }






    private void mostrarRespuestas(int id_actividad) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.smeag.online/ApiCalculoDiferencialApp/obtenerRespuestas.php?actividad="+ id_actividad,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                rtaList.add(new Respuesta(
                                        Temas.getInt("IdRtaActividad"),
                                        Temas.getString("NombreRta"),
                                        Temas.getString("ImagenRta"),
                                        Temas.getInt("IdSolucionRta")

                                ));
                            }
                            AdapterRespuesta adapter = new AdapterRespuesta(ActividadActivity.this, rtaList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(rtaList.get(recycler_seleccion_respuesta.getChildAdapterPosition(view)).getIdsolucionrta() == 1){
                                        AlertDialog.Builder Bien = new AlertDialog.Builder(ActividadActivity.this);
                                        Bien.setMessage("La respuesta que has seleccionado es correcta, desde este momento puedes continuar con el siguiente tema.")
                                                .setCancelable(false)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                });
                                        AlertDialog Titulo = Bien.create();
                                        Titulo.setTitle("Excelente!");
                                        Titulo.show();
                                    }else{
                                       recycler_seleccion_respuesta.setVisibility(View.GONE);
                                        AlertDialog.Builder Bien = new AlertDialog.Builder(ActividadActivity.this);
                                        Bien.setMessage("La respuesta que seleccionaste es incorrecta, puedes volver a intentarlo viendo un video.")
                                                .setCancelable(false)
                                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                })
                                                .setPositiveButton("Ver video", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        loadRewardedVideoAd();
                                                        if(mRewardedVideoAd.isLoaded()){
                                                            mRewardedVideoAd.show();
                                                        }
                                                    }
                                                });
                                        AlertDialog Titulo = Bien.create();
                                        Titulo.setTitle("Que mal!");
                                        Titulo.show();
                                    }
                                }
                            });
                            recycler_seleccion_respuesta.setAdapter(adapter);
                            progressBar_actividad.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_actividad.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(ActividadActivity.this);
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
                                rtaList.clear();
                                progressBar_actividad.setVisibility(View.VISIBLE);
                                mostrarActividad();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Error de conexión");
                Titulo.show();
            }
        });
        Volley.newRequestQueue(ActividadActivity.this).add(stringRequest);


    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "El video no pudo cargarse.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        recycler_seleccion_respuesta.setVisibility(View.VISIBLE);
    }
}