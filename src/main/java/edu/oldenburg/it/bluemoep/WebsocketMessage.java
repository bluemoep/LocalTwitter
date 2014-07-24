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
	private double radius = 0;
	private long time = 0;

	public static WebsocketMessage parse(String message) throws JsonParseException {
		WebsocketMessage msg = new WebsocketMessage();
		try {
			// Parse JSON
			JsonReader reader = Json.createReader(new StringReader(message));
			JsonObject root = reader.readObject();
			if (root.containsKey("north")) {
				JsonNumber jNorth = root.getJsonNumber("north");
				JsonNumber jEast = root.getJsonNumber("east");
				JsonNumber jSouth = root.getJsonNumber("south");
				JsonNumber jWest = root.getJsonNumber("west");
				msg.boundaries = new Boundaries(jNorth.doubleValue(),
						jEast.doubleValue(), jSouth.doubleValue(),
						jWest.doubleValue());
				JsonNumber jRadius = root.getJsonNumber("radius");
				msg.radius = jRadius.doubleValue();
				JsonNumber jTime = root.getJsonNumber("time");
				msg.time = jTime.longValue();
				msg.isBoundaries = true;
			} else {
				JsonNumber jRadius = root.getJsonNumber("radius");
				msg.radius = jRadius.doubleValue();
				JsonNumber jTime = root.getJsonNumber("time");
				msg.time = jTime.longValue();
				msg.isFullRequest = true;
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
	
	public double getRadius() {
		return radius;
	}
	
	public long getTime() {
		return time;
	}

}
