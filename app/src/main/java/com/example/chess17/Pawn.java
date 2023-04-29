package com.example.chess17;

/**
 * @author Manan Patel
 *  @author Nivesh Nayee
 */

public class Pawn extends Pieces
{
    private char color;
    private static boolean justMoved;


    /**

     Constructs a pawn object with the given color.
     @param c the color of the pawn ('w' for white, 'b' for black)
     */
    public  Pawn(char c, int num)
    {
        this.color = c;
        this.num = num;
        name = color + "P";
    }


    /**

     Moves the pawn on the game board from the source position to the destination position.

     If the pawn reaches the end of the board, it is promoted to a higher level piece.

     @param gameBoard the game board

     @param fromR the row index of the source position

     @param fromC the column index of the source position

     @param toR the row index of the destination position

     @param toC the column index of the destination position
     */
    public void move(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {
        if (color == 'w' && toR == 0) {
            gameBoard[toR][toC] = promo(promotion);
            gameBoard[toR][toC].promote = true;
        } else if (color == 'b' && toR == 7) {
            promote = true;
            gameBoard[toR][toC] = promo(promotion);
            gameBoard[toR][toC].promote = true;
        } else
        {

            if(enpassant)
            {
                if(gameBoard[toR][toC] == null && Math.abs(fromC-toC) == 1 && justMoved)
                {
                    gameBoard[fromR][toC] = null;
                    enpassant = false;
                }
            }
//            if(gameBoard[toR][toC] == null && Math.abs(fromC-toC) == 1 && justMoved)
//                gameBoard[fromR][toC] = null;

            gameBoard[toR][toC] = gameBoard[fromR][fromC];


            if(Math.abs(fromR - toR) == 2)
                justMoved = true;
            else
                justMoved = false;


        }

        gameBoard[fromR][fromC] = null;

    }



    /**

     Checks whether the pawn move from the source position to the destination position is valid.

     @param gameBoard the game board

     @param fromR the row index of the source position

     @param fromC the column index of the source position

     @param toR the row index of the destination position

     @param toC the column index of the destination position

     @return true if the move is valid, false otherwise
     */
    public boolean isValid(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {
        if(Math.abs(fromC-toC) == 1 && Math.abs(fromR-toR) == 1 && gameBoard[toR][toC] == null && gameBoard[fromR][toC] != null && gameBoard[fromR][toC].name.charAt(0) != color && justMoved)
        {
            enpassant = true;
            return true;
        }

        if(Math.abs(fromC-toC) == 1 && Math.abs(fromR-toR) == 1 && gameBoard[toR][toC] != null && gameBoard[toR][toC].name.charAt(0) != color)
        {
            if(gameBoard[fromR][fromC].name.charAt(0) == 'b' && toR > fromR)
                return true;
            else if(gameBoard[fromR][fromC].name.charAt(0) == 'w' && toR < fromR)
                return true;
        }


        if(color == 'w' && ( (fromR==6 && toR == fromR -2 && gameBoard[toR+1][toC] == null) ||toR == fromR-1 ) && fromC == toC && gameBoard[toR][toC] == null )
            return true;
        if(color == 'b' && ( (fromR==1 && toR == fromR +2 && gameBoard[toR-1][toC] == null) ||toR == fromR+1 ) && fromC == toC && gameBoard[toR][toC] == null )
            return true;


        return false;
    }


    /**
     * Promotes a pawn to a specified piece based on the given character input.
     *
     * @param c the character representing the piece to promote to ('N' for Knight, 'R' for Rook, 'B' for Bishop, and default for Queen)
     * @return the instance of the promoted piece corresponding to the given character with the same color as the pawn
     */
    private Pieces promo(char c)
    {
        if(c == 'N')
            return new Knight(color, num);
        if(c == 'R')
            return new Rook(color, num);
        if(c == 'B')
            return new Bishop(color, num);

        return new Queen(color, (color == 'w') ? whiteCount++ : blackCount++ );
    }

}

