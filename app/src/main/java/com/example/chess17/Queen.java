package com.example.chess17;

/**
 * @author Manan Patel
 *  @author Nivesh Nayee
 */

public class Queen extends Pieces
{
    /*
    The color of the Queen piece, represented as a char.
    */
    private char color;


    /**
     Constructs a Queen piece with a given color.
     @param c The color of the Queen piece, represented as a char.
     */
    public  Queen(char c, int num)
    {
        this.num = num;
        this.color = c;
        name = color + "Q";
    }


    /**
     Moves the Queen piece on the game board from a given starting position to a given destination.
     @param gameBoard The current game board.
     @param fromR The starting row of the Queen piece.
     @param fromC The starting column of the Queen piece.
     @param toR The destination row of the Queen piece.
     @param toC The destination column of the Queen piece.
     */
    public void move(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {
        gameBoard[toR][toC] = gameBoard[fromR][fromC];
        gameBoard[fromR][fromC] = null;
    }


    /**
     Checks if a given move for the Queen piece is valid.

     A Queen move is valid if it can move as a Rook or a Bishop.

     @param gameBoard The current game board.

     @param fromR The starting row of the Queen piece.

     @param fromC The starting column of the Queen piece.

     @param toR The destination row of the Queen piece.

     @param toC The destination column of the Queen piece.

     @return True if the given move is valid, false otherwise.
     */
    public boolean isValid(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {

        Rook r = new Rook(color, 0);
        Bishop b = new Bishop(color, 0);

        if(b.isValid(gameBoard,fromR, fromC, toR, toC))
            return true;
        if(r.isValid(gameBoard,fromR, fromC, toR, toC))
            return true;

        return false;
    }

}

