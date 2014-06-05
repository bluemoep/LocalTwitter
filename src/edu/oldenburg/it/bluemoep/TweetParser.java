package edu.oldenburg.it.bluemoep;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;

public class TweetParser {

	public static Message parse(String message) throws JsonParseException {
		try {
			// Parse JSON
			JsonReader reader = Json.createReader(new StringReader(message));
			JsonObject root = reader.readObject();
			JsonObject coords = root.getJsonObject("coordinates");
			JsonString type = coords.getJsonString("type");
			if (!type.getString().equals("Point"))
				throw new JsonParseException();
			JsonArray coordArr = coords.getJsonArray("coordinates");
			JsonNumber jlng = coordArr.getJsonNumber(0);
			JsonNumber jlat = coordArr.getJsonNumber(1);
			double lat = jlat.doubleValue();
			double lng = jlng.doubleValue();
			JsonString jid = root.getJsonString("id_str");
			String id = jid.getString();
			
			// Construct Message
			return new Message(id, lat, lng, message);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			throw new JsonParseException();
		}
	}
	
}
