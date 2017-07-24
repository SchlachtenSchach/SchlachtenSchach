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
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.th_bingen.schlachten_schach.online.Online;

import static de.th_bingen.schlachten_schach.GameLauncher.WINDOW_HEIGHT;
import static de.th_bingen.schlachten_schach.GameLauncher.WINDOW_WIDTH;

public class MainMenuScreen implements Screen {
	private final GameLauncher game;
	private Stage stage;
	private VerticalGroup mainLayout;
	private Table onlineLayout;
	private TextButton continueButton;
	private TextButton saveButton;
	private TextButton loadButton;
	private TextButton newOnlineGameStartButton;
	private TextField gameNameInput;
	private TextField playerNameInput;
	private Label waitMessageLabel;
	private Thread onlineConnectorThread;
	private Online online;
	private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

	public MainMenuScreen(GameLauncher game) {
		this.game = game;
		stage = new Stage(game.viewportMenu);
		setupGUI();
	}

    /**
     * sets up the Graphical User Interface of the menu screen
     */
	private void setupGUI() {
		waitMessageLabel = new Label("Warte auf Spieler...", game.skin);

		mainLayout = new VerticalGroup();
		mainLayout.setFillParent(true);
		mainLayout.align(Align.center);
		stage.addActor(mainLayout);

		onlineLayout = new Table(game.skin);
		onlineLayout.setFillParent(true);
		stage.addActor(onlineLayout);

		continueButton = new TextButton("Fortsetzen", game.skin);
		mainLayout.addActor(continueButton);
		continueButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(game.gameScreen);
			}
		});

		saveButton = new TextButton("Speichern", game.skin);
		mainLayout.addActor(saveButton);
		saveButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				try {
					game.gameScreen.save();
				} catch (IOException e) {
					e.printStackTrace();
					showErrorMessageScreen("ERROR:\nDer Spielstand konnte nich gespeichert werden.", "OK");
				}
			}
		});

		final TextButton newGameButton = new TextButton("Neues Spiel", game.skin);
		mainLayout.addActor(newGameButton);
		newGameButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (game.gameScreen != null) {
					game.gameScreen.dispose();
				}
				online = null;
				game.gameScreen = new GameScreen(game);
				game.setScreen(game.gameScreen);
			}
		});

		final TextButton newOnlineGameButton = new TextButton("Neues Online Spiel", game.skin);
		mainLayout.addActor(newOnlineGameButton);
		newOnlineGameButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				showOnlineMenu();
			}
		});

		loadButton = new TextButton("Laden", game.skin);
		mainLayout.addActor(loadButton);
		loadButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (game.gameScreen != null) {
					game.gameScreen.dispose();
				}
				try {
					game.gameScreen = new GameScreen(game);
					game.gameScreen.load();
					game.setScreen(game.gameScreen);
				} catch (IOException e) {
					game.gameScreen.dispose();
					//e.printStackTrace();
					showMessageScreen("Es konnte kein Spielstand gefunden/geladen werden.", "OK", game.menuScreen);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		final TextButton exitButton = new TextButton("Beenden", game.skin);
		mainLayout.addActor(exitButton);
		exitButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				dispose();
				Gdx.app.exit();
			}
		});

		onlineLayout.add("Spielname:");
		gameNameInput = new TextField("", game.skin);
		onlineLayout.add(gameNameInput).width(300);
		onlineLayout.row();

		// player name for future use
		//onlineLayout.add("Spielername:");
		playerNameInput = new TextField("- - - - -", game.skin);
		//onlineLayout.add(playerNameInput).width(300);
		//onlineLayout.row();

		onlineLayout.add(""); // empty cell
		final HorizontalGroup hGroup = new HorizontalGroup();
		onlineLayout.add(hGroup).align(Align.left);
		onlineLayout.row();

		newOnlineGameStartButton = new TextButton("OK", game.skin);
		hGroup.addActor(newOnlineGameStartButton);
		newOnlineGameStartButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if (game.gameScreen != null) {
					game.gameScreen.dispose();
				}
				continueButton.setVisible(false);
				saveButton.setVisible(false);
				waitMessageLabel.setVisible(true);
				newOnlineGameStartButton.setDisabled(true);

				online = new Online(gameNameInput.getText(), playerNameInput.getText());
				onlineConnectorThread = new Thread(new OnlineConnector());
				onlineConnectorThread.start();
			}
		});

		final TextButton cancelOnlineGameButton = new TextButton("Abbrechen", game.skin);
		hGroup.addActor(cancelOnlineGameButton);
		cancelOnlineGameButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if ((onlineConnectorThread != null) && (onlineConnectorThread.isAlive())) {
					Online tmp = online;
					online = null;
					tmp.close();
				}
				newOnlineGameStartButton.setDisabled(false);
				showMainMenu();
			}
		});

		onlineLayout.add(""); // empty cell
		onlineLayout.add(waitMessageLabel).align(Align.left);
		//onlineLayout.setDebug(true);

		showMainMenu();
	}

	/**
	 * show the main menu
	 */
	private void showMainMenu() {
		waitMessageLabel.setVisible(false);
		onlineLayout.setVisible(false);
		mainLayout.setVisible(true);
	}

	/**
	 * show the online menu
	 */
	private void showOnlineMenu() {
		waitMessageLabel.setVisible(false);
		mainLayout.setVisible(false);
		onlineLayout.setVisible(true);
	}

    /**
     * renders the main menu screen
     * @param delta
     */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		// clear screen (with anti-aliasing)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		game.camera.update();

		game.batch.begin();
		game.font.draw(game.batch, "Willkommen zu SchlachtenSchach", 100, 150);
		game.batch.end();

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

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) // continue game after ESC
				|| Gdx.input.isKeyJustPressed(Input.Keys.BACK)) { // android: continue game after back button
			if (game.gameScreen != null) {
				game.setScreen(game.gameScreen);
			}
		}

		Runnable runnable = queue.poll();
		if (runnable != null) {
			runnable.run();
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
		stage.mouseMoved(0, 1); // workaround: move the mouse 1px to unfocus all widgets

		showMainMenu();

		if (game.gameScreen == null) {
			continueButton.setVisible(false);
			saveButton.setVisible(false);
			loadButton.setDisabled(false);
		} else {
			continueButton.setVisible(true);
			saveButton.setVisible(true);
			if (online != null) {
				loadButton.setDisabled(true);
			}
		}
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		if (game.gameScreen != null) {
			game.gameScreen.dispose();
		}
		stage.dispose();
	}

	/**
	 * Show a message
	 * @param message the message to show
	 * @param buttonText the label of the button
	 * @param nextScreen show this screen after a click on the button
	 */
	public void showMessageScreen(final String message, final String buttonText, final Screen nextScreen) {
		queue.add(new Runnable() {
			@Override
			public void run() {
				game.setScreen(new MessageScreen(game, message, buttonText, nextScreen));
			}
		});
	}

	/**
	 * Show a message
	 * @param message the message to show
	 * @param buttonText the label of the button
	 */
	public void showErrorMessageScreen(final String message, final String buttonText) {
		showMessageScreen(message, buttonText, game.menuScreen);
	}

	class OnlineConnector implements Runnable {
		@Override
		public void run() {
			try {
				online.connect();

				queue.add(new Runnable() {
					@Override
					public void run() {
						showMainMenu();
						newOnlineGameStartButton.setDisabled(false);
						game.gameScreen = new GameScreen(game, online);

						String playerColor;
						if (online.getPlayerColor() == PieceColor.White) {
							playerColor = "weißen";
						} else {
							playerColor = "schwarzen";
						}
						showMessageScreen("Sie Spielen mit "+playerColor+" Figuren.", "OK" , game.gameScreen);
					}
				});

			} catch (IOException e) {
				if (online == null) { // user pressed cancel button
					return;
				}
				e.printStackTrace();
				showErrorMessageScreen("Verbindungsfehler", "OK");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				showErrorMessageScreen("Server und Client nicht kompatibel", "OK");
			}
		}
	}

}
