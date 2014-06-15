package edu.oldenburg.it.bluemoep;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
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
		String encoded = "status=" + URLEncoder.encode(message, "UTF-8")+"&lat="+URLEncoder.encode(lat, "UTF-8")+"&long="+URLEncoder.encode(lng, "UTF-8");
		System.out.println(encoded);

		Date current_time = new Date();
		String oauth_consumer_key = "DHo3qG8QHm0kFqmLTu2p7sHiv";
		String oauth_consumer_secret = "xSEzKKryxE3ENwEWlCMgkP5oTV73fPW90INS5TbaB833vUc713";
		String oauth_token = "2558944136-b8AhCt9CLr2jWk6u6mZo19C3QRg7Ht64P2Fom0A";
		String oauth_token_secret = "Ro9CHzMpywB8cmj0ZJTFukVypHRqanFBF4pZdhIAK58VT";
		String oauth_signature_method = "HMAC-SHA1";

		//Creating oauth_nonce
		String uuid_string = UUID.randomUUID().toString();
		uuid_string = uuid_string.replaceAll("-", "");
		String oauth_nonce = uuid_string; // any relatively random alphanumeric string will work here. I used UUID minus "-" signs

		//Creating oauth_timestamp
		String oauth_timestamp = (new Long(current_time.getTime()/1000)).toString(); // get current time in milliseconds, then divide by 1000 to get seconds

		//Parameter String to create oauth_signature
		String parameter_string ="&oauth_consumer_key=" + oauth_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + "&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + oauth_token + "&oauth_version=1.0" + "&status=" + URLEncoder.encode(message, "UTF-8");        
		System.out.println("parameter_string= " + parameter_string);

		//Creating signature_base_string
		String signature_base_string = "POST&"+ URLEncoder.encode("https://api.twitter.com/1.1/statuses/update.json", "UTF-8")+"&" + URLEncoder.encode(parameter_string, "UTF-8");
		System.out.println("signature_base_string= " + signature_base_string);

		//Create Signing Key for Signature
		String signing_key = URLEncoder.encode(oauth_consumer_secret, "UTF-8")+"&"+URLEncoder.encode(oauth_token_secret, "UTF-8");

		//Create the oauth_signature
		String oauth_signature = "";
		try {
			oauth_signature = computeSignature(signature_base_string, signing_key);
		} catch (GeneralSecurityException cse) 
		{  System.out.println("test" + cse.getMessage());  }

		System.out.println("oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8"));



		//Create authorization_header_string
		String authorization_header_string = 
				"OAuth oauth_consumer_key=\"" + URLEncoder.encode(oauth_consumer_key, "UTF-8") + "\", "
						+ "oauth_nonce=\"" + URLEncoder.encode(oauth_nonce, "UTF-8") + "\", "
						+ "oauth_signature=\"" + URLEncoder.encode(oauth_signature, "UTF-8") + "\", "
						+ "oauth_signature_method=\"HMAC-SHA1\", "
						+ "oauth_timestamp=\"" + URLEncoder.encode(oauth_timestamp, "UTF-8") + "\", "
						+ "oauth_token=\"" + URLEncoder.encode(oauth_token, "UTF-8") + "\", "
						+ "oauth_version=\"1.0\" ";

		System.out.println("authorization_header_string=" + authorization_header_string);



		//Finally the HttpRequest
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(
					"https://api.twitter.com/1.1/statuses/update.json?"+ URLEncoder.encode(message, "UTF-8"));
			httppost.setHeader("Authorization",authorization_header_string);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			System.out.println("--------------------------------------------------------");
			String responseBody = httpclient.execute(httppost, responseHandler);
			System.out.println("--------------------------------------------------------");
			System.out.println(responseBody);
			System.out.println("--------------------------------------------------------");
		}
		catch(ClientProtocolException cpe)  
		{  System.out.println(cpe.getMessage());  }
		catch(IOException ioe) 
		{  System.out.println(ioe.getMessage());  }
		finally { httpclient.getConnectionManager().shutdown();  }
	}



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

	private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {
		SecretKey secretKey = null;
		byte[] keyBytes = keyString.getBytes();
		secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);
		byte[] text = baseString.getBytes();
		return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
	}
}




