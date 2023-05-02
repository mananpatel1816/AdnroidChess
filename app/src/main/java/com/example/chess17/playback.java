package com.example.chess17;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class playback extends AppCompatActivity
{

    private Context context;
    static String name;
    String status;
    int lineCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback);

        context = this;

        ArrayList<String> lines = new ArrayList<>();

        File recordedGamesFolder = new File(context.getFilesDir(), "RecordedGames");
        File file = new File(recordedGamesFolder, name + ".txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line, prevLine = null;
            while ((line = reader.readLine()) != null)
            {
                if(prevLine != null)
                {
                    System.out.println(prevLine);
                    lines.add(prevLine);
                }
                prevLine = line;
            }
            status = prevLine;
            // Close the reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageButton nextButton = findViewById(R.id.next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (lineCounter < lines.size())
                {

                    int[] info = Arrays.stream(lines.get(lineCounter).split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    ImageView pieceToMove = findViewById(info[0]);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieceToMove.getLayoutParams();
                    params.leftMargin = info[1];
                    params.topMargin = info[2];

                    ImageView pieceToRemove = null;

                    if (info[3] != -1)
                        pieceToRemove = findViewById(info[3]);

                    System.out.println("pieceToRemove: " + info[3]);

                    ViewGroup parent = (ViewGroup) pieceToMove.getParent();
//                                        ViewGroup removeParent = (ViewGroup) pieceToRemove.getParent();
                    if (parent != null) {
                        parent.removeView(pieceToMove);
                        parent.removeView(pieceToRemove);
                        RelativeLayout boardLayout = findViewById(R.id.board_layout);

                        if (pieceToMove != null)
                            boardLayout.addView(pieceToMove, params);
                        if(pieceToRemove != null && info[4] != -1 && info[5] != -1)
                        {
                            RelativeLayout.LayoutParams castleParams = (RelativeLayout.LayoutParams) pieceToRemove.getLayoutParams();
                            castleParams.leftMargin = info[4];
                            castleParams.topMargin = info[5];
                            System.out.println("Remove left Margin: " + info[4]);
                            System.out.println("Remove right Margin: " + info[5]);

                            boardLayout.addView(pieceToRemove, castleParams);
                        }
                        if(info[6] != -1 && info[7] != -1)
                        {
                            pieceToMove.setId(info[6]);
                            pieceToMove.setImageResource(info[7]);
                        }
                    }

                }
                else
                {
                    TextView gameStatus = findViewById(R.id.status);
                    gameStatus.setText(status);
                }
                lineCounter++;
            }
        });
    }


}