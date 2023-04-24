package com.example.chess17;

/**
 * @author Manan Patel
 *  @author Nivesh Nayee
 */

public class Knight extends Pieces
{
    private char color;



    /**
     * Constructor to create a Knight instance with a specific color.
     * @param c a character representing the color of the knight ('W' for white, 'B' for black)
     */
    public  Knight(char c, int num)
    {
        this.color = c;
        this.num = num;
        name = color + "N";
    }



    /**
     * Moves the Knight piece within the game board.
     * @param gameBoard a 2D array representing the chess board
     * @param fromR the current row of the knight
     * @param fromC the current column of the knight
     * @param toR the destination row for the knight
     * @param toC the destination column for the knight
     */
    public void move(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {
        gameBoard[fromR][fromC] = null;
        gameBoard[toR][toC] = new Knight(color,num);
    }


    /**
     * Determines if a given move is valid for a knight on a game board.
     *
     * @param gameBoard A 2D array representing the game board with pieces.
     * @param fromR The row index of the knight's current position.
     * @param fromC The column index of the knight's current position.
     * @param toR The row index of the knight's target position.
     * @param toC The column index of the knight's target position.
     * @return true if the move is valid, false otherwise.
     */
    public boolean isValid(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {
        if ((fromR-toR == 2 && fromC-toC == 1) || (fromR-toR == -2 && fromC-toC == -1) || (fromR-toR == -2 && fromC-toC == 1) || (fromR-toR == 2 && fromC-toC == -1)
                || (fromC-toC == 2 && fromR-toR == 1) || (fromC-toC == -2 && fromR-toR == -1) || (fromC-toC == -2 && fromR-toR == 1) || (fromC-toC == 2 && fromR-toR == -1))
        {
            if(gameBoard[toR][toC] != null && gameBoard[toR][toC].name.charAt(0) == color)
                return false;
            return true;
        }

        return false;

    }

}