// 用户管理
$.namespace('emp.contract.contractTypeMgr');

//界面变量
Constant = {
	init : function(){
		//员工
		Constant.$delete_url = basePath + "/api/emp/info/delete";
		Constant.$addEdit_form_url = basePath + "/api/emp/info/addOrUpdate";
		Constant.$detail_url = basePath + "/api/emp/info/detail";
		
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		Constant.$post_combo_url = basePath + '/api/emp/post/combo';
		Constant.$wageplan_combo_url = basePath + '/api/emp/wageplan/combo';
		
		Constant.$header_img_url = basePath + '/api/emp/info/headerImg';
		Constant.$header_upload_url = basePath + '/api/emp/info/headerUpload';
		
		//员工家庭成员
		Constant.$family_grid_list_url = basePath + '/api/emp/family/list';
		Constant.$family_addEdit_form_url = basePath + "/api/emp/family/addOrUpdate";
		Constant.$family_delete_url = basePath + "/api/emp/family/delete";
		
		//员工学习经历
		Constant.$study_grid_list_url = basePath + '/api/emp/study/list';
		Constant.$study_addEdit_form_url = basePath + "/api/emp/study/addOrUpdate";
		Constant.$study_delete_url = basePath + "/api/emp/study/delete";
		
		//员工工作经历
		Constant.$work_grid_list_url = basePath + '/api/emp/work/list';
		Constant.$work_addEdit_form_url = basePath + "/api/emp/work/addOrUpdate";
		Constant.$work_delete_url = basePath + "/api/emp/work/delete";
		
		//员工附件
		Constant.$employeeAnnexList_url = basePath + "/api/emp/info/employeeAnnexList";
		Constant.$employeeAnnexUpload_url = basePath + "/api/emp/info/employeeAnnexUpload";
		Constant.$employeeAnnexDownload_url = basePath + "/api/emp/info/employeeAnnexDownload";
		Constant.$employeeAnnexDelete_url = basePath + "/api/emp/info/employeeAnnexDelete";
		
		//员工审核
		Constant.$employeeAudit_url = basePath + "/api/emp/info/audit";
		
		//员工编号自动生成
		Constant.$employeeCodeAutoCreate_url = basePath + "/api/emp/dept/newEmpCode";
		
	}
}

//界面组件
Component = {
	init : function(){
		
		//员工家庭成员
		Component.$family_grid = $("#family_datagrid");
		Component.$family_grid.datagrid({
			url : Constant.$family_grid_list_url,
			onBeforeLoad : function(param){
				param.employeeId = employeeId;
			}
		});
		
		//员工学习经历
		Component.$study_grid = $("#study_datagrid");
		Component.$study_grid.datagrid({
			url : Constant.$study_grid_list_url,
			onBeforeLoad : function(param){
				param.employeeId = employeeId;
			}
		});
		
		//员工工作经历
		
		Component.$work_grid = $("#work_datagrid");
		Component.$work_grid.datagrid({
			url : Constant.$work_grid_list_url,
			onBeforeLoad : function(param){
				param.employeeId = employeeId;
			}
		});
	}
}

//界面交互
Interactive = {
	init : function(){

		//得到员工头像
		Interactive.getHeadImg();
		
		//员工合同首页附件上传
		Interactive.employeeAnnexList("contractHome");
		
		//员工合同尾页附件上传
		Interactive.employeeAnnexList("contractFinal");
		
		//员工签收表附件上传
		Interactive.employeeAnnexList("signForm");
		
		//员工不购承诺附件上传
		Interactive.employeeAnnexList("nonPurchaseCommit");
		
		//职位变更附件上传
		Interactive.employeeAnnexList("postChangeAnnex");
		
		//部门变更附件上传
		Interactive.employeeAnnexList("deptChangeAnnex");
		
		//操作变更附件上传
		Interactive.employeeAnnexList("operaChangeAnnex");
		
		//工资变更附件上传
		Interactive.employeeAnnexList("wageChangeAnnex");

	},
	getHeadImg:function(){
		//得到用户头像
		$.ajax({
			url : Constant.$header_img_url,
			type : 'post',
			dataType : 'json',
			data : {id : employeeId},
			success : function(data) {
				$("#headerImg").attr("src",basePath + data.model);
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		})
	},
	employeeAnnexList : function(annexType){
		$.ajax({
			url : Constant.$employeeAnnexList_url,
			type : 'post',
			dataType : 'json',
			data : {id:employeeId ,employeeId : employeeId,annexType:annexType},
			success : function(data) {
				var result = "";
				if (data.message == "signForm") {
					//签到表
					$("#signFormAnnex_container").empty();
					if (data.model != null) {
						result += "<span>" + data.model + "</span>";
						result += "&nbsp;";
						result += "<br />";
						$("#signFormAnnex_container").append(result);
					}
				} else if (data.message == "nonPurchaseCommit"){
					//不购承诺
					$("#nonPurchaseCommitAnnex_container").empty();
					if (data.model != null) {
						result += "<span>" + data.model + "</span>";
						result += "&nbsp;";
						result += "<br />";
						$("#nonPurchaseCommitAnnex_container").append(result);
					}
				} else if (data.message == "contractHome"){
					//合同首页
					$("#homeAnnex_container").empty();
					if (data.model != null && data.model.length > 0) {
						for(var i = 0;i < data.model.length;i++){
							var model = data.model[i];
							result += "<span>" + model.annexName + "</span>";
							result += "&nbsp;";
							result += "<br />";
						}
						$("#homeAnnex_container").append(result);
					}
				} else if (data.message == "contractFinal"){
					//合同尾页
					$("#finalAnnex_container").empty();
					if (data.model != null && data.model.length > 0) {
						for(var i = 0;i < data.model.length;i++){
							var model = data.model[i];
							result += "<span>" + model.annexName + "</span>";
							result += "&nbsp;";
							result += "<br />";
						}
						$("#finalAnnex_container").append(result);
					}
				} else if (data.message == "postChangeAnnex"){
					//职位变更
					$("#postChangeAnnex_container").empty();
					if (data.model != null) {
						result += "<span>" + data.model + "</span>";
						result += "&nbsp;";
						result += "<br />";
						$("#postChangeAnnex_container").append(result);
					}
				} else if (data.message == "deptChangeAnnex"){
					//部门变更
					$("#deptChangeAnnex_container").empty();
					if (data.model != null) {
						result += "<span>" + data.model + "</span>";
						result += "&nbsp;";
						result += "<br />";
						$("#deptChangeAnnex_container").append(result);
					}
				} else if (data.message == "operaChangeAnnex"){
					//操作变更
					$("#operaChangeAnnex_container").empty();
					if (data.model != null) {
						result += "<span>" + data.model + "</span>";
						result += "&nbsp;";
						result += "<br />";
						$("#operaChangeAnnex_container").append(result);
					}
				} else if (data.message == "wageChangeAnnex"){
					//工资变更
					$("#wageChangeAnnex_container").empty();
					if (data.model != null) {
						result += "<span>" + data.model + "</span>";
						result += "&nbsp;";
						result += "<br />";
						$("#wageChangeAnnex_container").append(result);
					}
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		})
	},
	employeeAnnexUpload : function(annexType){
		Page.upload.open(Constant.$employeeAnnexUpload_url + "?content=" + employeeId + "_" + annexType, function() {
			Interactive.employeeAnnexList(annexType);
		});
	},
	employeeAnnexDownload : function(annexType,annexId){
		window.open(Constant.$employeeAnnexDownload_url + "?id=" + employeeId + "&annexType=" + annexType + "&annexId=" + annexId);
	},
	employeeAnnexDelete : function(annexType,annexId){
		$.messager.confirm('提示', '确定删除？', function(r){
			if (r){
				$.ajax({
					url : Constant.$employeeAnnexDelete_url,
					type : 'post',
					dataType : 'json',
					data : {
						id : employeeId,
						annexType : annexType,
						annexId : annexId
					},
					success : function(data) {
						if (data.success) {
							Interactive.employeeAnnexList(data.model);
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
	getNewEmployeeCode : function(){
		$.ajax({
			url : Constant.$employeeCodeAutoCreate_url,
			type : 'post',
			dataType : 'json',
			data : {id:$("#employeeDept").val(),employeeId:employeeId},
			success : function(data) {
				if (data.success) {
					$("#code").val(data.model);
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});