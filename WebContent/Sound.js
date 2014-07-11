function Sound() {

	// Singleton
	if (typeof Sound.instance === 'object') {
		return Sound.instance;
	}
	Sound.instance = this;

	var button = null;
	var _this = this;
	
	this.setText = function(sound) {
		if (LTmap.soundEnabled)
			button.controlText.innerHTML = '<b>Sound: on</b>';
		else
			button.controlText.innerHTML = '<b>Sound: off</b>';
	};

	var TimeControl = function(controlDiv, map) {

		// Set CSS styles for the DIV containing the control
		// Setting padding to 5 px will offset the control
		// from the edge of the map
		controlDiv.style.padding = '5px';

		// Set CSS for the control border
		this.controlUI = document.createElement('div');
		this.controlUI.style.backgroundColor = 'white';
		this.controlUI.style.borderStyle = 'solid';
		this.controlUI.style.borderWidth = '2px';
		this.controlUI.style.cursor = 'pointer';
		this.controlUI.style.textAlign = 'left';
		this.controlUI.style.width = '68px';
		this.controlUI.title = 'Sound einstellen';
		controlDiv.appendChild(this.controlUI);

		// Set CSS for the control interior
		this.controlText = document.createElement('div');
		this.controlText.style.fontFamily = 'Arial,sans-serif';
		this.controlText.style.fontSize = '12px';
		this.controlText.style.paddingLeft = '4px';
		this.controlText.style.paddingRight = '4px';
		this.controlText.innerHTML = '<b>Sound: </b>';
		this.controlUI.appendChild(this.controlText);

		// Setup the click event listeners: simply set the map to
		// Chicago
		google.maps.event.addDomListener(this.controlUI, 'click', function() {
			LTmap.soundEnabled = !LTmap.soundEnabled;
			_this.setText(LTmap.soundEnabled);
		});
	};

	var timeControlDiv = document.createElement('div');
	button = new TimeControl(timeControlDiv, new LTmap().getGoogleMap());

	timeControlDiv.index = 1000000;
	new LTmap().getGoogleMap().controls[google.maps.ControlPosition.TOP_RIGHT]
			.push(timeControlDiv);
	
	this.setText(LTmap.soundEnabled);
};
