package com.example.holamundo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.holamundo.adapters.DishAdapter;
import com.example.holamundo.models.Category;
import com.example.holamundo.models.Dish;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.holamundo.MainActivity.EXTRA_URL;

public class DishesActivity extends AppCompatActivity implements DishAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    DishAdapter dishAdapter;
    ArrayList<Dish> dishesList;
    RequestQueue requestQueue;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);
        recyclerView = findViewById(R.id.recyclerViewDishes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dishesList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        Intent intent;

        intent = getIntent();
        categoryId = intent.getIntExtra("categoryId", 0);
        fillRecyclerViewDishes();
    }

    private void fillRecyclerViewDishes() {
        String urlBase = "https://www.vmartinez1984.somee.com";
        String uri = "/Api/Dishes/Category/" + categoryId;
        JsonArrayRequest jsonArrayRequest;

        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlBase + uri,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject;
                                jsonObject = jsonArray.getJSONObject(i);
                                dishesList.add(new Dish(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("description"),
                                        urlBase + jsonObject.getString("imagePath"),
                                        jsonObject.getInt("price")
                                ));
                            }
                            dishAdapter = new DishAdapter(DishesActivity.this, dishesList);
                            recyclerView.setAdapter(dishAdapter);
                            dishAdapter.setOnItemClickListener(DishesActivity.this);
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
        Dish dish;

        dish = dishesList.get(position);
        Intent intent = new Intent(DishesActivity.this, AddDishActivity.class);
        intent.putExtra("Id", dish.getId()+"");
        intent.putExtra("Imagen", dish.getImagePath());
        intent.putExtra("Nombre", dish.getName());
        intent.putExtra("Precio", dish.getPrice()+"");
        intent.putExtra("Descripcion", dish.getDescription());
        startActivity(intent);
    }
}