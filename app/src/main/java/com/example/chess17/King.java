package com.example.chess17;

/**
 * @author Manan Patel
 *  @author Nivesh Nayee
 */

public class King extends Pieces
{
    /**
     * The row and column coordinates of the white king to keep track of the current position of King
     */
    static int wr = 7, wc = 4, br = 0, bc = 4;
    private char color;


    /**
     * Constructs a new king of the specified color.
     *
     * @param c the color of the king, either 'w' or 'b'
     */
    public  King(char c)
    {
        this.color = c;
        name = color + "K";
    }


    /**
     * Moves the king from the specified starting position to the specified
     * destination position on the game board. If the move involves a castling
     * move, this method will also move the corresponding rook.
     *
     * @param gameBoard the current game board
     * @param fromR the starting row coordinate of the king
     * @param fromC the starting column coordinate of the king
     * @param toR the destination row coordinate of the king
     * @param toC the destination column coordinate of the king
     */
    public void move(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {
        gameBoard[toR][toC] = gameBoard[fromR][fromC];
        gameBoard[fromR][fromC] = null;

        if(isCastle)
        {
            if(gameBoard[toR][toC].name.charAt(0) == 'w')
            {
                if(fromC < toC)
                {
                    gameBoard[7][5] = gameBoard[7][7];
                    gameBoard[7][7] = null;
                }
                else
                {
                    gameBoard[7][3] = gameBoard[7][0];
                    gameBoard[7][0] = null;
                }
            }
            else
            {
                if(fromC < toC)
                {
                    gameBoard[0][5] = gameBoard[0][7];
                    gameBoard[0][7] = null;
                }
                else
                {
                    gameBoard[0][3] = gameBoard[0][0];
                    gameBoard[0][0] = null;
                }
            }
        }

        if(color == 'w')
        {
            wr = toR;
            wc = toC;
        }
        else
        {
            br = toR;
            bc = toC;
        }
        isCastle = false;

        isMoved = true;
    }



    /**
     * Determines whether the specified move from the starting position to the
     * destination position is a valid move for the king.
     *
     * @param gameBoard the current game board
     * @param fromR the starting row coordinate of the king
     * @param fromC the starting column coordinate of the king
     * @param toR the destination row coordinate of the king
     * @param toC the destination column coordinate of the king
     * @return true if the move is valid, false otherwise
     */
    public boolean isValid(Pieces[][] gameBoard, int fromR, int fromC, int toR, int toC)
    {

        if(fromR == toR && Math.abs(fromC-toC) == 2 && !isMoved)
        {
            if(gameBoard[fromR][fromC].name.charAt(0) == 'w')
            {
                if(fromC < toC && !gameBoard[7][7].isMoved)
                {
                    if(gameBoard[7][5] == null && gameBoard[7][6] == null)
                    {
                        isCastle = true;
                        return true;
                    }
                }
                else
                {
                    if(!gameBoard[7][0].isMoved && gameBoard[7][1] == null && gameBoard[7][2] == null && gameBoard[7][3] == null)
                    {
                        isCastle = true;
                        return true;
                    }
                }
            }
            else
            {
                if(fromC < toC && !gameBoard[0][7].isMoved)
                {
                    if(gameBoard[0][5] == null && gameBoard[0][6] == null)
                    {
                        isCastle = true;
                        return true;
                    }
                }
                else
                {
                    if(!gameBoard[0][0].isMoved && gameBoard[0][1] == null && gameBoard[0][2] == null && gameBoard[0][3] == null)
                    {
                        isCastle = true;
                        return true;
                    }
                }
            }

        }

        if((fromR == toR && Math.abs(fromC- toC) == 1) || (fromC == toC && Math.abs(fromR - toR) == 1) || (Math.abs(fromR- toR) == 1) && Math.abs(fromC - toC) == 1  ) {
            if(gameBoard[toR][toC] != null && gameBoard[toR][toC].name.charAt(0) == color)
                return false;
            return true;
        }

        return false;

        // implement hasMoved to keep track of castling
    }
}