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
	
});