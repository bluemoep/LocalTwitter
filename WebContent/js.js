$(document).ready(function() {
	
	// Character limit
	$('#messagebox textarea').on('input propertychange', function() {
		$('#lettersleft').html($(this).attr('maxlength') - $(this).val().length);
	});
	$('#messagebox textarea').trigger('input');
	
	// Initialize map
	var map = new LTmap();
	map.addMessage({
		coordinates : {
			type : 'Point',
			coordinates : [LTmap.lng, LTmap.lat]
		},
		"id": 411031503817039874,
	    "id_str": "411031503817039874",
	    "text": "test $TWTR @twitterapi #hashtag http:\/\/t.co\/p5dOtmnZyu https:\/\/t.co\/ZSvIEMOPb8",
	    "created_at": "Thu Dec 12 07:15:21 +0000 2013",
	    "entities": {
	        "hashtags": [{
	            "text": "hashtag",
	            "indices": [23, 31]
	        }],
	        "symbols": [{
	            "text": "TWTR",
	            "indices": [5, 10]
	        }],
	        "urls": [{
	            "url": "http:\/\/t.co\/p5dOtmnZyu",
	            "expanded_url": "http:\/\/dev.twitter.com",
	            "display_url": "dev.twitter.com",
	            "indices": [32, 54]
	        }, {
	            "url": "https:\/\/t.co\/ZSvIEMOPb8",
	            "expanded_url": "https:\/\/ton.twitter.com\/1.1\/ton\/data\/dm\/411031503817039874\/411031503833792512\/cOkcq9FS.jpg",
	            "display_url": "pic.twitter.com\/ZSvIEMOPb8",
	            "indices": [55, 78]
	        }],
	        "user_mentions": [{
	            "screen_name": "twitterapi",
	            "name": "Twitter API",
	            "id": 6253282,
	            "id_str": "6253282",
	            "indices": [11, 22]
	        }],
	        "media": [{
	            "id": 411031503833792512,
	            "id_str": "411031503833792512",
	            "indices": [55, 78],
	            "media_url": "https://pbs.twimg.com/media/BEyUdcwCMAIyfRY.jpg",
	            "media_url_https": "https:\/\/ton.twitter.com\/1.1\/ton\/data\/dm\/411031503817039874\/411031503833792512\/cOkcq9FS.jpg",
	            "url": "https:\/\/t.co\/ZSvIEMOPb8",
	            "display_url": "pic.twitter.com\/ZSvIEMOPb8",
	            "expanded_url": "https:\/\/ton.twitter.com\/1.1\/ton\/data\/dm\/411031503817039874\/411031503833792512\/cOkcq9FS.jpg",
	            "type": "photo",
	            "sizes": {
	                "medium": {
	                    "w": 600,
	                    "h": 450,
	                    "resize": "fit"
	                },
	                "large": {
	                    "w": 1024,
	                    "h": 768,
	                    "resize": "fit"
	                },
	                "thumb": {
	                    "w": 150,
	                    "h": 150,
	                    "resize": "crop"
	                },
	                "small": {
	                    "w": 340,
	                    "h": 255,
	                    "resize": "fit"
	                }
	            }
	        }]
	    }
	});
	
	// Overlay stuff
	$('#overlay .nojs').hide();
	$('#overlay .search').show();
	
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

//Radius in Metern
function checkRadius(messageWidth, messageLength, positionWidth, positionLength, radius) {
	var deltaWidth = messageWidth - positionWidth;
	var deltaLength = messageLength - positionLength;
	var d = deltaWidth / (Math.cos(Math.atan(deltaLength / deltaWidth * Math.cos(messageWidth))));
	return d * 1853 <= radius;
}