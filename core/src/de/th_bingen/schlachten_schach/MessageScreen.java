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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import static de.th_bingen.schlachten_schach.GameLauncher.WINDOW_HEIGHT;
import static de.th_bingen.schlachten_schach.GameLauncher.WINDOW_WIDTH;

public class MessageScreen implements Screen {
	private final GameLauncher game;
	private Stage stage;
	private String message;
	private String buttonText;
	private Screen buttonAction;

	public MessageScreen(GameLauncher game, String message, String buttonText, Screen buttonAction) {
		this.game = game;
		this.message = message;
		this.buttonText = buttonText;
		this.buttonAction = buttonAction;

		stage = new Stage(game.viewportMenu);
		setupGUI();
	}

    /**
     * sets up the Graphical User Interface of the message screen
     */
	private void setupGUI() {
		VerticalGroup mainLayout = new VerticalGroup();
		mainLayout.setFillParent(true);
		mainLayout.align(Align.center);
		stage.addActor(mainLayout);

		Label messageLabel = new Label(message, game.skin);
		mainLayout.addActor(messageLabel);

		TextButton button = new TextButton(buttonText, game.skin);
		mainLayout.addActor(button);
		button.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(buttonAction);
			}
		});

	}

    /**
     * renders the message screen
     * @param delta
     */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		// clear screen (with anti-aliasing)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		game.camera.update();

		stage.act();
		stage.draw();

		// toggle fullscreen with F
		if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
			} else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		game.viewportMenu.update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
