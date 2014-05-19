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
	
	// Initialize map with defaults
	var map = new google.maps.Map(document.getElementById(LTmap.elementId), {
		zoom : LTmap.zoom,
		center : new google.maps.LatLng(LTmap.lat, LTmap.lng),
		mapTypeId : google.maps.MapTypeId.HYBRID
	});
	
	this.addMessage = function(message) {
		var marker = new google.maps.Marker({
			position : new google.maps.LatLng(message.lat, message.lng),
			map : map,
			title : 'Zum Öffnen klicken!',
		});
		
		marker.message = new google.maps.InfoWindow({
			content : message.content,
		});
		
		google.maps.event.addListener(marker, 'click', function() {
			marker.message.open(map, marker);
		});
	};
	
}
