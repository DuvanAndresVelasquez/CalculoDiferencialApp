package com.smeagtechnology.diferencial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppsActivity extends AppCompatActivity {
    private RecyclerView recycler_apps;
    List<Apps> appsList;
    ProgressBar progressBar_apps;
    TextView textview_mensje_apps;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Nuestras aplicaciones");

    textview_mensje_apps = findViewById(R.id.textview_mensaje_apps);
        progressBar_apps = findViewById(R.id.progressBar_apps);
        recycler_apps = findViewById(R.id.recycler_apps);
        recycler_apps.setHasFixedSize(true);
        recycler_apps.setLayoutManager(new LinearLayoutManager(this));
        appsList = new ArrayList<>();

        mostrarApps();

    }

    private void mostrarApps() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.smeag.online/ApiCalculoIntegralApp/obtenerApps.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length()==0){
                                textview_mensje_apps.setVisibility(View.VISIBLE);
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                appsList.add(new Apps(
                                        Temas.getInt("IdAplicacion"),
                                        Temas.getString("NombreAplicacion"),
                                        Temas.getString("DescripcionAplicacion"),
                                        Temas.getString("ImagenAplicacion"),
                                        Temas.getString("LinkAplicacion")

                                ));
                            }
                            AdapterApps adapter = new AdapterApps(AppsActivity.this, appsList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                Uri uri = Uri.parse(appsList.get(recycler_apps.getChildAdapterPosition(view)).getLinkaplicacion());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                                }
                            });
                            recycler_apps.setAdapter(adapter);
                            progressBar_apps.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_apps.setVisibility(View.INVISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(AppsActivity.this);
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
                                appsList.clear();
                                progressBar_apps.setVisibility(View.VISIBLE);
                                mostrarApps();
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