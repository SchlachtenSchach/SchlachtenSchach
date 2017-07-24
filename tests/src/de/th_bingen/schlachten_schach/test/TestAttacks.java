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

package de.th_bingen.schlachten_schach.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.th_bingen.schlachten_schach.BoardLogic;
import de.th_bingen.schlachten_schach.PieceColor;
import de.th_bingen.schlachten_schach.Position;
import de.th_bingen.schlachten_schach.pieces.*;
import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class TestAttacks {
	Piece[][] fields;
	boolean[][] manual;
	boolean[][] toTest;
	BoardLogic bl;

	@Before
	public void setup() {
		bl = new BoardLogic();
		fields = bl.getFields();
		manual = new boolean[8][8];
		toTest = new boolean[8][8];
		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				fields[i][j] = null;
			}
		}
		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				manual[i][j] = false;
			}
		}
		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				toTest[i][j] = false;
			}
		}
	}

	@Test
	public void testPawn(){
		boolean testingResult = true;
		fields[3][3] = new Pawn(PieceColor.White, bl);
		manual[3][4] = true;
		manual[2][4] = true;
		manual[4][4] = true;
		fields[3][3].calcLegalAttacks(3, 3, toTest);

		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				testingResult = testingResult && (manual[i][j] == toTest[i][j]);
			}
		}
		assertTrue(testingResult);
	}

	@Test
	public void testKing(){
		boolean testingResult = true;
		fields[3][3] = new King(PieceColor.White, bl);
		manual[2][2] = true;
		manual[2][3] = true;
		manual[2][4] = true;
		manual[3][2] = true;
		manual[3][4] = true;
		manual[4][2] = true;
		manual[4][3] = true;
		manual[4][4] = true;

		fields[3][3].calcLegalAttacks(3, 3, toTest);

		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				testingResult = testingResult && (manual[i][j] == toTest[i][j]);
			}
		}
		assertTrue(testingResult);
	}

	@Test
	public void testQueen(){
		boolean testingResult = true;
		fields[3][3] = new Queen(PieceColor.White, bl);
		manual[2][2] = true;
		manual[2][3] = true;
		manual[2][4] = true;
		manual[3][2] = true;
		manual[3][4] = true;
		manual[4][2] = true;
		manual[4][3] = true;
		manual[4][4] = true;

		fields[3][3].calcLegalAttacks(3, 3, toTest);

		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				testingResult = testingResult && (manual[i][j] == toTest[i][j]);
			}
		}
		assertTrue(testingResult);
	}

	@Test
	public void testBishop(){
		boolean testingResult = true;
		fields[3][3] = new Bishop(PieceColor.White, bl);
		manual[2][2] = true;
		manual[2][4] = true;
		manual[4][2] = true;
		manual[4][4] = true;

		fields[3][3].calcLegalAttacks(3, 3, toTest);

		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				testingResult = testingResult && (manual[i][j] == toTest[i][j]);
			}
		}
		assertTrue(testingResult);
	}

	@Test
	public void testKnight(){
		boolean testingResult = true;
		fields[3][3] = new Knight(PieceColor.White, bl);
		manual[2][3] = true;
		manual[3][2] = true;
		manual[3][4] = true;
		manual[4][3] = true;

		fields[3][3].calcLegalAttacks(3, 3, toTest);

		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				testingResult = testingResult && (manual[i][j] == toTest[i][j]);
			}
		}
		assertTrue(testingResult);
	}

	@Test
	public void testKnightCleave(){
		fields[3][3] = new Knight(PieceColor.White, bl);
		fields[2][4] = new King(PieceColor.Black, bl);
		fields[3][4] = new King(PieceColor.White, bl);
		fields[4][4] = new King(PieceColor.Black, bl);
		Knight k = (Knight) fields[3][3];
		k.hasMoved = true;
		bl.setCurrentPosition(3, 3);
		bl.calcLegalAttacks();
		bl.attack(new Position(3,3), new Position(3, 4));
		assertEquals(17, fields[2][4].getLife());
		assertEquals(14, fields[3][4].getLife());
		assertEquals(17, fields[4][4].getLife());
	}

	@Test
	public void testRook(){
		boolean testingResult = true;
		fields[3][3] = new Rook(PieceColor.White, bl);
		for (int i=0; i<7; i++) {
			for (int j=0; j<7; j++) {
				manual[i][j] = true;
			}
		}
		manual[2][2] = false;
		manual[2][3] = false;
		manual[2][4] = false;
		manual[3][2] = false;
		manual[3][3] = false;
		manual[3][4] = false;
		manual[4][2] = false;
		manual[4][3] = false;
		manual[4][4] = false;

		fields[3][3].calcLegalAttacks(3, 3, toTest);

		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				testingResult = testingResult && (manual[i][j] == toTest[i][j]);
			}
		}
		assertTrue(testingResult);
	}

	/*
    @Test
    public void testTemplate(){
        boolean testingResult = true;
        fields[3][3] = new Piece(PieceColor.White, bl);
        // TO DO: insert manually assigned legal moves here
        fields[3][3].calcLegalAttacks(3, 3, toTest);

        for (int i = 0; i<8; i++){
            for (int j = 0; j<8; j++){
                testingResult = testingResult && (manual[i][j] == toTest[i][j]);
            }
        }
        assertTrue(testingResult);
    }
    */

}
