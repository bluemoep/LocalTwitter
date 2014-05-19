var locationElement;

function getUserLocation() {
	locationElement = document.getElementById("loc");
	
	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(displayLocation, error);
	} else {
		error('Geolocation not supported');
	}
}

function displayLocation(userPosition) {
	// navigator.geolocation.getCurrentPosition(function(position) { var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
	var lat = userPosition.coords.latitude;
	var long = userPosition.coords.longitude;
}

function error(geolocationError) {
	switch(geolocationError.code) {
		case geolocationError.PERMISSION_DENIED:
		     	locationElement.innerHTML="User permission denied!";
			break;
		case geolocationError.POSITION_UNAVAILABLE: 
			locationElement.innerHTML="Position not available!";
			break;
		case geolocationError.TIMEOUT: 
			locationElement.innerHTML="Location retrieval timed out!";
			break;
		case geolocationError.UNKNOWN_ERROR:
			locationElement.innerHTML="Unknown error";
			break;
		default: 
			locationElement.innerHTML="Unknown error";
			break;  
	}
}