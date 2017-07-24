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

package de.th_bingen.schlachten_schach.online;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import de.th_bingen.schlachten_schach.PieceColor;

public class Online {
	public static final String SERVER = "schlachtenschach.mooo.com";
	public static final int PORT = 3210;
	private Socket socket;
	private ObjectOutputStream oo;
	private ObjectInputStream oi;
	private final String gameName;
	private final String playerName;
	private PieceColor playerColor;

	public Online(String gameName, String playerName) {
		this.gameName = gameName;
		this.playerName = playerName;
	}

	/**
	 * connect to the server
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void connect() throws IOException, ClassNotFoundException {
		socket = new Socket(SERVER, PORT);
		socket.setKeepAlive(true);
		oo = new ObjectOutputStream(socket.getOutputStream());
		oi = new ObjectInputStream(socket.getInputStream());

		Object obj;
		oo.writeObject(new ConnectionHeader(gameName, playerName));

		try {
			// get playerColor
			obj = read(oi);
			if (obj instanceof PieceColor) {
				playerColor = (PieceColor) obj;
				System.out.println(playerColor);
			} else {
				throw new IOException();
			}

			System.out.println("Waiting for player...");
			// wait for other player //TODO: timeout
			obj = read(oi);
			if (obj instanceof String) {
				//String message = (String) obj;
				// message == "ready", all players online
			} else {
				throw new IOException();
			}

		} catch (OtherPlayerDisconnectedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * close the connection
	 */
	public void close() {
		if (socket != null) {
			try {
				socket.close();
				System.out.println("Connection closed");
			} catch (IOException e) {
				System.out.println("Can't close connection");
				e.printStackTrace();
			}
		}
	}

	/**
	 * send a move
	 * @param move the move to send
	 * @throws IOException
	 */
	public void sendMove(PieceMove move) throws IOException {
		oo.writeObject(move);
	}

	/**
	 * receive a move from the server (wait for it)
	 * @return the received move
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws OtherPlayerDisconnectedException
	 */
	public PieceMove receiveMove() throws IOException, ClassNotFoundException, OtherPlayerDisconnectedException {
		Object obj = read(oi);
		if (obj instanceof PieceMove) {
			PieceMove move = (PieceMove) obj;
			System.out.println(move);
			return move;
		} else {
			throw new IOException();
		}
	}

	private Object read(ObjectInputStream oi) throws ClassNotFoundException, IOException, OtherPlayerDisconnectedException {
		Object obj = oi.readObject();
		while (obj instanceof PingData) {
			obj = oi.readObject();
		}
		if (obj instanceof String) {
			String message = (String) obj;
			System.out.println(message);
			if (message.equals("otherPlayerDisconnected")) {
				throw new OtherPlayerDisconnectedException();
			}
		}
		return obj;
	}

	/**
	 *
	 * @return the name of the game
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 *
	 * @return the name of the player
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 *
	 * @return the color of the player (randomly assigned by the server)
	 */
	public PieceColor getPlayerColor() {
		return playerColor;
	}
}
