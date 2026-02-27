$(document).ready(function (){
	fnGetWorkplaceList(1);

	$("#btnAddWorkplace").on("click", function (){
		$("#addWpName").val('');
		$("#addWpCode").val('');
		$("#addHqCode").val($("#addHqCode option:first").val());
		$('#SiteAdd').modal('show')
	});

	$("#modalWorkplaceAddBtn").on("click", function (){
		fnAddWorkplace();
	});

	$("#btnSearch").on("click", function (){
		fnGetWorkplaceList(1);
	});
})

var fnGetWorkplaceList = function(page){

	var param = {};

	param.page = page
	param.pageSize = $("#selPageSize").val();
	param.hqCode = $("#selHqCode").val();
	param.wpName = $("#searchWpName").val();

	$.ajax({
		type: 'POST',
		url: '/api/wp/list',
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
					html += "<td class=\"text-center\">" + d.hqName + "</td>"
					html += "<td class=\"text-center\">" + d.wpName + "</td>"
					html += "<td class=\"text-center\">" + d.wpCode + "</td>"
					html += "<td class=\"text-center\">" + d.regYmd + "</td>"
					if(d.status == 1)
					{
						html += "<td class=\"text-center\">사용</td>"
					}else{
						html += "<td class=\"text-center\">미사용</td>"
					}
					html += "<td class=\"text-center\">";
					html += "<button type=\"button\" class=\"btn btn-sm btn-outline-primary\"" +
						" onclick=\"fnOpenModifyModal('" + d.hqCode + "', '" + d.wpCode + "')\">수정</button>";
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

var fnAddWorkplace= function(){

	var param = {};

	param.hqCode = $("#addHqCode").val();
	param.wpName = $("#addWpName").val();
	param.wpCode = $("#addWpCode").val();

	$.ajax({
		type: 'POST',
		url: '/api/wp/reg',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {
			$("#SiteAdd").modal('hide');

			fnGetWorkplaceList(1);
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnOpenModifyModal = function(hqCode ,wpCode)
{
	$.ajax({
		type: 'POST',
		url: '/api/wp/' + hqCode + '/' + wpCode,
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#modWpName").val(result.wpName);
			$("#modWpCode").html(result.wpCode);
			$("#modHqCode").val(result.hqCode);

			$("#SiteEdit").modal('show');
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnUpdateWorkplace = function(status)
{
	var param = {};

	param.wpCode = $("#modWpCode").html();
	param.wpName = $("#modWpName").val();
	param.status = status;
	param.hqCode = $("#modHqCode").val();

	$.ajax({
		type: 'POST',
		url: '/api/wp/update',
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
			fnGetWorkplaceList(page);
		}
	});
};