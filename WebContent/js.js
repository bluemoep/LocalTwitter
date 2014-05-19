google.maps.event.addDomListener(window, 'load', function() {
	var map = new LTmap();
	map.addMessage({
		lat : LTmap.lat,
		lng : LTmap.lng,
		content : 'Hallo Welt!'
	});
});