function GeoLocation() {

	// Singleton
	if (typeof GeoLocation.instance === 'object') {
		return GeoLocation.instance;
	}
	GeoLocation.instance = this;

	var success = function(geo) {
		$('#overlay .search').hide();
		if(geo.coords.accuracy > 100)
			new Overlay().show('accuracyProblem');
		else {
			new Overlay().hide();
			var map = new LTmap();
			map.setLocation(geo.coords.latitude, geo.coords.longitude);
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

	new Overlay().show('search');
	if (typeof (navigator.geolocation) == 'undefined')
		failure();
	else
		geolocate();

}