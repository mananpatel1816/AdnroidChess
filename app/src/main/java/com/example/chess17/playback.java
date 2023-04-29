package com.example.chess17;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class playback extends AppCompatActivity
{

    private Context context;
    int lineCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback);

        context = this;

        ArrayList<String> lines = new ArrayList<>();

        File recordedGamesFolder = new File(context.getFilesDir(), "RecordedGames");
        File file = new File(recordedGamesFolder, "try" + ".txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null)
            {
                lines.add(line);
            }

            // Close the reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Button AIButton = findViewById(R.id.next);

        AIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (lineCounter < lines.size()) {

                    int[] info = Arrays.stream(lines.get(lineCounter).split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    ImageView pieceToMove = findViewById(info[0]);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieceToMove.getLayoutParams();
                    params.leftMargin = info[1];
                    params.topMargin = info[2];

                    ImageView pieceToRemove = null;
                    if (info[3] != 0)
                        pieceToRemove = findViewById(info[3]);

                    ViewGroup parent = (ViewGroup) pieceToMove.getParent();
//                                        ViewGroup removeParent = (ViewGroup) pieceToRemove.getParent();
                    if (parent != null) {
                        parent.removeView(pieceToMove);
                        parent.removeView(pieceToRemove);
                        RelativeLayout boardLayout = findViewById(R.id.board_layout);

                        if (pieceToMove != null)
                            boardLayout.addView(pieceToMove, params);
                    }

                }
                lineCounter++;
            }
        });
    }


}