package com.example.testhitmap;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.nfc.Tag;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";



    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:

                // получаем информацию о нажатой View
                AccessibilityNodeInfo nodeInfo = event.getSource();
                if (nodeInfo == null) {
                    return;
                }

                // получаем координаты нажатия
                Rect rect = new Rect();
                nodeInfo.getBoundsInScreen(rect);
                int x = rect.centerX();
                int y = rect.centerY();

                PackageManager packageManager = this.getPackageManager();
                String packageName = event.getPackageName().toString();
                String className = String.valueOf(event.getClassName());
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                    CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);

                    // создаем объект DBHelper для работы с базой данных
                    MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);

                    // открываем базу данных для записи
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // создаем объект ContentValues для хранения значений столбцов
                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseHelper.KEY_X, x);
                    values.put(MyDatabaseHelper.KEY_Y, y);
                    values.put(MyDatabaseHelper.KEY_NAMEPAGE, applicationLabel.toString());
                    values.put(MyDatabaseHelper.KEY_NAMEELEMENT, className);

                    // вставляем данные в таблицу
                    long newRowId = db.insert(MyDatabaseHelper.TABLE_LOGS, null, values);

                    // закрываем базу данных
                    db.close();

                    Log.e(TAG,"Row id: " + newRowId);

                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "NameNotFoundException!");
                }
                break;
            default:
                break;
        }

        Log.e(TAG, "onAccessibilityEvent: ");
    }


    @Override
    public void onInterrupt() {
        // Вызывается при прерывании сервиса
        Log.e(TAG, "onInterrupt: Ошибка ,что-то пошло не так!");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // выполнить дополнительную настройку здесь
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED;

        info.packageNames = new String[]{};
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;

        this.setServiceInfo(info);
        Log.e(TAG, "onServiceConnected: ");
    }
}
