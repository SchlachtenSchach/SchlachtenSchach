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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import de.th_bingen.schlachten_schach.pieces.Piece;

public class BoardRenderer {
	private GameLauncher game;
	private BoardLogic boardLogic;
	private SpriteBatch batch;
	private ShapeRenderer shapeBatch;
	private Texture swordHeart;
	private Position lastMousePosition;
	private Piece[][] fields;
	private final int FIELD_2D_SIZE = 130;

	BoardRenderer(BoardLogic boardLogic, GameLauncher game) {
		this.game = game;
		this.boardLogic = boardLogic;
		batch = game.batch;
		shapeBatch = game.shapeBatch;
		fields = boardLogic.getFields();

		swordHeart = new Texture(Gdx.files.internal("swordHeart.png"));
	}


    /**
     * draws the board and everything on it
     */
	public void draw() {
		float x1, y1;
		Vector2 mouse;

		lastMousePosition = null;

		shapeBatch.begin(ShapeRenderer.ShapeType.Filled);
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				if ((x+y) % 2 == 0) {
					shapeBatch.setColor(0.619607843f, 0.345098039f, 0.078431373f, 1); // #D18B47
				} else {
					shapeBatch.setColor(0.95f, 0.95f, 0.95f, 1); // #FFCE9E
				}

				x1 = 440+x*FIELD_2D_SIZE;
				y1 = 20+y*FIELD_2D_SIZE;

				shapeBatch.rect(x1, y1, FIELD_2D_SIZE, FIELD_2D_SIZE); //draw a field

				if (boardLogic.isLegalMove(x, y)) {
					shapeBatch.setColor(0, 0.6f, 1, 1);
					shapeBatch.rect(x1+3, y1+3, FIELD_2D_SIZE-6, FIELD_2D_SIZE-6);
				}

				if (boardLogic.isLegalAttack(x, y)) {
					shapeBatch.setColor(Color.RED);
					shapeBatch.rect(x1+3, y1+3, FIELD_2D_SIZE-6, FIELD_2D_SIZE-6);
				}

				if (new Position(x, y).equals(boardLogic.getCurrentPosition())) {
					if (boardLogic.isInAttackState()) {
						shapeBatch.setColor(Color.RED);
					} else {
						shapeBatch.setColor(0, 0.6f, 1, 1);
					}
					shapeBatch.circle(x1+FIELD_2D_SIZE/2, y1+FIELD_2D_SIZE/2, 60); // current piece position
				}

				mouse = game.viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
				if (CollisionUtil.detectSimple(mouse.x, mouse.y, 0, 0, x1, y1, FIELD_2D_SIZE, FIELD_2D_SIZE)) {
					lastMousePosition = new Position(x, y);
				}
			}
		}
		shapeBatch.end();

		batch.begin();
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				if (fields[x][y] != null) {
					x1 = 440+x*FIELD_2D_SIZE;
					y1 = 20+y*FIELD_2D_SIZE;
					batch.draw(fields[x][y].getTexture(), x1+15, y1+15+8, 100, 100);
					batch.draw(swordHeart, x1, y1, FIELD_2D_SIZE, 31);
					//game.font.draw(batch, ""+fields[x][y].getAttack(), x1+30, y1+31-5);
					//game.font.draw(batch, ""+fields[x][y].getLife(), x1+80, y1+31-5);
					game.font.draw(batch, ""+fields[x][y].getAttack(), x1+40, y1+31-5, Integer.toString(fields[x][y].getAttack()).length(), Align.center, false);
					game.font.draw(batch, ""+fields[x][y].getLife(), x1+86, y1+31-5, Integer.toString(fields[x][y].getLife()).length(), Align.center, false);
				}
			}
		}
		batch.end();
	}


    /**
     * draws the discontinued 2.5d version of the board
     */
	public void draw3d() {
		float x1, y1, x2, y2, x3, y3, x4, y4;
		Vector2 mouse;

		lastMousePosition = null;

		//shapeBatch.setColor(Color.GREEN);
		//drawParallelogram(shapeBatch,   600, 950,   1400, 950,   400, 150,   1600, 150); //the complete board

		shapeBatch.begin(ShapeRenderer.ShapeType.Filled);
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				if ((x+y) % 2 == 0) {
					shapeBatch.setColor(0.619607843f, 0.345098039f, 0.078431373f, 1); // #D18B47
				} else {
					shapeBatch.setColor(0.95f, 0.95f, 0.95f, 1); // #FFCE9E
				}

				x1 = 400+x*150+y*(25-6.25f*x);
				y1 = 150+y*100;
				x2 = 400+(x+1)*150+y*(25-6.25f*(x+1));
				y2 = y1;
				x3 = 400+x*150+(y+1)*(25-6.25f*x);
				y3 = 150+(y+1)*100;
				x4 = 400+(x+1)*150+(y+1)*(25-6.25f*(x+1));
				y4 = y3;

				drawParallelogram(shapeBatch,  x1, y1,  x2, y2,  x3, y3,  x4, y4); //draw a field

				if (boardLogic.isLegalMove(x, y)) {
					shapeBatch.setColor(0, 0.6f, 1, 1);
					drawParallelogram(shapeBatch,  x1+3, y1+3,  x2-3, y2+3,  x3+3, y3-3,  x4-3, y4-3);
				}

				if (boardLogic.isLegalAttack(x, y)) {
					shapeBatch.setColor(Color.RED);
					drawParallelogram(shapeBatch,  x1+3, y1+3,  x2-3, y2+3,  x3+3, y3-3,  x4-3, y4-3);
				}

				if (new Position(x, y).equals(boardLogic.getCurrentPosition())) {
					if (boardLogic.isInAttackState()) {
						shapeBatch.setColor(Color.RED);
					} else {
						shapeBatch.setColor(0, 0.6f, 1, 1);
					}
					shapeBatch.circle(400+x*150+(y*1.125f)*(25-6.25f*x)+75-3.125f*(y*1.125f), y1+50, (y3-y1)/2); // current piece position
				}

				mouse = game.viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
				if (CollisionUtil.detectSimple(mouse.x, mouse.y, 0, 0, Math.max(x1, x3), y1,  (int) (Math.min(x2, x4)-Math.max(x1, x3)), 100)) {
					lastMousePosition = new Position(x, y);
				}
			}
		}
		shapeBatch.end();

		batch.begin();
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				if (fields[x][y] != null) {
					y1 = 150+y*100;
					batch.draw(fields[x][y].getTexture(), 400+x*150+(y*1.125f)*(25-6.25f*x)+75-3.125f*(y*1.125f)-50, y1+50-50, 100, 100);
				}
			}
		}
		batch.end();
	}

    /**
     * draws a parallelogram with the given points and using the ShapeRenderer
     * @param shapeBatch
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     */
	private static void drawParallelogram(ShapeRenderer shapeBatch, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		shapeBatch.triangle(x1, y1, x2, y2, x3, y3);
		shapeBatch.triangle(x2, y2, x3, y3, x4, y4);
	}

	/**
	 *
	 * @return the last clicked piece position or null
	 */
	public Position getLastMousePosition() {
		return lastMousePosition;
	}

	/**
	 *
	 * @return the last clicked piece or null
	 */
	public Piece getLastMousePositionPiece() {
		if (lastMousePosition == null) {
			return null;
		}
		return fields[(int) lastMousePosition.x][(int) lastMousePosition.y];
	}

	public void setBoardLogic(BoardLogic boardLogic) {
	    this.boardLogic = boardLogic;
		fields = boardLogic.getFields();
    }

	public void dispose () {
	}
}
