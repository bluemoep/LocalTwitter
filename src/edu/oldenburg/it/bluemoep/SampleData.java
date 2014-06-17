package edu.oldenburg.it.bluemoep;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SampleData {

	public static String getTweet(double north, double east, double south,
			double west) {

		double lat = (Math.random() * (north - south)) + south;
		double lng = (Math.random() * (east - west)) + west;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE d MMM HH:mm:ss Z yyyy");
		String date = format.format(cal.getTime());
		long id = (long) (Math.random() * Long.MAX_VALUE);

		String s = "{\n" + "		\"coordinates\" : {\n"
				+ "			\"type\" : \"Point\",\n" + "			\"coordinates\" : ["
				+ lng
				+ ", "
				+ lat
				+ "]\n"
				+ "		},\n"
				+ "		\"id\": "
				+ id
				+ ",\n"
				+ "	    \"id_str\": \""
				+ id
				+ "\",\n"
				+ "	    \"text\": \"test $TWTR @twitterapi #hashtag http:\\/\\/t.co\\/p5dOtmnZyu https:\\/\\/t.co\\/ZSvIEMOPb8\",\n"
				+ "	    \"created_at\": \""
				+ date
				+ "\",\n"
				+ "	    \"entities\": {\n"
				+ "	        \"hashtags\": [{\n"
				+ "	            \"text\": \"hashtag\",\n"
				+ "	            \"indices\": [23, 31]\n"
				+ "	        }],\n"
				+ "	        \"symbols\": [{\n"
				+ "	            \"text\": \"TWTR\",\n"
				+ "	            \"indices\": [5, 10]\n"
				+ "	        }],\n"
				+ "	        \"urls\": [{\n"
				+ "	            \"url\": \"http:\\/\\/t.co\\/p5dOtmnZyu\",\n"
				+ "	            \"expanded_url\": \"http:\\/\\/dev.twitter.com\",\n"
				+ "	            \"display_url\": \"dev.twitter.com\",\n"
				+ "	            \"indices\": [32, 54]\n"
				+ "	        }, {\n"
				+ "	            \"url\": \"https:\\/\\/t.co\\/ZSvIEMOPb8\",\n"
				+ "	            \"expanded_url\": \"https:\\/\\/ton.twitter.com\\/1.1\\/ton\\/data\\/dm\\/411031503817039874\\/411031503833792512\\/cOkcq9FS.jpg\",\n"
				+ "	            \"display_url\": \"pic.twitter.com\\/ZSvIEMOPb8\",\n"
				+ "	            \"indices\": [55, 78]\n"
				+ "	        }],\n"
				+ "	        \"user_mentions\": [{\n"
				+ "	            \"screen_name\": \"twitterapi\",\n"
				+ "	            \"name\": \"Twitter API\",\n"
				+ "	            \"id\": 6253282,\n"
				+ "	            \"id_str\": \"6253282\",\n"
				+ "	            \"indices\": [11, 22]\n"
				+ "	        }],\n"
				+ "	        \"media\": [{\n"
				+ "	            \"id\": 411031503833792512,\n"
				+ "	            \"id_str\": \"411031503833792512\",\n"
				+ "	            \"indices\": [55, 78],\n"
				+ "	            \"media_url\": \"https://pbs.twimg.com/media/BEyUdcwCMAIyfRY.jpg\",\n"
				+ "	            \"media_url_https\": \"https:\\/\\/ton.twitter.com\\/1.1\\/ton\\/data\\/dm\\/411031503817039874\\/411031503833792512\\/cOkcq9FS.jpg\",\n"
				+ "	            \"url\": \"https:\\/\\/t.co\\/ZSvIEMOPb8\",\n"
				+ "	            \"display_url\": \"pic.twitter.com\\/ZSvIEMOPb8\",\n"
				+ "	            \"expanded_url\": \"https:\\/\\/ton.twitter.com\\/1.1\\/ton\\/data\\/dm\\/411031503817039874\\/411031503833792512\\/cOkcq9FS.jpg\",\n"
				+ "	            \"type\": \"photo\",\n"
				+ "	            \"sizes\": {\n"
				+ "	                \"medium\": {\n"
				+ "	                    \"w\": 600,\n"
				+ "	                    \"h\": 450,\n"
				+ "	                    \"resize\": \"fit\"\n"
				+ "	                },\n"
				+ "	                \"large\": {\n"
				+ "	                    \"w\": 1024,\n"
				+ "	                    \"h\": 768,\n"
				+ "	                    \"resize\": \"fit\"\n"
				+ "	                },\n"
				+ "	                \"thumb\": {\n"
				+ "	                    \"w\": 150,\n"
				+ "	                    \"h\": 150,\n"
				+ "	                    \"resize\": \"crop\"\n"
				+ "	                },\n"
				+ "	                \"small\": {\n"
				+ "	                    \"w\": 340,\n"
				+ "	                    \"h\": 255,\n"
				+ "	                    \"resize\": \"fit\"\n"
				+ "	                }\n"
				+ "	            }\n"
				+ "	        }]\n"
				+ "	    }\n" + "	}";
		System.out.println(s);
		return s;
	}

	private static SampleData instance = null;

	public static void startGeneration() {
		if (instance == null)
			instance = new SampleData();
	}

	private SampleData() {
		Thread runner = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int receiversSize;
					while (true) {
						receiversSize = TweetSource.getInstance()
								.getReceiverCount();
						if (receiversSize < 1)
							Thread.sleep(10000);
						else {
							sendMessage();
							Thread.sleep(10000);
						}
					}
				} catch (InterruptedException e) {
				}
			}

		});
		runner.start();
	}

	private void sendMessage() {
		try {

			// Get Receivers
			List<TweetReceiver> receivers = TweetSource.getInstance()
					.getReceivers();

			// Get Random Receiver, so a receiver in range exists
			TweetReceiver receiver = receivers
					.get((int) (receivers.size() * Math.random()));

			// Get Sample Message
			String sMessage = getTweet(receiver.getNorth(), receiver.getEast(),
					receiver.getSouth(), receiver.getWest());

			// Parse Message
			Message message = TweetParser.parse(sMessage);

			// Send Message
			TweetSource.getInstance().sendMessage(message);

		} catch (IndexOutOfBoundsException | JsonParseException e) {
		}
	}

}
