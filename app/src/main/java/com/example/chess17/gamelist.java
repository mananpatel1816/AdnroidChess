package com.example.chess17;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class gamelist extends AppCompatActivity
{
    private ArrayList<String> gameTitle = new ArrayList<>();

    ListView list;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelist);

        context = this;
        list = findViewById(R.id.gameList);

        File recordedGamesFolder = new File(context.getFilesDir(), "RecordedGames");
        File[] files = recordedGamesFolder.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt"))
            {
                Date date = new Date(file.lastModified());
                gameTitle.add(file.getName().substring(0, file.getName().length()-4) + "\n" + date);
            }
        }
        //gameTitle.add("trying");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameTitle);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = (String) parent.getItemAtPosition(position);
                playback.name = name.substring(0,name.indexOf("\n"));
            }

        });

        // Create GestureDetector
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Handle double-click event here
                Intent intent = new Intent(gamelist.this, playback.class);
                startActivity(intent);
                return super.onDoubleTap(e);
            }
        });

        // Create OnItemTouchListener
        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }


}