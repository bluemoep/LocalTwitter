package edu.oldenburg.it.bluemoep;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class TwitterRequest {

	protected final String oauth_consumer_key = "XiSYzq3ak6ZVrupuJDk1hh9xF";
	protected final String oauth_consumer_secret = "EyHAg9kX47YGpRkA0brxRkJEZEs4o6zXVmSvWTnrNBVSkXvpsX";
	protected final String oauth_token = "2558944136-WbSyJGI7iYDD47J6qtZyiwh3Mv7G7jTevqU4RkJ";
	protected final String oauth_token_secret = "T5v4Oc3fKoddNUnCnNHkq8EQbJwUO8AvwafRKgceijLRr";

	protected OAuthConsumer oAuthConsumer;

	protected HashMap<String, String> parameters;
	protected String url;

	public TwitterRequest(String url) {
		oAuthConsumer = new CommonsHttpOAuthConsumer(oauth_consumer_key,
				oauth_consumer_secret);
		oAuthConsumer.setTokenWithSecret(oauth_token, oauth_token_secret);
		parameters = new HashMap<String, String>();
		this.url = url;
	}

	public void addParameter(String key, String value) {
		parameters.put(key, value);
	}

	public HttpResponse doRequest() {
		HttpPost httpPost = new HttpPost(url + "?" + getParameterString());

		try {
			oAuthConsumer.sign(httpPost);
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException
				| OAuthCommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// send the request
		HttpClient httpClient = new DefaultHttpClient();
		try {
			return httpClient.execute(httpPost);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String getParameterString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, String>> iterator = parameters.entrySet()
				.iterator();
		Entry<String, String> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();
			sb.append(entry.getKey()).append(encode(entry.getValue()));
		}
		return sb.toString();
	}

	// Methode zur encodierung von Strings gem. RFC3986
	private String encode(String value) {
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
