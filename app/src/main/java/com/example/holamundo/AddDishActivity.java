package com.example.holamundo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.holamundo.models.Url;
import com.squareup.picasso.Picasso;

import static com.example.holamundo.MainActivity.EXTRA_CREATOR;
import static com.example.holamundo.MainActivity.EXTRA_LIKES;
import static com.example.holamundo.MainActivity.EXTRA_URL;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDishActivity extends AppCompatActivity {

    String nombre;
    String precio;
    String descripcion;
    int dishId;
    int orderId;
    int clientId;
    String urlBase = "https://www.vmartinez1984.somee.com/";
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);
        //acciones, procesos, intentos
        Intent intent = getIntent();
        dishId = Integer.parseInt(intent.getStringExtra("Id"));
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
        textViewPrice.setText("Precio: $" + precio);
        textViewDescription.setText(descripcion);
    }

    public void buttonAgregarPlatillo_onClick(View view) {
        //Revisar si ya esta la ordenId
        orderId = getOrderIdFromSqlite();
        if (orderId == 0) {
            //Recuperamos el clienteId
            clientId = getClientIdOfFromSqlite();
            //Se inicia la orden
            iniciarLaOrden();
        }else {
            registrarPlatillo();
        }
    }

    public void iniciarLaOrden() {
        String apiUrl = "/api/Orders/Init";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientId", clientId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(this);
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Url.Base + apiUrl,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            orderId = response.getInt("id");
                            //Guardamos la orden en la base SqLite
                            setOrderIdInSqlite(orderId);
                            ///progressDialog.dismiss();
                            //Registramos el platillo
                            registrarPlatillo();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void registrarPlatillo() {
        String apiUrl = "/api/OrderDetails";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dishId", dishId);
            jsonObject.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(this);
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Url.Base + apiUrl,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ///progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Platillo Registrado ", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public int getOrderIdFromSqlite(){
        return 0;
    }

    private int getClientIdOfFromSqlite(){
        return 13;
    }

    private void setOrderIdInSqlite(int orderId){

    }
}