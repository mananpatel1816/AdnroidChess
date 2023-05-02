package com.example.chess17;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class gamelist extends AppCompatActivity {
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

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {

                    Date date = new Date(file.lastModified());
                    String newDate = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat formatter = new SimpleDateFormat(newDate);
                    String formattedDate = formatter.format(date);
                    gameTitle.add(file.getName().substring(0, file.getName().length() - 4) + "\n" + formattedDate);
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameTitle);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                playback.name = name.substring(0, name.indexOf("\n"));
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


        Button alphabetic = findViewById(R.id.alpha);
        alphabetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort the gameTitle list based on last modified dates
                Collections.sort(gameTitle);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(gamelist.this, android.R.layout.simple_list_item_1, gameTitle);
//                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        Button dateButton = findViewById(R.id.date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort the gameTitle list based on last modified dates
                Collections.sort(gameTitle, new Comparator<String>()
                {
                    @Override
                    public int compare(String title1, String title2)
                    {
                        String[] parts1 = title1.split("\n");
                        String[] parts2 = title2.split("\n");
                        String date1 = parts1[1].trim();
                        String date2 = parts2[1].trim();

                        // Parse the dates to compare
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date dateObj1 = format.parse(date1);
                            Date dateObj2 = format.parse(date2);

                            // Compare the dates
                            return dateObj1.compareTo(dateObj2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        return 0;  // files have the same last modified date
                    }
                });

                adapter.notifyDataSetChanged();
            }
        });


    }
}