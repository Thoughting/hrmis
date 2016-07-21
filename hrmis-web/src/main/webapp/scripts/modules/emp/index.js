//界面变量
Constant = {
	init : function(){
		Constant.$dbGrid_list_url = basePath + '/api/emp/agency/order/db_list';
		Constant.$ybGrid_list_url = basePath + '/api/emp/agency/order/yb_list';
		
		//审核
		Constant.$dbGrid_confirm_url = basePath + "/api/emp/agency/order/db_confirm";
	}
}

//界面组件
Component = {
	init : function(){
		//待办
		Component.$dbGrid = $("#dbGrid");
		Component.$dbGrid.datagrid({
			url : Constant.$dbGrid_list_url,
			onBeforeLoad : function(param){
				param.createBy = $('#db_createBy').val();
			},
			onDblClickRow : function(rowIndex, rowData){
				//双击代办工单确认
				switch(rowData.type){
				case 0:
				case 1:
				case 2:
					$.messager.confirm('提示', '是否已处理？', function(r){
						if (r){
							$.ajax({
								url : Constant.$dbGrid_confirm_url,
								type : 'post',
								dataType : 'json',
								data : {id : rowData.id},
								success : function(data) {
									if (data.success) {
										Component.$dbGrid.datagrid('reload');
										Component.$ybGrid.datagrid('reload');
										$.messager.alert("提示", "处理成功");
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
					break;
				case 3:
				case 4:
				case 5:
					$.messager.confirm('提示', '是否跳转至员工档案界面？', function(r){
						if (r){
							$.ajax({
								url : Constant.$dbGrid_confirm_url,
								type : 'post',
								dataType : 'json',
								data : {id : rowData.id},
								success : function(data) {
									if (data.success) {
										window.open(basePath + '/emp/archives/employeeMgr?name=' + encodeURI(encodeURI(rowData.employeeName)),'_self');
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
					break;
				}
			}
		});
		
		//已办
		Component.$ybGrid = $("#ybGrid");
		Component.$ybGrid.datagrid({
			url : Constant.$ybGrid_list_url,
			onBeforeLoad : function(param){
				param.createBy = $('#yb_createBy').val();
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
				var tab = $('#mainTab').tabs('getSelected');
				var index = $('#mainTab').tabs('getTabIndex',tab);
				if (index == 0) {
					Component.$dbGrid.datagrid("reload");
				} else {
					Component.$ybGrid.datagrid("reload");
				}
			}
		});
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});