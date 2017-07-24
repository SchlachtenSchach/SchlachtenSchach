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

public class Knight extends Piece {
    public boolean hasMoved = false;

	public Knight(PieceColor player, BoardLogic bl) {
		this.player = player;
		life = MEDIUM_HEALTH;
		attack = LOW_ATTACK;
		this.bl = bl;

		if (player == PieceColor.Black) {
			texture = new Texture(Gdx.files.internal("pieces/240px-Chess_ndt45.svg.png"));
		} else {
			texture = new Texture(Gdx.files.internal("pieces/240px-Chess_nlt45.svg.png"));
		}
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}

	@Override
	public void calcLegalMoves(int x, int y, boolean[][] legalMoves) {

	    if (isValidMove(x+2, y+1))
            legalMoves[x+2][y+1] = true;

        if (isValidMove(x-2, y+1))
            legalMoves[x-2][y+1] = true;

        if (isValidMove(x-2, y-1))
            legalMoves[x-2][y-1] = true;

        if (isValidMove(x+2, y-1))
            legalMoves[x+2][y-1] = true;

        if (isValidMove(x+1, y+2))
            legalMoves[x+1][y+2] = true;

        if (isValidMove(x-1, y+2))
            legalMoves[x-1][y+2] = true;

        if (isValidMove(x+1, y-2))
            legalMoves[x+1][y-2] = true;

        if (isValidMove(x-1, y-2))
            legalMoves[x-1][y-2] = true;
	}

	@Override
	public void calcLegalAttacks(int x, int y, boolean[][] legalMoves) {
	    if (hasMoved) {
            if (isOnBoard(x + 1, y))
                legalMoves[x + 1][y] = true;
            if (isOnBoard(x - 1, y))
                legalMoves[x - 1][y] = true;
            if (isOnBoard(x, y + 1))
                legalMoves[x][y + 1] = true;
            if (isOnBoard(x, y - 1))
                legalMoves[x][y - 1] = true;
        }
        else {
            if (isValidAttack(x + 1, y))
                legalMoves[x + 1][y] = true;
            if (isValidAttack(x - 1, y))
                legalMoves[x - 1][y] = true;
            if (isValidAttack(x, y + 1))
                legalMoves[x][y + 1] = true;
            if (isValidAttack(x, y - 1))
                legalMoves[x][y - 1] = true;
        }
    }

	@Override
	public String getHelpText() {
		return "Springer: Kann ein anliegendes Feld angreifen. Hat er seit seiner letzten Bewegung nicht angegriffen, greift er das Hauptfeld 2x und anliegende Felder 1x an. Trifft Verbündete.";
	}

}
