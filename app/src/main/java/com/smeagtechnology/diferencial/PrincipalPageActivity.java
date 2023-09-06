package com.smeagtechnology.diferencial;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.ads.mediationtestsuite.MediationTestSuite;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PrincipalPageActivity extends AppCompatActivity  implements BillingProcessor.IBillingHandler, RewardedVideoAdListener {
    private String TemasPremium = "Bloqueados";
    private RewardedVideoAd mRewardedVideoAd;
    private BillingProcessor bp;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private TransactionDetails transactionDetails = null;
    private String llamada;
    private RecyclerView recycler_temas;
    List<Temas> temasList;
    ProgressBar progressBar_temas;
    Button button_proceso;
    ImageView btn_sheet;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);
        //MediationTestSuite.launch(PrincipalPageActivity.this);
        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk4E4zyXzmXJBHL0NwcmJn+TR87I0nQemhtKkMVZcEBYuOeeGFlHPnbgiqw9cFyMnyT1+Zeq2uxeVKG0FQQ3vJnKToxxxTqd02R3eN20Ylu/NGWr3rrWQhqhKcWjMWBXnTBCrH3tnp8hxVH3GFKg9Z5BTRpPewucKfsWJnmR/02kXXEKb1B9X0759cQJmLWyc1mq7fel4SO3Ot6ETt4doyqNzwYKDdFTZhxUefHlGNhvHV35cuQRLAwuyc6hg0c42CVX6IcK7nh88yJdS3ZO1TRx+hOH5iLOFzTeJw4kfpXEh1POUSm/MJ1PVgimzLxh+CwAWfgsOK0WbsN/UaHY7ZQIDAQAB",
                this);
        bp.initialize();


        this.setTitle("");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        btn_sheet = findViewById(R.id.btn_sheet);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        progressBar_temas = findViewById(R.id.progressBar_products);
        recycler_temas = findViewById(R.id.recycler_temas);
        recycler_temas.setHasFixedSize(true);
        recycler_temas.setLayoutManager(new LinearLayoutManager(this));
        temasList = new ArrayList<>();
        button_proceso = findViewById(R.id.button_proceso);
        VerifyProccess();
        mostrarTema();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        btn_sheet.setVisibility(View.GONE);
        btn_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowSheet();
            }
        });
        mostrarPasePublicidad();
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

    }




    private void loadRewardedVideoAd(){
        if(!mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.loadAd("ca-app-pub-1778393545986901/1618489144",  new AdRequest.Builder().build());
        }
    }



    private void ShowSheet(){
        SharedPreferences preferencesId = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
        final String NameUserR = preferencesId.getString("UserName","Not Result");
        SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
        final String Email = preferencesEmail.getString("UserEmail","Not Result");
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PrincipalPageActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer)
                );
        TextView nombre = bottomSheetView.findViewById(R.id.nombre_bottomSheet);
        nombre.setText(NameUserR);
        TextView correo = bottomSheetView.findViewById(R.id.correo_bottomSheet);
        correo.setText(Email);
       bottomSheetDialog.setContentView(bottomSheetView);
       bottomSheetDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutemas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case(R.id.action_search):
                Toast.makeText(this, "Opción no disponible", Toast.LENGTH_SHORT).show();
                break;
            case(R.id.action_refresh):
                temasList.clear();
                //progressBar_temas.setVisibility(View.VISIBLE);
                mostrarTema();
                break;
            case(R.id.action_datos):
                Intent openActivity  = new Intent(this, DatosActivity.class);
                startActivity(openActivity);
                break;
            case(R.id.action_salir):
                AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
                Bien.setMessage("¿Quieres cerrar sesión? esto es recomendable para continuar tu proceso con otra cuenta que tengas registrada (se cerrara sesión en este dispositivo permanentemente).")
                        .setCancelable(false)
                        .setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mGoogleSignInClient.signOut();
                                SharedPreferences preferencesName = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
                                String PersonName = "Not Result";
                                SharedPreferences.Editor editorName = preferencesName.edit();
                                editorName.putString("UserName", PersonName);
                                editorName.commit();
                                SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
                                String PersonEmail = "Not Result";
                                SharedPreferences.Editor editorEmail = preferencesEmail.edit();
                                editorEmail.putString("UserEmail", PersonEmail);
                                editorEmail.commit();
                                Intent salir = new Intent(PrincipalPageActivity.this, IndexActivity.class);
                                startActivity(salir);
                                finish();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Cerrar sesión!");
                Titulo.show();
                break;
            case(R.id.action_apps):
                Intent openApps  = new Intent(this, AppsActivity.class);
                startActivity(openApps);
                break;
            case(R.id.action_calificar):
                AlertDialog.Builder calificar = new AlertDialog.Builder(PrincipalPageActivity.this);
                calificar.setMessage("Dinos que te ha parecido nuestra aplicación de calculo diferencial, para que todos puedan ver tu opinión sobre nosotros.")
                        .setCancelable(false)
                        .setPositiveButton("Calificar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                 Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.smeagtechnology.diferencial&hl=es_CO&gl=US");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog title = calificar.create();
                title.setTitle("Califica Calculo Diferencial!");
                title.show();
            default:break;
        }
        return true;
    }

private void VerifyProccess(){
    SharedPreferences preferencesTema = getSharedPreferences("TemaEnProceso", Context.MODE_PRIVATE);
    final String NombreTema = preferencesTema.getString("NombreTema","Not Result");



    if(NombreTema.equals("Not Result")){
        button_proceso.setText("Comienza a aprender desde yá");
    }else{
        SharedPreferences preferencesIdTemaTema = getSharedPreferences("IdTemaEnProceso", Context.MODE_PRIVATE);
        final String IdTemaTexto = preferencesIdTemaTema.getString("IdTema","Not Result");
        final int IdTema = Integer.parseInt(IdTemaTexto);
        button_proceso.setText("Continua tu progreso con " + NombreTema);
        button_proceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterstitialAd = new InterstitialAd(PrincipalPageActivity.this);
                mInterstitialAd.setAdUnitId("ca-app-pub-1778393545986901/3921218851");
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
                mInterstitialAd.setAdListener(new AdListener() {


                    public void onAdLoaded(){
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }

                });
                Bundle extras = new Bundle();
                extras.putInt("IdTema" ,IdTema);
                extras.putString("NombreTema" ,NombreTema );
                Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                openTema.putExtras(extras);
                startActivity(openTema);
            }
        });
    }
}


    private void mostrarTema() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.smeag.online/ApiCalculoDiferencialApp/obtenerTemas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject Temas = array.getJSONObject(i);
                                temasList.add(new Temas(
                                        Temas.getInt("IdTema"),
                                        Temas.getString("NombreTema"),
                                        Temas.getString("DescripcionTema"),
                                        Temas.getString("FotoTema"),
                                        Temas.getString("suscripcion")
                                ));
                            }
                            AdapterTemas adapter = new AdapterTemas(PrincipalPageActivity.this, temasList);


                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                                    final String StateSuscription = preferencesId.getString("State","Not Result");

                                    if(StateSuscription.equals("NO")){

                                        if (temasList.get(recycler_temas.getChildAdapterPosition(view)).getSuscripcion().equals("NO")){
                                            SharedPreferences preferencesTema = getSharedPreferences("TemaEnProceso", Context.MODE_PRIVATE);
                                            String Tema = temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema();
                                            SharedPreferences.Editor editorTema = preferencesTema.edit();
                                            editorTema.putString("NombreTema", Tema);
                                            editorTema.commit();


                                            SharedPreferences preferencesIdTema = getSharedPreferences("IdTemaEnProceso", Context.MODE_PRIVATE);
                                            int IdTemaNumero = temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema();
                                            String IdTema = String.valueOf(IdTemaNumero);
                                            SharedPreferences.Editor editorIdTema = preferencesIdTema.edit();
                                            editorIdTema.putString("IdTema", IdTema);
                                            editorIdTema.commit();

                                            Bundle extras = new Bundle();
                                            extras.putInt("IdTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema());
                                            extras.putString("NombreTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                            Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                                            openTema.putExtras(extras);
                                            startActivity(openTema);
                                        }else{
                                            if(TemasPremium.equals("Bloqueados")){
                                                AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
                                                Bien.setMessage("Este tema requiere de una compra única del pase cálculo diferencial, adquierela y obtén acceso sin limite a todas los temas premium , desaparece la publicidad, ingresa a cientos de ejercicios con soluciones, y accede a temas muy bien explicados del cálculo diferencial.")
                                                        .setCancelable(true)
                                                        .setNegativeButton("Ver video para desbloquear", new DialogInterface.OnClickListener(){
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                loadRewardedVideoAd();
                                                                if(mRewardedVideoAd.isLoaded()){
                                                                    mRewardedVideoAd.show();
                                                                }
                                                            }
                                                        })
                                                        .setPositiveButton("Ver precio", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                if(bp.isSubscriptionUpdateSupported()){
                                                                    bp.subscribe(PrincipalPageActivity.this, "acceso_total_llave");
                                                                }else{
                                                                    Toast.makeText(PrincipalPageActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                AlertDialog Titulo = Bien.create();
                                                Titulo.setTitle("Adquiere el pase cálculo diferencial!");
                                                Titulo.show();
                                            }else if(TemasPremium.equals("Desbloqueado")){
                                                TemasPremium = "Bloqueados";
                                                Bundle extras = new Bundle();
                                                extras.putInt("IdTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema());
                                                extras.putString("NombreTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                                Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                                                openTema.putExtras(extras);
                                                startActivity(openTema);
                                            }

                                        }

                                    }else{
                                        SharedPreferences preferencesTema = getSharedPreferences("TemaEnProceso", Context.MODE_PRIVATE);
                                        String Tema = temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema();
                                        SharedPreferences.Editor editorTema = preferencesTema.edit();
                                        editorTema.putString("NombreTema", Tema);
                                        editorTema.commit();


                                        SharedPreferences preferencesIdTema = getSharedPreferences("IdTemaEnProceso", Context.MODE_PRIVATE);
                                        int IdTemaNumero = temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema();
                                        String IdTema = String.valueOf(IdTemaNumero);
                                        SharedPreferences.Editor editorIdTema = preferencesIdTema.edit();
                                        editorIdTema.putString("IdTema", IdTema);
                                        editorIdTema.commit();

                                        Bundle extras = new Bundle();
                                        extras.putInt("IdTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getIdtema());
                                        extras.putString("NombreTema" ,temasList.get(recycler_temas.getChildAdapterPosition(view)).getNombretema());
                                        Intent openTema = new Intent(PrincipalPageActivity.this, TemaEleccionActivity.class);
                                        openTema.putExtras(extras);
                                        startActivity(openTema);
                                    }








                                }

                            });
                            recycler_temas.setAdapter(adapter);
                            progressBar_temas.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_temas.setVisibility(View.GONE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
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
                                temasList.clear();
                                progressBar_temas.setVisibility(View.VISIBLE);
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


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PrincipalPageActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private boolean hasSuscription(){

        if(transactionDetails != null) {
            return transactionDetails.purchaseInfo != null;
        }
        return  false;
    }
    @Override
    public void onBillingInitialized() {
        transactionDetails =bp.getSubscriptionTransactionDetails ( "suscriptorid1" );
    }
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        SharedPreferences preferencesName = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
        String State = "SI";
        SharedPreferences.Editor editorName = preferencesName.edit();
        editorName.putString("State", State);
        editorName.commit();
        // Toast.makeText(this, "Hubo una suscripcion exitosa", Toast.LENGTH_SHORT).show();

        SharedPreferences preferencesId = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
        final String NameUserR = preferencesId.getString("UserName","Not Result");
        SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
        final String Email = preferencesEmail.getString("UserEmail","Not Result");
        String Nombre = NameUserR;
        String Correo = Email;


        new PrincipalPageActivity.RegistrarSuscripcion(PrincipalPageActivity.this).execute(Nombre, Correo);
        AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
        Bien.setMessage("Hemos registrado tu compra, este servicio será para siempre, asi desinstales la app y vuelvas a necesitarla, este producto estará disponible por siempre.")
                .setCancelable(false)
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Qué bien!");
        Titulo.show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
        Bien.setMessage("¿Ha ocurrido algo? puedes volver a intentar la compra.")
                .setCancelable(false)
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Volver a la compra ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(bp.isSubscriptionUpdateSupported()){
                            bp.subscribe(PrincipalPageActivity.this, "acceso_total_llave");
                        }else{
                            Toast.makeText(PrincipalPageActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Oh no!");
        Titulo.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
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
        AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
        Bien.setMessage("El video no se cargo exitosamente, probablemente es la conexión a internet o has visto el limite de videos de hoy, el cual es 2.")
                .setCancelable(false)
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Volver a intentar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadRewardedVideoAd();
                        if(mRewardedVideoAd.isLoaded()){
                            mRewardedVideoAd.show();
                        }
                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Oh no!");
        Titulo.show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        TemasPremium = "Desbloqueado";
        AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
        Bien.setMessage("Los temas premium se han desbloqueado, puedes elegir el que necesitas ver ahora mismo.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog Titulo = Bien.create();
        Titulo.setTitle("Temas premium desbloqueados!");
        Titulo.show();
    }

    public  class RegistrarSuscripcion extends AsyncTask<String, Void, String> {
        private WeakReference<Context> context;

        public RegistrarSuscripcion(Context context){
            this.context = new WeakReference<>(context);


        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... params){


            String registrar_url = "https://www.smeag.online/ApiCalculoIntegralApp/RegistrarSuscripcion.php";
            String resultado;

            try{
                URL url = new URL(registrar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String Nombre = params[0];
                String Correo = params[1];


                String data = URLEncoder.encode("Nombre", "UTF-8") + "="  + URLEncoder.encode(Nombre, "UTF-8")
                        + "&" + URLEncoder.encode("Correo", "UTF-8") + "=" + URLEncoder.encode(Correo, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                resultado = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            }catch (MalformedURLException e){
                Log.d("MiAPP", "Se ha utilizado un url con formato incorrecto.");
                resultado = "se ha producido un error";
            }catch (IOException e){
                Log.d("MiAPP", "Erro inesperado!, posibles problemas de conexion de red");
                resultado = "Se ha producido un Error, comprueba tu conexión a internet";

            }return resultado;
        }
        protected void onPostExecute(String resultado){

        }
    }


    private void mostrarPasePublicidad(){
        SharedPreferences preferencesId = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
        final String StateSuscription = preferencesId.getString("State","Not Result");

        if(StateSuscription.equals("NO")) {
            AlertDialog.Builder Bien = new AlertDialog.Builder(PrincipalPageActivity.this);
            Bien.setMessage("Tenemos una promoción para ti de una compra única del pase calculo diferencial, adquierela y obtén acceso sin limite a todas los temas premium, desaparece la publicidad, ingresa a cientos de ejercicios con soluciones, y accede a temas muy bien explicados del cálculo diferencial.")
                    .setCancelable(false)
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Ver precio", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (bp.isSubscriptionUpdateSupported()) {
                                bp.subscribe(PrincipalPageActivity.this, "acceso_total_llave");
                            } else {
                                Toast.makeText(PrincipalPageActivity.this, "Subscription update is not supported", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            AlertDialog Titulo = Bien.create();
            Titulo.setTitle("Adquiere el pase calculo diferencial!");
            Titulo.show();
        }else{

        }
    }


}