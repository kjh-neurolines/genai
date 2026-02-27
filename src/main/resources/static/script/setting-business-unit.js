$(document).ready(function (){
	fnGetHeadquarterList(1);

	$("#btnBusinessAdd").on("click", function (){
		$("#addHqCode").val('');
		$("#addHqName").val('');
		$('#BusinessAdd').modal('show')
	});

	$("#modalBusinessAddBtn").on("click", function (){
		fnAddHeadquarter();
	});

	$("#btnSearch").on("click", function (){
		fnGetHeadquarterList(1);
	});
})

var fnGetHeadquarterList = function(page){

	var param = {};

	param.page = page
	param.pageSize = $("#selPageSize").val();
	param.hqName = $("#searchHqName").val();

	$.ajax({
		type: 'POST',
		url: '/api/hq/list',
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

				html += "<tr><td class='text-center' colspan='6'>조회된 데이터가 없습니다.</td></tr>";
				$("#totalListSize").html("(0)")
				$("#listSummary").html("[전체 : 0/0]");

			} else {

			result.list.forEach(function (d, idx) {

				var i = (page - 1) * 10 + idx + 1;

				html += "<tr>";
				html += "<td class=\"text-center\">" + i + "</td>"
				html += "<td class=\"text-center\">" + d.hqName + "</td>"
				html += "<td class=\"text-center\">" + d.hqCode + "</td>"
				html += "<td class=\"text-center\">" + d.regYmd + "</td>"
				if(d.status == 1)
				{
					html += "<td class=\"text-center\">사용</td>"
				}else{
					html += "<td class=\"text-center\">미사용</td>"
				}
				html += "<td class=\"text-center\">";
				html += "<button type=\"button\" class=\"btn btn-sm btn-outline-primary\"" +
					" onclick=\"fnOpenModifyModal('" + d.hqCode + "')\">수정</button>";
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

var fnAddHeadquarter= function(){

	var param = {};

	param.hqCode = $("#addHqCode").val();
	param.hqName = $("#addHqName").val();

	$.ajax({
		type: 'POST',
		url: '/api/hq/reg',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {
			$("#BusinessAdd").modal('hide');
			fnGetHeadquarterList(1);
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnOpenModifyModal = function(hqCode)
{
	$.ajax({
		type: 'POST',
		url: '/api/hq/' + hqCode,
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#editModalHqCode").html(result.hqCode);
			$("#editModalHqName").val(result.hqName);

			$("#BusinessEdit").modal('show');
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnUpdateHeadquarter = function(status)
{
	var param = {};

	param.hqCode = $("#editModalHqCode").html();
	param.hqName = $("#editModalHqName").val();
	param.status = status
	$.ajax({
		type: 'POST',
		url: '/api/hq/update',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#BusinessEdit").modal('hide');
			fnGetHeadquarterList(1);

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
			fnGetHeadquarterList(page);
		}
	});
};