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

	@OnOpen
	public void onOpen(Session session) {
		// Handle new connection here
		this.session = session;
		TweetSource.getInstance().addTweetReceiver(this);
	}

	@OnMessage
	public synchronized void onMessage(String message) {
		try {
			Boundaries boundaries = BoundariesParser.parse(message);
			north = boundaries.getNorth();
			east = boundaries.getEast();
			south = boundaries.getSouth();
			west = boundaries.getWest();
			TweetSource.getInstance().fullRequest(this);
		} catch (JsonParseException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		// Handle closing connection here
		TweetSource.getInstance().removeTweetReceiver(this);
		this.session = null;
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Handle error during transport here
		throwable.printStackTrace();
	}

	@Override
	public void receive(Message message) {
		try {
			this.session.getBasicRemote().sendText(message.getMessage());
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
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
}
