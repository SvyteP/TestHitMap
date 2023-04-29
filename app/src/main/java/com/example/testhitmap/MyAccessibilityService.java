package com.example.testhitmap;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
                Log.e(TAG,"X="+x + " Y=" +y);
                // обрабатываем координаты нажатия
                // ...

                break;
            default:
                break;

        }



        Log.e(TAG, "onAccessibilityEvent: ");
        // Здесь обрабатываются события от других приложений
        String packageName = event.getPackageName().toString();
        String className = String.valueOf(event.getClassName());
        PackageManager packageManager = this.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName,0);
            CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
            Log.e(TAG,"App name is: " + applicationLabel);
            Log.e("MyAccessibilityService", "Button clicked: /" + className + "/");
        }
        catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG,"NameNotFoundException!");

        }

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


        info.packageNames = new String[] {};


        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;


        info.notificationTimeout = 100;

        this.setServiceInfo(info);
        Log.e(TAG,"onServiceConnected: ");

    }
}