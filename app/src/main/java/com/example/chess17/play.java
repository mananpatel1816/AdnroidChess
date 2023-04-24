package com.example.chess17;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.util.Scanner;

public class play extends AppCompatActivity {
    public int numClicks = 0;


    private ImageView chessboardImage;

    private static int fromRow, fromCol;

    Pieces[][] gameBoard = new Pieces[8][8];  // initializing the chess board with 2d array

    static boolean wTurn = true;
    static int aR, aC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        start(gameBoard); // initializes the gameBoard


        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        chessboardImage = findViewById(R.id.chessBoard);

        chessboardImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // User has touched the screen
                        if (numClicks == 1) {
                            int x2 = (int) event.getX();
                            int y2 = (int) event.getY();
                            int squareSize = chessboardImage.getWidth() / 8;
                            int toRow = y2 / squareSize;
                            int toCol = x2 / squareSize;
                            int resId;

                            System.out.println("To Row and Col: " + toRow + toCol);


                            if (wTurn) {
                                System.out.println("Num Clicks: " + numClicks);

                                System.out.println("white Turn");

                                if (!(gameBoard[fromRow][fromCol] instanceof King) && !(gameBoard[fromRow][fromCol] instanceof Queen)) {
                                    //System.out.println(gameBoard[fromRow][fromCol].name);
                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase() + gameBoard[fromRow][fromCol].num;
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                } else {
                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase();
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                }

                                System.out.println("From Row: " + fromRow + " From Col: " + fromCol);
                                if (gameBoard[fromRow][fromCol] != null && gameBoard[fromRow][fromCol].name.charAt(0) == 'w'
                                        && gameBoard[fromRow][fromCol].isValid(gameBoard, fromRow, fromCol, toRow, toCol))
                                {

                                    gameBoard[fromRow][fromCol].move(gameBoard, fromRow, fromCol, toRow, toCol);
                                    System.out.println("gameBoard Moved");

                                    if (check(gameBoard, King.wr, King.wc, 'w')) {
                                        gameBoard[fromRow][fromCol] = gameBoard[toRow][toCol];
                                        gameBoard[toRow][toCol] = null;
                                    } else {
                                        System.out.println("UI MOving");

                                        ImageView pieceToMove = findViewById(resId);


                                        // Update the layout params of the piece to move it to the new position
                                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieceToMove.getLayoutParams();
                                        params.leftMargin = toCol * squareSize;
                                        params.topMargin = toRow * squareSize;

                                        System.out.println("left Margin: " + params.leftMargin);
                                        System.out.println("To Col: " + toCol);
                                        System.out.println("To Row: " + toRow);
                                        System.out.println("top Margin: " + params.topMargin);

                                        ViewGroup parent = (ViewGroup) pieceToMove.getParent();
                                        if (parent != null) {
                                            parent.removeView(pieceToMove);
                                            RelativeLayout boardLayout = findViewById(R.id.board_layout);
                                            if (pieceToMove != null) {
                                                boardLayout.addView(pieceToMove, params);
                                            }
                                        }
                                        numClicks = 0;
                                        fromRow = -1;
                                        fromCol = -1;
                                        System.out.println("UI Moved");
                                        //System.out.println(fromPosition);
//                                        ViewGroup parent = (ViewGroup) pieceToMove.getParent();
//                                        if (parent != null) {
//                                            parent.removeView(pieceToMove);
//                                            RelativeLayout boardLayout = findViewById(R.id.board_layout);
//                                            if (pieceToMove != null) {
//                                                boardLayout.addView(pieceToMove, params);
//                                                System.out.println("UI Moved");
//                                                numClicks = 0;
//                                                fromRow = -1;
//                                                fromCol = -1;
//                                            }
//                                        }
                                        pieceToMove.setBackground(null);
                                        wTurn = false;
                                    }


                                }
                                else numClicks = 0;

                            } else {
                                System.out.println("Black's Turn");

                                if (!(gameBoard[fromRow][fromCol] instanceof King) && !(gameBoard[fromRow][fromCol] instanceof Queen)) {
                                    //System.out.println(gameBoard[fromRow][fromCol].name);
                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase() + gameBoard[fromRow][fromCol].num;
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                } else {
                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase();
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                }


                                if (gameBoard[fromRow][fromCol] != null && gameBoard[fromRow][fromCol].name.charAt(0) == 'b'
                                        && gameBoard[fromRow][fromCol].isValid(gameBoard, fromRow, fromCol, toRow, toCol))
                                {
                                    System.out.println("Black Piece gameBoard moved");
                                    gameBoard[fromRow][fromCol].move(gameBoard, fromRow, fromCol, toRow, toCol);

                                    if (check(gameBoard, King.br, King.bc, 'b')) {
                                        gameBoard[fromRow][fromCol] = gameBoard[toRow][toCol];
                                        gameBoard[toRow][toCol] = null;
                                    } else {
                                        System.out.println("UI Moving");
                                        ImageView pieceToMove = findViewById(resId);

                                        // Update the layout params of the piece to move it to the new position
                                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieceToMove.getLayoutParams();
                                        params.leftMargin = toCol * squareSize;
                                        params.topMargin = toRow * squareSize;

                                        ViewGroup parent = (ViewGroup) pieceToMove.getParent();
                                        if (parent != null) {
                                            parent.removeView(pieceToMove);
                                            RelativeLayout boardLayout = findViewById(R.id.board_layout);
                                            if (pieceToMove != null) {
                                                boardLayout.addView(pieceToMove, params);
                                            }
                                        }

                                        numClicks = 0;
                                        fromRow = -1;
                                        fromCol = -1;
                                        System.out.println("UI Moved");
                                        pieceToMove.setBackground(null);

                                        wTurn = true;
                                    }
                                }
                                else numClicks = 0;
                            }

                            if (check(gameBoard, King.wr, King.wc, 'w')) {
                                if (checkmate(gameBoard, King.wr, King.wc, 'w')) {
                                    System.out.println("Checkmate");
                                    System.out.println("Black Wins");
                                }
                            } else if (check(gameBoard, King.br, King.bc, 'b')) {
                                if (checkmate(gameBoard, King.br, King.bc, 'b')) {
                                    System.out.println("Checkmate");
                                    System.out.println("White Wins");
                                }
                            }


                        } else {
                            numClicks++;
                            int x = (int) event.getX();
                            int y = (int) event.getY();


                            System.out.println("X : " + x);
                            System.out.println("Y : " + y);

                            int squareSize = chessboardImage.getWidth() / 8;
                            System.out.println("width : " + chessboardImage.getWidth());
                            System.out.println("squareSize : " + squareSize);

                            fromRow = y / squareSize;
                            fromCol = x / squareSize;
                            System.out.println("row : " + fromRow);
                            System.out.println("col : " + fromCol);


                            if (gameBoard[fromRow][fromCol] == null || (gameBoard[fromRow][fromCol] != null && wTurn && gameBoard[fromRow][fromCol].name.charAt(0) == 'b') || (gameBoard[fromRow][fromCol] != null && !wTurn && gameBoard[fromRow][fromCol].name.charAt(0) == 'w')) {
                                numClicks = 0;
                            }
                            else
                            {
                                int resId;
                                if (!(gameBoard[fromRow][fromCol] instanceof King) && !(gameBoard[fromRow][fromCol] instanceof Queen)) {
                                    //System.out.println(gameBoard[fromRow][fromCol].name);
                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase() + gameBoard[fromRow][fromCol].num;
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                } else {
                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase();
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                }

                                ImageView piece = findViewById(resId);
                                // Create a border drawable with a red color and a 5-pixel width
                                GradientDrawable border = new GradientDrawable();
                                border.setStroke(5, Color.RED);

                                // Set the border drawable as the background of the ImageView
                                piece.setBackground(border);
                                //piece.setBackgroundColor(Color.parseColor("#80ADD8E6"));
                            }

                            //fromPosition = String.format("%02d%02d", row, col);
                            //int id = getResources().getIdentifier("piece_" + fromPosition, "id", getPackageName());
                            //ImageView pieceToMove = findViewById();
                            //pieceToMove.setId(id);
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });


    }


    //  Reused console version of chess code


    /**
     * Initializes the game board with the default configuration of pieces.
     *
     * @param gameBoard the 2D array representing the game board
     */
    private static void start(Pieces[][] gameBoard) {
        // Place black pawns in the second row and white pawns in the seventh row
        for (int col = 0; col < 8; col++) {
            gameBoard[1][col] = new Pawn('b', col + 1);
            gameBoard[6][col] = new Pawn('w', col + 1);
        }

        // place all the other pieces
        gameBoard[0][0] = new Rook('b', 1);
        gameBoard[0][1] = new Knight('b', 1);
        gameBoard[0][2] = new Bishop('b', 1);
        gameBoard[0][3] = new Queen('b');
        gameBoard[0][4] = new King('b');
        gameBoard[0][5] = new Bishop('b', 2);
        gameBoard[0][6] = new Knight('b', 2);
        gameBoard[0][7] = new Rook('b', 2);

        gameBoard[7][0] = new Rook('w', 1);
        gameBoard[7][1] = new Knight('w', 1);
        gameBoard[7][2] = new Bishop('w', 1);
        gameBoard[7][3] = new Queen('w');
        gameBoard[7][4] = new King('w');
        gameBoard[7][5] = new Bishop('w', 2);
        gameBoard[7][6] = new Knight('w', 2);
        gameBoard[7][7] = new Rook('w', 2);
    }


    /**
     * Determines whether a specific square on the chessboard is under attack by the opposite color.
     *
     * @param gameBoard the current state of the chessboard
     * @param r         the row index of the square being checked
     * @param c         the column index of the square being checked
     * @param color     the color of the player to check for a threat against
     * @return true if the square is under attack, false otherwise
     */
    private static boolean check(Pieces[][] gameBoard, int r, int c, char color) {
        //System.out.println(color);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoard[i][j] != null && gameBoard[i][j].name.charAt(0) != color && gameBoard[i][j].isValid(gameBoard, i, j, r, c)) {
                    aR = i;
                    aC = j;
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Checks if the given player is in checkmate.
     *
     * @param gameBoard the current game board
     * @param r         the row of the king
     * @param c         the column of the king
     * @param color     the color of the king
     * @return true if the player is in checkmate, false otherwise
     */
    private static boolean checkmate(Pieces[][] gameBoard, int r, int c, char color) {
        int checkNum = 8;
        int rDir = 0, cDir = 0;


        if (c < 7 && gameBoard[r][c + 1] == null && !check(gameBoard, r, c + 1, color))
            checkNum -= 1;


        if (c > 0 && gameBoard[r][c - 1] == null && !check(gameBoard, r, c - 1, color))
            checkNum -= 1;


        if (r < 7 && gameBoard[r + 1][c] == null && !check(gameBoard, r + 1, c, color))
            checkNum -= 1;


        if (r > 0 && gameBoard[r - 1][c] == null && !check(gameBoard, r - 1, c, color))
            checkNum -= 1;


        if (c < 7 && r < 7 && gameBoard[r + 1][c + 1] == null && !check(gameBoard, r + 1, c + 1, color))
            checkNum -= 1;


        if (c < 7 && r > 0 && gameBoard[r - 1][c + 1] == null && !check(gameBoard, r - 1, c + 1, color))
            checkNum -= 1;


        if (c > 0 && r < 7 && gameBoard[r + 1][c - 1] == null && !check(gameBoard, r + 1, c - 1, color))
            checkNum -= 1;


        if (c > 0 && r > 0 && gameBoard[r - 1][c - 1] == null && !check(gameBoard, r - 1, c - 1, color))
            checkNum -= 1;

        System.out.println(checkNum);

        // to know if there is any other piece can kill the attacker piece
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoard[i][j] != null && gameBoard[i][j].name.charAt(0) == color && gameBoard[i][j].isValid(gameBoard, i, j, aR, aC)) {
                    return false;
                }
            }
        }


        if (aR - r > 0) {
            rDir = 1;
        } else if (aR - r == 0) {
            rDir = 0;
        } else if (aR - r < 0) {
            rDir = -1;
        }


        if (aC - c > 0) {
            cDir = 1;
        } else if (aC - c == 0) {
            cDir = 0;
        } else if (aC - c < 0) {
            cDir = -1;
        }
        // to know if any other pieces can stop the check

        int kR = r, kC = c;
        while (kR != aR && kC != aC) {
            kR = kR + rDir;
            kC += cDir;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (gameBoard[i][j] != null && gameBoard[i][j].name.charAt(0) == color && gameBoard[i][j].isValid(gameBoard, i, j, kR, kC)) {
                        return false;
                    }
                }
            }
        }
        return checkNum == 8;
    }


//    ImageButton drawButton = findViewById(R.id.draw);
//
//drawButton.setOnClickListener(new View.OnClickListener()
//
//    {
//        public void onClick (View view){
//        AlertDialog.Builder builder = new AlertDialog.Builder(play.this);
//        builder.setMessage("The game has been drawn")
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Close the dialog
//                        dialog.dismiss();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//    });



}
