//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/wage/list';
		Constant.$addEdit_form_url = basePath + '/api/emp/employeewageitem/addOrUpdate';
		Constant.$export_url = basePath + '/api/emp/wage/stat_export';
		
		Constant.$wageplan_combo_url = basePath + '/api/emp/wageplan/combo';
		Constant.$wageplan_column_url = basePath + '/api/emp/wageplan/column';
		
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		Constant.$post_combo_url = basePath + '/api/emp/post/combo';
		
	}
}

//界面组件
Component = {
	init : function(){
		Component.$addAndEditPopWin = $("#addAndEditPopWin");
		Component.$grid = $("#datagrid");
	}
}

//界面交互
Interactive = {
	init : function(){
		//查询
		$("#searchBtn").linkbutton({
			onClick:function(){
				Interactive.refreshGridColumn();
				Component.$grid.datagrid('reload');
			}
		});
		
		//得到工资方案下拉框
		$.ajax({
			url : Constant.$wageplan_combo_url,
			type : 'post',
			dataType : 'json',
			async : false,
			data : {},
			success : function(data) {
				if (data.success) {
					$("#wagePlanCombo").empty();
					$.each(data.model, function(i, n){
						$("#wagePlanCombo").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		});
		
		$("#wagePlanCombo").change(function(){
			Interactive.getPostCombo();
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
		
		//导出
		$("#exportBtn").linkbutton({
			onClick:function(){
				Common.openPostWindow2(Constant.$export_url,{
					type : "统计",
					wageDateStr : $("#wageDate").val(),
    				wagePlanId : $("#wagePlanCombo").val(),
    				employeePost : $("#employeePost").val(),
    				employeeDept : $("#employeeDept").val(),
    				employeeName : $("#name").val()
				});
			}
		})
		
		Interactive.refreshGridColumn();
		Interactive.getPostCombo();
	},
	getPostCombo : function(){
		//得到岗位下拉框
		$.ajax({
			url : Constant.$post_combo_url,
			type : 'post',
			dataType : 'json',
			data : {wagePlanId : $("#wagePlanCombo").val()},
			success : function(data) {
				if (data.success) {
					$("#employeePost").empty();
					$("#employeePost").append("<option value=''>--请选择--</option>");
					$.each(data.model, function(i, n){
						$("#employeePost").append("<option value='" + n.id + "'>" + n.name + "</option>");
					});
				} else {
					$.messager.alert("错误", data.msg, "error");
				}
			},
			error : function(re, status, err) {
				$.messager.alert("错误", re.responseText, "error");
			}
		})
	},
	refreshGridColumn : function(){
		//得到工资方案表格头
		$.ajax({
			url : Constant.$wageplan_column_url,
			type : 'post',
			dataType : 'json',
			data : {id : $("#wagePlanCombo").val(),type:"统计"},
			success : function(data) {
				if (data.success) {
					for(var i = 0;i < data.model.length;i++){
						var cells = data.model[i];
						for(var j = 0;j < cells.length;j++){
							if (cells[j].showFormatter == 'valueFormatter') {
								cells[j].formatter = Interactive.valueFormatter;
							}
							//去掉单位
							var title = cells[j].title;
							if (title.indexOf("（") != -1) {
								cells[j].title = title.substr(0,title.indexOf("（"));
							}
						}
					}
					Component.$grid.datagrid({
						url : Constant.$grid_list_url,
		    			onBeforeLoad : function(param){
		    				param.type = "统计";
		    				param.wageDateStr = $("#wageDate").val();
		    				param.wagePlanId = $("#wagePlanCombo").val();
		    				param.employeePost = $("#employeePost").val();
		    				param.employeeDept = $("#employeeDept").val();
		    				param.employeeName = $("#name").val();
		    			},
						columns : data.model,
						frozenColumns : [[
						   {field:"deptName",title:"部门",align:"center",width:"120"},
						   {field:"postName",title:"岗位",align:"center",width:"120"},
			               {field:"employeeName",title:"姓名",align:"center",width:"100"},
			               {field:"bankCard",title:"银行卡号",align:"center",width:"100"}
			            ]],
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