/*
SchlachtenSchach
Copyright (C) 2017 Patrick Reths

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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class CollisionUtil {
	/**
	 * Detects if A collides with B. (rectangle based)
	 * @param xA x position of A
	 * @param yA y position of A
	 * @param wA width of A
	 * @param hA height of A
	 * @param xB x position of B
	 * @param yB y position of B
	 * @param wB width of B
	 * @param hB height of B
	 * @return true, when A and B collide
	 */
	public static boolean detectSimple(float xA, float yA, int wA, int hA,
									   float xB, float yB, int wB, int hB) {
		if ((xA >= xB) && (xA <= xB+wB) && (yA >= yB) && (yA <= yB+hB)
				|| (xA+wA >= xB) && (xA+wA <= xB+wB) && (yA+hA >= yB) && (yA+hA <= yB+hB)) {
			return true;
		}
		return false;
	}

	/**
	 * Detects if the point A collides with the texture B.
	 * Transparent pixels of B are ignored.
	 * @param xA x position of the point A
	 * @param yA y position of the point A
	 * @param textureB the texture of B
	 * @param xB the x position of the texture B
	 * @param yB the y position of the texture B
	 * @param maxAlpha the maximum alpha value for transparency
	 * @return true, when the alpha value of point A in texture B is greater or equal maxAlpha
	 */
	public static boolean detectTransparent(float xA, float yA, Texture textureB, float xB, float yB, float maxAlpha) {
		int xP = (int) (xA - xB);
		int yP = (int) (textureB.getHeight() - (yA - yB));
		if ((xP >= 0) && (xP < textureB.getWidth()) && (yP >= 0)&& (xP < textureB.getHeight())) {
			textureB.getTextureData().prepare();
			Pixmap pm = textureB.getTextureData().consumePixmap();
			Color c = new Color(pm.getPixel(xP, yP));
			if (c.a >= maxAlpha) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Detects if the point A collides with the texture B.
	 * Transparent pixels of B are ignored.
	 * @param xA x position of the point A
	 * @param yA y position of the point A
	 * @param textureB the texture of B
	 * @param xB the x position of the texture B
	 * @param yB the y position of the texture B
	 * @return true, when the point A in texture B has no transparency
	 */
	public static boolean detectTransparent(float xA, float yA, Texture textureB, float xB, float yB) {
		return detectTransparent(xA, yA, textureB, xB, yB, 1);
	}
}
