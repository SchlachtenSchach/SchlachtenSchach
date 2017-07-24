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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameLauncher extends Game {
	public static int GAME_WIDTH = 1920;
	public static int GAME_HEIGHT = 1080;
	public static int WINDOW_WIDTH = 1280;
	public static int WINDOW_HEIGHT = 720;
	private final int menuScale;

	OrthographicCamera camera;
	Viewport viewport;
	Viewport viewportMenu;
	SpriteBatch batch;
	ShapeRenderer shapeBatch;
	BitmapFont font;
	Skin skin;
	GameScreen gameScreen;
	MainMenuScreen menuScreen;

	public GameLauncher() {
		this(1);
	}

	public GameLauncher(int menuScale) {
		this.menuScale = menuScale;
	}

	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
		viewportMenu = new FitViewport(GAME_WIDTH/menuScale, GAME_HEIGHT/menuScale, camera);

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeBatch = new ShapeRenderer();
		shapeBatch.setProjectionMatrix(camera.combined);

		skin = new Skin(Gdx.files.internal("skins/holo/skin/dark-hdpi/Holo-dark-hdpi.json"));
		skin.getFont("default-font").getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		font = skin.getFont("default-font");

		menuScreen = new MainMenuScreen(this);
		setScreen(menuScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeBatch.dispose();
		skin.dispose();
	}

}
