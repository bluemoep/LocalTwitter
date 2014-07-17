package edu.oldenburg.it.bluemoep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.http.HttpResponse;

public class FullRequest {

	public static void doRequest(Websocket websocket) {
		Date since = new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		TwitterRequest tr = new TwitterRequest(TwitterRequest.Method.GET,
				"https://api.twitter.com/1.1/search/tweets.json");
		tr.addParameter("q", "include:retweets since:" + formatter.format(since));
		tr.addParameter("result_type", "recent");
		tr.addParameter("count", "250");
		tr.addParameter("geocode",
				websocket.getLatitude() + "," + websocket.getLongitude()
						+ ",5km");

		HttpResponse httpResponse = tr.doRequest();

		// getting response Messages
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		System.out.println(statusCode + ":"
				+ httpResponse.getStatusLine().getReasonPhrase());
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			JsonReader json = Json.createReader(reader);
			JsonArray statuses = json.readObject().getJsonArray("statuses");
			for(JsonValue status : statuses) {
				websocket.receive(TweetParser.parse(status.toString()));
			}

		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
		}
	}

}
