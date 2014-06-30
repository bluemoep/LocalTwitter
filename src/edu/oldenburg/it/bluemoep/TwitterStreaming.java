package edu.oldenburg.it.bluemoep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
					String encoded = "locations=" + encode("-180,-90,180,90");
					String oauth_consumer_key = "XiSYzq3ak6ZVrupuJDk1hh9xF";
					String oauth_consumer_secret = "EyHAg9kX47YGpRkA0brxRkJEZEs4o6zXVmSvWTnrNBVSkXvpsX";
					String oauth_token = "2558944136-WbSyJGI7iYDD47J6qtZyiwh3Mv7G7jTevqU4RkJ";
					String oauth_token_secret = "T5v4Oc3fKoddNUnCnNHkq8EQbJwUO8AvwafRKgceijLRr";

					OAuthConsumer oAuthConsumer = new CommonsHttpOAuthConsumer(
							oauth_consumer_key, oauth_consumer_secret);
					oAuthConsumer.setTokenWithSecret(oauth_token,
							oauth_token_secret);

					HttpPost httpPost = new HttpPost(
							"https://stream.twitter.com/1.1/statuses/filter.json?"
									+ encoded);

					try {
						oAuthConsumer.sign(httpPost);
					} catch (OAuthMessageSignerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthExpectationFailedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthCommunicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						// send the request
						HttpClient httpClient = new DefaultHttpClient();
						HttpResponse httpResponse;
						httpResponse = httpClient.execute(httpPost);

						// getting response Messages
						int statusCode = httpResponse.getStatusLine()
								.getStatusCode();
						System.out.println(statusCode
								+ ":"
								+ httpResponse.getStatusLine()
										.getReasonPhrase());
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(httpResponse.getEntity()
										.getContent()));

						TweetSource ts = TweetSource.getInstance();
						String str;
						while ((str = reader.readLine()) != null)
							ts.sendMessage(TweetParser.parse(str));

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});
		runner.start();
	}

	// Methode zur encodierung von Strings gem. RFC3986
	private static String encode(String value) {
		String encoded = null;
		try {
			encoded = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException ignore) {
		}
		StringBuilder buf = new StringBuilder(encoded.length());
		char focus;
		for (int i = 0; i < encoded.length(); i++) {
			focus = encoded.charAt(i);
			if (focus == '*') {
				buf.append("%2A");
			} else if (focus == '+') {
				buf.append("%20");
			} else if (focus == '%' && (i + 1) < encoded.length()
					&& encoded.charAt(i + 1) == '7'
					&& encoded.charAt(i + 2) == 'E') {
				buf.append('~');
				i += 2;
			} else {
				buf.append(focus);
			}
		}
		return buf.toString();
	}

}
