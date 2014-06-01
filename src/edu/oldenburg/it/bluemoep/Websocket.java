package edu.oldenburg.it.bluemoep;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws")
public class Websocket implements Observer {
	private Session ses;

	@OnOpen
	public void onOpen(Session session) {
		// Handle new connection here
		ses = session;
		try {
			session.getBasicRemote().sendText("Message from the server");
		} catch (IOException e) {
			e.printStackTrace();
		}
		WebsocketHandler.getInstance().addObserver(this);
	}

	@OnMessage
	public void onMessage(String message) {
		// Handle client received message here
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		// Handle closing connection here
		this.ses = null;
		WebsocketHandler.getInstance().deleteObserver(this);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Handle error during transport here
	}
	
	public void sendMessage(String message) throws IOException{
		this.ses.getBasicRemote().sendText(message);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof String) {
			try {
				this.sendMessage((String) arg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//TODO weiteres handeln von oberserver
	}
}
