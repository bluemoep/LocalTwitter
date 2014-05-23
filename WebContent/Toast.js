function Toast(message) {
	
	this.toast = $('<div />').addClass('toast').html(message);
	$('#toasts').prepend(this.toast);
	
	var _toast = this.toast;
	$(_toast).fadeIn('slow', function() {
		setTimeout(function() {
			$(_toast).fadeOut('slow', function() {
				$(_toast).remove();
			});
		}, 3000);
	});
	
}