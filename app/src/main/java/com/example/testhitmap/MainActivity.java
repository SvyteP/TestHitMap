package com.example.testhitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Date;
import java.text.SimpleDateFormat;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;


public class MainActivity extends AppCompatActivity {
    MyDatabaseHelper DBHelper;

    private Intent serviceLogs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isAccessibilityServiceEnabled(this, MyAccessibilityService.class)) {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }

        this.serviceLogs = new Intent(this, MyService.class);

        this.DBHelper = new MyDatabaseHelper(getApplicationContext());
    }

    public void startServiceLog(View v) {
        // Запускаем сервис для получения слепка активности пользователя
        startService(this.serviceLogs);
    }

    public void stopServiceLog(View v) {
        stopService(this.serviceLogs);
    }



    public void save(View v) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.UK);
        String date = dateFormat.format(currentDate);
        SQLiteDatabase database = DBHelper.getWritableDatabase();
        String fileName = date + "log.txt";
        String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/";
        String filePath = directoryPath + fileName;

        Cursor cursor = database.query(MyDatabaseHelper.TABLE_LOGS, null, null, null, null, null, null);

        StringBuilder dataBuilder = new StringBuilder();

        while (cursor.moveToNext()) {

            int idIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_ID);
            int XIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_X);
            int YIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_Y);
            int namepIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_NAMEPAGE);
            int nameeIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_NAMEELEMENT);

            int idValue = cursor.getInt(idIndex);
            String XValue = cursor.getString(XIndex);
            int YValue = cursor.getInt(YIndex);

            dataBuilder.append(String.format(Locale.UK, "ID= %d, NAME= %s, PASSWORD= %d \n", idValue, XValue, YValue, namepIndex, nameeIndex));
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
        database.delete(MyDatabaseHelper.TABLE_LOGS, null, null);
    }

    private boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> accessibilityServiceClass) {
        int accessibilityEnabled = 0;
        final String serviceId = context.getPackageName() + "/" + accessibilityServiceClass.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            // Ошибка при получении значения. Используется значение по умолчанию
        }
        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()) {
                    String accessibilityService = colonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(serviceId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}