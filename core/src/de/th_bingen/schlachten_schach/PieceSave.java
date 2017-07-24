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

import de.th_bingen.schlachten_schach.pieces.Bishop;
import de.th_bingen.schlachten_schach.pieces.Knight;
import de.th_bingen.schlachten_schach.pieces.Pawn;
import de.th_bingen.schlachten_schach.pieces.Piece;
import de.th_bingen.schlachten_schach.pieces.Queen;
import de.th_bingen.schlachten_schach.pieces.Rook;

/**
 * Container class for pieces
 */
public class PieceSave implements Serializable {
    static final long serialVersionUID = 944944654869485615l;
    public final int type;
    public final boolean currState;
    public final boolean pawnFirstMove;
    public final int life;
    public final PieceColor player;

    public PieceSave(Piece p) {
        player = p.getPlayer();
        life = p.getLife();

        if (p instanceof Pawn){
            type = 1;
            Pawn pawn = (Pawn) p;
            currState = pawn.turned;
            pawnFirstMove = pawn.firstMove;
        }
        else {
            pawnFirstMove = false;
            if (p instanceof Knight) {
                type = 3;
                Knight knight = (Knight) p;
                currState = knight.hasMoved;
            }
            else {
                currState=false;
                if (p instanceof Rook) {
                    type = 2;
                }
                else {
                    if (p instanceof Bishop) {
                        type = 4;
                    }
                    else {
                        if (p instanceof Queen) {
                            type = 5;
                        }
                        else {
                           type = 6;
                        }
                    }
                }
            }
        }
    }
}
