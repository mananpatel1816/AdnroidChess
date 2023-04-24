package com.example.chess17;

/**
 * @author Manan Patel
 *  @author Nivesh Nayee
 */


public class Bishop extends Pieces
{
    /**

     Color of the bishop.
     */
    private char color;

    /**

     Constructs a Bishop object with the specified color.
     @param c the color of the bishop
     */
    public  Bishop(char c, int num)
    {
        this.color = c;
        this.num = num;
        name = color + "B";
    }

    /**

     Moves the bishop from its current position to the specified position.
     @param gameBoard the game board
     @param fromR the row index of the bishop's current position
     @param fromC the column index of the bishop's current position
     @param toR the row index of the bishop's destination position
     @param toC the column index of the bishop's destination position
     */
    public void move(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {
        gameBoard[fromR][fromC] = null;
        gameBoard[toR][toC] = new Bishop(color,num);
    }


    /**

     Determines if the bishop can move from its current position to the specified position.
     @param gameBoard the game board
     @param fromR the row index of the bishop's current position
     @param fromC the column index of the bishop's current position
     @param toR the row index of the bishop's destination position
     @param toC the column index of the bishop's destination position
     @return true if the move is valid, false otherwise
     */
    public boolean isValid(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {
        if ((Math.abs(fromR - toR) == Math.abs(fromC - toC) ) && fromR != toR && fromC != toC)
        {
            if(gameBoard[toR][toC] != null && gameBoard[toR][toC].name.charAt(0) == color)
                return false;

            while(fromR != toR && fromC != toC)
            {
                if(fromR > toR && fromC < toC)
                {
                    fromR--;
                    fromC++;

                    if(fromR != toR && gameBoard[fromR][fromC] != null)
                        return false;

                }
                else if(fromR < toR && fromC > toC)
                {
                    fromR++;
                    fromC--;

                    if(fromR != toR && gameBoard[fromR][fromC] != null)
                        return false;
                }
                else if(fromR > toR && fromC > toC)
                {
                    fromR--;
                    fromC--;

                    if(fromR != toR && gameBoard[fromR][fromC] != null)
                        return false;
                }
                else
                {
                    fromR++;
                    fromC++;

                    if(fromR != toR && gameBoard[fromR][fromC] != null)
                        return false;
                }
            }

            return true;
        }

        return false;
    }
}