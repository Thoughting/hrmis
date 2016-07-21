//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/stat/list';
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		Constant.$export_url = basePath + "/api/emp/stat/export";
	}
}

//界面组件
Component = {
	init : function(){
		Component.$grid = $("#datagrid");
		Component.$grid.datagrid({
			url : Constant.$grid_list_url,
			onBeforeLoad : function(param){
				param.deptId = $("#employeeDept").val();
				param.compareDate1 = $("#compareDate1").val();
				param.compareDate2 = $("#compareDate2").val();
			}
		});
	}
}

//界面交互
Interactive = {
	init : function(){
		//查询
		$("#searchBtn").linkbutton({
			onClick:function(){
				Component.$grid.datagrid('reload');
			}
		});
		
		//导出
		$("#exportBtn").linkbutton({
			onClick : function(){
				Common.openPostWindow2(Constant.$export_url,{
					deptId : $("#employeeDept").val(),
					compareDate1 : $("#compareDate1").val(),
					compareDate2 : $("#compareDate2").val()
				});
			}
		})
		
		//得到部门下拉框
		$.ajax({
			url : Constant.$dept_combo_url,
			type : 'post',
			dataType : 'json',
			data : {},
			success : function(data) {
				if (data.success) {
					$("#employeeDept").empty();
					$("#employeeDept").append("<option value=''>--请选择--</option>");
					$.each(data.model, function(i, n){
						$("#employeeDept").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
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