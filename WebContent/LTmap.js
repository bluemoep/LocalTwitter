function LTmap() {

	// Singleton
	if (typeof LTmap.instance === 'object') {
		return LTmap.instance;
	}
	LTmap.instance = this;

	// Defaults
	LTmap.elementId = 'map';
	LTmap.lat = '53.147086';
	LTmap.lng = '8.180434';
	LTmap.zoom = 4;

	var firstSetLocation = true;
	var center = new google.maps.LatLng(LTmap.lat, LTmap.lng);

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
			var marker = new google.maps.Marker({
				position : new google.maps.LatLng(
						tweet.coordinates.coordinates[1],
						tweet.coordinates.coordinates[0]),
				map : map,
				title : 'Zum Öffnen klicken!',
			});

			marker.message = new google.maps.InfoWindow({
				content : $('<div />').addClass('tweet').html(new TweetParser(tweet).parse()).get(0),
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
			map.setZoom(16);
			google.maps.event.addListenerOnce(map, 'idle', function() {
				google.maps.event.addListenerOnce(map, 'tilesloaded',
						function() {
							$('#overlay .loadmap').hide();
							$('#overlay').hide();
						});
			});
		}
	};

	this.setRadius = function(){
        // Initialize circle defaults
                 var circleOptions = {
                         strokeColor: '#FF0000',
                         strokeOpacity: 0.8,
                         strokeWeight: 2,
                         //fillColor: '#FF0000',
                         fillOpacity: 0.35,
                         map: map,
                         center: map.center,
                         radius: 100
                 };
         // Add the circle to map
                 var mapCircle = new google.maps.Circle(circleOptions);
         };

	this.getGoogleMap = function() {
		return map;
	}

}
