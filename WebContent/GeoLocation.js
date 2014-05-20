function GeoLocation() {

	// Singleton
	if (typeof GeoLocation.instance === 'object') {
		return GeoLocation.instance;
	}
	GeoLocation.instance = this;

	var success = function(geo) {
		var map = new LTmap();
		map.setLocation(geo.coords.latitude, geo.coords.longitude);
		$('#overlay .search').hide();
	};

	var failure = function() {
		$('#overlay').show();
		$('#overlay .search').hide();
		$('#overlay .notfound').show();
	};

	var geolocate = function() {
		navigator.geolocation.watchPosition(success, failure, {
			enableHighAccuracy : true,
			timeout : 60000,
			maximumAge : 0
		});
	};

	if (typeof (navigator.geolocation) == 'undefined')
		failure();
	else
		geolocate();

}