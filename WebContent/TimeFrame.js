function TimeFrame() {

	// Singleton
	if (typeof TimeFrame.instance === 'object') {
		return TimeFrame.instance;
	}
	TimeFrame.instance = this;

	var time = 1;
	
	this.showVal = function(value, onchange) {
		value = Math.round(Math.exp(value*0.000140058002844074));
		var sec = value % 60;
		var day = Math.floor(value / 60);
		var min = day % 60;
		day = Math.floor(day / 60);
		var hour = day % 24;
		day = Math.floor(day / 24);
		
		if (day > 0) {
			if (hour > 0) {
				$("#timezone").html(day+"d "+hour+"h");
			} else {
				$("#timezone").html(day+"d");
			}
		} else if (hour > 0) {
			if (min > 0) {
				$("#timezone").html(hour+"h "+min+"m");
			} else {
				$("#timezone").html(hour+"h");
			}
		} else if (min > 0) {
			if (sec > 0) {
				$("#timezone").html(min+"m "+sec+"s");
			} else {
				$("#timezone").html(min+"m");
			}
		} else {
			$("#timezone").html(sec+"s");
		}
		
		time = value;
		
		if (onchange) {
			new LTmap.update();
		}
	}
	
	this.getTime = function() {
		return time * 1000;
	}
	
	var _this = this;
	$("#slider").on("change", null, null, function() {
		_this.showVal($(this).val(), true);
	});
	
	$("#slider").on("input", null, null, function() {
		_this.showVal($(this).val(), false);
	});
	
	this.showVal($("#slider").val(), false);
}
