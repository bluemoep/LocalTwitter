package edu.oldenburg.it.bluemoep;

public class Message {

	private String id, message;
	private double lat, lng;

	public Message(String id, double lat, double lng, String message) {
		this.id = id;
		this.lat = lat;
		this.lng = lng;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public double getLatitude() {
		return lat;
	}

	public double getLongitude() {
		return lng;
	}

	public String getMessage() {
		return message;
	}

	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(!(obj instanceof Message))
			return false;
		if(id.equals( ((Message) obj).getId() ))
			return true;
		return false;
	}
}
