function DistanceFrame() {

	// Singleton
	if (typeof DistanceFrame.instance === 'object') {
		return DistanceFrame.instance;
	}
	DistanceFrame.instance = this;
	DistanceFrame.min = 100; // Meter
	DistanceFrame.max = 5000;

	var distance = 1;
	var button = null;

	this.showVal = function(value, onchange) {
		value = value / 100000 * (DistanceFrame.max - DistanceFrame.min)
				+ DistanceFrame.min;
		distance = value;
		value = Math.round(value / 100);
		var z = Math.floor(value / 10);
		value %= 10;
		button.controlText.innerHTML = '<b>Radius: ' + z + ',' + value + 'km</b>';
		
		new LTmap().setRadius(distance);
		if (onchange) {
			new LTmap().update();
		}
	};

	this.getDistance = function() {
		return distance;
	};

	var _this = this;
	var hidden = true;
	this.hide;
	this.show;
	
	var DistanceControl = function(controlDiv, map) {

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
		this.controlUI.style.textAlign = 'center';
		this.controlUI.title = 'Anzeigeradius einstellen';
		controlDiv.appendChild(this.controlUI);

		// Set CSS for the control interior
		this.controlText = document.createElement('div');
		this.controlText.style.fontFamily = 'Arial,sans-serif';
		this.controlText.style.fontSize = '12px';
		this.controlText.style.paddingLeft = '4px';
		this.controlText.style.paddingRight = '4px';
		this.controlText.innerHTML = '<b>Radius: </b>';
		this.controlUI.appendChild(this.controlText);

		// Setup the click event listeners: simply set the map to
		// Chicago
		google.maps.event.addDomListener(this.controlUI, 'click', function() {
			if (hidden) {
				_this.show();
			} else {
				_this.hide();
			}
			new TimeFrame().hide();
		});
	};

	var distanceControlDiv = document.createElement('div');
	button = new DistanceControl(distanceControlDiv, new LTmap().getGoogleMap());

	distanceControlDiv.index = 1000000;
	new LTmap().getGoogleMap().controls[google.maps.ControlPosition.TOP_RIGHT]
			.push(distanceControlDiv);

	$("#DistanceSlider").on("change", null, null, function() {
		_this.showVal($(this).val(), true);
	});

	$("#DistanceSlider").on("input", null, null, function() {
		_this.showVal($(this).val(), false);
	});
	
	this.showVal($("#DistanceSlider").val(), false);

	this.hide = function() {
		hidden = true;
		$("#DistanceSlider").hide();
		button.controlUI.style.backgroundColor = 'white';
	};
	
	this.show = function() {
		hidden = false;
		$("#DistanceSlider").show();
		button.controlUI.style.backgroundColor = 'gray';
	};
};
