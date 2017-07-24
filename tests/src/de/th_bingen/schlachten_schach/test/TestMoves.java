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
import de.th_bingen.schlachten_schach.pieces.Bishop;
import de.th_bingen.schlachten_schach.pieces.King;
import de.th_bingen.schlachten_schach.pieces.Knight;
import de.th_bingen.schlachten_schach.pieces.Pawn;
import de.th_bingen.schlachten_schach.pieces.Piece;
import de.th_bingen.schlachten_schach.pieces.Queen;
import de.th_bingen.schlachten_schach.pieces.Rook;
import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class TestMoves {
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
	public void testKing(){
		boolean testingResult = true;
		fields[3][3] = new King(PieceColor.White, bl);
		// TO DO: insert manually assigned legal moves here
		manual[4][3] = true;
		manual[4][4] = true;
		manual[2][4] = true;
		manual[2][3] = true;
		manual[2][2] = true;
		manual[3][2] = true;
		manual[4][2] = true;
		manual[3][4] = true;

		fields[3][3].calcLegalMoves(3, 3, toTest);

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
		int k = 3;
		fields[3][3] = new Queen(PieceColor.White, bl);
		// TO DO: insert manually assigned legal moves here
		for(int i = 4; i <= 7; i++){
			manual[3][i] = true;
			manual[i][3] = true;
			manual[i][i] = true;
		}
		for(int i = 2; i >= 0; i--){
			manual[i][3] = true;
			manual[3][i] = true;
			manual[i][i] = true;
		}
		for(int i = 2; i >= 0; i--){
			k++;
			manual[i][k] = true;

		}

		k=3;

		for(int i = 4; i <= 6; i++){
			k--;
			manual[i][k] = true;

		}

		fields[3][3].calcLegalMoves(3, 3, toTest);

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
		int k = 3;
		fields[3][3] = new Bishop(PieceColor.White, bl);
		// TO DO: insert manually assigned legal moves here
		for(int i = 4; i <= 7; i++){
			manual[i][i] = true;
		}
		for(int i = 2; i >= 0; i--){
			manual[i][i] = true;
		}
		for(int i = 2; i >= 0; i--){
            k++;
			manual[i][k] = true;
		}

		k = 3;

		for(int i = 4; i <= 6; i++){
			k--;
			manual[i][k] = true;

		}

		fields[3][3].calcLegalMoves(3, 3, toTest);

		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				testingResult = testingResult && (manual[i][j] == toTest[i][j]);
			}
		}
		assertTrue(testingResult);
	}

	@Test
	public void testRook(){
		boolean testingResult = true;
		fields[3][3] = new Rook(PieceColor.White, bl);
		// TO DO: insert manually assigned legal moves here
		manual[4][3] = true;
		manual[2][3] = true;
		manual[3][2] = true;
		manual[3][4] = true;

		fields[3][3].calcLegalMoves(3, 3, toTest);

		for (int i = 0; i<8; i++){
			for (int j = 0; j<8; j++){
				testingResult = testingResult && (manual[i][j] == toTest[i][j]);
			}
		}
		assertTrue(testingResult);
	}

	@Test
	public void testPawn(){
		boolean testingResult = true;
		fields[3][3] = new Pawn(PieceColor.White, bl);
		// TO DO: insert manually assigned legal moves here
		manual[3][4] = true;
		manual[3][5] = true; // for first move

		fields[3][3].calcLegalMoves(3, 3, toTest);

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
		// TO DO: insert manually assigned legal moves here
		manual[4][5] = true;
		manual[2][5] = true;
		manual[1][4] = true;
		manual[1][2] = true;
		manual[2][1] = true;
		manual[4][1] = true;
		manual[5][2] = true;
		manual[5][4] = true;

		fields[3][3].calcLegalMoves(3, 3, toTest);

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
        fields[3][3].calcLegalMoves(3, 3, toTest);

        for (int i = 0; i<8; i++){
            for (int j = 0; j<8; j++){
                testingResult = testingResult && (manual[i][j] == toTest[i][j]);
            }
        }
        assertTrue(testingResult);
    }
    */

}
