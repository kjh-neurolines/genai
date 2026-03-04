var sessionId = null;

$(document).ready(function () {

	$("#logo").on("click", function () {
		$(".index-main").show(400);
		$(".prompt-main").hide(0);
		$(".quickactions-main").hide(0);
	})

	$("#mLogo").on("click", function () {

		const mobileMenu = document.getElementById("mobile-menu");

		$(".index-main").show(400);
		$(".prompt-main").hide(0);
		$(".quickactions-main").hide(0);

		mobileMenu.classList.remove("show");
	})

	$("#promptIcon").on("click", function () {
		$(".index-main").hide(0);
		$(".prompt-main").show(400);
		$(".quickactions-main").hide(0);
	})

	$("#mPrompt").on("click", function () {

		const mobileMenu = document.getElementById("mobile-menu");
		$(".index-main").hide(0);
		$(".prompt-main").show(400);
		$(".quickactions-main").hide(0);

		mobileMenu.classList.remove("show");
	})

	$("#quickAction").on("click", function () {
		$(".index-main").hide(0);
		$(".prompt-main").hide(0);
		$(".quickactions-main").show(400);

		fnGetChatHistory();
	});

	$("#mQuickAction").on("click", function () {

		const mobileMenu = document.getElementById("mobile-menu");

		$(".index-main").hide(0);
		$(".prompt-main").hide(0);
		$(".quickactions-main").show(400);

		mobileMenu.classList.remove("show");

		fnGetChatHistory();
	});

	$("#btnNewSend").on("click", function () {
		chat($("#newPrompt").val(), null, "fileUpload").then(function(){
			fnGetChatHistory();
		});

		$("#newPrompt").val("");
	})

	$("#newPrompt").on("keydown", function(e){
			if(e.keyCode === 13)
			{
				if($("#btnSend").prop("disabled")){
					e.preventDefault();
					return;
				}

				if(!e.shiftKey){
					e.preventDefault();
					chat($("#newPrompt").val(), null, "fileUpload").then(function(){
						fnGetChatHistory();
					});
					$("#newPrompt").val("");
				}
			}
	});

	$("#btnSend").on("click", function () {
		chat($("#prompt").val(), sessionId, "promptfileUpload");

		$("#prompt").val("");
	});

	$("#prompt").on("keydown", function(e){
		if(e.keyCode === 13)
		{
			if($("#btnSend").prop("disabled")){
				e.preventDefault();
				return;
			}
			if(!e.shiftKey ){
				e.preventDefault();
				chat($("#prompt").val(), sessionId, "promptfileUpload");
				$("#prompt").val("");
			}
		}
	});

});

var makeSession = function () {

	var param = {};

	$.ajax({
		type: 'POST',
		url: '/api/chat/session',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});

};

var chat = function (prompt, session, fileId) {

	if(prompt === "" || prompt === null)
		return;

	if(prompt.length > 2000)
	{
		alert("질문은 2000자까지만 가능합니다.");
		return ;
	}

	var param = {
		"prompt": prompt
	};

	if (session != null)
		param.session = session;

	var formData = new FormData();
	var file = [];

	var fileInput = document.getElementById(fileId);
	if (fileInput.files.length != 0) {
		file.push(fileInput.files[0]);

		file.forEach(function (singleFile) {
			formData.append("file", singleFile); // 동일한 키 "file"로 여러 파일 추가
		})
	}

	formData.append("file", new Blob([JSON.stringify(param)], {type: "application/json"}));
	formData.append("chatStreamDTO", new Blob([JSON.stringify(param)], {type: "application/json"}));

	return new Promise((resolve, reject) => {
		$.ajax({
			type: 'POST',
			url: '/api/chat',
			data: formData,
			processData: false,
			contentType: false,
			cache: false,
			timeout: 6000000,
			async: true, //동기처리 (기본이 true)
			success: function (data) {

				var contents = data.answer;

				$(".bot-load").remove();

				var html = '';
				var markcontents = marked.parse(contents).replaceAll("**", "");

				html += '<div class="chat-row bot">';
				html += '<div class="chat-logo-wrap">';
				html += '<div class="chat-logo"></div></div>';
				html += ' <div class="answer-flex">';
				html += '<div class="chat-bubble answer">';
				html += '</div>';

				$('.chat-wrap').append(html);
			//	$('.chat-wrap').append("<p class=\"answer-footer\">WorkFlow 항목으로 이동하여 관련 업무를 진행해 주세요.</p>");

				const answers = document.querySelectorAll('.answer');
				const lastAnswer = answers[answers.length - 1];

				lastAnswer.innerHTML += markcontents
				lastAnswer.innerHTML += '<button class="answer-footer" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">WorkFlow 항목으로 이동하여\n' +
					'                  관련 업무를 진행해 주세요.</button>';

				var workflowInven = '';
				var workflowMsds = '';
				var workflowLink = '';
				var workflow = data.workflow;


				if(workflow != null)
				{
					for(var i = 0; i < workflow.length; i++)
					{
						if(workflow[i].type.includes('inventory'))
						{
								workflowInven += "<a href='" + workflow[i].url + "' class='workflow-item'>" + workflow[i].label + "</a>";
						}else if(workflow[i].type.includes('msds'))
						{
							workflowMsds += "<a href='" + workflow[i].url + "' class='workflow-item'>" + workflow[i].label + "</a>";
						}else{
							workflowLink += '<div class="menu">' + workflow[i].label + '</div>'
							workflowLink += "<a href='" + workflow[i].url + "' class='workflow-item'>" + workflow[i].label + "</a>";
						}
					}
				}

				var workflowTotal = '<div class="title"><img src="' + INVENTORY_ICON_URL + '">업무메뉴</div>';

				if(workflowInven != '')
				{
					workflowTotal += '<div class="menu">Inventory</div>';
					workflowTotal += workflowInven
				}
				if(workflowMsds != '')
				{
					workflowTotal += '<div class="menu">MSDS</div>';
					workflowTotal += workflowMsds
				}
				if(workflowLink != '')
				{
					workflowTotal += '<div class="title mt20"><img src="' + INVENTORY_ICON_URL + '">Link</div>';
					workflowTotal += workflowLink
				}

				$("#workflow").html(workflowTotal);

				sessionId = data.session;
				$("#btnSend").attr("disabled", false);

				if(typeof resolve == 'function')
					resolve();

				const mySpace = document.querySelector('.chat-wrap');
				mySpace.scrollTo({
					top: mySpace.scrollHeight,
					behavior: 'smooth'
				});

				$("#fileUpload").val('');
				const fileInput = document.getElementById("fileUpload");
				const fileName = document.getElementById("fileName");
				const file = fileInput.files?.[0];
				fileName.textContent = file ? file.name : "선택된 파일 없음";

			},
			error: function (e) {
				alert("API 통신에 실패했습니다.");
				console.error(e);
			},
			beforeSend: function (xhr) {

				$(".answer-footer").remove()

				if (session == null)
					$('.chat-wrap').empty();

				$(".index-main").hide(0);
				$(".prompt-main").show(400);
				$(".quickactions-main").hide(0);

				var html = '';

				html += '<div class="chat-row me">';
				html += '<div class="chat-bubble">';
				html += param.prompt.replaceAll("\n", "<br>");
				html += '</div></div>';

				$('.chat-wrap').append(html);

				html = '';

				// 로딩바 추가
				html += '<div class="chat-row bot bot-load">';
				html += '<div class="chat-logo-wrap">';
				html += '<div class="chat-logo"></div>';
				html += '<div class="chat-logo-ring"></div>';
				html += '</div>';
				html += '<div class="answer-flex">';
				html += '<div class="chat-bubble answer is-loading">';
				html += '<p class="loading-text">화학물질 데이터를 분석하고 있어요...</p>';
				html += '<div class="loading-dots">';
				html += '<span></span><span></span><span></span>';
				html += '</div></div></div></div>';
				$('.chat-wrap').append(html);

				$("#btnSend").attr("disabled", true);

				const mySpace = document.querySelector('.chat-wrap');
				mySpace.scrollTo({
					top: mySpace.scrollHeight,
					behavior: 'smooth'
				});

				$("#workflow").empty();

			},
		});
	})

}
