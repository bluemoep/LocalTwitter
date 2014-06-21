package edu.oldenburg.it.bluemoep;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;



@WebServlet("/MessageReceiver")
public class MessageReceiver extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3383004164508555005L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String message = request.getParameter("message");
		String lat = request.getParameter("lat");
		String lng = request.getParameter("lng");
		System.out.println("Message: " + message);
		System.out.println("Lat: " + lat);
		System.out.println("Lng: " + lng);	
		String encoded = "status=" + encode(message)+"&lat="+encode(lat)+"&long="+encode(lng);
		System.out.println(encoded);

		String oauth_consumer_key = "XiSYzq3ak6ZVrupuJDk1hh9xF";
		String oauth_consumer_secret = "EyHAg9kX47YGpRkA0brxRkJEZEs4o6zXVmSvWTnrNBVSkXvpsX";
		String oauth_token = "2558944136-WbSyJGI7iYDD47J6qtZyiwh3Mv7G7jTevqU4RkJ";
		String oauth_token_secret = "T5v4Oc3fKoddNUnCnNHkq8EQbJwUO8AvwafRKgceijLRr";

		OAuthConsumer oAuthConsumer = new CommonsHttpOAuthConsumer(oauth_consumer_key,oauth_consumer_secret);
		oAuthConsumer.setTokenWithSecret(oauth_token, oauth_token_secret);

		HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/statuses/update.json?" + encoded);

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


		// send the request
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpPost);
		
		// getting response Messages
		int statusCode = httpResponse.getStatusLine().getStatusCode();
	    System.out.println(statusCode + ":" + httpResponse.getStatusLine().getReasonPhrase());
	    System.out.println(IOUtils.toString(httpResponse.getEntity().getContent()));
	 
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

	//Methode zur encodierung von Strings gem. RFC3986
	private static String encode(String value){
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
					&& encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
				buf.append('~');
				i += 2;
			} else {
				buf.append(focus);
			}
		}
		return buf.toString();
	}
}




