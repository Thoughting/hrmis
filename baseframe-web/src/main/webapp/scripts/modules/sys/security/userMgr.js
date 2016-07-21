// 用户管理
$.namespace('sys.security.userMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/sys/user/list';
		Constant.$addEdit_form_url = basePath + "/api/sys/user/addOrUpdate";
		Constant.$delete_url = basePath + "/api/sys/user/delete";
		Constant.$authOfficeTreeList_url = basePath + "/api/sys/user/authOfficeTreeList";
		Constant.$authRoleGrid_url = basePath + "/api/sys/user/authRoleGridList";
		Constant.$updateAuth_url = basePath + "/api/sys/user/updateAuth";
		
		Constant.$officeTreeList = basePath + "/api/sys/department/treeList";
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
				param.key = $("#searchText").textbox("getValue");
				var officeTreeSelectNode = $("#officeTree").tree("getSelected");
				if (officeTreeSelectNode != null) {
					param.departmentId = officeTreeSelectNode.id;
				}
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
		
		Component.$officeTree = $("#officeTree");
		Component.$officeTree.tree({
			url : Constant.$officeTreeList,
			onBeforeLoad : function(node, param){
				param.needRoot = 1;
			},
			onClick : function(node){
				Component.$grid.datagrid("reload");
			}
		})
		
		Component.$authRoleDataGrid = $("#authRoleDataGrid");
		Component.$authOfficeTree = $("#authOfficeTree");
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
				
				$("#ud_loginName").textbox("enable");
				$("#ud_password").textbox("enable");
			}
		});
		
		//编辑
		$("#editBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$grid.datagrid('getSelected');
				if (selectNode != null) {
					$("#ud_loginName").textbox("setValue",selectNode.loginName);
					$("#ud_name").textbox("setValue",selectNode.name);
					$("#ud_code").textbox("setValue",selectNode.code);
					$("#ud_email").textbox("setValue",selectNode.email);
					$("#ud_mobile").textbox("setValue",selectNode.mobile);
					$("#ud_userType").val(selectNode.userType);
					Component.$addEditForm.form({
						queryParams : {id : selectNode.id}
					});
					Component.$addAndEditPopWin.window('open');
					Component.$addAndEditPopWin.window('center');
					
					$("#ud_loginName").textbox("disable");
					$("#ud_password").textbox("disable");
					$("#ud_password").textbox("setValue","*******");
					
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
		
		//授权
		$("#authBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$grid.datagrid('getSelected');
				if (selectNode != null) {
					Component.$authRoleDataGrid.datagrid({
						url : Constant.$authRoleGrid_url,
						onBeforeLoad : function(param){
							param.id = selectNode.id;
						},
						onLoadSuccess : function(data){
							if (data.rows != null && data.rows.length > 0) {
								for(var i = 0;i < data.rows.length;i++){
									if (data.rows[i].checked) {
										Component.$authRoleDataGrid.datagrid("checkRow",i);
									}
								}
							}
						}
					});
					Component.$authOfficeTree.tree({
						url : Constant.$authOfficeTreeList_url,
						onBeforeLoad : function(node, param){
							param.id = selectNode.id;
						}
					})
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
				var roleNodes = Component.$authRoleDataGrid.datagrid('getChecked');
				var officeNodes = Component.$authOfficeTree.tree('getChecked');
				var userNode = Component.$grid.datagrid('getSelected');
				if (roleNodes != null && officeNodes != null && userNode != null) {
					$.ajax({
						url : Constant.$updateAuth_url,
						type : 'post',
						dataType : 'json',
						data : {
							userId : userNode.id,
							roleNodes : JSON.stringify(roleNodes),
							officeNodes : JSON.stringify(officeNodes)
						},
						success : function(data) {
							if (data.success) {
								$.messager.alert("提示", "操作成功");
								Component.$grid.datagrid('reload');
								Component.$authTreePopWin.window('close');
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