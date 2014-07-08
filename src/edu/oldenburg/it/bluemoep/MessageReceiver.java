package edu.oldenburg.it.bluemoep;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;



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

		TwitterRequest tr = new TwitterRequest("https://api.twitter.com/1.1/statuses/update.json");
		tr.addParameter("status", message);
		tr.addParameter("lat", lat);
		tr.addParameter("long", lng);

		HttpResponse httpResponse = tr.doRequest();
		
		// getting response Messages
		int statusCode = httpResponse.getStatusLine().getStatusCode();
	    System.out.println(statusCode + ":" + httpResponse.getStatusLine().getReasonPhrase());
	    String str = IOUtils.toString(httpResponse.getEntity().getContent());
	    System.out.println(str);
	 
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

}




