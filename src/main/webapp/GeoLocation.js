function GeoLocation() {

	// Singleton
	if (typeof GeoLocation.instance === 'object') {
		return GeoLocation.instance;
	}
	GeoLocation.instance = this;
	
	var lat = 0;
	var lng = 0;
	var accuracy = 100;
	var oldGeo = null;

	var success = function(geo) {
		oldGeo = geo;
		$('#overlay .search').hide();
		if(geo.coords.accuracy > accuracy)
			new Overlay().show('accuracyProblem');
		else {
			new Overlay().hide();
			var map = new LTmap();
			lat = geo.coords.latitude;
			lng = geo.coords.longitude;
			map.setLocation(lat, lng);
		}
	};

	var failure = function() {
		new Overlay().show('notfound');
	};

	var geolocate = function() {
		navigator.geolocation.watchPosition(success, failure, {
			enableHighAccuracy : true,
			timeout : 60000,
			maximumAge : 0
		});
	};
	
	this.setAccuracy = function(acc) {
		accuracy = acc;
		success(oldGeo);
	};

	new Overlay().show('search');
	if (typeof (navigator.geolocation) == 'undefined')
		failure();
	else
		geolocate();
	
	this.getLat = function() {
		return lat;
	};
	
	this.getLng = function() {
		return lng;
	};
}