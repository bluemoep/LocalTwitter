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
	LTmap.markerUnread = {
		url : 'TwitterBlue.png',
		size : new google.maps.Size(43, 36),
		origin : new google.maps.Point(0, 0),
		anchor : new google.maps.Point(21, 18)
	};
	LTmap.markerRead = {
		url : 'TwitterDark.png',
		size : new google.maps.Size(43, 36),
		origin : new google.maps.Point(0, 0),
		anchor : new google.maps.Point(21, 18)
	};
	LTmap.markerSpiderfiedUnread = {
		url : 'TwitterRedBlue.png',
		size : new google.maps.Size(43, 36),
		origin : new google.maps.Point(0, 0),
		anchor : new google.maps.Point(21, 18)
	};
	LTmap.markerSpiderfiedRead = {
		url : 'TwitterRedDark.png',
		size : new google.maps.Size(43, 36),
		origin : new google.maps.Point(0, 0),
		anchor : new google.maps.Point(21, 18)
	};
	LTmap.markerShape = {
		coords : [ 0, 33, 7, 36, 22, 36, 31, 31, 38, 22, 39, 19, 39, 12, 43, 8,
				43, 0, 24, 0, 22, 1, 19, 5, 18, 8, 17, 9, 15, 7, 13, 7, 5, 1,
				3, 1, 1, 4, 1, 17, 4, 21, 4, 24, 7, 26, 5, 28, 1, 28, 0, 29 ],
		type : 'poly'
	};
	
	LTmap.soundEnabled = true;
	LTmap.audio = document.getElementById('audio');

	var firstSetLocation = true;
	var center = new google.maps.LatLng(LTmap.lat, LTmap.lng);
	var circle = null;
	var openedMarker = null;
	var onMap = {};

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
	var oms = new OverlappingMarkerSpiderfier(map, {
		nearbyDistance : 40,
		circleFootSeparation : 40,
		spiralFootSeparation : 35,
		spiralLengthFactor : 7,
		spiralLengthStart : 12
	});
	oms.addListener('click', function(marker, event) {
		marker.openclick();
	});
	oms.addListener('spiderfy', function(markers) {
		for(var key in markers)
			if(markers[key].isRead)
				markers[key].setIcon(LTmap.markerSpiderfiedRead);
			else
				markers[key].setIcon(LTmap.markerSpiderfiedUnread);
	});
	oms.addListener('unspiderfy', function(markers) {
		for(var key in markers)
			if(markers[key].isRead)
				markers[key].setIcon(LTmap.markerRead);
			else
				markers[key].setIcon(LTmap.markerUnread);
	});

	var checkAddMarker = function(tweet) {
		if (tweet.coordinates && tweet.coordinates.type
				&& tweet.coordinates.type == 'Point'
				&& tweet.coordinates.coordinates
				&& tweet.coordinates.coordinates[1]
				&& tweet.coordinates.coordinates[0]) {
			if (circle.contains(new google.maps.LatLng(
					tweet.coordinates.coordinates[1],
					tweet.coordinates.coordinates[0]))
					&& new Date(tweet.created_at) > new Date(new Date()
							- new TimeFrame().getTime())) {
				return true;
			} else
				return false;
		} else
			return false;
	};

	var removeMessage = function(id_str) {
		// Never remove open markers or markers which should not be removed
		if (onMap[id_str].marker.isOpen || checkAddMarker(onMap[id_str].tweet))
			return;
		onMap[id_str].marker.setMap(null);
		delete onMap[id_str];
	};
	
	var updateTime = function() {
		if(openedMarker == null)
			return;
		var content = openedMarker.message.content;
		$('div.tweetDateTime', content).html('vor ' + TimeFrame.parse(new Date() - new Date(openedMarker.tweet.created_at)));
	};

	this.cleanMarkers = function() {
		for ( var id_str in onMap)
			removeMessage(id_str);
	};

	this.addMessage = function(tweet) {
		// Never add markers which should not be added or markers which already
		// exist
		if (!checkAddMarker(tweet) || onMap.hasOwnProperty(tweet.id_str))
			return;

		var isRead = new MessageReadStorage().messageRead(tweet.id_str);
		var icon = isRead ? LTmap.markerRead : LTmap.markerUnread;
		var marker = new google.maps.Marker({
			position : new google.maps.LatLng(tweet.coordinates.coordinates[1],
					tweet.coordinates.coordinates[0]),
			map : map,
			title : 'Zum Öffnen klicken!',
			icon : icon,
			shape : LTmap.markerShape,
			zIndex : 1
		});
		oms.addMarker(marker);
		marker.isRead = isRead;

		marker.tweet = tweet;
		marker.message = new google.maps.InfoWindow({
			content : new TweetParser(tweet).parse(),
			maxWidth : 150
		});

		marker.isOpen = false;

		marker.closeclick = function() {
			openedMarker = null;
			marker.isOpen = false;
			// Remove from map when under certain conditions
			removeMessage(tweet.id_str);
			marker.message.close();
		};

		marker.openclick = function() {
			if (openedMarker != null)
				openedMarker.closeclick();
			marker.isOpen = true;
			openedMarker = marker;
			updateTime();
			marker.message.open(map, marker);
			marker.setIcon(LTmap.markerRead);
			new MessageReadStorage().addMessage(tweet);
			marker.isRead = true;
		};

		google.maps.event.addListener(marker.message, 'closeclick',
				marker.closeclick);

		onMap[tweet.id_str] = {
			marker : marker,
			tweet : tweet
		};

		if (!marker.isRead && openedMarker == null)
			marker.openclick();
		
		if(LTmap.soundEnabled && !isRead)
			LTmap.audio.play();
	};

	this.setLocation = function(lat, lng) {
		center = new google.maps.LatLng(lat, lng);
		if (firstSetLocation) {
			firstSetLocation = false;
			new Overlay().show('loadmap');
			map.setCenter(center);
			circle = new google.maps.Circle({
				strokeColor : '#00f',
				strokeOpacity : 0.8,
				strokeWeight : 4,
				fillColor : '#00f',
				fillOpacity : 0,
				map : map,
				center : center,
				radius : new DistanceFrame().getDistance(),
				clickable : false
			});
			circle.dot = new google.maps.Circle({
				strokeColor : '#f00',
				strokeOpacity : 0.8,
				strokeWeight : 20,
				fillColor : '#f00',
				fillOpacity : 0,
				map : map,
				center : center,
				radius : 1,
				clickable : false
			});
			map.fitBounds(circle.getBounds());
			map.setZoom(map.getZoom() + 1);
			google.maps.event.addListenerOnce(map, 'idle', function() {
				google.maps.event.addListenerOnce(map, 'tilesloaded',
						function() {
							new Overlay().hide();
						});
			});
			this.update();
		} else {
			if(google.maps.geometry.spherical.computeDistanceBetween(circle
						.getCenter(), center) > 50) {
				circle.setCenter(center);
				circle.dot.setCenter(center);
				this.update();
			}
		}
	};

	this.update = function() {
		if(circle == null)
			return;
		this.cleanMarkers();
		// Avoid Re-Requests on Twitter Streaming
		var temp = new google.maps.Circle({
			strokeOpacity : 0,
			fillOpacity : 0,
			center : circle.center,
			radius : DistanceFrame.max,
			clickable : false
		});
		var bounds = temp.getBounds();
		var northEast = bounds.getNorthEast();
		var southWest = bounds.getSouthWest();
		var boundaries = {
			north : northEast.lat(),
			east : northEast.lng(),
			south : southWest.lat(),
			west : southWest.lng(),
			radius : circle.getRadius()
		};
		new Websocket().send(boundaries);
	};
	
	this.fullRequest = function() {
		new Websocket().send({
			radius : circle.getRadius()
		});
	};
	
	this.setRadius = function(meter) {
		if(circle == null)
			return;
		circle.setRadius(meter);
	};

	this.getGoogleMap = function() {
		return map;
	};

	this.getGoogleCircle = function() {
		return circle;
	};

	setInterval(this.cleanMarkers, 1000);
	setInterval(updateTime, 1000);

}
