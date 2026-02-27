$(document).ready(function(){
	$("#btnLogin").on("click", function(){
		login();
	});

	$("#btnLogout").on("click", function(){
		logout();
	});

	$("#mLogin").on("click", function(){
		login();
	});

	$("#mLogout").on("click", function(){
		logout();
	});

});

function login() {
	var param = {
		"email": "kwpark@neurolines.net",
		"password": "123456"
	};

	$.ajax({
		type: 'POST',
		url: '/auth/login',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		async: true, //동기처리 (기본이 true)
		success: function (data) {

			alert("로그인되었습니다.");
			window.location.reload();

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	})
}

function logout() {
	var param = {
		"email": "kwpark@neurolines.net",
		"password": "123456"
	};

	$.ajax({
		type: 'POST',
		url: '/auth/logout',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		async: true, //동기처리 (기본이 true)
		success: function (data) {

			alert("로그아웃되었습니다.");
			window.location.reload();

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	})
}