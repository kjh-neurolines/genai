$(document).ready(function (){
	fnGetPidDepMappingList(1);

	$("#btnPidDepMappingAdd").on("click", function (){
		$("#addProductCode").val('');
		$("#addProductName").val('');
		$("#addVendor").val('');
		$("#addVendor").data("code", "");

		$('#MaterialDeptMapAdd').modal('show')
	});

	$("#btnSearch").on("click", function (){
		fnGetPidDepMappingList(1);
	});

	$("#btnAddVendor").on("click", function (){

		$("#vendorTable tbody").empty();
		$("#modalSearchVendor").val("");

		$("#VendorSearch").modal('show');
	});

	$("#btnSearchVendor").on("click", function (){
		fnGetVendorList();
	});

	$("#btnSearchProduct").on("click", function (){
		$('#MaterialCodeSearch').modal('show');
		$('#modalProductCodeSearch').val('');
		$("#productTable tbody").empty();
	});

	$("#btnSearchProductList").on("click", function (){
		fnGetProductList();
	});

	$("#btnRegPidDepMapping").on("click", function (){
		fnAddPidDepMapping();
	});

	$("#btnModPidDep").on("click", function (){
		fnUpdatePidDepMapping();
	})

})

var fnGetPidDepMappingList = function(page){

	var param = {};

	param.page = page
	param.pageSize = $("#selPageSize").val();
	param.productCode = $("#searchProductCode").val();
	param.departmentCode = $("#searchDmCode").val();
	param.departmentName = $("#searchDepartmentName").val();

	$.ajax({
		type: 'POST',
		url: '/api/pidDep/list',
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

				html += "<tr><td class='text-center' colspan='10'>조회된 데이터가 없습니다.</td></tr>";
				$("#totalListSize").html("(0)")
				$("#listSummary").html("[전체 : 0/0]");

			} else {

				result.list.forEach(function (d, idx) {

					var i = (page - 1) * 10 + idx + 1;

					html += "<tr>";
					html += "<td class=\"text-center\">" + i + "</td>"
					html += "<td class=\"text-center\">" + d.productId + "</td>"
					html += "<td class=\"text-center\">" + d.productCode + "</td>"
					html += "<td class=\"text-center\">" + d.productCodeName + "</td>"
					html += "<td class=\"text-center\">" + d.departmentCode + "</td>"
					html += "<td class=\"text-center\">" + d.departmentName + "</td>"
					html += "<td class=\"text-center\">" + d.regDate + "</td>"
					if(d.status == 1)
					{
						html += "<td class=\"text-center\">사용</td>"
					}else{
						html += "<td class=\"text-center\">미사용</td>"
					}
					html += "<td class=\"text-center\">";
					html += "<button type=\"button\" class=\"btn btn-sm btn-outline-primary\"" +
						" onclick=\"fnOpenModifyModal('" + d.productId + "', '" + d.departmentCode + "')\">수정</button>";
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

var fnAddPidDepMapping = function(){

	var param = {};

	param.productId =  $("#addProductCode").data('productid')
	param.productCode = $("#addProductCode").val();
	param.productCodeName = $("#addProductName").val();
	param.departmentCode = $("#selAddDepartmentCode").val();

	if(param.productCode == "")
	{
		alert("자재코드를 선택해주세요.");
		return;
	}
	if(param.departmentCode == "")
	{
		alert("부서를 선택해주세요.");
		return;
	}

	$.ajax({
		type: 'POST',
		url: '/api/pidDep/reg',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {
			$("#MaterialDeptMapAdd").modal('hide');
			fnGetPidDepMappingList(1);
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnGetProductList = function(){

	var param = {};

	param.productCodeName = $("#modalProductCodeSearch").val();

	$.ajax({
		type: 'POST',
		url: '/api/productId/search',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			var html = "";

			$("#productTable tbody").empty();
			if (result.length > 0) {

				result.forEach(function (d, idx) {
					html += "<tr>";
					html += "<td>" + (idx + 1) + "</td>";
					html += "<td>" + d.productId + "</td>";
					html += "<td>" + d.productCode + "</td>";
					html += "<td>" + d.productCodeName + "</td>";
					html += " <td class=\"text-center\">" +
						"<button type=\"button\" class=\"btn btn-dark btn-sm\" onclick=\"fnSetProduct('" + d.productId + "', '"
						+ d.productCodeName + "', '" + d.productCode + "')\">선택</button>" +
						"</td>";
					html += "</td>"
				})
			}else{
				html += "<tr><td class='text-center' colspan='8'>조회된 데이터가 없습니다.</td></tr>";
			}

			$("#productTable tbody").html(html);

		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnSetProduct = function (productId, productCodeName, productCode){
	$("#addProductCodeName").val(productCodeName);
	$("#addProductCode").val(productCode);
	$("#addProductCode").data('productid', productId);
	$("#MaterialCodeSearch").modal("hide");
}


var fnOpenModifyModal = function(productId, depCode)
{
	$.ajax({
		type: 'POST',
		url: '/api/pidDep/' + productId + '/' + depCode,
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#modProductCode").val(result.productCode);
			$("#modProductCode").data('productid', result.productId);
			$("#modProductName").val(result.productCodeName);
			$("#modDepartment").val(result.departmentCode);

			if(result.status == 1)
			{
				$('input[id="useY"]').prop('checked',true);
				$('input[id="useN"]').prop('checked',false);
			}else{
				$('input[id="useY"]').prop('checked',false);
				$('input[id="useN"]').prop('checked',true);
			}

			$("#MaterialDeptMapEdit").modal('show');
		},
		error: function (e) {
			alert("API 통신에 실패했습니다.");
			console.error(e);
		}
	});
}

var fnUpdatePidDepMapping = function()
{
	var param = {};

	param.productId = $("#modProductCode").data('productid');
	param.productCodeName = $("#modProductCode").val();
	param.departmentCode =  $("#modDepartment").val()

	if($("#useY").is(":checked"))
		param.status = 1;
	else
		param.status = 0;

	$.ajax({
		type: 'POST',
		url: '/api/pidDep/update',
		data: JSON.stringify(param),
		contentType: 'application/json',
		cache: false,
		timeout: 6000000,
		success: function (result) {

			$("#MaterialDeptMapEdit").modal('hide');
			fnGetPidDepMappingList(1);

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