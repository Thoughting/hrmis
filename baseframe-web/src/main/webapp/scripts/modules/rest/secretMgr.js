// 用户管理
$.namespace('sys.security.roleMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/sys/rest/secret/list';
		Constant.$addEdit_form_url = basePath + "/api/sys/rest/secret/addOrUpdate";
		Constant.$delete_url = basePath + "/api/sys/rest/secret/delete";
		Constant.$reset_url = basePath + "/api/sys/rest/secret/reset";
		Constant.$authResourceTreeList = basePath + "/api/sys/rest/secret/authRestResourceTreeList";
		Constant.$updateAuth_url = basePath + "/api/sys/rest/secret/updateAuth";
	}
}

//界面组件
Component = {
	init : function(){
		Component.$addAndEditPopWin = $("#addAndEditPopWin");
		Component.$authTreePopWin = $("#authTreePopWin");
		
		Component.$grid = $("#datagrid");
		Component.$grid.datagrid({
			url : Constant.$grid_list_url,
			onBeforeLoad : function(param){
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
					$.messager.alert('提示','操作失败!');
				}
			}
		});
		
		Component.$authResourceTree = $("#authResourceTree");
	}
}

//界面交互
Interactive = {
	init : function(){
		//新增
		$("#addBtn").linkbutton({
			onClick : function(){
				Component.$addEditForm.form("reset");
				Component.$addAndEditPopWin.window('open');
				Component.$addAndEditPopWin.window('center');
				
				Component.$addEditForm.form({
					queryParams : {}
				});
				
				$("#ud_code").textbox("enable");
			}
		});
		
		//编辑
		$("#editBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$grid.datagrid('getSelected');
				if (selectNode != null) {
					$("#ud_code").textbox("setValue",selectNode.code);
					$("#ud_type").val(selectNode.type);
					$("#ud_enable").val(selectNode.enable);
					$("#ud_remark").textbox("setValue",selectNode.remark);
					Component.$addEditForm.form({
						queryParams : {id : selectNode.id}
					});
					Component.$addAndEditPopWin.window('open');
					Component.$addAndEditPopWin.window('center');
					
					$("#ud_code").textbox("disable");
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		});
		
		//删除
		$("#delBtn").linkbutton({
			onClick : function(){
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
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		})
		
		//重置
		$("#resetBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$grid.datagrid('getSelected');
				if (selectNode != null) {
					$.messager.confirm('提示', '确定重置SECRET？', function(r){
						if (r){
							$.ajax({
								url : Constant.$reset_url,
								type : 'post',
								dataType : 'json',
								data : {id : selectNode.id},
								success : function(data) {
									if (data.success) {
										Component.$grid.datagrid('reload');
										$.messager.alert("提示", "重置成功");
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
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		})
		
		//授权
		$("#addAuthBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$grid.datagrid('getSelected');
				if (selectNode != null) {
					Component.$authResourceTree.tree({
						url : Constant.$authResourceTreeList,
						onBeforeLoad : function(node, param){
							param.id = selectNode.id;
						}
					});
					Component.$authTreePopWin.window('open');
					Component.$authTreePopWin.window('center');
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		})
		
		//授权确认
		$("#confirmAuthBtn").linkbutton({
			onClick : function(){
				var checkResourceNodes = Component.$authResourceTree.tree('getChecked');
				var selectNode = Component.$grid.datagrid('getSelected');
				if (checkResourceNodes != null && selectNode != null) {
					$.messager.progress({msg:'请稍后...'});
					$.ajax({
						url : Constant.$updateAuth_url,
						type : 'post',
						dataType : 'json',
						data : {
							checkResourceNodes : JSON.stringify(checkResourceNodes),
							selectNode : JSON.stringify(selectNode)
						},
						success : function(data) {
							$.messager.progress('close');
							if (data.success) {
								Component.$authTreePopWin.window('close');
								Component.$grid.datagrid('reload');
								$.messager.alert("提示", "操作成功");
							} else {
								$.messager.alert("错误", data.msg, "error");
							}
						},
						error : function(re, status, err) {
							$.messager.progress('close');
							$.messager.alert("错误", re.responseText, "error");
						}
					});
				}
			}
		})
		
		//编辑框提交
		$("#addEditSubmitBtn").on("click",function(){
			$.messager.progress({msg:'请稍后...'});
			$('#addEditForm').form('submit');
		});
		
		//编辑框取消
		$("#addEditCancleBtn").on("click",function(){
			Component.$addAndEditPopWin.window('close');
		})
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});