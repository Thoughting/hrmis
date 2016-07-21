// 用户管理
$.namespace('emp.contract.contractTypeMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/sys/dict/list';
		Constant.$addEdit_form_url = basePath + "/api/sys/dict/addOrUpdate";
		Constant.$delete_url = basePath + "/api/sys/dict/delete";
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
				param.parentId = "402816815281ce610152820205ed00c1";
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
		//新增
		$("#addBtn").linkbutton({
			onClick : function(){
				Component.$addEditForm.form({
					queryParams : {
						parentId : "402816815281ce610152820205ed00c1"
					}
				});
				Component.$addEditForm.form("reset");
				Component.$addAndEditPopWin.window('open');
				Component.$addAndEditPopWin.window('center');
				
				$("#ud_code").textbox("enable");
			}
		});
		
		//编辑
		$("#editBtn").linkbutton({
			onClick : function(){
				var selectNode = Component.$grid.datagrid('getSelected');
				if (selectNode != null) {
					$("#ud_code").textbox("setValue",selectNode.code);
					$("#ud_name").textbox("setValue",selectNode.name);
					$("#ud_description").textbox("setValue",selectNode.description);
					Component.$addEditForm.form({
						queryParams : {id : selectNode.id,type : "3000" }
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
		$("#delBtn").on("click",function(){
			var selectNode = Component.$grid.datagrid('getSelected');
			if (selectNode != null) {
				$.messager.confirm('提示', '该节点及其所有子节点删除后将不可恢复！确定删除？', function(r){
					if (r){
						$.ajax({
							url : Constant.$delete_url,
							type : 'post',
							dataType : 'json',
							data : Component.$grid.datagrid('getSelected'),
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
		
		//字典类型下拉框改变
		$("#dictselect").bind("change",function(){
			Component.$grid.datagrid('reload');
		})
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});