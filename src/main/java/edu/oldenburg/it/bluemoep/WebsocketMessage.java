package edu.oldenburg.it.bluemoep;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class WebsocketMessage {

	private boolean isFullRequest = false;
	private boolean isBoundaries = false;
	private Boundaries boundaries = null;

	public static WebsocketMessage parse(String message) throws JsonParseException {
		WebsocketMessage msg = new WebsocketMessage();
		try {
			// Parse JSON
			JsonReader reader = Json.createReader(new StringReader(message));
			String test = reader.toString();
			if (test.equals("fullRequest")) {
				msg.isFullRequest = true;
			} else {
				JsonObject root = reader.readObject();
				JsonNumber jNorth = root.getJsonNumber("north");
				JsonNumber jEast = root.getJsonNumber("east");
				JsonNumber jSouth = root.getJsonNumber("south");
				JsonNumber jWest = root.getJsonNumber("west");
				msg.boundaries = new Boundaries(jNorth.doubleValue(),
						jEast.doubleValue(), jSouth.doubleValue(),
						jWest.doubleValue());
				msg.isBoundaries = true;
			}
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			throw new JsonParseException();
		}
		return msg;
	}

	private WebsocketMessage() {

	}
	
	public boolean isFullRequest() {
		return isFullRequest;
	}

	public boolean isBoundaries() {
		return isBoundaries;
	}

	public Boundaries getBoundaries() {
		return boundaries;
	}

}
