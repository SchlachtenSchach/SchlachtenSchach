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

public class Pawn extends Piece {
	public boolean turned = false;
	public boolean firstMove = true;

	public Pawn(PieceColor player, BoardLogic bl) {
		this.player = player;
		life = LOW_HEALTH;
		attack = LOW_ATTACK;
		this.bl = bl;

		if (player == PieceColor.Black) {
			texture = new Texture(Gdx.files.internal("pieces/240px-Chess_pdt45.svg.png"));
		} else {
			texture = new Texture(Gdx.files.internal("pieces/240px-Chess_plt45.svg.png"));
		}
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}

	@Override
	public void calcLegalMoves(int x, int y, boolean[][] legalMoves) {
		if ((player == PieceColor.White) ^ turned) {  //EITHER a pawn is black OR white pawn is turned around
            if (isValidMove(x, y+1))
                legalMoves[x][y+1] = true;
            if (firstMove)  {
                if (isValidMove(x, y + 2) && (legalMoves[x][y+1]))
                    legalMoves[x][y + 2] = true;
            }
		} else {  //EITHER black pawn and is turned around OR white pawn and is not turned around
            if (isValidMove(x, y-1))
                legalMoves[x][y-1] = true;
            if (firstMove){
                if (isValidMove(x, y-2) && legalMoves[x][y-1])
                    legalMoves[x][y-2] = true;
                }
        }
	}

	@Override
	public void calcLegalAttacks(int x, int y, boolean[][] legalMoves){
		if ((player == PieceColor.White) ^ turned) { //EITHER pawn is black OR white pawn turned around

			if (isValidAttack(x-1, y+1))
		        legalMoves[x-1][y+1] = true;

			if (isValidAttack(x, y+1))
			    legalMoves[x][y+1] = true;

			if (isValidAttack(x+1, y+1))
			    legalMoves[x+1][y+1] = true;

		} else { //EITHER black pawn is turned around OR white pawn and is not turned around

            if (isValidAttack(x-1, y-1))
                legalMoves[x-1][y-1] = true;

            if (isValidAttack(x, y-1))
                legalMoves[x][y-1] = true;

            if (isValidAttack(x+1, y-1))
                legalMoves[x+1][y-1] = true;

		}
	}

	@Override
	public String getHelpText() {
		return "Bauer: Kann von drei Feldern (vor ihm) eines zum angreifen auswählen";
	}

}
