$(document).ready(function (){
	function updateClock() {
		var now = new Date();

		var dateString = now.toLocaleDateString('ko-KR', {
			year: 'numeric',
			month: 'numeric',
			day: 'numeric'
		});

		var timeString = now.toLocaleTimeString('ko-KR', {
			hour12: true,
			hour: 'numeric',
			minute: '2-digit',
			second: '2-digit'
		});

		var formattedTime = dateString.replace(/\s/g, '') + ' ' + timeString;

		$('#nowTime').text(formattedTime);
	}

	setInterval(updateClock, 1000);

	updateClock();
});