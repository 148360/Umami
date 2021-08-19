package com.example.holamundo.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String nombre, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context,nombre,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //crear tabla a base de datos
        db.execSQL("create table usuarios(id interger primary key, name text, surname text, email text, celPhone text, address text, reference text, orderId interger)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop table if exists usuarios");
    db.execSQL("create table usuarios(id interger primary key, name text, surname text, email text, celPhone text, address text, reference text, orderId interger)");
    }

}
