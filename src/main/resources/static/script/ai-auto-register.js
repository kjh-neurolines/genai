$(document).ready(function (){
		$("#selHqCode").on("change", function (){

			if($(this).val() != "")
				fnWpSelect($("#selHqCode").val());
			else{
				var html =  "<option value=''>선택</option>";
				$('#selWpCode').html(html);
				$('#selDmCode').html(html);
			}
		});

		$("#selWpCode").on("change", function (){

			if($(this).val() != "")
				fnDmSelect($("#selHqCode").val(), $("#selWpCode").val());
			else{
				var html =  "<option value=''>선택</option>";
				$('#selDmCode').html(html);
			}
		});

	$("#addProductCode").on("keyup", function(){
		fnFindVendor($(this).val());
	});

	$("#btnSubmit").on("click", function (){
		fnSaveInventory();
	});
});

var fnSaveInventory = function ()
{
	var param =  {
		"gubun": $("#selProductType").val(),
		"productCode": $("#addProductCode").val(),
		"productId" : $("#selVendor option:selected").data("productid"),
		"vendorCode" : $("#selVendor option:selected").data("vendorcode"),
		"country" : "kor",
		"langCode" : "ko"
	};

	var formData = new FormData();
	var fileElements = document.getElementsByClassName("file");
	var fileList = []; // 파일을 담을 배열 (변수명은 의미 있게 설정)

	Array.from(fileElements).forEach(function (input, index) {

		if (input.files && input.files[0]) {
			var targetFile = input.files[0];

			fileList.push(targetFile);
		}
	});

	fileList.forEach(function(singleFile) {
		formData.append("file", singleFile); // 동일한 키 "file"로 여러 파일 추가
	});

	formData.append("requestDTO", new Blob([JSON.stringify(param)], { type: "application/json" }));

	$("#btnSubmit").attr("disabled", true);

	$.ajax({
			type: 'POST',
			enctype: 'multipart/form-data',
			url: '/api/iv/upload',
			data: formData,
			processData: false,
			contentType: false,
			cache: false,
			timeout: 60000000,
			success: function (data) {
				$("#selProductType").val("")
				$("#addProductCode").val("")
				$("#selVendor").empty()

				Array.from(fileElements).forEach(function (input, index) {
					$(this).val("");
				});

				if(data.status != "1")
					alert(data.errorMsg)


				$("#btnSubmit").attr("disabled", false);

			},
			error: function (e) {
				alert("MSDS 등록 실패했습니다.");
				$("#btnSubmit").attr("disabled", false);
			},
			beforeSend: function (e) {
				$("#popup-overlay").removeClass('hidden')
			},
			complete: function (e) {
				$("#popup-overlay").addClass('hidden')
			}
		}
	);
}

var fnWpSelect = function (hqCode){

	$.ajax({
		type: 'POST',
		url: '/api/wp/' + hqCode,
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			var html =  "<option value=''>선택</option>";

			result.forEach(function (d){

				html += "<option value='" + d.wpCode + "'>"
				html += d.wpName
				html += "</option>"

			});

			$('#selWpCode').html(html);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnDmSelect = function (hqCode, wpCode){

	$.ajax({
		type: 'POST',
		url: '/api/dm/' + hqCode + '/' + wpCode,
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			var html = "<option value=''>선택</option>";

			result.forEach(function (d) {

				html += "<option value='" + d.dmcode + "'>"
				html += d.dmName
				html += "</option>"

			});
			$('#selDmCode').html(html);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnFindVendor = function () {

	var param = {}
	param.productCode = $("#addProductCode").val()

	$.ajax({
		type: 'POST',
		url: '/api/vendor/find',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {
			var html = "";

			result.forEach(function(v){


				var productId = '';
				var vendorCode = '';
				var vendorName = '';

				if(v.productId != undefined)
					productId = v.productId;
				if(v.vendorName != undefined)
					vendorName = v.vendorName;
				if(v.vendorCode != undefined){
					vendorCode = v.vendorCode;
					vendorName += "(" + v.vendorCode + ")";
				}

				html += "<option data-productid='" + productId + "' data-vendorCode='" + vendorCode + "'>";
				html += vendorName;
				html += "</option>"

			});

			$("#selVendor").html(html);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}
