package edu.oldenburg.it.bluemoep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

public class TwitterStreaming {

	private static TwitterStreaming instance = null;

	public static void startStreaming() {
		if (instance == null)
			instance = new TwitterStreaming();
	}

	private TwitterStreaming() {
		Thread runner = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					TwitterRequest tr = new TwitterRequest(
							"https://stream.twitter.com/1.1/statuses/filter.json");
					tr.addParameter("locations", "-180,-90,180,90");
					HttpResponse httpResponse = tr.doRequest();

					// getting response Messages
					int statusCode = httpResponse.getStatusLine()
							.getStatusCode();
					System.out.println(statusCode + ":"
							+ httpResponse.getStatusLine().getReasonPhrase());
					BufferedReader reader;
					try {
						reader = new BufferedReader(new InputStreamReader(
								httpResponse.getEntity().getContent()));

						TweetSource ts = TweetSource.getInstance();
						String str;
						while ((str = reader.readLine()) != null)
							ts.sendMessage(TweetParser.parse(str));

					} catch (IllegalStateException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});
		runner.start();
	}

}
