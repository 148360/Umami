package com.example.holamundo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AvisoPrivacidadActivity extends AppCompatActivity {

    Button btnRegresar;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_aviso);

        btnRegresar = (Button)findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistro = new Intent(AvisoPrivacidadActivity.this, RegistroActivity.class);
                startActivity(intentRegistro);
            }
        });
    }
}
