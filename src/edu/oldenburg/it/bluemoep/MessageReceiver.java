package edu.oldenburg.it.bluemoep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.ProtocolException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpRequest;

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
		String encoded = "status=" + URLEncoder.encode(message, "UTF-8")+"&lat="+URLEncoder.encode(lat, "UTF-8")+"&long="+URLEncoder.encode(lng, "UTF-8");
		System.out.println(encoded);
		
		// Authentication 
		// Can be seen at: https://dev.twitter.com/apps
		String ConsumerKey = "DHo3qG8QHm0kFqmLTu2p7sHiv" ;
		String ConsumerSecret = "xSEzKKryxE3ENwEWlCMgkP5oTV73fPW90INS5TbaB833vUc713";
		String AccessToken = "2558944136-b8AhCt9CLr2jWk6u6mZo19C3QRg7Ht64P2Fom0A";
		String AccessTokenSecret = "Ro9CHzMpywB8cmj0ZJTFukVypHRqanFBF4pZdhIAK58VT";

		//HttpURLConnection connection = null;
		OutputStreamWriter wr = null;
		BufferedReader rd = null;
		StringBuilder sb = null;
		String line = null;

        // create a consumer object and configure it with the access
        // token and token secret obtained from the service provider
        OAuthConsumer consumer = new DefaultOAuthConsumer(ConsumerKey, ConsumerSecret);
        consumer.setTokenWithSecret(AccessToken, AccessTokenSecret);

        // create an HTTP request to a protected resource
        URL url = new URL("https://api.twitter.com/1.1/statuses/update.json");
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
        
        try {
			consumer.sign(httpUrlConn);
		} catch (OAuthMessageSignerException e1) {
			// TODO Auto-generated catch block
			System.out.println("MessageSignerException");
			e1.printStackTrace();
		} catch (OAuthExpectationFailedException e1) {
			// TODO Auto-generated catch block
			System.out.println("ExpectionFailedException");
			e1.printStackTrace();
		} catch (OAuthCommunicationException e1) {
			// TODO Auto-generated catch block
			System.out.println("CommunicationException");
			e1.printStackTrace();
		}

        // send the request
        httpUrlConn.setDoOutput(true);
       // httpUrlConn.setDoInput(true);
        httpUrlConn.connect();
				
		// ServerMessage sending
		try {


			//get the output stream writer and write the output to the server
			wr = new OutputStreamWriter(httpUrlConn.getOutputStream(), "UTF-8");
			wr.write(encoded);
			wr.flush();

			//read the result from the server
			rd = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream(), "UTF-8"));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null)
			{
				sb.append(line + '\n');
			}

			System.out.println(sb.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			//close the connection, set all objects to null
			httpUrlConn.disconnect();
			rd = null;
			sb = null;
			wr = null;
			//connection = null;
		}

	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}
}


