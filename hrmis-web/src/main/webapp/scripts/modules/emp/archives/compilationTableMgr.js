// 用户管理
$.namespace('sys.security.roleMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/compilationtable/list';
		Constant.$addEdit_form_url = basePath + "/api/emp/compilationtable/addOrUpdate";
		Constant.$delete_url = basePath + "/api/emp/compilationtable/delete";
		
		Constant.$setDeptTreeList = basePath + "/api/emp/compilationtable/deptTreeList";
		Constant.$setPostTreeList = basePath + "/api/emp/compilationtable/postTreeList";
		Constant.$updateSet_url = basePath + "/api/emp/compilationtable/updateDeptPostSet";
	}
}

//界面组件
Component = {
	init : function(){
		Component.$addAndEditPopWin = $("#addAndEditPopWin");
		Component.$setTreePopWin = $("#setTreePopWin");
		
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
		
		Component.$setDeptTree = $("#setDeptTree");
		Component.$setPostTree = $("#setPostTree");
	}
}

//界面交互
Interactive = {
	init : function(){
		//新增
		$("#addBtn").linkbutton({
			onClick : function(){
				Component.$addEditForm.form({
					queryParams : {}
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
					$("#ud_name").textbox("setValue",selectNode.name);
					$("#ud_nameCn").textbox("setValue",selectNode.nameCn);
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
		
		//配置
		$("#addSettingBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$grid.datagrid('getSelected');
				if (selectNode != null) {
					Component.$setDeptTree.tree({
						url : Constant.$setDeptTreeList,
						onBeforeLoad : function(node, param){
							param.id = selectNode.id;
						}
					});
					Component.$setPostTree.tree({
						url : Constant.$setPostTreeList,
						onBeforeLoad : function(node, param){
							param.id = selectNode.id;
						}
					});
					Component.$setTreePopWin.window('open');
					Component.$setTreePopWin.window('center');
				} else {
					$.messager.alert('提示','请选择需要操作的记录!');
				}
			}
		})
		
		//配置确认
		$("#confirmSetBtn").linkbutton({
			onClick : function(){
				var checkDeptNodes = Component.$setDeptTree.tree('getChecked');
				var checkPostNodes = Component.$setPostTree.tree('getChecked');
				var selectNode = Component.$grid.datagrid('getSelected');
				if (checkDeptNodes != null && checkPostNodes != null && selectNode != null) {
					$.messager.progress({msg:'请稍后...'});
					$.ajax({
						url : Constant.$updateSet_url,
						type : 'post',
						dataType : 'json',
						data : {
							checkDeptNodes : JSON.stringify(checkDeptNodes),
							checkPostNodes : JSON.stringify(checkPostNodes),
							selectNode : JSON.stringify(selectNode)
						},
						success : function(data) {
							$.messager.progress('close');
							if (data.success) {
								Component.$setTreePopWin.window('close');
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