$(document).ready(function (){
	fnGetDepartmentList(1);

	$("#btnAddDepartment").on("click", function (){
		$("#addDmCode").val('');
		$("#addDmName").val('');
		$("#addHqCode").val($("#addHqCode option:first").val());
		$('#DepartmentAdd').modal('show')
	});

	$("#modalDepartmentAddBtn").on("click", function (){
		fnAddDepartment();
	});

	$("#btnSearch").on("click", function (){
		fnGetDepartmentList(1);
	});


	$("#addHeadquarter").on("change", function (){
		fnModalWpSelect("#addWorkplace", $("#addHeadquarter").val() )
	});

	$("#modHqCode").on("change", function (){
		fnModalWpSelect("#modWpCde", $("#modHqCode").val() )
	});

	$("#selHqCode").on("change", function (){

		if($(this).val() != "")
			fnModalWpSelect("#selWpCode", $("#selHqCode").val() )
		else
			$("#selWpCode").html("<option value=''>선택</option>0.")
	});
})

var fnGetDepartmentList = function(page){

	var param = {};

	param.page = page
	param.pageSize = $("#selPageSize").val();
	param.hqCode = $("#selHqCode").val();
	param.wpCode = $("#selWpCode").val();
	param.dmName = $("#searchDmName").val();

	$.ajax({
		type: 'POST',
		url: '/api/dm/list',
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

				html += "<tr><td class='text-center' colspan='8'>조회된 데이터가 없습니다.</td></tr>";
				$("#totalListSize").html("(0)")
				$("#listSummary").html("[전체 : 0/0]");

			} else {

				result.list.forEach(function (d, idx) {

					var i = (page - 1) * 10 + idx + 1;

					html += "<tr>";
					html += "<td class=\"text-center\">" + i + "</td>"
					html += "<td class=\"text-center\">" + d.hqName + "</td>"
					html += "<td class=\"text-center\">" + d.wpName + "</td>"
					html += "<td class=\"text-center\">" + d.dmCode + "</td>"
					html += "<td class=\"text-center\">" + d.dmName + "</td>"
					html += "<td class=\"text-center\">" + d.regYmd + "</td>"
					if(d.status == 1)
					{
						html += "<td class=\"text-center\">사용</td>"
					}else{
						html += "<td class=\"text-center\">미사용</td>"
					}
					html += "<td class=\"text-center\">";
					html += "<button type=\"button\" class=\"btn btn-sm btn-outline-primary\"" +
						" onclick=\"fnOpenModifyModal('" + d.hqCode + "', '" + d.wpCode + "', '" + d.dmCode + "')\">수정</button>";
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

var fnModalWpSelect = function (id, hqCode){

	$.ajax({
		type: 'POST',
		url: '/api/wp/' + hqCode,
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			var html = "";

			result.forEach(function (d){

				html += "<option value='" + d.wpCode + "'>"
				html += d.wpName
				html += "</option>"

			});

			if(id === '#selWpCode')
				html = "<option value=''>선택</option>" + html;

			$(id).html(html);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});

}

var fnAddDepartment= function(){

	var param = {};

	param.hqCode = $("#addHeadquarter").val();
	param.wpCode = $("#addWorkplace").val();
	param.dmCode = $("#addDmCode").val();
	param.dmName = $("#addDmName").val();

	$.ajax({
		type: 'POST',
		url: '/api/dm/reg',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {
			$("#DepartmentAdd").modal('hide');

			fnGetDepartmentList(1);
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnOpenModifyModal = function(hqCode ,wpCode, dmCCode)
{
	$.ajax({
		type: 'POST',
		url: '/api/dm/' + hqCode + '/' + wpCode + '/' + dmCCode,
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#modDmCode").val(result.dmCode);
			$("#modDmName").val(result.dmName);
			$("#modWpCode").html(result.wpCode);
			$("#modHqCode").val(result.hqCode);

			$("#DepartmentEdit").modal('show');
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnUpdateDepartment = function(status)
{
	var param = {};
	param.hqCode = $("#modHqCode").val();
	param.wpCode = $("#modWpCode").html();
	param.dmCode = $("#modDmCode").html();
	param.dmName = $("#modDmName").html();
	param.status = status;

	$.ajax({
		type: 'POST',
		url: '/api/dm/update',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#SiteEdit").modal('hide');
			fnGetWorkplaceList(1);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var paging = function(data)
{
	$('#pagination').twbsPagination('destroy');

	$('#pagination').twbsPagination({
		totalPages: data.pages,
		startPage: data.pageNum,
		visiblePages: 5,
		initiateStartPageClick: false,
		prev : '<a class="page-link page-prev" href="#" aria-label="이전"><i class="bi bi-chevron-left"></i><span class="visually-hidden">이전</span></a>',
		next : '<a class="page-link page-next" href="#" aria-label="다음"><i class="bi bi-chevron-right"></i><span class="visually-hidden">다음</span></a>',
		first : '',
		last : '',
		onPageClick: function (event, page) {
			fnGetDepartmentList(page);
		}
	});
};