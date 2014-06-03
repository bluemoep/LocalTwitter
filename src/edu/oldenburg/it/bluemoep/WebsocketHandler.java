package edu.oldenburg.it.bluemoep;

import java.util.Observable;

public class WebsocketHandler extends Observable {
	private final static WebsocketHandler instance = new WebsocketHandler();
	
	private WebsocketHandler() {
	}
	
	public static WebsocketHandler getInstance() {
		return instance;
	}
	
	public synchronized void sendNewMessage(String message) {
		this.setChanged();
		this.notifyObservers(message);
	}
}
