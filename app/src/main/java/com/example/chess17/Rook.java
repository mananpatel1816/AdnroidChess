package com.example.chess17;

/**
 * @author Manan Patel
 *  @author Nivesh Nayee
 */


public class Rook extends Pieces
{
    private char color; /** The color of the Rook piece. */

    /**
     * Constructs a Rook object with the specified color.
     * @param c The color of the Rook piece.
     */
    public  Rook(char c, int num)
    {
        this.color = c;
        this.num = num;
        name = color + "R";
    }


    /**
     * Moves the Rook piece on the game board.
     * Overrides the move method in the Pieces class.
     * @param gameBoard The game board with all the pieces.
     * @param fromR The starting row of the piece.
     * @param fromC The starting column of the piece.
     * @param toR The ending row of the piece.
     * @param toC The ending column of the piece.
     */
    public void move(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {
        gameBoard[toR][toC] = gameBoard[fromR][fromC];
        gameBoard[fromR][fromC] = null;
        isMoved = true;
    }


    /**
     * Checks if the move made by the Rook piece is valid.
     * Overrides the isValid method in the Pieces class.
     * @param gameBoard The game board with all the pieces.
     * @param fromR The starting row of the piece.
     * @param fromC The starting column of the piece.
     * @param toR The ending row of the piece.
     * @param toC The ending column of the piece.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValid(Pieces[][] gameBoard,int fromR, int fromC, int toR, int toC)
    {

        // before checking other conditions, check if toR and toC is not white when moving a white piece and same for black
        // check if there are any other piece on the way from-to

        if (fromR == toR && fromC -toC != 0 || fromC == toC && fromR -toR != 0 )
        {
            if(gameBoard[toR][toC] != null && gameBoard[toR][toC].name.charAt(0) == color)
                return false;

            int rowDiff = toR - fromR;
            int colDiff = toC - fromC;
            int rowDirection = rowDiff == 0 ? 0 : rowDiff / Math.abs(rowDiff);
            int colDirection = colDiff == 0 ? 0 : colDiff / Math.abs(colDiff);
            int currentRow = fromR + rowDirection;
            int currentCol = fromC + colDirection;

            while (currentRow != toR || currentCol != toC) {
                if (gameBoard[currentRow][currentCol] != null) {
                    return false;
                }

                currentRow += rowDirection;
                currentCol += colDirection;
            }

            // All conditions passed, move is valid
            return true;
        }

        return false;
    }

}