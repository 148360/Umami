package com.example.holamundo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.security.Principal;

public class PrincipalActivity extends AppCompatActivity {

    Button btnSugeridos, btnMenu, btnHome, btnSucursales;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_principal);


        btnSugeridos = (Button) findViewById(R.id.btnSugeridos);
        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnSucursales = (Button) findViewById(R.id.btnSucursales);

    }

    //public void setBtnSugeridos_onClick(View view) {
      //  Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
       // startActivity(intent);

    public void setBtnMenu_onClick(View view) {
        Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void setBtnSucursales_onClick(View view){
        Intent intent = new Intent(PrincipalActivity.this,SucursalActivity.class);
        startActivity(intent);
                    }

}
