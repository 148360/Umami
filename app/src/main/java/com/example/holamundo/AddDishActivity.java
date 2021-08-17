package com.example.holamundo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.holamundo.MainActivity.EXTRA_CREATOR;
import static com.example.holamundo.MainActivity.EXTRA_LIKES;
import static com.example.holamundo.MainActivity.EXTRA_URL;

public class AddDishActivity extends AppCompatActivity {

    String nombre, precio, descripcion;
    String urlBase = "https://www.vmartinez1984.somee.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);
        //acciones, procesos, intentos
        Intent intent = getIntent();
        urlBase = intent.getStringExtra("Imagen");
        nombre = intent.getStringExtra("Nombre");
        precio = intent.getStringExtra("Precio");
        descripcion = intent.getStringExtra("Descripcion");

        //integrar variables de xml a java y viceversa
        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_name);
        TextView textViewPrice = findViewById(R.id.text_view_price);
        TextView textViewDescription = findViewById(R.id.text_view_description);

        //Integra los componentes de datos con valorew de json
        Picasso.get().load(urlBase).fit().centerInside().into(imageView);
        textViewCreator.setText(nombre);
        textViewPrice.setText("Precio: $"+precio);
        textViewDescription.setText(descripcion);
        //textViewLikes.setText("Precio:$ "+ likeCount);
    }
}