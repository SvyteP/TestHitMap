package com.example.testhitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import java.util.Date;
import java.text.SimpleDateFormat;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;


public class MainActivity extends AppCompatActivity {
    public Button button;
    MyDatabaseHelper DBHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        // Запускаем сервис для получения слепка активности пользователя
        startService(new Intent(this, MyService.class));



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

            }
        });
    }





    public void save(View v) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String date = dateFormat.format(currentDate);
        SQLiteDatabase database = DBHelper.getWritableDatabase();
        String fileName = date + "log.txt";
        String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/";
        String filePath = directoryPath + fileName;

        Cursor cursor = database.query(DBHelper.TABLE_LOGS, null, null, null, null, null, null);

        StringBuilder dataBuilder = new StringBuilder();

        while (cursor.moveToNext()) {

            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int XIndex = cursor.getColumnIndex(DBHelper.KEY_X);
            int YIndex = cursor.getColumnIndex(DBHelper.KEY_Y);
            int namepIndex = cursor.getColumnIndex(DBHelper.KEY_NAMEPAGE);
            int nameeIndex = cursor.getColumnIndex(DBHelper.KEY_NAMEELEMENT);

            int idValue = cursor.getInt(idIndex);
            String XValue = cursor.getString(XIndex);
            int YValue = cursor.getInt(YIndex);

            dataBuilder.append(String.format("ID= %d, NAME= %s, PASSWORD= %d \n", idValue, XValue, YValue, namepIndex, nameeIndex));
        }

        cursor.close();
        DBHelper.close();

        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(filePath);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(dataBuilder.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void clear(View v) {
        SQLiteDatabase database = DBHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_LOGS, null, null);


    }
}