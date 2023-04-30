package com.example.chess17;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playB = findViewById(R.id.play);
        playB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, play.class);
            startActivity(intent);
        });

        Button playback = findViewById(R.id.playback);
        playback.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, gamelist.class);
            startActivity(intent);
        });
    }



}