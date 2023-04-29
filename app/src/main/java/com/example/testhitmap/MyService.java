package com.example.testhitmap;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.List;

public class MyService extends Service {

    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    @Override
    public void onCreate() {
        super.onCreate();

        // Определяем задачу, которую нужно выполнить сервису в фоновом режиме
        mRunnable = new Runnable() {
            @Override
            public void run() {

                // Получаем слепок активности пользователя
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();

                // Сохраняем данные о слепке активности пользователя

                // Перезапускаем задачу через 5 секунд
                mHandler.postDelayed(mRunnable, 5000);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Запускаем задачу в фоновом режиме
        mHandler.post(mRunnable);

        // Не удаляем сервис из памяти приложения, если он был остановлен системой
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Метод не используется в данном приложении
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Останавливаем задачу в фоновом режиме
        mHandler.removeCallbacks(mRunnable);
    }
}