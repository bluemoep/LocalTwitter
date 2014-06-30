package edu.oldenburg.it.bluemoep;

import java.util.LinkedList;
import java.util.List;

public class TweetSource {

	private static TweetSource instance = null;

	public static TweetSource getInstance() {
		if (instance == null)
			instance = new TweetSource();
		return instance;
	}

	private TweetSource() {
		TwitterStreaming.startStreaming();
	}

	private List<TweetReceiver> receivers = new LinkedList<TweetReceiver>();

	public void addTweetReceiver(TweetReceiver receiver) {
		receivers.add(receiver);
	}

	public void removeTweetReceiver(TweetReceiver receiver) {
		receivers.remove(receiver);
	}

	private void notifyReceivers(Message message) {
		// Notify Receivers in Range
		double lat = message.getLatitude();
		double lng = message.getLongitude();
		for (TweetReceiver receiver : receivers) {
			try {
				if (receiver.getNorth() > lat && lat > receiver.getSouth()
						&& receiver.getEast() > lng && lng > receiver.getWest()) {
					receiver.receive(message);
				}
			} catch (NullPointerException e) {
				receivers.remove(receiver);
			}
		}
	}

	public void sendMessage(Message message) {
		if(message == null)
			return;
		
		// Add Message to MessageStorage
		MessageStorage.getInstance().addMessage(message);

		// Notify Receivers about message
		notifyReceivers(message);
	}

	public List<TweetReceiver> getReceivers() {
		return receivers;
	}

	public int getReceiverCount() {
		return receivers.size();
	}

	public void fullRequest(TweetReceiver receiver) {
		double rNorth = receiver.getNorth();
		double rEast = receiver.getEast();
		double rSouth = receiver.getSouth();
		double rWest = receiver.getWest();
		List<Message> messages = MessageStorage.getInstance().getMessages(
				rNorth, rEast, rSouth, rWest);
		for (Message message : messages)
			if (rNorth > message.getLatitude()
					&& message.getLatitude() > rSouth
					&& rEast > message.getLongitude()
					&& message.getLongitude() > rEast)
				receiver.receive(message);
	}
}
