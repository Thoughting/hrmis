// 用户管理
$.namespace('sys.security.roleMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/sys/rest/resource/list';
		Constant.$addEdit_form_url = basePath + "/api/sys/rest/resource/addOrUpdate";
		Constant.$delete_url = basePath + "/api/sys/rest/resource/delete";
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
			}
		});
		
		//编辑
		$("#editBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$grid.datagrid('getSelected');
				if (selectNode != null) {
					$("#ud_name").textbox("setValue",selectNode.name);
					$("#ud_permission").textbox("setValue",selectNode.permission);
					$("#ud_url").textbox("setValue",selectNode.url);
					$("#ud_remark").textbox("setValue",selectNode.remark);
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