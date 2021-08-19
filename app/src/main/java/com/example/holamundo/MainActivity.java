package com.example.holamundo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.holamundo.adapters.CategoryAdapter;
import com.example.holamundo.models.AdminSQLiteOpenHelper;
import com.example.holamundo.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener {
    //Constantes globales de aplicicacion /////////////////////////////////////
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";
    /////////////////////////////////////////////////////////////////////////////
    private RecyclerView mRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categoriesList;
    private RequestQueue mRequestQueue;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriesList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        fillReciclerView();
    }

    private void fillReciclerView() {
        // String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true";
        //String url = "https://pixabay.com/api/?key=19805017-9bd001171ce637dcf771be927&q=lions&image_type=photo&pretty=true";
        String url = "https://www.vmartinez1984.somee.com/Api/Categories";
        String urlBase = "https://www.vmartinez1984.somee.com/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String creatorName = hit.getString("name");
                                String imageUrl = urlBase + hit.getString("imagePath");
                                int likeCount = hit.getInt("id");
                                categoriesList.add(new Category(imageUrl, creatorName, likeCount));
                            }
                            categoryAdapter = new CategoryAdapter(MainActivity.this, categoriesList);
                            mRecyclerView.setAdapter(categoryAdapter);
                            ///////////////////Integracion de evento OnClick () ////////////////////////////////////////////////////////////////
                            categoryAdapter.setOnItemClicklistener(MainActivity.this);
                            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }


    @Override
    public void onItemClick(int position) {//inicia metodo
        Intent intent = new Intent(this, DishesActivity.class);
        Category categoryCurrent = categoriesList.get(position);
        intent.putExtra("categoryId", categoryCurrent.getId());
        //detailIntent.putExtra(EXTRA_URL,clickedItem.getImageUrl());
        //detailIntent.putExtra(EXTRA_CREATOR,clickedItem.getName());
        //detailIntent.putExtra(EXTRA_LIKES,clickedItem.getId());
        startActivity(intent);
    }//termina metodo




}