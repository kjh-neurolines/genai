$(document).ready(function(){
	fnGetDepartmentPrompt();
	fnGetChatHistory();
	fnSetDepartDefault();

	$("#btnConfirmDelete").on("click", function(){
		fnDeleteSession();
	})

	$("#btnConfirmEdit").on("click", function(){
		fnModifySession();
	})
});

var fnGetDepartmentPrompt = function(){
	var param = {};

	$.ajax({
		type: 'GET',
		url: '/api/chat/dm/prompt',
/*		data: JSON.stringify(param),*/
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			var mainHtml = "";
			var quickHtml = "";

			if(result.length > 0) {
				result.forEach(function (d, idx) {

					if(idx < 4)
						mainHtml += "<a class=\"faq-card\" onclick=\"fnSetQuestion('" + d.prompt +"')\">" +
						"<span class=\"ico ico-report\"></span>" +
						"<h3 class=\"faq-card-title\">" + d.prompt + "</h3>" +
						"</a>";

					quickHtml += "<a class=\"faq-card faq-card-type\" onclick=\"fnSetQuickQuestion('" + d.prompt + "')\">" +
						"<span class=\"ico ico-report\"></span>" +
						"<h3 class=\"faq-card-title\">" + d.prompt + "</h3>" +
						"</a>";

				});
			}

			$("#quickArea").html(quickHtml);
			$("#main-quick-area").html(mainHtml);
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnGetChatHistory = function(){
	var param = {};

	$.ajax({
		type: 'GET',
		url: '/api/chat/history',
		/*		data: JSON.stringify(param),*/
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			var quickHtml = "";

			if(result.length > 0) {
				result.forEach(function (d, idx) {

					if(idx < 20)
					quickHtml += "<a class=\"faq-card faq-card-type\" onclick=\"fnSetHistoryPrompt('" + d.session + "')\">" +
						"<span class=\"ico ico-question\"></span>" +
						"<h3 class=\"faq-card-title\">" + d.prompt + "</h3>" +
						"</a>";
				});
			}

			$("#quickHistoryArea").html(quickHtml);
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});

}

var fnSetQuestion = function (prompt)
{
	$("#newPrompt").val(prompt);
}

var fnSetQuickQuestion = function(prompt)
{
	$("#newPrompt").val(prompt);
	$(".index-main").show(400);
	$(".prompt-main").hide(0);
	$(".quickactions-main").hide(0);
}

var fnSetDepartDefault = function()
{
	$.ajax({
		type: 'GET',
		url: '/api/chat/dm/default',
		/*		data: JSON.stringify(param),*/
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {
			var html = "";

			result.forEach(function(d)
			{

				if(d.type == "R") {
					html += "<a class=\"box\">";
					html += "<span class=\"box-text\" onclick=\"fnNewUserSession('" + d.sessionName + "')\">" + d.sessionName + "</span>";
					html += "</a>"
				}else if(d.type == "U")
				{
					html += "<a class=\"box\">";
					html += "<span class=\"box-text\" onclick=\"fnSetPromptAndHistory('" + d.session + "')\">" + d.sessionName + "</span>";
					html += " <div class=\"btn-area-type\">"
					html += "<button class=\"ico ico-edit\" data-bs-toggle=\"modal\" data-bs-target=\"#confirmEditModal\"" +
						" onclick=\"fnSetModifyModal('" + d.regNo + "', '" + d.sessionName + "'); event.preventDefault(); event.stopPropagation();\"></button>\n"
					html += "<button class=\"ico ico-delete\" data-bs-toggle=\"modal\" data-bs-target=\"#confirmDeleteModal\" " +
						"onclick=\"fnSetDeleteModal('" + d.regNo + "'); event.preventDefault(); event.stopPropagation();\"></button>\n"
					html += "</div>"
					html += "</a>"
				}
			});

			$('.quickactions-area').html(html);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnSetPromptAndHistory = function(session)
{
	var prompt = $("#newPrompt").val();

	$("#prompt").val(prompt);
	$("#newPrompt").val("");

	fnSetHistoryPrompt(session)
}

var fnNewUserSession = function (prompt)
{
	chat(prompt, null, "fileUpload").then(response => {
		var param = {};

		param.sessionName = prompt;
		param.session = sessionId

		$.ajax({
				type: 'POST',
				url: '/api/chat/user/session',
				data: JSON.stringify(param),
				contentType: 'application/json',
				cache: false,
				timeout: 6000000,
				async: true, //동기처리 (기본이 true)
				error: function (e) {
					alert("API 통신에 실패했습니다.");
					console.error(e);

				},
				success: function (result) {

					fnSetDepartDefault();
					fnGetChatHistory();

				}
			}
		);
	});
}

var fnSetHistoryPrompt = function(session) {

	sessionId = session;

	const promptFileInput = document.getElementById("promptfileUpload");
	const promptFileName = document.getElementById("promptFileName");
	$("#promptfileUpload").val('');
	const file = promptFileInput.files?.[0];
	promptFileName.textContent = file ? file.name : "선택된 파일 없음";

	$(".index-main").hide(0);
	$(".prompt-main").show(400);
	$(".quickactions-main").hide(0);

	$(".chat-wrap").empty();

	$.ajax({
		timeout: 6000000,
		type: 'POST',
		url: '/api/chat/session/history/' + session,
		cache: false,
		contentType: 'application/json',
		/*		data: JSON.stringify(param),*/
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		},
		success: function (result) {

			if (result.items == undefined) {
				alert("이전 대화 내용을 불러오지 못하였습니다.");
			} else {

				var html = '';

				result.items.forEach(function (d, idx) {

					if(d.type == "message") {
						if (d.role == 'user') {
							html += '<div class="chat-row me">';
							html += '<div class="chat-bubble">';
							html += d.content[0].text.replaceAll("\n", "<br>");
							html += '</div></div>'
						} else {
							var markcontents = marked.parse(d.content[0].text).replaceAll("**", "");

							html += '<div class="chat-row bot">';
							html += '<div class="chat-logo-wrap">';
							html += '<div class="chat-logo"></div></div>';
							html += ' <div class="answer-flex">';
							html += '<div class="chat-bubble answer">';
							html += markcontents;
							html += '</div>'
							html += '</div>'
						}
					}
				});

				const answers = document.querySelector('.chat-wrap');
				answers.innerHTML += html;

				const mySpace = document.querySelector('.chat-wrap');
				mySpace.scrollTo({
					top: mySpace.scrollHeight,
					behavior: 'smooth'
				});

			}
		},
	});
}

var fnSetDeleteModal = function(idx)
{
	$("#confirmDeleteModal").data("idx", idx);

}

var fnDeleteSession = function ()
{
	var param = {};

	param.aiUsRegNo = $("#confirmDeleteModal").data("idx");

	$.ajax({
		type: 'DELETE',
		url: '/api/chat/user/session',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		async: true, //동기처리 (기본이 true)
		success: function (data) {

			fnSetDepartDefault();
			$("#confirmDeleteModal").data("idx", "");
			$('#confirmDeleteModal').modal('hide')

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}


var fnSetModifyModal =  function(idx, sessionName)
{

	$("#confirmEditModal").data("idx", idx);
	$("#confirmEditModalText").val(sessionName);

}

var fnModifySession = function ()
{
	var param = {};

	param.aiUsRegNo = $("#confirmEditModal").data("idx");
	param.sessionName = $("#confirmEditModalText").val();

	$.ajax({
		type: 'PATCH',
		url: '/api/chat/user/session',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		async: true, //동기처리 (기본이 true)
		success: function (data) {

			fnSetDepartDefault();
			$("#confirmEditModal").data("idx", "");
			$("#confirmEditModalText").val("");
			$('#confirmEditModal').modal('hide')

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

