package com.example.holamundo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.holamundo.adapters.DishAdapter;
import com.example.holamundo.models.AdminSQLiteOpenHelper;
import com.example.holamundo.models.Dish;
import com.example.holamundo.models.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements DishAdapter.OnItemClickListener {
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    Button buttonSendOrder;
    RadioButton radioButtonPagoEnEfectivo;
    RadioButton radioButtonPagoConTarjeta;
    RecyclerView recyclerViewDishes;
    DishAdapter dishAdapter;
    ArrayList<Dish> dishesList;
    int totalAPagar;
    TextView textViewTotalAPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        buttonSendOrder = findViewById(R.id.buttonSendOrder);
        radioButtonPagoConTarjeta = findViewById(R.id.radioButtonPagoConTarjeta);
        radioButtonPagoEnEfectivo = findViewById(R.id.radioButtonPagoEnEfectivo);
        textViewTotalAPagar = findViewById(R.id.textViewTotalAPagar);
        recyclerViewDishes = findViewById(R.id.reciclerViewDishesFromCart);
        recyclerViewDishes.setHasFixedSize(true);
        recyclerViewDishes.setLayoutManager(new LinearLayoutManager(this));
        dishesList = new ArrayList<>();
        totalAPagar =0;

        fillRecyclerViewDishes();
    }

    private void fillRecyclerViewDishes() {
        JsonArrayRequest jsonArrayRequest;
        String uri;
        int orderId;

        orderId = getOrderIdFromSqlite();
        uri = "/Api/OrderDetails/Order/" + orderId;
        Toast.makeText(this, "Orden número: "+orderId, Toast.LENGTH_SHORT).show();
        requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                Url.Base + uri,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject;
                                jsonObject = jsonArray.getJSONObject(i);
                                dishesList.add(new Dish(
                                        jsonObject.getInt("orderDetailId"),
                                        jsonObject.getString("name"),
                                        "",
                                        Url.Base + jsonObject.getString("imagePath"),
                                        jsonObject.getInt("price")
                                ));
                                totalAPagar += jsonObject.getInt("price");
                            }
                            textViewTotalAPagar.setText("Total a pagar: $"+totalAPagar);
                            dishAdapter = new DishAdapter(CartActivity.this, dishesList);
                            recyclerViewDishes.setAdapter(dishAdapter);
                            dishAdapter.setOnItemClickListener(CartActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onItemClick(int position) {
        /*Dish dish;

        dish = dishesList.get(position);
        Intent intent = new Intent(CartActivity.this, AddDishActivity.class);
        intent.putExtra("Id", dish.getId()+"");
        intent.putExtra("Imagen", dish.getImagePath());
        intent.putExtra("Nombre", dish.getName());
        intent.putExtra("Precio", dish.getPrice()+"");
        intent.putExtra("Descripcion", dish.getDescription());
        startActivity(intent);
        */
    }

    public void buttonSendOrder_onClick(View view) {
        if (radioButtonPagoEnEfectivo.isChecked() == true || radioButtonPagoConTarjeta.isChecked() == true) {
            buttonSendOrder.setEnabled(false);
            enviarOrdenAlServidor();
        } else {
            Toast.makeText(this, "Seleccione una forma de pago", Toast.LENGTH_SHORT).show();
        }
    }

    public void enviarOrdenAlServidor() {
        String apiUrl = "/api/Orders/Send";
        int orderId;
        String wayToPay;

        JSONObject jsonObject = new JSONObject();
        orderId = getOrderIdFromSqlite();
        if (radioButtonPagoConTarjeta.isChecked() == true)
            wayToPay = "Pago con tarjeta";
        else
            wayToPay = "Pago en efectivo";
        try {
            jsonObject.put("id", orderId);
            jsonObject.put("wayToPay", wayToPay);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(this);
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                Url.Base + apiUrl,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setOrderIdInSqlite(0);//Se limpia el número de orden
                        Toast.makeText(getApplicationContext(), "Orden enviada", Toast.LENGTH_LONG).show();
                        buttonSendOrder.setEnabled(true);
                        Intent intent;

                        intent = new Intent(CartActivity.this, PrincipalActivity.class);

                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        buttonSendOrder.setEnabled(true);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public int getOrderIdFromSqlite() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "registros", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        int orderId;

        // Se genera un cursor para busqueda de un campo distintivo en tabla
        Cursor fila = bd.rawQuery("SELECT orderId from usuarios", null);

        if (fila.moveToFirst()) {//condicion verdadera si encontro un registro que lo imprima
            orderId = fila.getInt(0);
        } else {//condicion falsa si no encontro registro
            orderId = 0;
            Toast.makeText(this, "No hay orden registrada", Toast.LENGTH_LONG).show();
            bd.close();
        }

        return orderId;
    }

    private void setOrderIdInSqlite(int orderId) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "registros", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        ContentValues contentValues;
        contentValues = new ContentValues();
        contentValues.put("orderId", orderId);
        database.update("usuarios", contentValues, null, null);
        database.close();
    }
}