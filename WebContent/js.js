$(document).ready(function() {
	
	// Character limit
	$('#messagebox textarea').on('input propertychange', function() {
		$('#lettersleft').html($(this).attr('maxlength') - $(this).val().length);
	});
	$('#messagebox textarea').trigger('input');
	
	// Initialize map
	new LTmap();
	
	// GeoLocation
	new GeoLocation();
	
	$("#bubble").click(function(){
		$("#messagebox").addClass('show');
		$("#messagebox").find("textarea").focus();
	});
	
	$("#back").click(function(){
		$("#messagebox").removeClass('show');
	});
	
	$(document).on('click', '.gm-style-iw a', function(e) {
		e.preventDefault();
		var win = window.open($(e.target).closest('a').attr('href'), '_blank');
		win.focus();
	});
	
});