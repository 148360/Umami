package com.example.holamundo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.holamundo.models.AdminSQLiteOpenHelper;
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
    String urlBase;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    Button buttonAddDish;
    Button buttonGoCart;

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
        buttonAddDish = findViewById(R.id.btnAddDish);
        buttonGoCart = findViewById(R.id.btnGoCart);

        //Integra los componentes de datos con valorew de json
        Picasso.get().load(urlBase).fit().centerInside().into(imageView);
        textViewCreator.setText(nombre);
        textViewPrice.setText("Precio: $" + precio);
        textViewDescription.setText(descripcion);
    }

    public void buttonAgregarPlatillo_onClick(View view) {
        buttonGoCart.setEnabled(false);
        buttonAddDish.setEnabled(false);
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
                            buttonGoCart.setEnabled(false);
                            buttonAddDish.setEnabled(false);
                            registrarPlatillo();
                            Toast.makeText(getApplicationContext(), nombre + " agregado", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        buttonGoCart.setEnabled(true);
                        buttonAddDish.setEnabled(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        buttonGoCart.setEnabled(true);
                        buttonAddDish.setEnabled(true);
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
                        buttonGoCart.setEnabled(true);
                        buttonAddDish.setEnabled(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        buttonGoCart.setEnabled(true);
                        buttonAddDish.setEnabled(true);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public int getOrderIdFromSqlite(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "registros", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        int orderId;

        // Se genera un cursor para busqueda de un campo distintivo en tabla
        Cursor fila = bd.rawQuery("SELECT orderId from usuarios", null);

        if (fila.moveToFirst()) {//condicion verdadera si encontro un registro que lo imprima
            orderId =  fila.getInt(0);
        } else {//condicion falsa si no encontro registro
            orderId = 0;
            Toast.makeText(this, "No hay orden registrada", Toast.LENGTH_LONG).show();
            bd.close();
        }

        return orderId;
    }

    private int getClientIdOfFromSqlite(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "registros", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        int clientId;

        // Se genera un cursor para busqueda de un campo distintivo en tabla
        Cursor fila = bd.rawQuery("SELECT id from usuarios", null);

        if (fila.moveToFirst()) {//condicion verdadera si encontro un registro que lo imprima
           clientId =  fila.getInt(0);
        } else {//condicion falsa si no encontro registro
            clientId = 0;
            Toast.makeText(this, "No hay cliente registrado", Toast.LENGTH_LONG).show();
            bd.close();
        }

        return clientId;
    }

    private void setOrderIdInSqlite(int orderId){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "registros", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        ContentValues contentValues;
        contentValues = new ContentValues();
        contentValues.put("orderId",orderId);
        database.update("usuarios",contentValues,null,null);
        database.close();
    }

    public void buttonGoCart_onClick(View view){
        Intent intent;

        intent = new Intent(AddDishActivity.this, CartActivity.class);

        startActivity(intent);
    }
}