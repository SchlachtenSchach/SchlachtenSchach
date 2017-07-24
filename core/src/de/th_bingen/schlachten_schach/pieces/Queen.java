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

public class Queen extends Piece {

    public Queen(PieceColor player, BoardLogic bl) {
        this.player = player;
        life = HIGH_HEALTH;
        attack = HIGH_ATTACK;
        this.bl = bl;

        if (player == PieceColor.Black) {
            texture = new Texture(Gdx.files.internal("pieces/240px-Chess_qdt45.svg.png"));
        } else {
            texture = new Texture(Gdx.files.internal("pieces/240px-Chess_qlt45.svg.png"));
        }
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void calcLegalMoves(int x, int y, boolean[][] legalMoves) {
        int h = y;
        int g = x;

        while (isValidMove(x,h + 1)) {
           h = h + 1;
            legalMoves[g][h] = true;
        }

        h = y;
        g = x;

        while (isValidMove(x,h - 1)) {
            h = h - 1;
        legalMoves[g][h] = true;
        }

        h = y;
        g = x;

        while (isValidMove(g+1, y)) {
            g = g + 1;
            legalMoves[g][h] = true;
        }

        h = y;
        g = x;

        while  (isValidMove(g-1,y)) {
            g = g - 1;
            legalMoves[g][h] = true;
        }

        h = y;
        g = x;


        while (isValidMove(g-1,h-1)) {
            g = g - 1;
            h = h - 1;
            legalMoves[g][h] = true;
        }

        h = y;
        g = x;

        while (isValidMove(g+1,h+1)) {
            g = g + 1;
            h = h + 1;
            legalMoves[g][h] = true;
        }

        h = y;
        g = x;
        while (isValidMove(g-1,h+1)) {
            g = g - 1;
            h = h + 1;
            legalMoves[g][h] = true;
        }

        h = y;
        g = x;
        while (isValidMove(g+1,h-1)) {
            g = g + 1;
            h = h - 1;
            legalMoves[g][h] = true;
        }

    }

	@Override
    public void calcLegalAttacks(int x, int y, boolean[][] legalMoves) {

         if (isValidAttack(x,y+1))
            legalMoves[x][y+1] = true;

        if (isValidAttack(x,y-1))
            legalMoves[x][y-1] = true;

        if (isValidAttack(x+1,y))
            legalMoves[x+1][y] = true;

        if (isValidAttack(x-1,y))
            legalMoves[x-1][y] = true;

        if (isValidAttack(x-1,y-1))
            legalMoves[x-1][y-1] = true;

        if (isValidAttack(x+1,y+1))
            legalMoves[x+1][y+1] = true;

        if (isValidAttack(x-1,y+1))
            legalMoves[x-1][y+1] = true;

        if (isValidAttack(x+1,y-1))
            legalMoves[x+1][y-1] = true;

    }

    @Override
    public String getHelpText() {
        return "Dame: Kann ein anliegendes Feld angreifen.";
    }

}
