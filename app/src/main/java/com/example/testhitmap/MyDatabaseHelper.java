package com.example.testhitmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "logsDb";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_LOGS = "logs";
    public static final String KEY_ID = "_id";
    public static final String KEY_X = "x";
    public static final String KEY_Y = "y";
    public static final String KEY_NAMEPAGE = "namep";
    public static final String KEY_NAMEELEMENT = "namee";

    public MyDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // создание таблицы
        db.execSQL("create table " + TABLE_LOGS + "(" + KEY_ID + " integer primary key," + KEY_X + " text," + KEY_Y + " text,"  + KEY_NAMEPAGE + " text," + KEY_NAMEELEMENT + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // обновление таблицы
        db.execSQL("drop table if exists " + TABLE_LOGS);
        onCreate(db);
    }
}