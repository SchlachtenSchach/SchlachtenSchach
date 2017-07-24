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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.th_bingen.schlachten_schach.online.Online;
import de.th_bingen.schlachten_schach.online.OtherPlayerDisconnectedException;
import de.th_bingen.schlachten_schach.online.PieceMove;

import static de.th_bingen.schlachten_schach.GameLauncher.GAME_HEIGHT;
import static de.th_bingen.schlachten_schach.GameLauncher.GAME_WIDTH;
import static de.th_bingen.schlachten_schach.GameLauncher.WINDOW_HEIGHT;
import static de.th_bingen.schlachten_schach.GameLauncher.WINDOW_WIDTH;

public class GameScreen implements Screen {
	private final GameLauncher game;
	private BoardRenderer boardRenderer;
	private BoardLogic boardLogic;
	private boolean gameMode3d;
	private StateType state;
	private PieceColor currentPlayer;
	private boolean gameOver;
	private boolean onlineMode;
	private Thread onlineThread;
	private Online online;
	private Stage stage;
	private Texture background;
	private Texture helpBackground;
	private Texture gameOverWhite;
	private Texture gameOverBlack;
	private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

	public GameScreen(GameLauncher game) {
		this(game, null);
	}

	public GameScreen(GameLauncher game, Online online) {
		this.game = game;
		this.online = online;
		if (online != null) {
			onlineMode = true;
		}

		stage = new Stage(game.viewport);
		setupGUI();

		boardLogic = new BoardLogic();
		boardRenderer = new BoardRenderer(boardLogic, game);
		background = new Texture(Gdx.files.internal("background.png"));
		helpBackground = new Texture(Gdx.files.internal("helpBackground.png"));
		helpBackground.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		gameOverWhite = new Texture(Gdx.files.internal("gameOverWhite.png"));
		gameOverWhite.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		gameOverBlack = new Texture(Gdx.files.internal("gameOverBlack.png"));
		gameOverBlack.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		state = StateType.SelectMV;
		currentPlayer = PieceColor.White;
		gameOver = false;
	}

    /**
     * sets up the Graphical User Interface of the game screen
     */
	private void setupGUI() {
		Table table = new Table(game.skin);
		//table.setDebug(true);
		table.setSize(400, 400);
		table.setPosition(10, GAME_HEIGHT-table.getHeight()-10);
		table.align(Align.topLeft);
		stage.addActor(table);

		if (!onlineMode) {
			final TextButton newGameButton = new TextButton("Neues Spiel", game.skin);
			table.add(newGameButton);
			table.row();
			newGameButton.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					newGame();
				}
			});
		}

		final TextButton menuButton = new TextButton("Menü", game.skin);
		table.add(menuButton);
		table.row();
		menuButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(game.menuScreen);
			}
		});

		final TextButton skipAttackButton = new TextButton("Keine Attacke", game.skin);
		table.add(skipAttackButton);
		table.row();
		skipAttackButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if ((state == StateType.SelectAT) || state == StateType.Attack) {
					if ((onlineMode) && (currentPlayer == online.getPlayerColor())) {
						try {
							online.sendMove(new PieceMove(boardLogic.getLastSelectedPiecePos().x, boardLogic.getLastSelectedPiecePos().y, boardLogic.getLastSelectedPiecePos().x, boardLogic.getLastSelectedPiecePos().y));
						} catch (IOException e) {
							//e.printStackTrace();
							showNetworkErrorMessageScreen();
						}
					}
					boardLogic.resetLegalAttacks();
					boardLogic.resetCurrentPosition();
					state = StateType.SelectMV;
					currentPlayer = currentPlayer.other();
				}
			}
		});

	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		// clear screen (with anti-aliasing)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		game.camera.update(); // tell the camera to update its matrices

		game.batch.begin();
		game.batch.draw(background, 0, 0);
		game.batch.draw(helpBackground, 0, 0, 300, 535);
		if (currentPlayer == PieceColor.White) {
			game.font.draw(game.batch, "Am Zug: Weiß", 15, 570);
		} else {
			game.font.setColor(Color.BLACK);
			game.font.draw(game.batch, "Am Zug: Schwarz", 15, 570);
			game.font.setColor(Color.WHITE);
		}
		if (boardLogic.getCurrentPiece() != null) {
			game.batch.draw(boardLogic.getCurrentPiece().getTexture(), 98, 395, 100, 100);
			game.font.setColor(Color.BLACK);
			game.font.draw(game.batch, boardLogic.getCurrentPiece().getHelpText(), 40, 400, 230, Align.left, true);
			game.font.setColor(Color.WHITE);
		}
		game.batch.end();

		if (gameMode3d) {
			boardRenderer.draw3d();
		} else {
			boardRenderer.draw();
		}

		stage.act();
		stage.draw();

		if (gameOver) {
			game.batch.begin();
			if (currentPlayer == PieceColor.White) {
				game.batch.draw(gameOverWhite, GAME_WIDTH/2-gameOverWhite.getWidth()/2, GAME_HEIGHT/2-gameOverWhite.getHeight()/2);
			} else {
				game.batch.draw(gameOverBlack, GAME_WIDTH/2-gameOverBlack.getWidth()/2, GAME_HEIGHT/2-gameOverBlack.getHeight()/2);
			}
			game.batch.end();
		} else {
			if (onlineMode) {
				if (currentPlayer == online.getPlayerColor()) {
					mouseInputHandler();
				} else {
					if ((onlineThread == null) || (!onlineThread.isAlive())) {
						onlineThread = new Thread(new OnlinePlayerHandler());
						onlineThread.start();
					}
				}
			} else {
				mouseInputHandler();
			}
		}

		keyboardInputHandler();

		Runnable runnable = queue.poll();
		if (runnable != null) {
			runnable.run();
		}
	}

    /**
     * handles mouse input and manages turn order, game over and attack/move phase
     */
	private void mouseInputHandler() {
		PieceMove move;

		if (Gdx.input.justTouched()) {
			Position mousePosition = boardRenderer.getLastMousePosition();

			if (mousePosition != null) {
				if ((state == StateType.Move) && (!boardLogic.isLegalMove(mousePosition))) {
					state = StateType.SelectMV;
				}
				if ((state == StateType.Attack) && (!boardLogic.isLegalAttack(mousePosition))) {
					state = StateType.SelectAT;
				}

				switch (state) {
					case SelectMV:
						if ((boardLogic.isEmpty(mousePosition)) || (boardLogic.getPlayer(mousePosition) != currentPlayer)) {
							boardLogic.resetLegalMoves();
							boardLogic.resetCurrentPosition();
						} else {
							state = StateType.Move;
							boardLogic.setCurrentPosition(mousePosition);
							boardLogic.calcLegalMoves();
							boardLogic.setAttackState(false);
						}
						break;

					case Move:
						move = new PieceMove(boardLogic.getCurrentXPosition(), boardLogic.getCurrentYPosition(), mousePosition.x, mousePosition.y);
						boardLogic.move(mousePosition);
						boardLogic.resetLegalMoves();
						boardLogic.resetCurrentPosition();
						state = StateType.SelectAT;
						if (onlineMode) {
							try {
								online.sendMove(move);
							} catch (IOException e) {
								//e.printStackTrace();
								showNetworkErrorMessageScreen();
							}
						}
						break;

					case SelectAT:
						if ((boardLogic.isEmpty(mousePosition)) || (boardLogic.getPlayer(mousePosition) != currentPlayer)) {
							boardLogic.resetLegalAttacks();
							boardLogic.resetCurrentPosition();
						} else {
							state = StateType.Attack;
							boardLogic.setCurrentPosition(mousePosition);
							boardLogic.calcLegalAttacks();
							boardLogic.setAttackState(true);
						}
						break;

					case Attack:
						move = new PieceMove(boardLogic.getCurrentXPosition(), boardLogic.getCurrentYPosition(), mousePosition.x, mousePosition.y);
						if (boardLogic.attack(mousePosition)) {
							gameOver = true;
						} else {
							currentPlayer = currentPlayer.other();
						}
						boardLogic.resetLegalAttacks();
						boardLogic.resetCurrentPosition();
						state = StateType.SelectMV;
						if (onlineMode) {
							try {
								online.sendMove(move);
							} catch (IOException e) {
								//e.printStackTrace();
								showNetworkErrorMessageScreen();
							}
						}
						break;
				}

			} else {
				boardLogic.resetLegalMoves();
				boardLogic.resetLegalAttacks();
				boardLogic.resetCurrentPosition();

				if (state == StateType.Move) {
					state = StateType.SelectMV;
				} else if (state == StateType.Attack) {
					state = StateType.SelectAT;
				}
			}
		}
	}

    /**
     * handles keyboard input
     */
	private void keyboardInputHandler() {
		// toggle 2d/3d mode
		if(Gdx.input.isKeyJustPressed(Keys.F3)) {
			gameMode3d = !gameMode3d;
		}

		// toggle fullscreen with F
		if(Gdx.input.isKeyJustPressed(Keys.F)) {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
			} else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) // show menu after ESC
				|| Gdx.input.isKeyJustPressed(Input.Keys.BACK)) { // android: show menu after back button
			game.setScreen(game.menuScreen);
		}
	}

	@Override
	public void resize (int width, int height) {
		game.viewport.update(width, height, true);
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
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose () {
		boardRenderer.dispose();
		stage.dispose();
		background.dispose();
		helpBackground.dispose();
		gameOverWhite.dispose();
		gameOverBlack.dispose();
		if (onlineMode) {
			online.close();
		}
		game.gameScreen = null;
	}


	private class OnlinePlayerHandler implements Runnable {
		@Override
		public void run() {
			try {
				// move
				PieceMove move = online.receiveMove();
				boardLogic.setAttackState(false);
				boardLogic.setCurrentPosition(new Position(move.oldX, move.oldY));
				boardLogic.calcLegalMoves();
				boardLogic.move(new Position(move.newX, move.newY));
				boardLogic.resetLegalMoves();
				boardLogic.setCurrentPosition(new Position(move.newX, move.newY)); // to highlight the new position
				// attack
				move = online.receiveMove();
				boardLogic.setAttackState(true);
				boardLogic.setCurrentPosition(new Position(move.oldX, move.oldY));
				boardLogic.calcLegalAttacks();
				if (boardLogic.attack(new Position(move.oldX, move.oldY), new Position(move.newX, move.newY))) {
					gameOver = true;
				} else {
					currentPlayer = currentPlayer.other();
				}
				boardLogic.resetLegalAttacks();
				boardLogic.setCurrentPosition(new Position(move.newX, move.newY)); // to highlight the defending

			} catch (OtherPlayerDisconnectedException e) {
				//e.printStackTrace();
				showErrorMessageScreen("ERROR:\nIhr Mitspieler hat das Spiel verlassen oder die Verbindung zum Server verloren.", "OK");
			} catch (IOException e) {
				//e.printStackTrace();
				showNetworkErrorMessageScreen();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				Gdx.app.exit();
			}
		}
	}

    /**
     * saves the game to an internal savegame.dat
     * @throws IOException when it encounters a problem with IO devices
     */
    public void save() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(Gdx.files.local("savegame.dat").write(false));
        oos.writeObject(new SaveContainer(boardLogic.getFields(), state, currentPlayer));
        oos.close();
    }

    /**
     * loads the game to an internal savegame.dat
     * @throws IOException when it encounters a problem with IO devices
     * @throws ClassNotFoundException if someone exchanges savegame.dat for something that should not be there
     */
    public void load() throws IOException, ClassNotFoundException {
		try {
			ObjectInputStream ois = new ObjectInputStream(Gdx.files.local("savegame.dat").read());
			SaveContainer sc = (SaveContainer) ois.readObject();
			ois.close();
			currentPlayer = sc.getCurrPlayer();
			state = sc.getState();
			boardLogic = new BoardLogic(sc.getBoard());
			boardRenderer.setBoardLogic(boardLogic);
		} catch (GdxRuntimeException ex) {
			throw new IOException(ex.getMessage());
		}
    }

    /**
     * starts a new game
     */
    public void newGame() {
		if (onlineMode) {
			try {
				onlineThread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.setScreen(game.menuScreen);
			dispose();
			return;
			/*
			// synchronization between the players is not granted
			// (one player is in the new and the other player is in the old game)
			try {
				online.close();
				onlineMode = false;
				Online newOnline = new Online(online.getGameName(), online.getPlayerName());
				newOnline.connect();
				game.gameScreen = new GameScreen(game, newOnline);
			} catch (IOException|ClassNotFoundException e) {
				e.printStackTrace();
				game.setScreen(game.menuScreen);
				dispose();
				return;
			}
			*/
		} else {
			game.gameScreen = new GameScreen(game);
		}
		game.setScreen(game.gameScreen);
		dispose();
    }

	/**
	 * Show a message and call dispose()
	 * @param message the message to show
	 * @param buttonText the label of the button
	 */
	public void showErrorMessageScreen(final String message, final String buttonText) {
		queue.add(new Runnable() {
			@Override
			public void run() {
				game.setScreen(new MessageScreen(game, message, buttonText, game.menuScreen));
				dispose();
			}
		});
	}

	/**
	 * Show the network error message and call dispose()
	 */
	public void showNetworkErrorMessageScreen() {
		showErrorMessageScreen("ERROR:\nEs ist ein Netzwerkfehler aufgetreten.", "OK");
	}

}
