package edu.oldenburg.it.bluemoep;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class BoundariesParser {

	public static Boundaries parse(String message) throws JsonParseException {
		try {
			// Parse JSON
			JsonReader reader = Json.createReader(new StringReader(message));
			JsonObject root = reader.readObject();
			JsonNumber jNorth = root.getJsonNumber("north");
			JsonNumber jEast = root.getJsonNumber("east");
			JsonNumber jSouth = root.getJsonNumber("south");
			JsonNumber jWest = root.getJsonNumber("west");
			return new Boundaries(jNorth.doubleValue(), jEast.doubleValue(), jSouth.doubleValue(), jWest.doubleValue());
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			throw new JsonParseException();
		}
	}
	
}
