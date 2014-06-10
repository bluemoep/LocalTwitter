package edu.oldenburg.it.bluemoep;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}
}

