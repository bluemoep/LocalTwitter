function Overlay() {
	
	// Singleton
	if (typeof Overlay.instance === 'object') {
		return Overlay.instance;
	}
	Overlay.instance = this;
	
	this.show = function(element) {
		var $all = $('#overlay div div');
		$all.filter(function() {
			return !$(this).hasClass(element);
		}).hide();
		$all.filter(function() {
			return $(this).hasClass(element);
		}).show();
		$('#overlay').show();
	};
	
	this.hide = function() {
		$('#overlay').hide();
	};
	
}