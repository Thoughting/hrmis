//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/differencework/list';
		Constant.$export_url = basePath + "/api/emp/differencework/export";
		
		//部门下拉框
		Constant.$dept_combo_url = basePath + '/api/emp/dept/combo';
		//表格头
		Constant.$setwork_column_url = basePath + '/api/emp/differencework/column';
	}
}

//界面组件
Component = {
	init : function(){
		Component.$grid = $("#datagrid");
		Component.$grid.datagrid({
			url : Constant.$grid_list_url,
			onBeforeLoad : function(param){
				param.employeeDept = $("#employeeDept").val();
				param.setWorkDate = $("#setWorkDate").val();
			},
			onDblClickCell : function(index,field,value){
				var today = Common.parseISO8601(systemDate);
				var currentDay = Common.parseISO8601(field);
				if (isNaN(currentDay.getDate())) {
					return;
				}
				
				var row = Component.$grid.datagrid("getData").rows[index];
				$("#ud_name").empty();
				$("#ud_name").append(row.name);
				$("#ud_setWorkDate").val(field);
				Interactive.popDateChange();
				
				$("#ud_work_radio").empty();
				$("#ud_work_radio").append('<input type="radio" value="1" name="isAtWork" checked="checked">');
				$("#ud_holiday_radio").empty();
				$("#ud_holiday_radio").append('<input type="radio" value="0" name="isAtWork">');
				
				var id = "";
				if (value == null) {
					$("input[name=isAtWork][value=1]").attr("checked",'checked');
					$("#ud_atWorkTimer").val(0);
					$("#ud_atWorkSpecialStatus").val(0);
					$("#ud_atHolidayType").val(0);
					$("#ud_atHolidayTimer").val(0);
				} else {
					id = value.id;
					$("input[name=isAtWork][value=" + value.isAtWork + "]").attr("checked",true);
					$("#ud_atWorkTimer").val(value.atWorkTimer);
					$("#ud_atWorkSpecialStatus").val(value.atWorkSpecialStatus);
					$("#ud_atHolidayType").val(value.atHolidayType);
					$("#ud_atHolidayTimer").val(value.atHolidayTimer);
				}
				Component.$addEditForm.form({
					queryParams : {id : id,employeeId : row.employeeId}
				});
				Component.$addAndEditPopWin.window('open');
				Component.$addAndEditPopWin.window('center');
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
				$("#searchBtn").click();
			}
		});
		
		//得到部门下拉框
		$.ajax({
			url : Constant.$dept_combo_url,
			type : 'post',
			dataType : 'json',
			async : false,
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
		
		//查询
		$("#searchBtn").linkbutton({
			onClick:function(){
				Component.$grid.datagrid('reload');
			}
		});
		
		//导出
		$("#exportBtn").linkbutton({
			onClick:function(){
				Common.openPostWindow2(Constant.$export_url,{
					employeeDept : $("#employeeDept").val(),
					setWorkDate : $("#setWorkDate").val()
				});
			}
		})
		
		Interactive.refreshGridColumn();
	},
	refreshGridColumn : function(){
		//得到排班表格表格头
		$.ajax({
			url : Constant.$setwork_column_url,
			type : 'post',
			dataType : 'json',
			data : {date : $("#setWorkDate").val()},
			success : function(data) {
				if (data.success) {
					var workCells = data.model[1];
					for(var i = 0;i < workCells.length;i++){
						var today = Common.parseISO8601(systemDate);
						var currentDay = Common.parseISO8601(workCells[i].field);
						if (isNaN(currentDay.getDate()) || currentDay > today) {
							workCells[i].formatter = Interactive.noneCellFormatter;
						} else {
							workCells[i].formatter = Interactive.workTypeCellFormatter;
						}

					}
					Component.$grid.datagrid({
						columns : data.model,
						frozenColumns : [[
			               {field:"code",title:"编号",align:"center",width:"80"},
			               {field:"name",title:"姓名",align:"center",width:"80"}
			            ]]
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
	noneCellFormatter : function(value,row,index){
		return "";
	},
	workTypeCellFormatter : function(value,row,index){
		var backgoundColor = "#FFF";
		var describe = "&nbsp;";
		if(value != null){
			if (value.setwork != null) {
				describe += value.setwork.gridCellDescribe[1];
				backgoundColor = "#FF0";
			} else {
				describe += "全天";
			}
			describe += "<br /><hr />";
			if (value.checkwork != null) {
				describe += value.checkwork.gridCellDescribe[1];
				backgoundColor = "#FF0";
			} else {
				describe += "全天";
			}
			
			if (value.setwork != null && value.checkwork == null && value.setwork.gridCellDescribe[1] == "&nbsp;") {
				backgoundColor = "#FFF";
				describe = "&nbsp;";
			}
			if (value.setwork == null && value.checkwork != null && value.checkwork.gridCellDescribe[1] == "&nbsp;") {
				backgoundColor = "#FFF";
				describe = "&nbsp;";
			}
			if (value.setwork != null && value.checkwork != null && value.setwork.gridCellDescribe[1] == value.checkwork.gridCellDescribe[1]) {
				backgoundColor = "#FFF";
				describe = "&nbsp;";
			}
		}
		return '<div style="border: 1px solid #CCCCCC;background-color:' + backgoundColor + '"><a style="text-decoration:none;">' + describe + '</a></div>';
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});