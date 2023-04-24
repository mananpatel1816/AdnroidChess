package com.example.chess17;

/**
 * @author Manan Patel
 * @author Nivesh Nayee
 */

public abstract class Pieces {

    /**

     The name of the chess piece.
     */
    String name;
    int num;
    /**

     The promotion type of the pawn if it reaches the end of the board.
     */
    char promotion;
    boolean isMoved, isCastle = false;


    /**

     Checks if a given move for the chess piece is valid.
     @param gameBoard The current game board.
     @param fromR The starting row of the chess piece.
     @param fromC The starting column of the chess piece.
     @param toR The destination row of the chess piece.
     @param toC The destination column of the chess piece.
     @return True if the given move is valid, false otherwise.
     */
    public abstract boolean isValid(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC);

    /**

     Moves the chess piece on the game board from a given starting position to a given destination.

     @param gameBoard The current game board.

     @param fromR The starting row of the chess piece.

     @param fromC The starting column of the chess piece.

     @param toR The destination row of the chess piece.

     @param toC The destination column of the chess piece.
     */
    public abstract void move(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC);


}