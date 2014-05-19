$(document).ready(function() {
	
	$('#messagebox textarea').on('input propertychange', function() {
	    $('#lettersleft').html($(this).attr('maxlength') - $(this).val().length);
	});
	$('#messagebox textarea').trigger('input');
	
	var map = new LTmap();
	map.addMessage({
		lat : LTmap.lat,
		lng : LTmap.lng,
		content : 'Hallo Welt!'
	});
	
	$('#overlay .nojs').hide();
	$('#overlay .search').show();
	// TODO: Geolocation
	// onsuccess: $('#overlay').hide();
	// onfailure: $('#overlay .search').hide();
	// onfailure: $('#overlay .notfound').show();
	
});