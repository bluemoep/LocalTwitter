package edu.oldenburg.it.bluemoep;

public interface TweetReceiver {

	public void receive(Message message);
	
	public double getNorth();
	public double getEast();
	public double getSouth();
	public double getWest();
	
	public double getLatitude();
	public double getLongitude();
	
}
