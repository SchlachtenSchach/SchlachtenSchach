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

import com.badlogic.gdx.graphics.Texture;

import de.th_bingen.schlachten_schach.BoardLogic;
import de.th_bingen.schlachten_schach.PieceColor;

public abstract class Piece {
	public static final int LOW_ATTACK = 3;
	public static final int LOW_HEALTH = 3;
	public static final int MEDIUM_ATTACK = 5;
	public static final int MEDIUM_HEALTH = 7;
	public static final int HIGH_ATTACK = 10;
	public static final int HIGH_HEALTH = 10;
	public static final int KING_ATTACK = 20;
	public static final int KING_HEALTH = 20;
	protected PieceColor player;
	protected int life;
	protected Texture texture;
	protected int attack;
	protected BoardLogic bl;

	// Getter
	public PieceColor getPlayer() {
		return player;
	}

	public int getLife() {
		return life;
	}

	public int getAttack() {
		return attack;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setLife(int i){ life = i;}

	/**
	 * Moves the pieces
	 * Movement pattern of each piece: see Readme
	 * @param x Current x position of the piece
	 * @param y Current y position of the piece
	 * @param legalMoves Position is true when move is legal
	 */

	abstract public void calcLegalMoves(int x, int y, boolean[][] legalMoves);

	/**
	 * Attacks with the pieces
	 * Attack pattern of each piece: see Readme
	 * @param x Current x positon of the piece
	 * @param y Current y positon of the piece
	 * @param legalMoves Position is true when attack is legal
	 */
	abstract public void calcLegalAttacks(int x, int y, boolean[][] legalMoves);

	/**
	 * Description of the characteristics of the piece
	 * @return Returns the description
	 */
	abstract public String getHelpText();

	/**
	 * Called when a piece attacks another piece
	 * Substracts attack from life of attacked piece
	 * @param attack Damage of the attacking piece
	 */
	public void damage(int attack) {
		life -= attack;
		if (life < 0) {
			life = 0;
		}
	}

	/**
	 * Checks unoccupied spaces and array boundaries
	 * @param x x position of movement
	 * @param y y position of movement
	 * @return Returns true if not out of bounds and unoccupied
	 */
	public boolean isValidMove(int x, int y) {
		return (isOnBoard(x, y) && bl.isEmpty(x, y));
	}

	/**
	 * Checks array boundaries
	 * @param x x position of attack or movement
	 * @param y y positon of attack or movement
	 * @return Returns true if not out of bounds
	 */
	public static boolean isOnBoard(int x,int y) {
		return ((x>=0) && (x<=7) && (y>=0) && (y<=7));
	}


    /**
     * Checks array boundaries and whether the attacked space is occupied by an ally
     * @param x x position of attack or movement
     * @param y y positon of attack or movement
     * @return Returns true if not out of bounds and attacked space is not occupied by an ally
     */
	public boolean isValidAttack(int x, int y) { return (isOnBoard(x, y) && (bl.isEmpty(x, y) || bl.getFields()[x][y].getPlayer() != getPlayer())); }
}
