/*
SchlachtenSchach
Copyright (C) 2017 Jonas Trojahn

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.th_bingen.schlachten_schach;

import java.io.Serializable;

import de.th_bingen.schlachten_schach.pieces.Piece;

/**
 * class to save/load board states
 */
public class SaveContainer implements Serializable{
    static final long serialVersionUID = 666511356165156l;
    private PieceSave[][] board;
    private StateType gameState;
    private PieceColor currPlayer;

    public SaveContainer (Piece[][] fields, StateType st, PieceColor player){
        gameState = st;
        currPlayer = player;
        board = new PieceSave[8][8];

        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if (fields[i][j] != null){
                    board[i][j] = new PieceSave(fields[i][j]);
                }
            }
        }
    }

    public PieceSave[][] getBoard(){
        return board;
    }

    public StateType getState() {
        return gameState;
    }

    public PieceColor getCurrPlayer() {
        return currPlayer;
    }
}
