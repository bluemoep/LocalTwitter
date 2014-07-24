function Hotkeys() {

	// Singleton
	if (typeof Hotkeys.instance === 'object') {
		return Hotkeys.instance;
	}
	Hotkeys.instance = this;
	
	$(document).keydown(function(e) {
		if(e.ctrlKey) {
			if(e.keyCode == 13) {
				// Strg+Enter
				e.preventDefault();
				$('#form').submit();
			} else if(e.keyCode == 77) {
				// Strg+M
				e.preventDefault();
				$('#form textarea').focus();
			}
		}
	});
	
}