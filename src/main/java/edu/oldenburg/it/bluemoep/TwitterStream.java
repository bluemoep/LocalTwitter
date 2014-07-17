package edu.oldenburg.it.bluemoep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

public class TwitterStream {

	private volatile boolean run = true;

	public TwitterStream(final double north, final double east,
			final double south, final double west, final TweetReceiver receiver) {
		Thread runner = new Thread(new Runnable() {

			@Override
			public void run() {
				while (run) {
					TwitterRequest tr = new TwitterRequest(
							TwitterRequest.Method.POST,
							"https://stream.twitter.com/1.1/statuses/filter.json");
					tr.addParameter("locations", west + "," + south + ","
							+ east + "," + north);
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

						String str;
						while ((str = reader.readLine()) != null && run)
							receiver.receive(TweetParser.parse(str));

					} catch (IllegalStateException | IOException e) {
						e.printStackTrace();
					} catch (NullPointerException e) {
						// TODO: Close connection
						break;
					}

					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						break;
					}
				}
				System.out.println("Stream stopped");
			}

		});
		runner.start();
	}

	public void stop() {
		run = false;
	}

}
