google.maps.event.addDomListener(window, 'load', function() {
	var map = new LTmap();
	
	$('#overlay .nojs').hide();
	$('#overlay .search').show();
	// TODO: Geolocation
	// onsuccess: $('#overlay').hide();
	// onfailure: $('#overlay .search').hide();
	// onfailure: $('#overlay .notfound').show();
	
	map.addMessage({
		lat : LTmap.lat,
		lng : LTmap.lng,
		content : 'Hallo Welt!'
	});
});