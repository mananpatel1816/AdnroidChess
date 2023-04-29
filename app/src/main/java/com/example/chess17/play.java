package com.example.chess17;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class play extends AppCompatActivity {
    public int numClicks = 0;

    private Context context;

    private ImageView chessboardImage, blackTurnImg, whiteTurnImg, whiteUndoButton, blackUndoButton;

    private ImageButton draw, resign;

    private static int fromRow, fromCol;

    ImageView piece;

    TextView textView;


    Pieces[][] gameBoard = new Pieces[8][8];  // initializing the chess board with 2d array

    static boolean wTurn = true, castle = false, passant = false;
    String pawnEnPassantId;
    static int aR, aC;

    int prevFromRow, prevFromCol, prevToRow, prevToCol;

    int pawnImgResource, pawnImgID;

    ImageView prevFromImage, prevToImage;

    Pieces removedPiece, fromPiece, toPiece, undoPawn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        context = this;

        start(gameBoard); // initializes the gameBoard

        StringBuilder gameMoves = new StringBuilder();

        textView = findViewById(R.id.textView);

        chessboardImage = findViewById(R.id.chessBoard);
        blackTurnImg = findViewById(R.id.blackTurn);
        whiteTurnImg = findViewById(R.id.whiteTurn);

        whiteTurnImg.requestFocus();
        blackTurnImg.clearFocus();


        whiteUndoButton = findViewById(R.id.whiteUndo);
        blackUndoButton = findViewById(R.id.blackUndo);

        ImageButton AIButton = findViewById(R.id.bot);

        AIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(piece != null) piece.setBackground(null);
                int resId, enPassantresId = 0;

                int newId = 1;

                Random random = new Random();

                while (true) {
                    int randomRow = random.nextInt(8);
                    int randomCol = random.nextInt(8);

                    Pieces randomPiece = gameBoard[randomRow][randomCol];
                    if (randomPiece != null && ( (wTurn && randomPiece.name.charAt(0) == 'w') || (!wTurn && randomPiece.name.charAt(0) == 'b') ) )
                    {
                        String fromImgId;

                        if (!(randomPiece instanceof King) ) {
                            //System.out.println(gameBoard[fromRow][fromCol].name);
                            fromImgId = randomPiece.name.toLowerCase() + randomPiece.num;
                            resId = getResources().getIdentifier(fromImgId, "id", getPackageName());
                        } else {
                            fromImgId = randomPiece.name.toLowerCase();
                            resId = getResources().getIdentifier(fromImgId, "id", getPackageName());
                        }
                        if(resId == 0)
                        {
                            resId  = randomPiece.num;
                        }

                        ImageView pieceToMove = findViewById(resId);

                        // Perform a random movement for the selected piece
                        int newRow = random.nextInt(8);
                        int newCol = random.nextInt(8);

                        if (randomPiece.isValid(gameBoard, randomRow, randomCol, newRow, newCol)) {
                            // Update the chessboard array with the new position
                            ImageView pieceToRemove = null;
                            removedPiece = null;

                            if(gameBoard[newRow][newCol] != null)
                            {
                                if (!(gameBoard[newRow][newCol] instanceof King) ) {
                                    //System.out.println(gameBoard[fromRow][fromCol].name);
                                    String imgId = gameBoard[newRow][newCol].name.toLowerCase() + gameBoard[newRow][newCol].num;
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                } else {
                                    String imgId = gameBoard[newRow][newCol].name.toLowerCase();
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                }
                                if(resId == 0)
                                {
                                    resId  = gameBoard[newRow][newCol].num;
//
//                                            resId = getResources().getIdentifier("wq1", "id", getPackageName());
//                                            resId += gameBoard[toRow][toCol].num;
                                }

                                pieceToRemove = findViewById(resId);
                                removedPiece = gameBoard[newRow][newCol];
                            }


                            if(randomPiece instanceof King && randomPiece.isCastle)
                            {
                                fromPiece = randomPiece;
                                castle = true;
                            }

                            if(randomPiece instanceof Pawn && randomPiece.enpassant)
                            {
                                fromPiece = randomPiece;
                                toPiece = gameBoard[fromRow][newCol];


                                pawnEnPassantId = gameBoard[fromRow][newCol].name.toLowerCase() + gameBoard[fromRow][newCol].num;
                                enPassantresId = getResources().getIdentifier(pawnEnPassantId, "id", getPackageName());

                                passant = true;
                            }
                            if(randomPiece.name.charAt(1) == 'P')
                                undoPawn = randomPiece;

                            randomPiece.move(gameBoard, randomRow, randomCol, newRow, newCol);


                            System.out.println("gameBoard Moved");

                            if ( (wTurn && check(gameBoard, King.wr, King.wc, 'w') ) || (!wTurn && check(gameBoard, King.br, King.bc, 'b')))
                            {
                                gameBoard[randomRow][randomCol] = gameBoard[newRow][newCol];
                                gameBoard[newRow][newCol] = null;
                                if(gameBoard[randomRow][randomCol].name.charAt(1) == 'K')
                                {
                                    King.wr = randomRow;
                                    King.wc = randomCol;
                                }
                                if(castle)
                                {
                                    System.out.println("Castling case for CHECK:");

                                    if(randomCol < newCol)
                                    {
                                        gameBoard[randomRow][7] = gameBoard[randomRow][newCol-1];
                                        gameBoard[randomRow][newCol-1] = null;
                                    }
                                    if(randomCol > newCol)
                                    {
                                        gameBoard[randomRow][0] = gameBoard[randomRow][newCol+1];
                                        gameBoard[randomRow][newCol+1] = null;
                                    }
                                    fromPiece = null;
                                    castle = false;
                                }
                                else if(passant)
                                {
                                    System.out.println("CHECK Passant true");
                                    gameBoard[randomRow][newCol] = toPiece;
                                    toPiece = null;
                                    fromPiece = null;
                                    passant = false;
                                }
                                else if(removedPiece != null)
                                {
                                    gameBoard[newRow][newCol] = removedPiece;
                                    System.out.println("Removed Piece: " + removedPiece);
                                    removedPiece = null;
                                }
                                print(gameBoard);
                                System.out.println("check Moved");
                            } else {
                                System.out.println("UI MOving");

//                                        moveUIBoard(gameBoard);

                                print(gameBoard);

                                prevFromRow = randomRow;
                                System.out.println("Initialize: " + prevFromRow);
                                prevFromCol = randomCol;

                                prevToRow = newRow;
                                prevToCol = newCol;

                                prevFromImage = pieceToMove;
                                prevToImage = pieceToRemove;

                                int squareSize = chessboardImage.getWidth()/8;
                                // Update the layout params of the piece to move it to the new position
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieceToMove.getLayoutParams();
                                params.leftMargin = newCol * squareSize;
                                params.topMargin = newRow * squareSize;


                                ViewGroup parent = (ViewGroup) pieceToMove.getParent();
//                                        ViewGroup removeParent = (ViewGroup) pieceToRemove.getParent();
                                if (parent != null) {
                                    parent.removeView(pieceToMove);
                                    parent.removeView(pieceToRemove);
                                    RelativeLayout boardLayout = findViewById(R.id.board_layout);

                                    if (castle) {
                                        if (randomCol < newCol) {
                                            prevToCol = 7;
                                            toPiece = gameBoard[randomRow][newCol - 1];

                                            ImageView rookCastle = findViewById(R.id.wr2);
                                            prevToImage = rookCastle;

                                            RelativeLayout.LayoutParams castleParams = (RelativeLayout.LayoutParams) rookCastle.getLayoutParams();
                                            castleParams.leftMargin = (newCol - 1) * squareSize;
                                            castleParams.topMargin = newRow * squareSize;

                                            parent.removeView(rookCastle);

                                            boardLayout.addView(rookCastle, castleParams);
                                        } else if (randomCol > newCol) {
                                            prevToCol = 0;
                                            toPiece = gameBoard[randomRow][newCol + 1];

                                            ImageView rookCastle = findViewById(R.id.wr1);
                                            prevToImage = rookCastle;

                                            RelativeLayout.LayoutParams castleParams = (RelativeLayout.LayoutParams) rookCastle.getLayoutParams();
                                            castleParams.leftMargin = (newCol + 1) * squareSize;
                                            castleParams.topMargin = newRow * squareSize;

                                            parent.removeView(rookCastle);

                                            boardLayout.addView(rookCastle, castleParams);
                                        }
                                        castle = false;
                                    }

                                    if (passant) {
                                        prevToRow = randomRow;

                                        ImageView pawnEnpassant = findViewById(enPassantresId);
                                        prevToImage = pawnEnpassant;

                                        parent.removeView(pawnEnpassant);
                                        passant = false;
                                    }

                                    if (pieceToMove != null) {
                                        boardLayout.addView(pieceToMove, params);
                                        if (gameBoard[newRow][newCol].promote) {
                                            pawnImgResource = R.drawable.wp;
                                            String imgId = undoPawn.name.toLowerCase() + undoPawn.num;
                                            pawnImgID = getResources().getIdentifier(imgId, "id", getPackageName());

                                            pieceToMove.setImageResource(R.drawable.wq);
//                                                    String id = gameBoard[toRow][toCol].name.toLowerCase() + 1;
                                            newId++;
                                            ImageView temp = findViewById(newId);
                                            while (temp != null) {
                                                newId++;
                                                temp = findViewById(newId);
                                            }
                                            //System.out.println("white queen count: " + gameBoard[toRow][toCol].num);
                                            //int newId = getResources().getIdentifier(id, "id", getPackageName());
                                            //System.out.println("New ID for queen: " + newId);
                                            pieceToMove.setId(newId);
                                            gameBoard[newRow][newCol].num = newId;
                                            gameBoard[newRow][newCol].promote = false;
                                        } else
                                            undoPawn = null;
                                    }
                                }
                                numClicks = 0;
                                fromRow = -1;
                                fromCol = -1;
                                System.out.println("UI Moved");
                                pieceToMove.setBackground(null);


                                whiteTurnImg.setClickable(false);
                                blackTurnImg.setClickable(true);

                                textView = findViewById(R.id.textView);
                                if(wTurn)
                                {
                                    whiteUndoButton.setEnabled(true);
                                    blackUndoButton.setEnabled(false);
                                    textView.setText("Black's move");
                                    wTurn = false;
                                }
                                else
                                {
                                    whiteUndoButton.setEnabled(false);
                                    blackUndoButton.setEnabled(true);
                                    textView.setText("White's move");
                                    wTurn = true;
                                }
                                print(gameBoard);
                                break;
                            }
                        }
                    }
                }
            }
        });

        whiteUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int squareSize = chessboardImage.getWidth()/8;

                if(gameBoard[prevToRow][prevToCol] != null)
                {
                    gameBoard[prevFromRow][prevFromCol] = gameBoard[prevToRow][prevToCol];
                    gameBoard[prevToRow][prevToCol] = null;

                    if(prevToImage != null)
                    {
                        gameBoard[prevToRow][prevToCol] = removedPiece;
                    }

                    if(undoPawn != null)
                    {
                        gameBoard[prevFromRow][prevFromCol] = undoPawn;
                    }
                }
                else
                {
                    RelativeLayout.LayoutParams fromPieceParams = (RelativeLayout.LayoutParams) prevFromImage.getLayoutParams();

                    int row = fromPieceParams.topMargin/squareSize;
                    int col = fromPieceParams.leftMargin/squareSize;
                    gameBoard[row][col] = null;

                    if(prevToImage != null)
                    {
                        RelativeLayout.LayoutParams toPieceParams = (RelativeLayout.LayoutParams) prevToImage.getLayoutParams();

                        int toR = toPieceParams.topMargin/squareSize;
                        int toC = toPieceParams.leftMargin/squareSize;

                        gameBoard[toR][toC] = null;
                    }

                    gameBoard[prevFromRow][prevFromCol] = fromPiece;
                    gameBoard[prevToRow][prevToCol] = toPiece;
                }



                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) prevFromImage.getLayoutParams();
                params.leftMargin = prevFromCol * squareSize;
                params.topMargin = prevFromRow * squareSize;

                System.out.println(prevFromRow + " " + prevFromCol);

//                                        ViewGroup removeParent = (ViewGroup) pieceToRemove.getParent();
                RelativeLayout boardLayout = findViewById(R.id.board_layout);
                ViewGroup parent = (ViewGroup) prevFromImage.getParent();
                if (parent != null) {

                    parent.removeView(prevFromImage);
                    //parent.removeView(pieceToRemove);

                    if (prevFromImage != null)
                    {
                        if(pawnImgID != 0)
                        {
                            prevFromImage.setImageResource(pawnImgResource);
                            prevFromImage.setId(pawnImgID);
                        }

                        boardLayout.addView(prevFromImage, params);
                    }
                }


                if(prevToImage != null)
                {
                    RelativeLayout.LayoutParams toParam = (RelativeLayout.LayoutParams) prevToImage.getLayoutParams();
                    toParam.leftMargin = prevToCol * squareSize;
                    toParam.topMargin = prevToRow * squareSize;



                    if (parent != null) {
                        parent.removeView(prevToImage);
                        boardLayout.addView(prevToImage, toParam);
                    }
                }

                wTurn = true;
                textView = findViewById(R.id.textView);
                textView.setText("White's Move");
                prevToImage = prevFromImage = null;
                prevToRow = prevToCol= prevFromCol= prevFromRow = -1;
                pawnImgID = pawnImgResource = 0;
                removedPiece = fromPiece = toPiece = undoPawn = null;
                whiteUndoButton.setEnabled(false);
            }
        });

        blackUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int squareSize = chessboardImage.getWidth()/8;

                if(gameBoard[prevToRow][prevToCol] != null)
                {
                    gameBoard[prevFromRow][prevFromCol] = gameBoard[prevToRow][prevToCol];
                    gameBoard[prevToRow][prevToCol] = null;

                    if(prevToImage != null)
                    {
                        gameBoard[prevToRow][prevToCol] = removedPiece;
                    }

                    if(undoPawn != null)
                    {
                        gameBoard[prevFromRow][prevFromCol] = undoPawn;
                    }
                }
                else
                {
                    RelativeLayout.LayoutParams fromPieceParams = (RelativeLayout.LayoutParams) prevFromImage.getLayoutParams();

                    int row = fromPieceParams.topMargin/squareSize;
                    int col = fromPieceParams.leftMargin/squareSize;
                    gameBoard[row][col] = null;

                    if(prevToImage != null)
                    {
                        RelativeLayout.LayoutParams toPieceParams = (RelativeLayout.LayoutParams) prevToImage.getLayoutParams();

                        int toR = toPieceParams.topMargin/squareSize;
                        int toC = toPieceParams.leftMargin/squareSize;

                        gameBoard[toR][toC] = null;
                    }

                    gameBoard[prevFromRow][prevFromCol] = fromPiece;
                    gameBoard[prevToRow][prevToCol] = toPiece;
                }



                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) prevFromImage.getLayoutParams();
                params.leftMargin = prevFromCol * squareSize;
                params.topMargin = prevFromRow * squareSize;

                System.out.println(prevFromRow + " " + prevFromCol);

//                                        ViewGroup removeParent = (ViewGroup) pieceToRemove.getParent();
                RelativeLayout boardLayout = findViewById(R.id.board_layout);
                ViewGroup parent = (ViewGroup) prevFromImage.getParent();
                if (parent != null) {

                    parent.removeView(prevFromImage);
                    //parent.removeView(pieceToRemove);

                    if (prevFromImage != null)
                    {
                        if(pawnImgID != 0)
                        {
                            prevFromImage.setImageResource(pawnImgResource);
                            prevFromImage.setId(pawnImgID);
                        }

                        boardLayout.addView(prevFromImage, params);
                    }
                }


                if(prevToImage != null)
                {
                    RelativeLayout.LayoutParams toParam = (RelativeLayout.LayoutParams) prevToImage.getLayoutParams();
                    toParam.leftMargin = prevToCol * squareSize;
                    toParam.topMargin = prevToRow * squareSize;



                    if (parent != null) {
                        parent.removeView(prevToImage);
                        boardLayout.addView(prevToImage, toParam);
                    }
                }

                wTurn = false;
                textView = findViewById(R.id.textView);
                textView.setText("Black's Move");
                prevToImage = prevFromImage = null;
                prevToRow = prevToCol= prevFromCol= prevFromRow = -1;
                pawnImgID = pawnImgResource = 0;
                removedPiece = fromPiece = toPiece = undoPawn = null;
                blackUndoButton.setEnabled(false);
            }
        });



        chessboardImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // User has touched the screen
                        if (numClicks == 1)
                        {
                            int x2 = (int) event.getX();
                            int y2 = (int) event.getY();
                            int squareSize = chessboardImage.getWidth() / 8;
                            int toRow = y2 / squareSize;
                            int toCol = x2 / squareSize;

                            int resId, enPassantresId = 0;

                            int newId = 1;

                            System.out.println("To Row and Col: " + toRow + toCol);


                            if (wTurn)
                            {


                                System.out.println("white Turn");

                                String fromImgId;
                                if (!(gameBoard[fromRow][fromCol] instanceof King) ) {
                                    //System.out.println(gameBoard[fromRow][fromCol].name);
                                    fromImgId = gameBoard[fromRow][fromCol].name.toLowerCase() + gameBoard[fromRow][fromCol].num;
                                    resId = getResources().getIdentifier(fromImgId, "id", getPackageName());
                                } else {
                                    fromImgId = gameBoard[fromRow][fromCol].name.toLowerCase();
                                    resId = getResources().getIdentifier(fromImgId, "id", getPackageName());
                                }
                                if(resId == 0)
                                {
                                    resId  = gameBoard[fromRow][fromCol].num;

//                                    resId = getResources().getIdentifier("wq1", "id", getPackageName());
//                                    resId += gameBoard[fromRow][fromCol].num;
                                }

                                ImageView pieceToMove = findViewById(resId);


                                if (gameBoard[fromRow][fromCol] != null && gameBoard[fromRow][fromCol].name.charAt(0) == 'w'
                                        && gameBoard[fromRow][fromCol].isValid(gameBoard, fromRow, fromCol, toRow, toCol))
                                {
                                    ImageView pieceToRemove = null;
                                    removedPiece = null;

                                    if(gameBoard[toRow][toCol] != null)
                                    {
                                        if (!(gameBoard[toRow][toCol] instanceof King) ) {
                                            //System.out.println(gameBoard[fromRow][fromCol].name);
                                            String imgId = gameBoard[toRow][toCol].name.toLowerCase() + gameBoard[toRow][toCol].num;
                                            resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                        } else {
                                            String imgId = gameBoard[toRow][toCol].name.toLowerCase();
                                            resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                        }
                                        if(resId == 0)
                                        {
                                            resId  = gameBoard[toRow][toCol].num;
//
//                                            resId = getResources().getIdentifier("wq1", "id", getPackageName());
//                                            resId += gameBoard[toRow][toCol].num;
                                        }

                                        pieceToRemove = findViewById(resId);
                                        removedPiece = gameBoard[toRow][toCol];
                                    }


                                    if(gameBoard[fromRow][fromCol] instanceof King && gameBoard[fromRow][fromCol].isCastle)
                                    {
                                        fromPiece = gameBoard[fromRow][fromCol];
                                        castle = true;
                                    }

                                    if(gameBoard[fromRow][fromCol] instanceof Pawn && gameBoard[fromRow][fromCol].enpassant)
                                    {
                                        fromPiece = gameBoard[fromRow][fromCol];
                                        toPiece = gameBoard[fromRow][toCol];


                                        pawnEnPassantId = gameBoard[fromRow][toCol].name.toLowerCase() + gameBoard[fromRow][toCol].num;
                                        enPassantresId = getResources().getIdentifier(pawnEnPassantId, "id", getPackageName());

                                        passant = true;
                                    }
                                    if(gameBoard[fromRow][fromCol].name.charAt(1) == 'P')
                                        undoPawn = gameBoard[fromRow][fromCol];

                                    gameBoard[fromRow][fromCol].move(gameBoard, fromRow, fromCol, toRow, toCol);


                                    System.out.println("gameBoard Moved");

                                    if (check(gameBoard, King.wr, King.wc, 'w'))
                                    {
                                        gameBoard[fromRow][fromCol] = gameBoard[toRow][toCol];
                                        gameBoard[toRow][toCol] = null;
                                        if(gameBoard[fromRow][fromCol].name.charAt(1) == 'K')
                                        {
                                            King.wr = fromRow;
                                            King.wc = fromCol;
                                        }
                                        if(castle)
                                        {
                                            System.out.println("Castling case for CHECK:");

                                            if(fromCol < toCol)
                                            {
                                                gameBoard[fromRow][7] = gameBoard[fromRow][toCol-1];
                                                gameBoard[fromRow][toCol-1] = null;
                                            }
                                            if(fromCol > toCol)
                                            {
                                                gameBoard[fromRow][0] = gameBoard[fromRow][toCol+1];
                                                gameBoard[fromRow][toCol+1] = null;
                                            }
                                            fromPiece = null;
                                            castle = false;
                                        }
                                        else if(passant)
                                        {
                                            System.out.println("CHECK Passant true");
                                            gameBoard[fromRow][toCol] = toPiece;
                                            toPiece = null;
                                            fromPiece = null;
                                            passant = false;
                                        }
                                        else if(removedPiece != null)
                                        {
                                            gameBoard[toRow][toCol] = removedPiece;
                                            System.out.println("Removed Piece: " + removedPiece);
                                            removedPiece = null;
                                        }
                                        print(gameBoard);
                                        System.out.println("check Moved");
                                    } else {
                                        System.out.println("UI MOving");

//                                        moveUIBoard(gameBoard);

                                        print(gameBoard);

                                        prevFromRow = fromRow;
                                        System.out.println("Initialize: "+prevFromRow);
                                        prevFromCol = fromCol;

                                        prevToRow = toRow;
                                        prevToCol = toCol;

                                        prevFromImage = pieceToMove;
                                        prevToImage = pieceToRemove;

                                        // Update the layout params of the piece to move it to the new position
                                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieceToMove.getLayoutParams();
                                        params.leftMargin = toCol * squareSize;
                                        params.topMargin = toRow * squareSize;


                                        gameMoves.append(pieceToMove.getId()).append(",").append(params.leftMargin).append(",").append(params.topMargin).append(",");
                                        if(pieceToRemove != null)
                                            gameMoves.append(pieceToRemove.getId());
                                        else
                                            gameMoves.append(0);
                                        gameMoves.append("\n");

                                        ViewGroup parent = (ViewGroup) pieceToMove.getParent();
//                                        ViewGroup removeParent = (ViewGroup) pieceToRemove.getParent();
                                        if (parent != null) {
                                            parent.removeView(pieceToMove);
                                            parent.removeView(pieceToRemove);
                                            RelativeLayout boardLayout = findViewById(R.id.board_layout);

                                            if(castle)
                                            {
                                                if(fromCol < toCol)
                                                {
                                                    prevToCol = 7;
                                                    toPiece = gameBoard[fromRow][toCol-1];

                                                    ImageView rookCastle = findViewById(R.id.wr2);
                                                    prevToImage = rookCastle;

                                                    RelativeLayout.LayoutParams castleParams = (RelativeLayout.LayoutParams) rookCastle.getLayoutParams();
                                                    castleParams.leftMargin = (toCol-1) * squareSize;
                                                    castleParams.topMargin = toRow * squareSize;

                                                    parent.removeView(rookCastle);

                                                    boardLayout.addView(rookCastle, castleParams);
                                                }
                                                else if(fromCol > toCol)
                                                {
                                                    prevToCol = 0;
                                                    toPiece = gameBoard[fromRow][toCol+1];

                                                    ImageView rookCastle = findViewById(R.id.wr1);
                                                    prevToImage = rookCastle;

                                                    RelativeLayout.LayoutParams castleParams = (RelativeLayout.LayoutParams) rookCastle.getLayoutParams();
                                                    castleParams.leftMargin = (toCol+1) * squareSize;
                                                    castleParams.topMargin = toRow * squareSize;

                                                    parent.removeView(rookCastle);

                                                    boardLayout.addView(rookCastle, castleParams);
                                                }
                                                castle = false;
                                            }

                                            if(passant)
                                            {
                                                prevToRow = fromRow;

                                                ImageView pawnEnpassant = findViewById(enPassantresId);
                                                prevToImage = pawnEnpassant;

                                                parent.removeView(pawnEnpassant);
                                                passant = false;
                                            }

                                            if (pieceToMove != null) {
                                                boardLayout.addView(pieceToMove, params);
                                                if(gameBoard[toRow][toCol].promote)
                                                {
                                                    pawnImgResource = R.drawable.wp;
                                                    String imgId = undoPawn.name.toLowerCase() + undoPawn.num;
                                                    pawnImgID = getResources().getIdentifier(imgId, "id", getPackageName());

                                                    pieceToMove.setImageResource(R.drawable.wq);
//                                                    String id = gameBoard[toRow][toCol].name.toLowerCase() + 1;
                                                    newId++;
                                                    ImageView temp = findViewById(newId);
                                                    while(temp != null)
                                                    {
                                                        newId++;
                                                        temp =  findViewById(newId);
                                                    }
                                                    //System.out.println("white queen count: " + gameBoard[toRow][toCol].num);
                                                    //int newId = getResources().getIdentifier(id, "id", getPackageName());
                                                    //System.out.println("New ID for queen: " + newId);
                                                    pieceToMove.setId(newId);
                                                    gameBoard[toRow][toCol].num = newId;
                                                    gameBoard[toRow][toCol].promote = false;
                                                }
                                                else
                                                    undoPawn = null;
                                            }
                                        }


                                        numClicks = 0;
                                        fromRow = -1;
                                        fromCol = -1;
                                        System.out.println("UI Moved");
                                        pieceToMove.setBackground(null);
                                        whiteUndoButton.setEnabled(true);
                                        blackUndoButton.setEnabled(false);

                                        whiteTurnImg.setClickable(false);
                                        blackTurnImg.setClickable(true);
                                        textView = findViewById(R.id.textView);
                                        textView.setText("Black's Move");
                                        wTurn = false;
                                    }


                                } else
                                {
                                    int nextResId = 0;
                                    if(gameBoard[toRow][toCol] != null && gameBoard[toRow][toCol].name.charAt(0) == 'w')
                                    {
                                        if (!(gameBoard[toRow][toCol] instanceof King)) {
                                            //System.out.println(gameBoard[fromRow][fromCol].name);
                                            String imgId = gameBoard[toRow][toCol].name.toLowerCase() + gameBoard[toRow][toCol].num;
                                            nextResId = getResources().getIdentifier(imgId, "id", getPackageName());
                                        } else
                                        {
                                            String imgId = gameBoard[toRow][toCol].name.toLowerCase();
                                            nextResId = getResources().getIdentifier(imgId, "id", getPackageName());
                                        }
                                        if(nextResId == 0)
                                        {
                                            nextResId  = gameBoard[toRow][toCol].num;

//                                            nextResId = getResources().getIdentifier("wq1", "id", getPackageName());
//                                            nextResId += gameBoard[toRow][toCol].num;
                                        }
                                        ImageView whitePiece = findViewById(nextResId);
                                        pieceToMove.setBackground(null);

                                        fromRow = toRow;
                                        fromCol = toCol;

                                        GradientDrawable border = new GradientDrawable();
                                        border.setStroke(5, Color.RED);

                                        whitePiece.setBackground(border);
                                    }
//                                    else numClicks = 0;
                                }

                            } else
                            {
                                System.out.println("Black's Turn");
                                System.out.println("Initial: "+ fromCol + "  "+ fromRow);

                                if (!(gameBoard[fromRow][fromCol] instanceof King)) {
                                    //System.out.println(gameBoard[fromRow][fromCol].name);
                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase() + gameBoard[fromRow][fromCol].num;
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                } else {
                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase();
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                }
                                if(resId == 0)
                                {
                                    resId  = gameBoard[fromRow][fromCol].num;
//                                    resId = getResources().getIdentifier("bq1", "id", getPackageName());
//                                    resId += gameBoard[fromRow][fromCol].num;

                                }

                                ImageView pieceToMove = findViewById(resId);

                                if (gameBoard[fromRow][fromCol] != null && gameBoard[fromRow][fromCol].name.charAt(0) == 'b'
                                        && gameBoard[fromRow][fromCol].isValid(gameBoard, fromRow, fromCol, toRow, toCol))
                                {

                                    ImageView pieceToRemove = null;
                                    removedPiece = null;

                                    if(gameBoard[toRow][toCol] != null)
                                    {
                                        if (!(gameBoard[toRow][toCol] instanceof King) ) {
                                            //System.out.println(gameBoard[fromRow][fromCol].name);
                                            String imgId = gameBoard[toRow][toCol].name.toLowerCase() + gameBoard[toRow][toCol].num;
                                            resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                        } else {
                                            String imgId = gameBoard[toRow][toCol].name.toLowerCase();
                                            resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                        }
                                        if(resId == 0)
                                        {
                                            resId  = gameBoard[toRow][toCol].num;
//                                            resId = getResources().getIdentifier("bq1", "id", getPackageName());
//                                            resId += gameBoard[toRow][toCol].num;

                                        }

                                        pieceToRemove = findViewById(resId);
                                        removedPiece = gameBoard[toRow][toCol];
                                    }

                                    if(gameBoard[fromRow][fromCol] instanceof King && gameBoard[fromRow][fromCol].isCastle)
                                    {
                                        fromPiece = gameBoard[fromRow][fromCol];
                                        castle = true;
                                    }

                                    if(gameBoard[fromRow][fromCol] instanceof Pawn && gameBoard[fromRow][fromCol].enpassant)
                                    {
                                        fromPiece = gameBoard[fromRow][fromCol];
                                        toPiece = gameBoard[fromRow][toCol];

                                        pawnEnPassantId = gameBoard[fromRow][toCol].name.toLowerCase() + gameBoard[fromRow][toCol].num;
                                        enPassantresId = getResources().getIdentifier(pawnEnPassantId, "id", getPackageName());
                                        passant = true;
                                    }

                                    if(gameBoard[fromRow][fromCol].name.charAt(1) == 'P')
                                        undoPawn = gameBoard[fromRow][fromCol];

                                    gameBoard[fromRow][fromCol].move(gameBoard, fromRow, fromCol, toRow, toCol);
                                    System.out.println("gameboard moved");
                                    if (check(gameBoard, King.br, King.bc, 'b')) {
                                        gameBoard[fromRow][fromCol] = gameBoard[toRow][toCol];
                                        gameBoard[toRow][toCol] = null;

                                        if(gameBoard[fromRow][fromCol].name.charAt(1) == 'K')
                                        {
                                            King.br = fromRow;
                                            King.bc = fromCol;
                                        }

                                        if(castle)
                                        {
                                            System.out.println("Castling case for CHECK:");

                                            if(fromCol < toCol)
                                            {
                                                gameBoard[fromRow][7] = gameBoard[fromRow][toCol-1];
                                                gameBoard[fromRow][toCol-1] = null;
                                            }
                                            if(fromCol > toCol)
                                            {
                                                gameBoard[fromRow][0] = gameBoard[fromRow][toCol+1];
                                                gameBoard[fromRow][toCol+1] = null;
                                            }
                                            fromPiece = null;
                                            castle = false;
                                        }
                                        else if(passant)
                                        {
                                            System.out.println("CHECK Passant true");
                                            gameBoard[fromRow][toCol] = toPiece;
                                            toPiece = null;
                                            fromPiece = null;
                                            passant = false;
                                        }
                                        else if(removedPiece != null)
                                        {
                                            gameBoard[toRow][toCol] = removedPiece;
                                            System.out.println("Removed Piece: " + removedPiece);
                                            removedPiece = null;
                                        }

                                        System.out.println("gameboard backed to normal");
                                    } else {
                                        System.out.println("UI Moving");

                                        print(gameBoard);

                                        prevFromRow = fromRow;
                                        prevFromCol = fromCol;

                                        prevToRow = toRow;
                                        prevToCol = toCol;

                                        prevFromImage = pieceToMove;
                                        prevToImage = pieceToRemove;

                                        // Update the layout params of the piece to move it to the new position
                                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieceToMove.getLayoutParams();
                                        params.leftMargin = toCol * squareSize;
                                        params.topMargin = toRow * squareSize;

                                        gameMoves.append(pieceToMove.getId()).append(",").append(params.leftMargin).append(",").append(params.topMargin).append(",");
                                        if(pieceToRemove != null)
                                            gameMoves.append(pieceToRemove.getId());
                                        else
                                            gameMoves.append(0);
                                        gameMoves.append("\n");


                                        ViewGroup parent = (ViewGroup) pieceToMove.getParent();
//                                        ViewGroup removeParent = (ViewGroup) pieceToRemove.getParent();

                                        if (parent != null) {
                                            parent.removeView(pieceToMove);
                                            parent.removeView(pieceToRemove);
                                            RelativeLayout boardLayout = findViewById(R.id.board_layout);

                                            if(castle)
                                            {
                                                if(fromCol < toCol)
                                                {
                                                    prevToCol = 7;
                                                    toPiece = gameBoard[fromRow][toCol-1];

                                                    ImageView rookCastle = findViewById(R.id.br2);
                                                    prevToImage = rookCastle;

                                                    RelativeLayout.LayoutParams castleParams = (RelativeLayout.LayoutParams) rookCastle.getLayoutParams();
                                                    castleParams.leftMargin = (toCol-1) * squareSize;
                                                    castleParams.topMargin = toRow * squareSize;

                                                    parent.removeView(rookCastle);

                                                    boardLayout.addView(rookCastle, castleParams);
                                                }
                                                else if(fromCol > toCol)
                                                {
                                                    prevToCol = 0;
                                                    toPiece = gameBoard[fromRow][toCol+1];
                                                    //System.out.println(gameBoard[fromRow][toCol+1]);

                                                    ImageView rookCastle = findViewById(R.id.br1);
                                                    prevToImage = rookCastle;

                                                    RelativeLayout.LayoutParams castleParams = (RelativeLayout.LayoutParams) rookCastle.getLayoutParams();
                                                    castleParams.leftMargin = (toCol+1) * squareSize;
                                                    castleParams.topMargin = toRow * squareSize;

                                                    parent.removeView(rookCastle);

                                                    boardLayout.addView(rookCastle, castleParams);
                                                }
                                                castle = false;
                                            }

                                            if(passant)
                                            {
                                                prevToRow = fromRow;

                                                ImageView pawnEnpassant = findViewById(enPassantresId);
                                                prevToImage = pawnEnpassant;

                                                parent.removeView(pawnEnpassant);
                                                passant = false;
                                            }

                                            if (pieceToMove != null) {
                                                boardLayout.addView(pieceToMove, params);

                                                if(gameBoard[toRow][toCol].promote)
                                                {
                                                    pawnImgResource = R.drawable.bp;
                                                    String imgId = undoPawn.name.toLowerCase() + undoPawn.num;
                                                    pawnImgID = getResources().getIdentifier(imgId, "id", getPackageName());

                                                    pieceToMove.setImageResource(R.drawable.bq);
//                                                    String id = gameBoard[toRow][toCol].name.toLowerCase() + 1;
                                                    newId++;
                                                    ImageView temp = findViewById(newId);
                                                    while(temp != null)
                                                    {
                                                        newId++;
                                                        temp =  findViewById(newId);
                                                    }
                                                    //System.out.println("white queen count: " + gameBoard[toRow][toCol].num);
                                                    //int newId = getResources().getIdentifier(id, "id", getPackageName());
                                                    //System.out.println("New ID for queen: " + newId);
                                                    pieceToMove.setId(newId);
                                                    gameBoard[toRow][toCol].num = newId;
//                                                    System.out.println("New ID: " + newId);
                                                    //System.out.println("Id of black Queen" + gameBoard[toRow][toCol].num + " " + newId);

                                                    gameBoard[toRow][toCol].promote = false;
                                                }
                                                else
                                                    undoPawn = null;
                                            }
                                        }

                                        numClicks = 0;
                                        fromRow = -1;
                                        fromCol = -1;
                                        System.out.println("UI Moved");
                                        pieceToMove.setBackground(null);
                                        whiteUndoButton.setEnabled(false);
                                        blackUndoButton.setEnabled(true);

                                        whiteTurnImg.setClickable(true);
                                        blackTurnImg.setClickable(false);
                                        textView = findViewById(R.id.textView);
                                        textView.setText("White's Move");
                                        wTurn = true;
                                    }
                                } else
                                {
                                    int nextResId = 0;
                                    if(gameBoard[toRow][toCol] != null && gameBoard[toRow][toCol].name.charAt(0) == 'b')
                                    {
                                        if (!(gameBoard[toRow][toCol] instanceof King)) {
                                            //System.out.println(gameBoard[fromRow][fromCol].name);
                                            String imgId = gameBoard[toRow][toCol].name.toLowerCase() + gameBoard[toRow][toCol].num;
                                            nextResId = getResources().getIdentifier(imgId, "id", getPackageName());
                                        } else {
                                            String imgId = gameBoard[toRow][toCol].name.toLowerCase();
                                            nextResId = getResources().getIdentifier(imgId, "id", getPackageName());
                                        }
                                        if(nextResId == 0)
                                        {
                                            nextResId  = gameBoard[toRow][toCol].num;
//                                            nextResId = getResources().getIdentifier("bq1", "id", getPackageName());
//                                            nextResId += gameBoard[toRow][toCol].num;
                                        }
                                        System.out.println("Error: " + gameBoard[toRow][toCol].name + gameBoard[toRow][toCol].num);

                                        ImageView blackPiece = findViewById(nextResId);
                                        pieceToMove.setBackground(null);

                                        fromRow = toRow;
                                        fromCol = toCol;

                                        GradientDrawable border = new GradientDrawable();
                                        border.setStroke(5, Color.RED);

                                        blackPiece.setBackground(border);
                                    }
//                                    else {
//                                        numClicks = 0;
//                                    }
                                }
                            }

                            System.out.println("CheckING");
                            if (check(gameBoard, King.wr, King.wc, 'w')) {
                                System.out.println("Check");
                                if (checkmate(gameBoard, King.wr, King.wc, 'w')) {

                                    System.out.println("Checkmate");
                                    System.out.println("Black Wins");
                                }
                            } else if (check(gameBoard, King.br, King.bc, 'b')) {
                                System.out.println("Check");
                                if (checkmate(gameBoard, King.br, King.bc, 'b')) {
                                    System.out.println("Checkmate");
                                    System.out.println("White Wins");
                                }
                            }

                        }
                        else
                        {
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
                            } else
                            {
                                int resId;
                                if (!(gameBoard[fromRow][fromCol] instanceof King) ) {
                                    //System.out.println(gameBoard[fromRow][fromCol].name);
                                    System.out.println("ERROR CHECKING: " + gameBoard[fromRow][fromCol].name + gameBoard[fromRow][fromCol].num);

                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase() + gameBoard[fromRow][fromCol].num;
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                } else {
                                    String imgId = gameBoard[fromRow][fromCol].name.toLowerCase();
                                    resId = getResources().getIdentifier(imgId, "id", getPackageName());
                                }
                                if(resId == 0)
                                {
                                    resId = gameBoard[fromRow][fromCol].num;

//                                    if(wTurn)
//                                    {
//                                        resId = getResources().getIdentifier("wq1", "id", getPackageName());
//                                        resId += gameBoard[fromRow][fromCol].num;
//                                    }
//                                    else
//                                    {
//                                        resId = getResources().getIdentifier("bq1", "id", getPackageName());
//                                        resId += gameBoard[fromRow][fromCol].num;
//                                    }

                                }


                                piece = findViewById(resId);
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


        draw = findViewById(R.id.draw);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(play.this);

                // Set the dialog title and message
                builder.setTitle("Draw");
                builder.setMessage("Are you sure you want to offer a draw?");

                // Set the "OK" button
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // Set the "Cancel" button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Cancel", do nothing
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        resign = findViewById(R.id.resign);
        resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(play.this);

                // Set the dialog title and message
                builder.setTitle("Resign");
                builder.setMessage("Are you sure you want resign?");

                // Set the "OK" button
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(play.this);

                        final EditText input = new EditText(play.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = input.getText().toString();

                                try
                                {
                                    File recordedGamesFolder = new File(context.getFilesDir(), "RecordedGames");
                                    recordedGamesFolder.mkdirs(); // Create the "RecordedGames" folder if it doesn't exist
                                    File file = new File(recordedGamesFolder, name + ".txt");
                                    FileWriter fileWriter = new FileWriter(file);
                                    fileWriter.write(gameMoves.toString());
                                    fileWriter.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //nameList.add(name);
                                // Save the game with the name provided by the user
                                // ...
                            }

                        });
                        AlertDialog wDialog = builder.create();
                        wDialog.show();
                    }
                });

                // Set the "Cancel" button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Cancel", do nothing
                    }
                });

                // Create and show the dialog box
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        ImageButton home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(play.this, MainActivity.class);
                startActivity(intent);
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
        gameBoard[0][3] = new Queen('b',1);
        gameBoard[0][4] = new King('b');
        gameBoard[0][5] = new Bishop('b', 2);
        gameBoard[0][6] = new Knight('b', 2);
        gameBoard[0][7] = new Rook('b', 2);

        gameBoard[7][0] = new Rook('w', 1);
        gameBoard[7][1] = new Knight('w', 1);
        gameBoard[7][2] = new Bishop('w', 1);
        gameBoard[7][3] = new Queen('w', 1);
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
                if (gameBoard[i][j] != null && gameBoard[i][j].name.charAt(0) == color && gameBoard[i][j].isValid(gameBoard, i, j, aR, aC))
                {
                    System.out.println("Piece Name " + gameBoard[i][j].name);
                    System.out.println("above can kill the attacker");
                    System.out.println(i + " " + j);
                    System.out.println(aR + " " + aC);
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
                    if (gameBoard[i][j] != null && gameBoard[i][j].name.charAt(0) == color && gameBoard[i][j].name.charAt(1) != 'K' && gameBoard[i][j].isValid(gameBoard, i, j, kR, kC))
                    {
                        System.out.println("Piece Name " + gameBoard[i][j].name);
                        System.out.println("someone can stop the checkmate");
                        System.out.println(i + " " + j);
                        System.out.println(kR + " " + kC);
                        return false;
                    }
                }
            }
        }

        return checkNum == 8;
    }




    private static void print(Pieces[][] gameBoard)
    {
        boolean space = true;
        int row = 8;

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(space)
                {
                    if(gameBoard[i][j] == null)
                    {
                        System.out.print("  ");
                    }
                    else
                        System.out.print(gameBoard[i][j].name);

                    System.out.print(" ");

                    if(j != 7)
                        space = false;
                    else
                        System.out.print(row--);
                }
                else
                {
                    if(gameBoard[i][j] == null)
                    {
                        System.out.print("##");
                    }
                    else
                        System.out.print(gameBoard[i][j].name);

                    System.out.print(" ");

                    if(j != 7)
                        space = true;
                    else
                        System.out.print(row--);
                }

            }
            System.out.println();
        }
        System.out.println(" a  b  c  d  e  f  g  h");

    }

}
