//界面变量
Constant = {
	init : function(){
		Constant.$grid_list_url = basePath + '/api/emp/wagetemplate/list';
		Constant.$addEdit_form_url = basePath + '/api/emp/wagetemplate/addOrUpdate';
		Constant.$export_url = basePath + "/api/emp/wagetemplate/export";
		
		//工资方案下拉框
		Constant.$wageplan_combo_url = basePath + '/api/emp/wageplan/combo';
		//工资方案表格头
		Constant.$wageplan_column_url = basePath + '/api/emp/wageplan/column';
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
				param.wagePlanId = $("#wagePlanCombo").val();
			}
		});
		
	}
}

//界面交互
Interactive = {
	init : function(){
		
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
			Interactive.refreshGridColumn();
		})
		
		//导出
		$("#exportBtn").linkbutton({
			onClick:function(){
				Common.openPostWindow2(Constant.$export_url,{
					wagePlanId : $("#wagePlanCombo").val()
				});
			}
		})
		
		Interactive.refreshGridColumn();
	},
	refreshGridColumn : function(){
		//得到工资方案表格头
		$.ajax({
			url : Constant.$wageplan_column_url,
			type : 'post',
			dataType : 'json',
			data : {id : $("#wagePlanCombo").val(),type : "配置"},
			success : function(data) {
				if (data.success) {
					for(var i = 0;i < data.model.length;i++){
						var cells = data.model[i];
						for(var j = 0;j < cells.length;j++){
							if (cells[j].showFormatter == 'editFormatter') {
								cells[j].formatter = Interactive.editFormatter;
							} else if (cells[j].showFormatter == 'nullFormatter') {
								cells[j].formatter = Interactive.nullFormatter;
							}
						}
					}
					Component.$grid.datagrid({
						columns : data.model,
						frozenColumns : [[
			               {field:"postName",title:"岗位",align:"center",width:"120"}
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
	nullFormatter : function(value,row,index){
		return "";
	},
	editFormatter : function(value,row,index){
		var wagePlanId = row.wagePlanId;
		var postId = row.postId;
		var wageCountItemId = value.wageCountItemId;
		var showValue = 0;
		var wageTemplateId = null;
		if (value.model != null) {
			showValue = value.model.count;
			wageTemplateId = value.model.id;
		}
		if (hasGridEdit) {
			return '<input type="text" style="width: 70px;text-align:center;border: none;IME-MODE: disabled;" maxlength="8" onchange="Interactive.inputChangeHandler(\'' + wageTemplateId +'\',\'' + wagePlanId + '\',\'' + postId + '\',\'' + wageCountItemId + '\',this.value)" value="' + showValue + '"/>';
		} else {
			return showValue;
		}
	},
	inputChangeHandler : function(wageTemplateId,wagePlanId,postId,wageCountItemId,value){
		$.ajax({
			url : Constant.$addEdit_form_url,
			type : 'post',
			dataType : 'json',
			data : {
				id : wageTemplateId,
				wagePlanId : wagePlanId,
				wageCountItemId : wageCountItemId,
				postId : postId,
				count : value
			},
			success : function(data) {
				if (data.success) {
					Component.$grid.datagrid('reload');
				} else {
					$.messager.alert("错误", data.msg, "error");
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