package com.example.testhitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    private TextView countdownTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 60000; // время в миллисекундах

    TextView tv;
    float x;
    float y;
    String sDown;
    String sMove;
    String sUp;
    public   Button button;
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


}