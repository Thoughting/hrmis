// 用户管理
$.namespace('emp.contract.contractTypeMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$add_url = basePath + "/emp/archives/employeeAdd";
		Constant.$edit_url = basePath + "/emp/archives/employeeEdit";
		Constant.$detail_url = basePath + "/emp/archives/employeeDetail";

		Constant.$grid_list_url = basePath + '/api/emp/info/list';
		Constant.$export_url = basePath + "/api/emp/info/export";
		Constant.$delete_url = basePath + "/api/emp/info/delete";
		
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		Constant.$post_combo_url = basePath + '/api/emp/post/combo';
		
		//员工附件
		Constant.$employeeAnnexUpload_url = basePath + "/api/emp/info/employeeAnnexUpload";
		Constant.$employeeAnnexDownload_url = basePath + "/api/emp/info/employeeAnnexDownload";
		Constant.$employeeAnnexDelete_url = basePath + "/api/emp/info/employeeAnnexDelete";
		
		Constant.$quitCompany_url = basePath + "/api/emp/info/quitCompany";
		Constant.$regular_url = basePath + "/api/emp/info/regular";
		
		//员工审核
		Constant.$employeeAudit_url = basePath + "/api/emp/info/audit";
		
	}
}

//界面组件
Component = {
	init : function(){
		Component.$grid = $("#datagrid");
		Component.$grid.datagrid({
			url : Constant.$grid_list_url,
			frozenColumns : [[
  			    {field:"ck",checkbox:true}
  	        ]],
			onBeforeLoad : function(param){
				param.name = $("#name").val();
				param.sex = $("#sex").val();
				param.nativePlaceType = $("#nativePlaceType").val();
				param.nativePlaceAddr = $("#nativePlaceAddr").val();
				param.code = $("#code").val();
				param.employeeDept = $("#employeeDept").val();
				param.employeePost = $("#employeePost").val();
				param.employeePostType = $("#employeePostType").val();
				param.start_enrtyDate = $("#start_enrtyDate").val();
				param.end_enrtyDate = $("#end_enrtyDate").val();
				param.start_regularDate = $("#start_regularDate").val();
				param.end_regularDate = $("#end_regularDate").val();
				param.start_birthDate = $("#start_retareDate").val();
				param.end_birthDate = $("#end_retareDate").val();
				param.laborType = $("#laborType").val();
				param.education = $("#education").val();
				param.contractType = $("#contractType").val();
				param.contractStartDate = $("#contractStartDate").val();
				param.contractEndDate = $("#contractEndDate").val();
				param.start_retareDate = $("#start_retareDate").val();
				param.end_retareDate = $("#end_retareDate").val();
				param.mealRoomType = $("#mealRoomType").val();
				param.hasInsure = $("#hasInsure").val();
				param.insureNo = $("#insureNo").val();
				param.start_insureDate = $("#start_insureDate").val();
				param.end_insureDate = $("#end_insureDate").val();
				param.driveLicenseType = $("#driveLicenseType").val();
				param.hasPublicFund = $("#hasPublicFund").val();
				param.start_publicFundDate = $("#start_publicFundDate").val();
				param.end_publicFundDate = $("#end_publicFundDate").val();
				param.hasQuitCompany = $("input[name='hasQuitCompany']:checked").val();
				param.start_quitCompanyDate = $("#start_quitCompanyDate").val();
				param.end_quitCompanyDate = $("#end_quitCompanyDate").val();
				param.auditStatus = $("#auditStatus").val();
			}
		});
	},
	getParam : function(){
		var param = {};
		param.name = $("#name").val();
		param.sex = $("#sex").val();
		param.nativePlaceType = $("#nativePlaceType").val();
		param.nativePlaceAddr = $("#nativePlaceAddr").val();
		param.code = $("#code").val();
		param.employeeDept = $("#employeeDept").val();
		param.employeePost = $("#employeePost").val();
		param.employeePostType = $("#employeePostType").val();
		param.start_enrtyDate = $("#start_enrtyDate").val();
		param.end_enrtyDate = $("#end_enrtyDate").val();
		param.start_regularDate = $("#start_regularDate").val();
		param.end_regularDate = $("#end_regularDate").val();
		param.start_birthDate = $("#start_retareDate").val();
		param.end_birthDate = $("#end_retareDate").val();
		param.laborType = $("#laborType").val();
		param.education = $("#education").val();
		param.contractType = $("#contractType").val();
		param.contractStartDate = $("#contractStartDate").val();
		param.contractEndDate = $("#contractEndDate").val();
		param.start_retareDate = $("#start_retareDate").val();
		param.end_retareDate = $("#end_retareDate").val();
		param.mealRoomType = $("#mealRoomType").val();
		param.hasInsure = $("#hasInsure").val();
		param.insureNo = $("#insureNo").val();
		param.start_insureDate = $("#start_insureDate").val();
		param.end_insureDate = $("#end_insureDate").val();
		param.driveLicenseType = $("#driveLicenseType").val();
		param.hasPublicFund = $("#hasPublicFund").val();
		param.start_publicFundDate = $("#start_publicFundDate").val();
		param.end_publicFundDate = $("#end_publicFundDate").val();
		param.hasQuitCompany = $("input[name='hasQuitCompany']:checked").val();
		param.start_quitCompanyDate = $("#start_quitCompanyDate").val();
		param.end_quitCompanyDate = $("#end_quitCompanyDate").val();
		param.auditStatus = $("#auditStatus").val();
		return param;
	}
}

//界面交互
Interactive = {
	init : function(){
		//回车查询
		$("body").keydown(function(event) {
			var c = event.keyCode;
			if(c == 13){
				$("#searchBtn").click();
			}
		});
		
		//查询
		$("#searchBtn").linkbutton({
			onClick:function(){
				Component.$grid.datagrid('reload');
			}
		});
		
		//新增
		$("#addBtn").linkbutton({
			onClick : function(){
				window.open(Constant.$add_url,'_self');
			}
		});
		
		//编辑
		$("#editBtn").linkbutton({
			onClick : function(){
				var selectNodes = Component.$grid.datagrid('getSelections');
				if (selectNodes != null && selectNodes.length == 1) {
					Common.openPostWindow(Constant.$edit_url,selectNodes[0].id,"_self");
				} else {
					$.messager.alert('提示','请选择一条需要操作的记录!');
				}
			}
		});
		
		//删除
		$("#delBtn").linkbutton({
			onClick : function () {
				var selectNodes = Component.$grid.datagrid('getSelections');
				if (selectNodes != null) {
					$.messager.confirm('提示', '确定删除？', function(r){
						if (r){
							$.ajax({
								url : Constant.$delete_url,
								type : 'post',
								dataType : 'json',
								data : {deleteJson : JSON.stringify(selectNodes)},
								success : function(data) {
									if (data.success) {
										Component.$grid.datagrid('reload');
										$.messager.alert("提示", "删除成功");
									} else {
										$.messager.alert("错误", data.message, "error");
									}
								},
								error : function(re, status, err) {
									$.messager.alert("错误", re.responseText, "error");
								}
							});
						}
					});
				} else {
					$.messager.alert('提示','请选择一条需要操作的记录!');
				}
			}
		})
		
		//附件上传
		$("#uploadAnnexBtn").linkbutton({
			onClick:function(){
				var selectNodes = Component.$grid.datagrid('getSelections');
				if (selectNodes != null && selectNodes.length == 1) {
					Page.upload.open(Constant.$employeeAnnexUpload_url + "?content=" + selectNodes[0].id + "_annex", function() {
						Component.$grid.datagrid('reload');
					});
				} else {
					$.messager.alert('提示','请选择一条需要操作的记录!');
				}
			}
		});
		
		//导出
		$("#exportBtn").linkbutton({
			onClick : function(){
				Common.openPostWindow2(Constant.$export_url,Component.getParam());
			}
		})
		
		//转正form
		$("#regular_form").form({
			url : Constant.$regular_url,
			onSubmit : function(){
				var isValid = $(this).form('validate');
				
				if ($("input[name='ud_isRegular']:checked").val() == 1 && !Common.checkNotNull("第一阶段转正时间",$("#ud_regularDate").val())) {
					isValid = false;
				}
				if (!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success: function(data){
				$.messager.progress('close');
				var data = eval('(' + data + ')');
				if (data.success) {
					Component.$grid.datagrid('reload');
					$.messager.alert("提示", "转正成功");
					$("#regularPopWin").window('close');
				} else {
					$.messager.alert('提示','操作失败!');
				}
			}
		});
		//转正
		$("#regularBtn").linkbutton({
			onClick : function(){
				var selectNodes = Component.$grid.datagrid('getSelections');
				if (selectNodes != null) {
					$("input[name=ud_isRegular][value=1]").attr("checked",'checked');
					$("#ud_regularDate").val(selectNodes[0].regularDate);
					$("#ud_regularDateTwo").val(selectNodes[0].regularDateTwo);
					
					$("#regular_form").form({
						queryParams : {employees : JSON.stringify(selectNodes)}
					});
					$("#regularPopWin").window('open');
					$("#regularPopWin").window('center');
				} else {
					$.messager.alert('提示','请选择一条需要操作的记录!');
				}
			}
		});
		//转正提交
		$("#regular_submitBtn").click(function(){
			$.messager.progress({msg:'请稍后...'});
			$('#regular_form').form('submit');
		});
		
		//离职
		$("#quitCompany_form").form({
			url : Constant.$quitCompany_url,
			onSubmit : function(){
				var isValid = $(this).form('validate');
				//如果选择已离职，离职时间不能为空
				if ($("input[name='ud_hasQuitCompany']:checked").val() == 1 && !Common.checkNotNull("离职时间",$("#ud_quitCompanyDate").val())) {
					isValid = false;
				}
				if (!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success: function(data){
				$.messager.progress('close');
				var data = eval('(' + data + ')');
				if (data.success) {
					Component.$grid.datagrid('reload');
					$.messager.alert("提示", "操作成功");
					$("#quitCompanyPopWin").window('close');
				} else {
					$.messager.alert('提示','操作失败!');
				}
			}
		});
		$("#quitCompanyBtn").click(function(){
			var selectNodes = Component.$grid.datagrid('getSelections');
			if (selectNodes != null) {
				$("input[name=ud_hasQuitCompany][value=1]").attr("checked",'checked');
				
				$("#ud_quitCompanyType").val(selectNodes[0].quitCompanyType);
				$("#ud_quitCompanyDate").val(selectNodes[0].quitCompanyDate);
				$("#ud_quitCompanyResult").textbox("setValue",selectNodes[0].quitCompanyResult);
				$("#quitCompany_form").form({
					queryParams : {employees : JSON.stringify(selectNodes)}
				});
				$("#quitCompanyPopWin").window('open');
				$("#quitCompanyPopWin").window('center');
			} else {
				$.messager.alert('提示','请选择一条需要操作的记录!');
			}
		});
		//离职提交
		$("#quitCompany_submitBtn").click(function(){
			$.messager.progress({msg:'请稍后...'});
			$('#quitCompany_form').form('submit');
		});

		//提交审核
		$("#sendAuditBtn").linkbutton({
			onClick : function(){
				var selectNodes = Component.$grid.datagrid('getSelections');
				if (selectNodes != null) {
					$.messager.confirm('提示', '是否提交审核?', function(r){
						if (r){
							$.ajax({
								url : Constant.$employeeAudit_url,
								type : 'post',
								dataType : 'json',
								data : {
									auditStatus : 1,
									employees : JSON.stringify(selectNodes)
								},
								success : function(data) {
									if (data.success) {
										Component.$grid.datagrid('reload');
										$.messager.alert("提示", "操作成功");
									} else {
										$.messager.alert("错误", data.msg, "error");
									}
								},
								error : function(re, status, err) {
									$.messager.alert("错误", re.responseText, "error");
								}
							})
						}
					});
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		});

		//审核
		$("#audit_form").form({
			url : Constant.$employeeAudit_url,
			onSubmit : function(){
				var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success: function(data){
				$.messager.progress('close');
				var data = eval('(' + data + ')');
				if (data.success) {
					Component.$grid.datagrid('reload');
					$.messager.alert("提示", "操作成功");
					$("#audit_PopWin").window('close');
				} else {
					$.messager.alert('提示','操作失败!');
				}
			}
		});
		$("#auditBtn").click(function(){
			var selectNodes = Component.$grid.datagrid('getSelections');
			if (selectNodes != null) {
				$("#audit_form").form({
					queryParams : {employees : JSON.stringify(selectNodes)}
				});
				$("#audit_PopWin").window('open');
				$("#audit_PopWin").window('center');
			} else {
				$.messager.alert('提示','请选择需要操作的记录!');
			}
		});
		//审核提交
		$("#audit_submitBtn").click(function(){
			$.messager.progress({msg:'请稍后...'});
			$('#audit_form').form('submit');
		});

		//得到部门下拉框
		$.ajax({
			url : Constant.$dept_combo_url,
			type : 'post',
			dataType : 'json',
			data : {},
			success : function(data) {
				if (data.success) {
					$("#employeeDept").empty();
					$("#employeeDept").append("<option value=''>--请选择--</option>");
					$.each(data.model, function(i, n){
						$("#employeeDept").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});
		
		//得到岗位下拉框
		$("#employeePostType").change(function(){
			$("#employeePost").empty();
			$("#employeePost").append("<option value=''>--请选择--</option>");
			if($("#employeePostType").val() != ''){
				$.ajax({
					url : Constant.$post_combo_url,
					type : 'post',
					dataType : 'json',
					data : {type:$("#employeePostType").val()},
					success : function(data) {
						if (data.success) {
							$.each(data.model, function(i, n){
								$("#employeePost").append("<option value='" + n.id + "'>" + n.name + "</option>");
							});
						} else {
							$.messager.alert("错误", data.msg, "error");
						}
					},
					error : function(re, status, err) {
						$.messager.alert("错误", re.responseText, "error");
					}
				})
			}
		});
		
	},
	annexFormatter : function(value,row,index){
		var result = "";
		if (row.annexs != null && row.annexs.length > 0){
			for(var i = 0;i < row.annexs.length;i++){
				var annex = row.annexs[i];
				result += "<a href=\"javascript:Interactive.annexDownload('" + row.id + "','" + annex.id + "')\">" + annex.annexName + "</a>";
				result += "&nbsp;";
				if (hasAnnexDelete) {
					result += "<a href=\"javascript:Interactive.annexDelete('" + row.id + "','" + annex.id + "')\">删除</a>";
				}
				result += "<br />";
			}
		}
		return result;
	},
	annexDownload : function(employeeId,annexId){
		window.open(Constant.$employeeAnnexDownload_url + "?id=" + employeeId + "&annexType=annex&annexId=" + annexId);
	},
	annexDelete : function(employeeId,annexId){
		$.messager.confirm('提示', '确定删除？', function(r){
			if (r){
				$.ajax({
					url : Constant.$employeeAnnexDelete_url,
					type : 'post',
					dataType : 'json',
					data : {
						id : employeeId,
						annexType : 'annex',
						annexId : annexId
					},
					success : function(data) {
						if (data.success) {
							Component.$grid.datagrid('reload');
							$.messager.alert("提示", "删除成功");
						} else {
							$.messager.alert("错误", data.message, "error");
						}
					},
					error : function(re, status, err) {
						$.messager.alert("错误", re.responseText, "error");
					}
				});
			}
		});
	},
	sexFormatter : function(value,row,index){
		if (value == 1) {
			return '男';
		}
		return '女';
	},
	youwuFormatter : function(value,row,index){
		if (value == 1) {
			return '有';
		}
		return '无';
	},
	shifouFormatter : function(value,row,index){
		if (value == 1) {
			return '是';
		}
		return '否';
	},
	auditFormatter : function(value,row,index){
		if (row.auditContent == null) {
			return value;
		}
		return "<div title='" + row.auditContent + "'>" + value + "</div>";
	},
	browseFormatter : function(value,row,index){
		return '<a href="javascript:Interactive.browseDetail(\'' + row.id + '\');" class="easyui-linkbutton">档案详情</a>';
	},
	browseDetail : function(id){
		Common.openPostWindow(Constant.$detail_url,id,"_blank");
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});