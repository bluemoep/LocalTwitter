$(document).ready(function() {
	
	// Character limit
	$('#messagebox textarea').on('input propertychange', function() {
		$('#lettersleft').html($(this).attr('maxlength') - $(this).val().length);
	});
	$('#messagebox textarea').trigger('input');
	
	// Initialize map
	var map = new LTmap();
	map.addMessage({
		lat : LTmap.lat,
		lng : LTmap.lng,
		content : 'Hallo Welt!'
	});
	
	// Overlay stuff
	$('#overlay .nojs').hide();
	$('#overlay .search').show();
	
	// GeoLocation
	new GeoLocation();
	
	$("#bubble").click(function(){
		$("#messagebox").show();
		$("#messagebox").find("textarea").focus();
	});
	
	$("#back").click(function(){
		$("#messagebox").hide();
	});
});

//Radius in Metern
function checkRadius(messageWidth, messageLength, positionWidth, positionLength, radius) {
	var deltaWidth = messageWidth - positionWidth;
	var deltaLength = messageLength - positionLength;
	var d = deltaWidth / (Math.cos(Math.atan(deltaLength / deltaWidth * Math.cos(messageWidth))));
	return d * 1853 <= radius;
}