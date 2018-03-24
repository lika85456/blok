package com.example.lika85456.blokusdeskgame.Model;

import android.graphics.Point;

import java.security.InvalidParameterException;

/**
 * Logic board containing all pieces + some matrix logic etc.
 * Created by lika85456 on 24.03.2018.
 */

public class Board {
    byte[][] board = new byte [20][20];

    public Board()
    {
        //Initialize array
        for(int i = 0;i<20;i++)
        {
            for(int ii=0;ii<20;ii++)
            {
                board[i][ii] = -1;
            }
        }
    }

    /***
     * Adds piece to array board !!!WITHOUT CONTROL!!!
     * @param piece
     */
    public void addPiece(Piece piece)
    {
        for(int i = 0;i<piece.list.size();i++)
        {
            Point tPoint = piece.list.get(i);
            board[tPoint.x][tPoint.y] = piece.color;
        }
    }

    /***
     *
     * @param piece
     * @return if piece collides with another piece on the board
     */
    public boolean collides(Piece piece)
    {
        for(int i = 0;i<piece.list.size();i++)
        {
            Point tPoint = piece.list.get(i);
            if(board[tPoint.x][tPoint.y]!=-1)
                return true;
        }
        return false;
    }

    /***
     *
     * @param piece with valid color
     * @return
     */
    public boolean isOnCorner(Piece piece, int x, int y) {
        if (piece.color == -1) throw new InvalidParameterException();
        for (int i = 0; i < piece.list.size(); i++) {
            Point temp = piece.list.get(i);

            if (getColor(temp.x - 1 + x, temp.y + y) == piece.color) return false;
            if (getColor(temp.x + x, temp.y - 1 + y) == piece.color) return false;
            if (getColor(temp.x + x, temp.y + 1 + y) == piece.color) return false;
            if (getColor(temp.x + 1 + x, temp.y + y) == piece.color) return false;
        }
        return true;
    }

    /***
     * returns color in byte
     * @param x
     * @param y
     * @return
     */
    private byte getColor(int x, int y) {
        return board[x][y];
    }

}
