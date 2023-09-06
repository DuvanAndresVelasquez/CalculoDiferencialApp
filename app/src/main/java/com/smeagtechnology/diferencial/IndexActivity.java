package com.smeagtechnology.diferencial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

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

public class IndexActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 111;
    private static final String  TAG = "tag";
private GoogleSignInClient mGoogleSignInClient;
EditText nombre, correo;
Button btn_registrar, btn_regis, btn_google;
ProgressBar progressbar_index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        getSupportActionBar().hide();
        final EditText edittext_nombre = findViewById(R.id.edittext_nombre);
        final EditText edittext_correo = findViewById(R.id.edittext_correo);
        final Button btn_registrar = findViewById(R.id.btn_registrar);
        btn_regis =findViewById(R.id.btn_registrar);
        progressbar_index = findViewById(R.id.progressbar_index);
        nombre = findViewById(R.id.edittext_nombre);
        correo = findViewById(R.id.edittext_correo);
        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        signIn();
    }
});
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Nombre = edittext_nombre.getText().toString();
                String Correo = edittext_correo.getText().toString();

                    progressbar_index.setVisibility(View.VISIBLE);
                    btn_regis.setVisibility(View.GONE);
                    new RegistrarDatos(IndexActivity.this).execute(Nombre, Correo);

            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
           updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "No hay resultados", Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

   private void updateUI(GoogleSignInAccount account){
        if(account == null){
            Toast.makeText(this, "No se obtuvieron datos", Toast.LENGTH_SHORT).show();
        }else {
            progressbar_index.setVisibility(View.VISIBLE);
            btn_regis.setVisibility(View.GONE);

            String Correo = account.getEmail();
            String Nombre = account.getDisplayName();
            nombre.setVisibility(View.INVISIBLE);
            correo.setVisibility(View.INVISIBLE);
            nombre.setText(Nombre);
            correo.setText(Correo);
            new RegistrarDatos(IndexActivity.this).execute(Nombre, Correo);


        }
    }
    public  class RegistrarDatos extends AsyncTask<String, Void, String> {
        private WeakReference<Context> context;

        public RegistrarDatos(Context context){
            this.context = new WeakReference<>(context);


        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... params){


            String registrar_url = "https://www.smeag.online/ApiCalculoIntegralApp/RegistrarUsuarioS.php";
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
            if(resultado.equals("camposincompletos")){
                progressbar_index.setVisibility(View.GONE);
                btn_regis.setVisibility(View.VISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(IndexActivity.this);
                Bien.setMessage("Debes llenar los dos campos, para poder continuar.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Campos incompletos!");
                Titulo.show();
            }else   if(resultado.equals("correoexistenteconsuscripcion")){
                progressbar_index.setVisibility(View.GONE);
                btn_regis.setVisibility(View.VISIBLE);
                SharedPreferences preferencesName = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
                String PersonName = nombre.getText().toString();
                SharedPreferences.Editor editorName = preferencesName.edit();
                editorName.putString("UserName", PersonName);
                editorName.commit();
                SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
                String PersonEmail = correo.getText().toString();
                SharedPreferences.Editor editorEmail = preferencesEmail.edit();
                editorEmail.putString("UserEmail", PersonEmail);
                editorEmail.commit();

                SharedPreferences preferencesState = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                String State = "SI";
                SharedPreferences.Editor editorState = preferencesState.edit();
                editorState.putString("State", State);
                editorState.commit();
                AlertDialog.Builder Bien = new AlertDialog.Builder(IndexActivity.this);
                Bien.setMessage("Parece que ya te has registrado anteriormente en Smeag Technology, y tienes una suscripción activa, puedes continuar tu progreso a continuación.")
                        .setCancelable(false)
                        .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent continuar = new Intent(IndexActivity.this, PrincipalPageActivity.class);
                                startActivity(continuar);
                                finish();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Ya estas dentro!");
                Titulo.show();
            }else   if(resultado.equals("correoexistentesinsuscripcion")){
                progressbar_index.setVisibility(View.GONE);
                btn_regis.setVisibility(View.VISIBLE);
                SharedPreferences preferencesName = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
                String PersonName = nombre.getText().toString();
                SharedPreferences.Editor editorName = preferencesName.edit();
                editorName.putString("UserName", PersonName);
                editorName.commit();
                SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
                String PersonEmail = correo.getText().toString();
                SharedPreferences.Editor editorEmail = preferencesEmail.edit();
                editorEmail.putString("UserEmail", PersonEmail);
                editorEmail.commit();

                SharedPreferences preferencesState = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                String State = "NO";
                SharedPreferences.Editor editorState = preferencesState.edit();
                editorState.putString("State", State);
                editorState.commit();
                AlertDialog.Builder Bien = new AlertDialog.Builder(IndexActivity.this);
                Bien.setMessage("Parece que ya te has registrado anteriormente en Smeag Technology, puedes continuar tu progreso a continuación.")
                        .setCancelable(false)
                        .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent continuar = new Intent(IndexActivity.this, PrincipalPageActivity.class);
                                startActivity(continuar);
                                finish();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Ya estas dentro!");
                Titulo.show();
            }else   if(resultado.equals("exitoso")){
                progressbar_index.setVisibility(View.GONE);
                btn_regis.setVisibility(View.VISIBLE);
                SharedPreferences preferencesName = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
                String PersonName = nombre.getText().toString();
                SharedPreferences.Editor editorName = preferencesName.edit();
                editorName.putString("UserName", PersonName);
                editorName.commit();
                SharedPreferences preferencesEmail = getSharedPreferences("EmailUserSave", Context.MODE_PRIVATE);
                String PersonEmail = correo.getText().toString();
                SharedPreferences.Editor editorEmail = preferencesEmail.edit();
                editorEmail.putString("UserEmail", PersonEmail);
                editorEmail.commit();
                SharedPreferences preferencesState = getSharedPreferences("SuscripcionState", Context.MODE_PRIVATE);
                String State = "NO";
                SharedPreferences.Editor editorState = preferencesState.edit();
                editorState.putString("State", State);
                editorState.commit();
                AlertDialog.Builder Bien = new AlertDialog.Builder(IndexActivity.this);
                Bien.setMessage("Excelente, ya hemos registrado tus datos, puedes disfrutar del aprendizaje que te brindamos a continuación.")
                        .setCancelable(false)
                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                Intent continuar = new Intent(IndexActivity.this, PrincipalPageActivity.class);
                                startActivity(continuar);
                                finish();
                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Datos guardados!");
                Titulo.show();
            }else   if(resultado.equals("noexitoso")){
                progressbar_index.setVisibility(View.GONE);
                btn_regis.setVisibility(View.VISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(IndexActivity.this);
                Bien.setMessage("Al parecer no se pudieron guardar estos datos, por favor intentalo de nuevo.")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Ocurrio un error!");
                Titulo.show();
            }else{
                progressbar_index.setVisibility(View.GONE);
                btn_regis.setVisibility(View.VISIBLE);
                AlertDialog.Builder Bien = new AlertDialog.Builder(IndexActivity.this);
                Bien.setMessage("Ocurrio un error externo a nuestro servidor, por favor verifica tu conexión a internet y vuelve a intentarlo.")
                        .setCancelable(false)
                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog Titulo = Bien.create();
                Titulo.setTitle("Ocurrio un error!");
                Titulo.show();
            }
        }
    }
}