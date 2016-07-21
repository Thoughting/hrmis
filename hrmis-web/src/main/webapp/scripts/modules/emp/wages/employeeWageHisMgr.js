//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/wagehis/list';
		Constant.$export_url = basePath + '/api/emp/wagehis/stat_export';
		
		Constant.$tabs_url = basePath + '/api/emp/wagehis/tabs';
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		
	}
}

//界面组件
Component = {
	init : function(){
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
				Common.openPostWindow3(basePath + "/emp/wages/employeeWageHisMgr","_self",{
					start_wageDate : $("#start_wageDate").val(),
					end_wageDate : $("#end_wageDate").val(),
					deptName : $("#employeeDept").find("option:selected").text(),
					employeeName : $("#employeeName").val()
				});
			}
		});
		
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
						if (n.name == deptName) {
							$("#employeeDept").append("<option value='" + n.id + "' selected=\"true\" >" + n.name + "</option>");
						} else {
							$("#employeeDept").append("<option value='" + n.id + "'>" + n.name + "</option>");
						}
					});
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});
	},
	valueFormatter : function(value,row,index){
		var showValue = 0;
		if (value.model != null) {
			showValue = value.model.count;
		}
		return showValue;
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});