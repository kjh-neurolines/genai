$(document).ready(function (){
	fnGetProductIdList(1);

	$("#btnProductIdAdd").on("click", function (){
		$("#addProductCode").val('');
		$("#addProductName").val('');
		$("#addVendor").val('');
		$("#addVendor").data("code", "");


		$('#materialAdd').modal('show')
	});

	$("#modalProductAdd").on("click", function (){
		fnAddProductId();
	});

	$("#btnSearch").on("click", function (){
		fnGetProductIdList(1);
	});

	$("#btnAddVendor").on("click", function (){

		$("#vendorTable tbody").empty();
		$("#modalSearchVendor").val("");

		$("#VendorSearch").modal('show');
	});

	 $("#btnSearchVendor").on("click", function (){
		  fnGetVendorList();
	 });
})

var fnGetProductIdList = function(page){

	var param = {};

	param.page = page
	param.pageSize = $("#selPageSize").val();
	param.productCode = $("#searchProductCode").val();
	param.vendorName = $("#searchVendorName").val();
	param.productId = $("#searchProductId").val();

	$.ajax({
		type: 'POST',
		url: '/api/productId/list',
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
					html += "<td class=\"text-center\">" + d.productId + "</td>"
					html += "<td class=\"text-center\">" + d.productCode + "</td>"
					html += "<td class=\"text-center\">" + d.vendorCode + "</td>"

					if(d.vendorName == null)
						html += "<td class=\"text-center\"></td>"
					else html += "<td class=\"text-center\">" + d.vendorName + "</td>"

					html += "<td class=\"text-center\">" + d.regDate + "</td>"
					if(d.status == 1)
					{
						html += "<td class=\"text-center\">사용</td>"
					}else{
						html += "<td class=\"text-center\">미사용</td>"
					}
					html += "<td class=\"text-center\">";
					html += "<button type=\"button\" class=\"btn btn-sm btn-outline-primary\"" +
						" onclick=\"fnOpenModifyModal('" + d.productId + "')\">수정</button>";
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

var fnAddProductId = function(){

	var param = {};

	param.productCode = $("#addProductCode").val();
	param.productCodeName = $("#addProductName").val();
	param.vendorCode = $("#addVendor").data("code");

	$.ajax({
		type: 'POST',
		url: '/api/productId/reg',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {
			$("#materialAdd").modal('hide');
			fnGetProductIdList(1);
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnGetVendorList = function(){

	var param = {};

	param.vendorName = $("#modalSearchVendor").val();
	param.status = 1;

	$.ajax({
		type: 'POST',
		url: '/api/vendor/search',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			var html = "";

			$("#vendorTable tbody").empty();
			if (result.length > 0) {

				result.forEach(function (d, idx) {
					html += "<tr>";
					html += "<td>" + (idx + 1) + "</td>";
					html += "<td>" + d.vendorCode + "</td>";
					html += "<td>" + d.vendorName + "</td>";
					html += " <td class=\"text-center\">" +
						"<button type=\"button\" class=\"btn btn-dark btn-sm\" onclick=\"fnSetVendor('" + d.vendorCode + "', '"
						+ d.vendorName + "')\">선택</button>" +
						"</td>";
					html += "</td>"
				})
			}else{
				html += "<tr><td class='text-center' colspan='8'>조회된 데이터가 없습니다.</td></tr>";
			}

			$("#vendorTable tbody").html(html);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnSetVendor = function (vendorCode, vendorName){
		$("#addVendor").val(vendorName);
		$("#addVendor").data("code", vendorCode);
		$("#VendorSearch").modal("hide");
}


var fnOpenModifyModal = function(productId)
{
	$.ajax({
		type: 'POST',
		url: '/api/productId/' + productId,
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#modProductId").html(result.productId);
			$("#modProductCode").html(result.productCode);
			$("#modVendorName").html(result.vendorName);
			$("#modProductName").val(result.productCodeName);

			if(result.status == 1)
			{
				$('input[id="useY"]').prop('checked',true);
				$('input[id="useN"]').prop('checked',false);
			}else{
				$('input[id="useY"]').prop('checked',false);
				$('input[id="useN"]').prop('checked',true);
			}

			$("#MaterialEdit").modal('show');
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnUpdateProductId = function()
{
	var param = {};

	param.productId = $("#modProductId").html();
	param.productCodeName = $("#modProductName").val();

	if($("#useY").is(":checked"))
		param.status = 1;
	else
		param.status = 0;

	$.ajax({
		type: 'POST',
		url: '/api/productId/update',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#MaterialEdit").modal('hide');
			fnGetProductIdList(1);

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