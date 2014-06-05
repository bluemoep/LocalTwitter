package edu.oldenburg.it.bluemoep;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MessageStorage {

	private static final int PRECISION = 100;

	// Singleton
	private static MessageStorage instance = null;

	public static MessageStorage getInstance() {
		if (instance == null)
			instance = new MessageStorage();
		return instance;
	}

	private SortedMap<Integer, SortedMap<Integer, List<Message>>> storage;

	private MessageStorage() {
		storage = new TreeMap<Integer, SortedMap<Integer, List<Message>>>();
	}

	public void addMessage(Message message) {
		double lat = message.getLatitude();
		double lng = message.getLongitude();
		int slat = (int) (lat * PRECISION);
		int slng = (int) (lng * PRECISION);

		SortedMap<Integer, List<Message>> storage2 = storage.get(slat);
		if (storage2 == null) {
			storage2 = new TreeMap<Integer, List<Message>>();
			storage.put(slat, storage2);
		}

		List<Message> messages = storage2.get(slng);
		if (messages == null) {
			messages = new LinkedList<Message>();
			storage2.put(slng, messages);
		}

		if (!messages.contains(message))
			messages.add(message);
	}

	public List<Message> getMessages(double north, double east, double south,
			double west) {
		int snorth = (int) (north * PRECISION) + 1;
		int ssouth = (int) (south * PRECISION) - 1;
		int seast = (int) (east * PRECISION) + 1;
		int swest = (int) (west * PRECISION) - 1;

		List<Message> result = new LinkedList<Message>();
		SortedMap<Integer, SortedMap<Integer, List<Message>>> subset = storage
				.subMap(ssouth, snorth);
		for (SortedMap<Integer, List<Message>> storage2 : subset.values()) {
			SortedMap<Integer, List<Message>> subset2 = storage2.subMap(swest,
					seast);
			for (List<Message> messages : subset2.values())
				result.addAll(messages);
		}

		return result;
	}

}
