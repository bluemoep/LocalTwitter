function TimeFrame() {

	// Singleton
	if (typeof TimeFrame.instance === 'object') {
		return TimeFrame.instance;
	}
	TimeFrame.instance = this;

	var time = 1;
	var button = null;
	
	this.showVal = function(value, onchange) {
//		value = Math.round(Math.exp(value*0.000140058002844074));
//		var z = value;
//		value = Math.round(Math.exp(value*0.0001396356066709325330678666368668732889951811875578));//0.0001400577630925227900127158222498938463700531047550));
//		value += 29 + Math.floor(z/2);
		value = Math.round(value*value*value*0.00000000120957 + 30.0);
		var sec = value % 60;
		var day = Math.floor(value / 60);
		var min = day % 60;
		day = Math.floor(day / 60);
		var hour = day % 24;
		day = Math.floor(day / 24);
		
		if (day > 0) {
			if (hour > 0) {
				button.controlText.innerHTML = "<b>Zeitfilter: " + day+"d "+hour+"h</b>";
			} else {
				button.controlText.innerHTML = "<b>Zeitfilter: " + day+"d</b>";
			}
		} else if (hour > 0) {
			if (min > 0) {
				button.controlText.innerHTML = "<b>Zeitfilter: " + hour+"h "+min+"m</b>";
			} else {
				button.controlText.innerHTML = "<b>Zeitfilter: " + hour+"h</b>";
			}
		} else if (min > 0) {
			if (sec > 0) {
				button.controlText.innerHTML = "<b>Zeitfilter: " + min+"m "+sec+"s</b>";
			} else {
				button.controlText.innerHTML = "<b>Zeitfilter: " + min+"m</b>";
			}
		} else {
			button.controlText.innerHTML = "<b>Zeitfilter: " + sec+"s</b>";
		}
		
		time = value;
		
		if (onchange) {
			new LTmap().update();
		}
	};
	
	this.getTime = function() {
		return time * 1000;
	};
	
	var TimeControl = function(controlDiv, map) {

		// Set CSS styles for the DIV containing the control
		// Setting padding to 5 px will offset the control
		// from the edge of the map
		controlDiv.style.padding = '5px';

		// Set CSS for the control border
		var controlUI = document.createElement('div');
		controlUI.style.backgroundColor = 'white';
		controlUI.style.borderStyle = 'solid';
		controlUI.style.borderWidth = '2px';
		controlUI.style.cursor = 'pointer';
		controlUI.style.textAlign = 'center';
		controlUI.title = 'Anzeigeradius einstellen';
		controlDiv.appendChild(controlUI);

		// Set CSS for the control interior
		this.controlText = document.createElement('div');
		this.controlText.style.fontFamily = 'Arial,sans-serif';
		this.controlText.style.fontSize = '12px';
		this.controlText.style.paddingLeft = '4px';
		this.controlText.style.paddingRight = '4px';
		this.controlText.innerHTML = '<b>Zeitfenster: </b>';
		controlUI.appendChild(this.controlText);

		// Setup the click event listeners: simply set the map to
		// Chicago
		google.maps.event.addDomListener(controlUI, 'click', function() {
			$("#TimeSlider").toggle();
			new DistanceFrame().getSlider().hide();
		});
	};
	
	var timeControlDiv = document.createElement('div');
	button = new TimeControl(timeControlDiv, new LTmap().getGoogleMap());

	timeControlDiv.index = 1000000;
	new LTmap().getGoogleMap().controls[google.maps.ControlPosition.TOP_RIGHT]
			.push(timeControlDiv);

	var _this = this;
	$("#TimeSlider").on("change", null, null, function() {
		_this.showVal($(this).val(), true);
	});
	
	$("#TimeSlider").on("input", null, null, function() {
		_this.showVal($(this).val(), false);
	});
	
	this.showVal($("#TimeSlider").val(), false);

	this.getSlider = function() {
		return $("#TimeSlider");
	};
}
