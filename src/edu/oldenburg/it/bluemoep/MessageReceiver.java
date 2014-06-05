package edu.oldenburg.it.bluemoep;

//import javax.json.Json;
//import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MessageReceiver extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//

	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub


		//	response.setContentType("text/html");
		//	PrintWriter out = response.getWriter();

		String username = "";
		String message = request.getParameter("message");
		String lat = request.getParameter("lat");
		String lng = request.getParameter("lng");
		String encoded;
		System.out.println("Message: " + message);
		System.out.println("Lat: " + lat);
		System.out.println("Lng: " + lng);


		//	JsonObject value = Json.createObjectBuilder()
		//		     .add("status", message)
		//		     .add("lat", lat)
		//		     .add("long", lng)
		//		     .build();

		//	URL url = new URL("https://api.twitter.com/1.1/statuses/update.json");
			encoded = "status=" + URLEncoder.encode(message, "UTF-8")+"&lat="+URLEncoder.encode(lat, "UTF-8")+"&long="+URLEncoder.encode(lng, "UTF-8");
		System.out.println(encoded);
		//	URLConnection con = url.openConnection();
		//	HttpURLConnection con = url.openConnection()
		//	con.setDoOutput(true);
		//
		//	con.connect();
		//	con.getOutputStream();
		//	
		//	OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
		//	writer.write(encoded);
		//	writer.flush();
		//	writer.close();
		//	con.disconnect();
		//	}
		
		
		HttpURLConnection connection = null;
		OutputStreamWriter wr = null;
		BufferedReader rd  = null;
		StringBuilder sb = null;
		String line = null;

		URL serverAddress = null;

		try {
			serverAddress = new URL("https://api.twitter.com/1.1/statuses/update.json");
			//set up out communications stuff
			connection = null;

			//Set up the initial connection
			connection = (HttpURLConnection)serverAddress.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(10000);

			connection.connect();

			//get the output stream writer and write the output to the server
			wr = new OutputStreamWriter(connection.getOutputStream());
			wr.write(encoded);
			wr.flush();

			//read the result from the server
			rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
			connection.disconnect();
			rd = null;
			sb = null;
			wr = null;
			connection = null;
		}
	}
}

