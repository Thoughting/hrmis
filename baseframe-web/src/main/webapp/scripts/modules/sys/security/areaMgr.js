// 用户管理
$.namespace('sys.security.areaMgr');

//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/sys/area/treeList';
		Constant.$addEdit_form_url = basePath + "/api/sys/area/addOrUpdate";
		Constant.$delete_url = basePath + "/api/sys/area/delete";
		Constant.$saveSort_url = basePath + "/api/sys/area/saveSort";
	},
	//得到树图父子关系以及排序信息
	getTreeCascadeSortData : function(treeData,sortData){
		if(treeData != null && treeData.length > 0){
			for(var i = 0;i < treeData.length; i++){
				var item = {
					id:treeData[i].id,
					name:treeData[i].name,
					parentId:treeData[i]._parentId,
					sort:i
				};
				sortData.push(item);
				if(treeData[i].children != null && treeData[i].children.length > 0){
					Constant.getTreeCascadeSortData(treeData[i].children,sortData);
				}
			}
		}
	}
}

//界面组件
Component = {
	init : function(){
		Component.$addAndEditPopWin = $("#addAndEditPopWin");
		
		Component.$grid = $("#datagrid");
		Component.$grid.treegrid({
			url : Constant.$grid_list_url,
			onBeforeLoad : function(row, param){
				param.key = $("#searchText").textbox("getValue");
			}, 
			onLoadSuccess: function(row){
				$(this).treegrid('enableDnd', row ? row.id:null);
			},
			onDrop : function(targetRow,sourceRow,point) {
				$("#saveSort").linkbutton('enable');
				$("#cancelSort").linkbutton('enable');
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
					if (data.message == "add") {
						Component.$grid.treegrid('append',{
							parent: data.model.parentId,
							data: [data.model]
						});
					} else if (data.message == "update"){
						Component.$grid.treegrid('update',{
							id: data.model.id,
							row: data.model
						});
					}
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
		//新增子节点
		$("#addChildrenBtn").on("click", function(){
			var selectNode = Component.$grid.treegrid('getSelected');
			var params = {parentId : null}
			if (selectNode != null) {
				params = {parentId : selectNode.id}
			}
			Component.$addEditForm.form({
				queryParams : params
			});
			Component.$addEditForm.form("reset");
			Component.$addAndEditPopWin.window('open');
			Component.$addAndEditPopWin.window('center');
			
			$("#ud_code").textbox("enable");
		});
		
		//新增兄弟节点
		$("#addBrotherBtn").on("click",function(){
			var selectNode = Component.$grid.treegrid('getSelected');
			var params = {parentId : null}
			if (selectNode != null) {
				params = {parentId : selectNode.parentId}
			}
			Component.$addEditForm.form({
				queryParams : params
			});
			Component.$addEditForm.form("reset");
			Component.$addAndEditPopWin.window('open');
			Component.$addAndEditPopWin.window('center');
			
			$("#ud_code").textbox("enable");
		})
		
		//编辑
		$("#editBtn").on("click",function(){
			var selectNode = Component.$grid.treegrid('getSelected');
			if (selectNode != null) {
				$("#ud_code").textbox("setValue",selectNode.code);
				$("#ud_name").textbox("setValue",selectNode.name);
				$("#ud_type").val(selectNode.type);
				$("#ud_remarks").textbox("setValue",selectNode.remarks);
				Component.$addEditForm.form({
					queryParams : {id : selectNode.id}
				});
				Component.$addAndEditPopWin.window('open');
				Component.$addAndEditPopWin.window('center');
				
				$("#ud_code").textbox("disable");
			} else {
				$.messager.alert('提示','请选择需要操作的记录!');
			}
		})
		
		//删除
		$("#delBtn").on("click",function(){
			var selectNode = Component.$grid.treegrid('getSelected');
			if (selectNode != null) {
				$.messager.confirm('提示', '该节点及其所有子节点删除后将不可恢复！确定删除？', function(r){
					if (r){
						$.ajax({
							url : Constant.$delete_url,
							type : 'post',
							dataType : 'json',
							data : Component.$grid.treegrid('getSelected'),
							success : function(data) {
								if (data.success) {
									Component.$grid.treegrid('remove',data.model);
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
		
		//保存排列顺序
		$("#saveSort").linkbutton({
			onClick : function(){
				var treeData = Component.$grid.treegrid('getData');
				var sortData = new Array();
				Constant.getTreeCascadeSortData(treeData,sortData);
				$.messager.confirm('提示', '确定保存吗?', function(r){
					if (r){
						$.ajax({
							url : Constant.$saveSort_url,
							type : 'post',
							data : {sortJson : JSON.stringify(sortData)},
							success : function(data) {
								var data = eval('(' + data + ')');
								if (data.success) {
									$("#saveSort").linkbutton('disable');
									$("#cancelSort").linkbutton('disable');
									$.messager.alert('提示','保存成功!');
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
		})
		
		//取消更改
		$("#cancelSort").linkbutton({
			onClick : function(){
				$("#saveSort").linkbutton('disable');
				$("#cancelSort").linkbutton('disable');
				Component.$grid.treegrid("reload");
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