// 用户管理
$.namespace('emp.contract.contractTypeMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/deptleader/list';
		Constant.$addEdit_form_url = basePath + "/api/emp/deptleader/addOrUpdate";
		Constant.$delete_url = basePath + "/api/emp/deptleader/delete";
		
		//部门树图
		Constant.$dept_tree_url = basePath + "/api/emp/dept/tree";
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
			}
		});
		
		Component.$deptTree = $("#deptTree");
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
		
		//新增
		$("#addBtn").linkbutton({
			onClick : function(){
				$("#addEditForm").form("reset");
				deptLeaderId = "";
				
				Component.$deptTree.tree({
					url : Constant.$dept_tree_url,
					onBeforeLoad : function(node, param){
					}
				});
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
					$("#ud_remark").textbox("setValue",selectNode.remark);
					deptLeaderId = selectNode.id;
					Component.$deptTree.tree({
						url : Constant.$dept_tree_url,
						onBeforeLoad : function(node, param){
							param.leaderId = selectNode.id;
						}
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
		
		//编辑框提交
		$("#addEditSubmitBtn").on("click",function(){
			$.messager.progress({msg:'请稍后...'});
			var checkDeptNodes = Component.$deptTree.tree('getChecked');
			$.ajax({
				url : Constant.$addEdit_form_url,
				type : 'post',
				dataType : 'json',
				data : {
					id : deptLeaderId,
					checkDeptNodes : JSON.stringify(checkDeptNodes),
					name : $("#ud_name").val(),
					remark : $("#ud_remark").val()
				},
				success : function(data) {
					$.messager.progress('close');
					if (data.success) {
						Component.$addAndEditPopWin.window('close');
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