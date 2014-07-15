function TimeFrame() {

	// Singleton
	if (typeof TimeFrame.instance === 'object') {
		return TimeFrame.instance;
	}
	TimeFrame.instance = this;

	var time = 1;
	var button = null;
	var timeout = null;
	var _this = this;

	TimeFrame.parse = function(value) {
		value = Math.floor(value / 1000);
		var sec = value % 60;
		var day = Math.floor(value / 60);
		var min = day % 60;
		day = Math.floor(day / 60);
		var hour = day % 24;
		day = Math.floor(day / 24);

		if (day > 0) {
			if (hour > 0) {
				return day + "d " + hour + "h";
			} else {
				return day + "d";
			}
		} else if (hour > 0) {
			if (min > 0) {
				return hour + "h " + min + "m";
			} else {
				return hour + "h";
			}
		} else if (min > 0) {
			if (sec > 0) {
				return min + "m " + sec + "s";
			} else {
				return min + "m";
			}
		} else {
			return sec + "s";
		}
	};

	this.showVal = function(value, onchange) {
		clearTimeout(timeout);

		// value = Math.round(Math.exp(value*0.000140058002844074));
		// var z = value;
		// value =
		// Math.round(Math.exp(value*0.0001396356066709325330678666368668732889951811875578));//0.0001400577630925227900127158222498938463700531047550));
		// value += 29 + Math.floor(z/2);
		value = Math.round(value * value * value * 0.00000000120957 + 30.0);
		button.controlText.innerHTML = "<b>Zeitfilter: " + TimeFrame.parse(value*1000) + "</b>";
		time = value;

		if (onchange) {
			new LTmap().fullRequest();
			timeout = setTimeout(function() {
				_this.fadeout();
			}, 3000);
		}
	};

	this.getTime = function() {
		return time * 1000;
	};

	var hidden = true;
	this.hide;
	this.show;

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
		this.controlUI.style.textAlign = 'center';
		this.controlUI.title = 'Zeitfilter einstellen';
		controlDiv.appendChild(this.controlUI);

		// Set CSS for the control interior
		this.controlText = document.createElement('div');
		this.controlText.style.fontFamily = 'Arial,sans-serif';
		this.controlText.style.fontSize = '12px';
		this.controlText.style.paddingLeft = '4px';
		this.controlText.style.paddingRight = '4px';
		this.controlText.innerHTML = '<b>Zeitfenster: </b>';
		this.controlUI.appendChild(this.controlText);

		// Setup the click event listeners: simply set the map to
		// Chicago
		google.maps.event.addDomListener(this.controlUI, 'click', function() {
			if (hidden) {
				_this.show();
			} else {
				_this.hide();
			}
			new DistanceFrame().hide();
		});
	};

	var timeControlDiv = document.createElement('div');
	button = new TimeControl(timeControlDiv, new LTmap().getGoogleMap());

	timeControlDiv.index = 1000000;
	new LTmap().getGoogleMap().controls[google.maps.ControlPosition.TOP_RIGHT]
			.push(timeControlDiv);

	$("#TimeSlider").on("change", null, null, function() {
		_this.showVal($(this).val(), true);
	});

	$("#TimeSlider").on("input", null, null, function() {
		_this.showVal($(this).val(), false);
	});

	this.showVal($("#TimeSlider").val(), false);

	this.hide = function() {
		hidden = true;
		$("#TimeSlider").hide();
		button.controlUI.style.backgroundColor = 'white';
	};

	this.fadeout = function() {
		hidden = true;
		$("#TimeSlider").fadeOut('slow');
		button.controlUI.style.backgroundColor = 'white';
	};

	this.show = function() {
		hidden = false;
		$("#TimeSlider").show();
		button.controlUI.style.backgroundColor = 'gray';
		timeout = setTimeout(function() {
			_this.fadeout();
		}, 3000);
	};
};
