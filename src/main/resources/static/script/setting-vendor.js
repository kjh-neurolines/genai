$(document).ready(function (){

	fnGetVendorList(1)

	$("#btnAddVendor").on("click", function (){
		$("#addVendorCode").val('');
		$("#addVendorName").val('');
		$("#addManager").val('');
		$("#addManagerTel").val('');
		$("#addEmail").val('');
		$("#addTel").val('');
		$('#VendorAdd').modal('show')
	});

	$("#modalBtnAddVendor").on("click", function (){
		fnAddVendor();
	});

	$("#btnSearch").on("click", function (){
		fnGetVendorList(1);
	});
});

var fnGetVendorList = function(page){

	var param = {};

	param.page = page
	param.pageSize = $("#selPageSize").val();
	param.vendorName = $("#searchVendorName").val();
	param.vendorCode = $("#searchVendorCode").val();

	$.ajax({
		type: 'POST',
		url: '/api/vendor/list',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			console.log(result);
			$("#dataTable tbody").empty();
			$("#pagination").empty();

			var html = "";

			if (result.total < 1) {

				html += "<tr><td class='text-center' colspan='7'>조회된 데이터가 없습니다.</td></tr>";
				$("#totalListSize").html("(0)")
				$("#listSummary").html("[전체 : 0/0]");

			} else {

				result.list.forEach(function (d, idx) {

					var i = (page - 1) * 10 + idx + 1;

					html += "<tr>";
					html += "<td class=\"text-center\">" + i + "</td>"
					html += "<td class=\"text-center\">" + d.vendorCode + "</td>"
					html += "<td class=\"text-center\">" + d.vendorName + "</td>"
					html += "<td class=\"text-center\">" + d.manager + "</td>"
					html += "<td class=\"text-center\">" + d.managerTel + "</td>"
					html += "<td class=\"text-center\">" + d.regDate + "</td>"
					if(d.status == 1)
					{
						html += "<td class=\"text-center\">사용</td>"
					}else{
						html += "<td class=\"text-center\">미사용</td>"
					}
					html += "<td class=\"text-center\">";
					html += "<button type=\"button\" class=\"btn btn-sm btn-outline-primary\"" +
						" onclick=\"fnOpenModifyModal('" + d.vendorCode + "')\">수정</button>";
					html += "</td>";
					html += "</tr>";
				});

				paging(result)

				$("#totalListSize").html("(" + result.total + ")")
				$("#listSummary").html("[전체 : " + result.pageNum + "/" + result.pages + "]");

			}

			$("#dataTable tbody").html(html);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnAddVendor = function(){

	var param = {};

	param.vendorCode = $("#addVendorCode").val();
	param.vendorName = $("#addVendorName").val();
	param.manager = $("#addManager").val();
	param.managerTel = $("#addManagerTel").val();
	param.tel = $("#addTel").val();
	param.email = $("#addEmail").val();

	if(param.vendorCode == "")
	{
		alert("벤더코드를 입력해주세요.");
		return;
	}
	if(param.vendorName == "")
	{
		alert("벤더명을 입력해주세요.");
		return;
	}

	$.ajax({
		type: 'POST',
		url: '/api/vendor/reg',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {
			$("#VendorAdd").modal('hide');
			fnGetVendorList(1);
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnOpenModifyModal = function(vendorCode)
{
	$.ajax({
		type: 'POST',
		url: '/api/vendor/' + vendorCode,
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#modVendorCode").html(result.vendorCode);
			$("#modVendorName").val(result.vendorName);
			$("#modManager").val(result.manager);
			$("#modManagerTel").val(result.managerTel);
			$("#modEmail").val(result.email);
			$("#modTel").val(result.tel);

			if(result.status == 1)
			{
				$('input[id="useY"]').prop('checked',true);
				$('input[id="useN"]').prop('checked',false);
			}else{
				$('input[id="useY"]').prop('checked',false);
				$('input[id="useN"]').prop('checked',true);
			}

			$("#VendorEdit").modal('show');
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnUpdateVendor = function()
{
	var param = {};

	param.vendorCode = $("#modVendorCode").text();
	param.vendorName = $("#modVendorName").val();
	param.manager = $("#modManager").val();
	param.managerTel = $("#modManagerTel").val();
	param.tel = $("#addTel").val();
	param.email = $("#addEmail").val();

	if($("#useY").is(":checked"))
		param.status = 1;
	else
		param.status = 0;

	$.ajax({
		type: 'POST',
		url: '/api/vendor/update',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#VendorEdit").modal('hide');
			fnGetVendorList(1);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var paging = function(data) {
	$('#pagination').twbsPagination('destroy');

	$('#pagination').twbsPagination({
		totalPages: data.pages,
		startPage: data.pageNum,
		visiblePages: 5,
		initiateStartPageClick: false,
		prev: '<a class="page-link page-prev" href="#" aria-label="이전"><i class="bi bi-chevron-left"></i><span class="visually-hidden">이전</span></a>',
		next: '<a class="page-link page-next" href="#" aria-label="다음"><i class="bi bi-chevron-right"></i><span class="visually-hidden">다음</span></a>',
		first: '',
		last: '',
		onPageClick: function (event, page) {
			fnGetHeadquarterList(page);
		}
	});
}

