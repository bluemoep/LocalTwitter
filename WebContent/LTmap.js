function LTmap() {

	// Singleton
	if (typeof LTmap.instance === 'object') {
		return LTmap.instance;
	}
	LTmap.instance = this;

	google.maps.Circle.prototype.contains = function(latLng) {
		return this.getBounds().contains(latLng)
				&& google.maps.geometry.spherical.computeDistanceBetween(this
						.getCenter(), latLng) <= this.getRadius();
	};

	// Defaults
	LTmap.elementId = 'map';
	LTmap.lat = '53.147086';
	LTmap.lng = '8.180434';
	LTmap.zoom = 4;

	var firstSetLocation = true;
	var center = new google.maps.LatLng(LTmap.lat, LTmap.lng);
	var circle = null;

	// Initialize map with defaults
	var map = new google.maps.Map(document.getElementById(LTmap.elementId), {
		zoom : LTmap.zoom,
		center : new google.maps.LatLng(LTmap.lat, LTmap.lng),
		mapTypeId : google.maps.MapTypeId.HYBRID,
		minZoom : 4,
		maxZoom : 18,
		zoomControlOptions : {
			position : google.maps.ControlPosition.RIGHT_TOP,
		},
		streetViewControlOptions : {
			position : google.maps.ControlPosition.RIGHT_TOP,
		},
		panControlOptions : {
			position : google.maps.ControlPosition.RIGHT_TOP,
		},
	});

	this.addMessage = function(tweet) {
		if (tweet.coordinates && tweet.coordinates.type
				&& tweet.coordinates.type == 'Point'
				&& tweet.coordinates.coordinates
				&& tweet.coordinates.coordinates[1]
				&& tweet.coordinates.coordinates[0]) {

			var position = new google.maps.LatLng(
					tweet.coordinates.coordinates[1],
					tweet.coordinates.coordinates[0]);

			if (!circle.contains(position))
				return;

			var marker = new google.maps.Marker({
				position : position,
				map : map,
				title : 'Zum Öffnen klicken!',
			});

			marker.message = new google.maps.InfoWindow({
				content : $('<div />').addClass('tweet').html(
						new TweetParser(tweet).parse()).get(0),
				maxWidth : 150
			});

			google.maps.event.addListener(marker, 'click', function() {
				marker.message.open(map, marker);
			});

		}
	};

	this.setLocation = function(lat, lng) {
		center = new google.maps.LatLng(lat, lng);
		if (firstSetLocation) {
			firstSetLocation = false;
			$('#overlay .loadmap').show();
			map.setCenter(center);
			circle = new google.maps.Circle({
				strokeColor : '#00f',
				strokeOpacity : 0.8,
				strokeWeight : 4,
				fillColor : '#00f',
				fillOpacity : 0,
				map : map,
				center : center,
				radius : 2000
			});
			circle.dot = new google.maps.Circle({
				strokeColor : '#f00',
				strokeOpacity : 0.8,
				strokeWeight : 20,
				fillColor : '#f00',
				fillOpacity : 0,
				map : map,
				center : center,
				radius : 0
			});
			map.fitBounds(circle.getBounds());
			map.setZoom(map.getZoom() + 1);
			google.maps.event.addListenerOnce(map, 'idle', function() {
				google.maps.event.addListenerOnce(map, 'tilesloaded',
						function() {
							$('#overlay .loadmap').hide();
							$('#overlay').hide();
						});
			});
			
			this.addMessage({
				coordinates : {
					type : 'Point',
					coordinates : [LTmap.lng, LTmap.lat]
				},
				"id": 411031503817039874,
			    "id_str": "411031503817039874",
			    "text": "test $TWTR @twitterapi #hashtag http:\/\/t.co\/p5dOtmnZyu https:\/\/t.co\/ZSvIEMOPb8",
			    "created_at": "Thu Dec 12 07:15:21 +0000 2013",
			    "entities": {
			        "hashtags": [{
			            "text": "hashtag",
			            "indices": [23, 31]
			        }],
			        "symbols": [{
			            "text": "TWTR",
			            "indices": [5, 10]
			        }],
			        "urls": [{
			            "url": "http:\/\/t.co\/p5dOtmnZyu",
			            "expanded_url": "http:\/\/dev.twitter.com",
			            "display_url": "dev.twitter.com",
			            "indices": [32, 54]
			        }, {
			            "url": "https:\/\/t.co\/ZSvIEMOPb8",
			            "expanded_url": "https:\/\/ton.twitter.com\/1.1\/ton\/data\/dm\/411031503817039874\/411031503833792512\/cOkcq9FS.jpg",
			            "display_url": "pic.twitter.com\/ZSvIEMOPb8",
			            "indices": [55, 78]
			        }],
			        "user_mentions": [{
			            "screen_name": "twitterapi",
			            "name": "Twitter API",
			            "id": 6253282,
			            "id_str": "6253282",
			            "indices": [11, 22]
			        }],
			        "media": [{
			            "id": 411031503833792512,
			            "id_str": "411031503833792512",
			            "indices": [55, 78],
			            "media_url": "https://pbs.twimg.com/media/BEyUdcwCMAIyfRY.jpg",
			            "media_url_https": "https:\/\/ton.twitter.com\/1.1\/ton\/data\/dm\/411031503817039874\/411031503833792512\/cOkcq9FS.jpg",
			            "url": "https:\/\/t.co\/ZSvIEMOPb8",
			            "display_url": "pic.twitter.com\/ZSvIEMOPb8",
			            "expanded_url": "https:\/\/ton.twitter.com\/1.1\/ton\/data\/dm\/411031503817039874\/411031503833792512\/cOkcq9FS.jpg",
			            "type": "photo",
			            "sizes": {
			                "medium": {
			                    "w": 600,
			                    "h": 450,
			                    "resize": "fit"
			                },
			                "large": {
			                    "w": 1024,
			                    "h": 768,
			                    "resize": "fit"
			                },
			                "thumb": {
			                    "w": 150,
			                    "h": 150,
			                    "resize": "crop"
			                },
			                "small": {
			                    "w": 340,
			                    "h": 255,
			                    "resize": "fit"
			                }
			            }
			        }]
			    }
			});
			
		} else {
			circle.setCenter(center);
			circle.dot.setCenter(center);
		}
	};

	this.getGoogleMap = function() {
		return map;
	};

}
