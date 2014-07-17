package edu.oldenburg.it.bluemoep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;

public class FullRequest {

	public static void doRequest(Websocket websocket) {
		Date since = new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		TwitterRequest tr = new TwitterRequest(TwitterRequest.Method.GET,
				"https://api.twitter.com/1.1/search/tweets.json");
		tr.addParameter("q", "since:" + formatter.format(since));
		tr.addParameter("result_type", "recent");
		tr.addParameter("geocode",
				websocket.getLatitude() + "," + websocket.getLongitude()
						+ ",5.1km");

		HttpResponse httpResponse = tr.doRequest();

		// getting response Messages
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		System.out.println(statusCode + ":"
				+ httpResponse.getStatusLine().getReasonPhrase());
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			String str;
			while ((str = reader.readLine()) != null)
				websocket.receive(TweetParser.parse(str));

		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
		}
	}

}
