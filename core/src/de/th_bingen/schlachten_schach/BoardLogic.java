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

package de.th_bingen.schlachten_schach;

import de.th_bingen.schlachten_schach.pieces.Bishop;
import de.th_bingen.schlachten_schach.pieces.King;
import de.th_bingen.schlachten_schach.pieces.Knight;
import de.th_bingen.schlachten_schach.pieces.Pawn;
import de.th_bingen.schlachten_schach.pieces.Piece;
import de.th_bingen.schlachten_schach.pieces.Queen;
import de.th_bingen.schlachten_schach.pieces.Rook;

public class BoardLogic {
	private Piece[][] fields;
	private boolean[][] legalMoves;
	private boolean[][] legalAttacks;
	private Position currentPosition;
	private Position lastSelectedPiecePos;
	private boolean attackState;

	public BoardLogic() {
		legalMoves = new boolean[8][8];
		legalAttacks = new boolean[8][8];
		resetLegalMoves();
		resetLegalAttacks();
		attackState = false;

		fields = new Piece[8][8];
        for (int y=0; y<fields.length; y++) {
            for (int x=0; x<fields[y].length; x++) {
                fields[x][y] = null;
            }
        }


        fields[0][7] = new Rook(PieceColor.Black, this);
        fields[1][7] = new Knight(PieceColor.Black, this);
        fields[2][7] = new Bishop(PieceColor.Black, this);
        fields[3][7] = new Queen(PieceColor.Black, this);
        fields[4][7] = new King(PieceColor.Black, this);
        fields[5][7] = new Bishop(PieceColor.Black, this);
        fields[6][7] = new Knight(PieceColor.Black, this);
        fields[7][7] = new Rook(PieceColor.Black, this);

        for (int x=0; x<8; x++) {
            fields[x][6] = new Pawn(PieceColor.Black, this);
        }


        fields[0][0] = new Rook(PieceColor.White, this);
        fields[1][0] = new Knight(PieceColor.White, this);
        fields[2][0] = new Bishop(PieceColor.White, this);
        fields[3][0] = new Queen(PieceColor.White, this);
        fields[4][0] = new King(PieceColor.White, this);
        fields[5][0] = new Bishop(PieceColor.White, this);
        fields[6][0] = new Knight(PieceColor.White, this);
        fields[7][0] = new Rook(PieceColor.White, this);

        for (int x=0; x<8; x++) {
            fields[x][1] = new Pawn(PieceColor.White, this);
        }
	}

	public BoardLogic(PieceSave[][] boardState){
        legalMoves = new boolean[8][8];
        legalAttacks = new boolean[8][8];
        resetLegalMoves();
        resetLegalAttacks();
        attackState = false;
        fields = new Piece[8][8];

        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if (boardState[i][j] != null) {
                    switch (boardState[i][j].type) {
                        case 1:
                            Pawn p = new Pawn(boardState[i][j].player, this);
                            p.firstMove = boardState[i][j].pawnFirstMove;
                            p.turned = boardState[i][j].currState;
                            p.setLife(boardState[i][j].life);
                            fields[i][j] = p;
                            break;
                        case 2:
                            fields[i][j] = new Rook(boardState[i][j].player, this);
                            fields[i][j].setLife(boardState[i][j].life);
                            break;
                        case 3:
                            Knight k = new Knight(boardState[i][j].player, this);
                            k.hasMoved = boardState[i][j].currState;
                            k.setLife(boardState[i][j].life);
                            fields[i][j] = k;
                            break;
                        case 4:
                            fields[i][j] = new Bishop(boardState[i][j].player, this);
                            fields[i][j].setLife(boardState[i][j].life);
                            break;
                        case 5:
                            fields[i][j] = new Queen(boardState[i][j].player, this);
                            fields[i][j].setLife(boardState[i][j].life);
                            break;
                        case 6:
                            fields[i][j] = new King(boardState[i][j].player, this);
                            fields[i][j].setLife(boardState[i][j].life);
                            break;
                    }
                }
            }
        }
    }

	//getters
	public Piece[][] getFields() {
		return this.fields;
	}

	public Piece getFieldPiece(int x, int y) {
		return fields[x][y];
	}

	public PieceColor getPlayer(Position position){
	    return getFieldPiece(position.x, position.y).getPlayer();
    }

	public Piece getCurrentPiece() {
		if (currentPosition == null) {
			return null;
		}
		return fields[currentPosition.x][currentPosition.y];
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public Position getLastSelectedPiecePos() {
		return lastSelectedPiecePos;
	}

	public int getCurrentXPosition() {
		return currentPosition.x;
	}

	public int getCurrentYPosition() {
		return currentPosition.y;
	}

	//setters
	public void setCurrentPosition(int x, int y) {
		currentPosition = new Position(x, y);
		lastSelectedPiecePos = currentPosition;
	}

    public void setCurrentPosition(Position position) {
        currentPosition = position;
		lastSelectedPiecePos = position;
    }


    /**
     * resets currentPosition to null
     */
	public void resetCurrentPosition() {
		currentPosition = null;
	}

	/**
	 * Moves the piece at oldPosition to the empty space newPosition
	 * if the piece is a knight, saves that it has moved this turn
	 * if the piece is a pawn, sets firstMove to false and turns it around when it reaches the end of the board
	 * @param newPosition the position the piece should be moved to
	 */
	public boolean move(Position newPosition) {
		if (currentPosition == null) {
			return false;
		}
		move(currentPosition, newPosition);
		return true;
	}

	/**
     * Moves the piece at oldPosition to the empty space newPosition
     * if the piece is a knight, saves that it has moved this turn
     * if the piece is a pawn, sets firstMove to false and turns it around when it reaches the end of the board
     * @param oldPosition the current position of the piece
     * @param newPosition the position the piece should be moved to
     */
	public void move(Position oldPosition, Position newPosition) {
        Piece old = fields[oldPosition.x][oldPosition.y];

		if (isLegalMove(newPosition)) {
			lastSelectedPiecePos = newPosition;
			//System.out.println("move from "+oldPosition+" to "+newPosition);
            fields[newPosition.x][newPosition.y] = old;
			fields[oldPosition.x][oldPosition.y] = null;

			if (old instanceof Knight) {
				Knight k = (Knight) old;
                k.hasMoved = true;
            }

            if (old instanceof Pawn){
				Pawn p = (Pawn) old;
			    p.firstMove = false;
			    if ((newPosition.y==0) || (newPosition.y==7)){
			        p.turned = !p.turned;
                }
            }

		}
	}

	/**
	 * damages the defending piece by the attack value of the attacking piece
	 * destroys the defending piece if its life reaches 0
	 * resets hasMoved if the attacking piece is a knight
	 * @param defending position of the piece that is being attacked
	 * @return true when the king is slain, false in all other cases
	 */
	public boolean attack(Position defending) {
		if (currentPosition == null) {
			return false;
		}
		return attack(currentPosition, defending);
	}

    /**
     * damages the defending piece by the attack value of the attacking piece
     * destroys the defending piece if its life reaches 0
     * resets hasMoved if the attacking piece is a knight
     * @param attacker position of the attacking piece
     * @param defending position of the piece that is being attacked
     * @return true when the king is slain, false in all other cases
     */
	public boolean attack(Position attacker, Position defending) {
		boolean ret = false;

		if (isLegalAttack(defending)) {
			Piece off = fields[attacker.x][attacker.y];
			Piece def = fields[defending.x][defending.y];

			if ((off instanceof Knight) && (((Knight) off).hasMoved)) {
				Knight k = (Knight) off;
				k.hasMoved = false;

				if ((attacker.x - defending.x) != 0) {
					ret = ret || attackHelper(off, defending.x, defending.y + 1);
					ret = ret || attackHelper(off, defending.x, defending.y - 1);
				} else {
					ret = ret || attackHelper(off, defending.x + 1, defending.y);
					ret = ret || attackHelper(off, defending.x - 1, defending.y);
				}

				if (def != null) {
					def.damage(off.getAttack() * 2);

					if (def.getLife() == 0) {
						if (def instanceof King) {
							return true;
						} else {
							fields[defending.x][defending.y] = null;
						}
					}
				}
			} else {
				if ((def != null) && (off.getPlayer() != def.getPlayer())) {
					def.damage(off.getAttack());

					if (def.getLife() == 0) {
						if (def instanceof King) {
							return true;
						} else {
							fields[defending.x][defending.y] = null;
						}
					}
				}
			}

		}

		return ret;
	}

	private boolean attackHelper(Piece off, int defX, int defY) {
		if (Piece.isOnBoard(defX, defY) && fields[defX][defY] != null) {
			fields[defX][defY].damage(off.getAttack());
			if (fields[defX][defY].getLife() == 0) {
				if (fields[defX][defY] instanceof King) {
					return true;
				} else {
					fields[defX][defY] = null;
				}
			}
		}
		return false;
	}

	/**
	 *
	 * @param attackState set this true if the game is in attack sate
	 */
	public void setAttackState(boolean attackState) {
		this.attackState = attackState;
	}

	/**
	 *
	 * @return true if the game is in attack state
	 */
	public boolean isInAttackState() {
		return attackState;
	}

    /**
     *
     * @param x horizontal position on the board
     * @param y vertical position on the board
     * @return  true if the specified place on the board is unoccupied
     */
	public boolean isEmpty(int x, int y) {
		return fields[x][y] == null;
	}

    /**
     *
     * @param position position on the board (contains x and y position)
     * @return true if the specified place on the board is unoccupied
     */
	public boolean isEmpty(Position position) {
		return fields[position.x][position.y] == null;
	}

    /**
     * sets legalMoves to the legal moves of the piece specified by current position
     */
	public void calcLegalMoves() {
		resetLegalMoves();
		if (fields[currentPosition.x][currentPosition.y] == null) {
			return;
		}
		fields[currentPosition.x][currentPosition.y].calcLegalMoves(currentPosition.x, currentPosition.y, legalMoves);
	}

    /**
     *
     * @param x horizontal position on the board
     * @param y vertical position on the board
     * @return true, if the specified position is a legal movement option
     */
	public boolean isLegalMove(int x, int y) {
		return legalMoves[x][y];
	}

    /**
     *
     * @param position position on the board (contains x and y position)
     * @return true, if the specified position is a legal movement option
     */
	public boolean isLegalMove(Position position) {
		if (position == null) {
			return false;
		}
		return legalMoves[position.x][position.y];
	}

    /**
     * sets legalAttacks to the legal attacks of the piece specified by current position
     */
	public void calcLegalAttacks() {
		resetLegalAttacks();
		if (fields[currentPosition.x][currentPosition.y] == null) {
			return;
		}
		fields[currentPosition.x][currentPosition.y].calcLegalAttacks(currentPosition.x, currentPosition.y, legalAttacks);
	}

    /**
     *
     * @param x horizontal position on the board
     * @param y vertical position on the board
     * @return true, if the specified position is a legal attack option
     */
	public boolean isLegalAttack(int x, int y) {
		return legalAttacks[x][y];
	}

    /**
     *
     * @param position position on the board (contains x and y position)
     * @return true, if the specified position is a legal attack option
     */
	public boolean isLegalAttack(Position position) {
		return isLegalAttack(position.x, position.y);
	}

    /**
     * sets all values in the legalMoves array to false
     */
	public void resetLegalMoves() {
		for (int y=0; y<legalMoves.length; y++) {
			for (int x=0; x<legalMoves[y].length; x++) {
				legalMoves[x][y] = false;
			}
		}
	}

    /**
     * sets all values in the legalAttacks array to false
     */
	public void resetLegalAttacks() {
		for (int y=0; y<legalAttacks.length; y++) {
			for (int x=0; x<legalAttacks[y].length; x++) {
				legalAttacks[x][y] = false;
			}
		}
	}

}
