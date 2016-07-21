// 用户管理
$.namespace('emp.contract.contractTypeMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/contract/list';
		Constant.$addEdit_form_url = basePath + "/api/emp/contract/addOrUpdate";
		Constant.$delete_url = basePath + "/api/emp/contract/delete";
		Constant.$annexUpload_url = basePath + "/api/emp/contract/annexUpload";
		Constant.$annexDownload_url = basePath + "/api/emp/contract/annexDownload";
		Constant.$annexDelete_url = basePath + "/api/emp/contract/annexDelete";
		
	}
}

//界面组件
Component = {
	init : function(){
		Component.$addAndEditPopWin = $("#addAndEditPopWin");
		
		Component.$grid = $("#datagrid");
		Component.$grid.datagrid({
			url : Constant.$grid_list_url,
			onBeforeLoad : function(param){
				param.type = $("#contractType").val();
				param.code = $("#code").val();
				param.name = $("#name").val();
				param.keyDesr = $("#keyDesr").val();
			}
		});
		
		Component.$addEditForm = $('#addEditForm');
		Component.$addEditForm.form({
			url : Constant.$addEdit_form_url,
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
					Component.$addAndEditPopWin.window('close');
				} else {
					$.messager.alert('提示',data.message);
				}
			}
		});
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
				Component.$addEditForm.form({
					queryParams : {
					}
				});
				Component.$addEditForm.form("reset");
				Component.$addAndEditPopWin.window('open');
				Component.$addAndEditPopWin.window('center');
			}
		});
		
		//编辑
		$("#editBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$grid.datagrid('getSelected');
				if (selectNode != null) {
					$("#ud_contractType").val(selectNode.type);
					$("#ud_code").textbox("setValue",selectNode.code);
					$("#ud_name").textbox("setValue",selectNode.name);
					$("#ud_keyDesr").textbox("setValue",selectNode.keyDesr);
					Component.$addEditForm.form({
						queryParams : {id : selectNode.id}
					});
					Component.$addAndEditPopWin.window('open');
					Component.$addAndEditPopWin.window('center');
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		});
		
		//删除
		$("#delBtn").linkbutton({
			onClick : function(){
				var selectNodes = Component.$grid.datagrid('getSelections');
				if (selectNodes != null && selectNodes.length > 0) {
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
										$.messager.alert("错误", data.msg, "error");
									}
								},
								error : function(re, status, err) {
									$.messager.alert("错误", re.responseText, "error");
								}
							});
						}
					});
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		})
		
		//附件上传
		$("#uploadBtn").linkbutton({
			onClick:function(){
				var selectNodes = Component.$grid.datagrid('getSelections');
				if (selectNodes != null && selectNodes.length == 1) {
					Page.upload.open(Constant.$annexUpload_url + "?contractId=" + selectNodes[0].id, function() {
						Component.$grid.datagrid('reload');
					});
				} else {
					$.messager.alert('提示','请选择一条需要操作的记录!');
				}
			}
		});
		
		//编辑框提交
		$("#addEditSubmitBtn").on("click",function(){
			$.messager.progress({msg:'请稍后...'});
			$('#addEditForm').form('submit');
		});
		
		//编辑框取消
		$("#addEditCancleBtn").on("click",function(){
			Component.$addAndEditPopWin.window('close');
		})
		
	},
	contractAnnexFormatter : function(value,row,index){
		var result = "";
		if (row.annexs != null && row.annexs.length > 0){
			for(var i = 0;i < row.annexs.length;i++){
				var annex = row.annexs[i];
				result += "<a href=\"javascript:Interactive.contractAnnexDownload('" + annex.id + "')\">" + annex.annexName + "</a>";
				result += "&nbsp;";
				if (hasDelete) {
					result += "<a href=\"javascript:Interactive.contractAnnexDelete('" + annex.id + "')\">删除</a>";
				}
				result += "<br />";
			}
		}
		return result;
	},
	contractAnnexDownload : function(annexId){
		window.open(Constant.$annexDownload_url + "/" + annexId);
	},
	contractAnnexDelete : function(annexId){
		$.messager.confirm('提示', '确定删除？', function(r){
			if (r){
				$.ajax({
					url : Constant.$annexDelete_url,
					type : 'post',
					dataType : 'json',
					data : {annexId : annexId},
					success : function(data) {
						if (data.success) {
							Component.$grid.datagrid('reload');
							$.messager.alert("提示", "删除成功");
						} else {
							$.messager.alert("错误", data.msg, "error");
						}
					},
					error : function(re, status, err) {
						$.messager.alert("错误", re.responseText, "error");
					}
				});
			}
		});
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});