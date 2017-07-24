/*
SchlachtenSchach
Copyright (C) 2017 Kathrina Kreis, Jonas Trojahn, Patrick Reths

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

package de.th_bingen.schlachten_schach.pieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import de.th_bingen.schlachten_schach.*;

public class Rook extends Piece {

    public Rook(PieceColor player, BoardLogic bl) {
        this.player = player;
        life = MEDIUM_HEALTH;
        attack = MEDIUM_ATTACK;
        this.bl = bl;

        if (player == PieceColor.Black) {
            texture = new Texture(Gdx.files.internal("pieces/240px-Chess_rdt45.svg.png"));
        } else {
            texture = new Texture(Gdx.files.internal("pieces/240px-Chess_rlt45.svg.png"));
        }
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void calcLegalMoves(int x, int y, boolean[][] legalMoves) {

        if (isValidMove(x,y+1))
            legalMoves[x][y+1] = true;

        if (isValidMove(x,y-1))
            legalMoves[x][y-1] = true;

        if (isValidMove(x+1,y))
            legalMoves[x+1][y] = true;

        if (isValidMove(x-1,y))
            legalMoves[x-1][y] = true;

    }

    @Override
    public void calcLegalAttacks(int x, int y, boolean[][] legalMoves) {

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (isValidAttack(x-3+i, y-3+j))
                    legalMoves[x-3+i][y-3+j] = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (isValidAttack(x-1+i, y-1+j))
                    legalMoves[x-1+i][y-1+j] = false;
            }
        }
    }

    @Override
    public String getHelpText() {
        return "Turm: Kann bis zu drei Felder weit angreifen. Kann nicht direkt anliegende Felder um sich angreifen.";
    }

}
