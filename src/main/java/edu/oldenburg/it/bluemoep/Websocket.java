package edu.oldenburg.it.bluemoep;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws")
public class Websocket implements TweetReceiver {

	private Session session;
	private double north, east, south, west;
	private TwitterStream stream = null;

	@OnOpen
	public void onOpen(Session session) {
		// Handle new connection here
		this.session = session;
	}

	@OnMessage
	public synchronized void onMessage(String message) {
		try {
			WebsocketMessage msg = WebsocketMessage.parse(message);
			if (msg.isBoundaries()) {
				System.out.println("Location change in work.");
				Boundaries boundaries = msg.getBoundaries();
				north = boundaries.getNorth();
				east = boundaries.getEast();
				south = boundaries.getSouth();
				west = boundaries.getWest();
				if (stream != null)
					stream.stop();
				stream = new TwitterStream(north, east, south, west, this);
				FullRequest.doRequest(this, msg);
			} else if (msg.isFullRequest()) {
				System.out.println("FullRequest in work.");
				FullRequest.doRequest(this, msg);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		// Handle closing connection here
		if (stream != null)
			stream.stop();
		this.session = null;
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Handle error during transport here
		if (stream != null)
			stream.stop();
		throwable.printStackTrace();
	}

	@Override
	public void receive(Message message) {
		try {
			this.session.getBasicRemote().sendText(message.getMessage());
		} catch (NullPointerException | IOException e) {
		}
	}

	@Override
	public double getNorth() {
		return north;
	}

	@Override
	public double getEast() {
		return east;
	}

	@Override
	public double getSouth() {
		return south;
	}

	@Override
	public double getWest() {
		return west;
	}

	@Override
	public double getLatitude() {
		return ((getNorth() - getSouth()) / 2) + getSouth();
	}

	@Override
	public double getLongitude() {
		return ((getEast() - getWest()) / 2) + getWest();
	}
}
