package com.smeagtechnology.diferencial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MainActivity extends AppCompatActivity {
Button btn_ingresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
       // VerifyGoogleAccount();
        verify_user_existent();
        btn_ingresar = findViewById(R.id.btn_ingresar);

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ingresar = new Intent(MainActivity.this, IndexActivity.class);
                startActivity(ingresar);
            }
        });
    }

    private void VerifyGoogleAccount(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Intent rediretion_client = new Intent(this, PrincipalPageActivity.class);
            startActivity(rediretion_client);
            finish();
        }
    }





    private void verify_user_existent(){
        SharedPreferences preferencesId = getSharedPreferences("NameUserSave", Context.MODE_PRIVATE);
        final String NameUserR = preferencesId.getString("UserName","Not Result");

        if(NameUserR.equals("Not Result")){

        }else{
            Intent rediretion_client = new Intent(this, PrincipalPageActivity.class);
            startActivity(rediretion_client);
            finish();
        }
    }



}