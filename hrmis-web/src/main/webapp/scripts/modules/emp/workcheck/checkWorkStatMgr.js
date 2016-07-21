//界面变量
Constant = {
	init : function(){
		//echarts图表
		require.config({
			paths : {
				echarts : basePath + '/static/plugins/echarts/2.2.7/'
			}
		});
		
		Constant.$dept_stat_grid_list_url = basePath + '/api/emp/checkworkstat/dept_list';
		Constant.$dept_export_url = basePath + '/api/emp/checkworkstat/dept_export';
		
		Constant.$emp_stat_grid_list_url = basePath + '/api/emp/checkworkstat/emp_list';
	}
}

//界面组件
Component = {
	init : function(){
		Component.$dept_stat_datagrid = $("#dept_stat_datagrid");
		Component.$dept_stat_datagrid.datagrid({
			url : Constant.$dept_stat_grid_list_url,
			onBeforeLoad : function(param){
				param.statMonth = $("#statMonth").val();
			}
		});
		
		Component.$emp_stat_datagrid = $("#emp_stat_datagrid");
	}
}

var currentTitle = "";
var currentXAxisData = [];
var currentSeriesData = [];

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
		
		//导出
		$("#exportBtn").linkbutton({
			onClick:function(){
				Common.openPostWindow2(Constant.$dept_export_url,{
					statMonth : $("#statMonth").val()
				});
			}
		});
		
		$("#statChartContainer").panel({
			onResize : function(width,height){
				$("#chart").width($("#statChartContainer").width());
				if (currentTitle != "") {
					Interactive.refreshChart(currentTitle,currentXAxisData,currentSeriesData);
				}
			}
		})
		
	},
	deptNameFormatter : function(value,row,index){
		return '<a href="javascript:Interactive.refreshDeptStatChart(\'' + value + '\',\'' + row.ID + '\')">' + value + '</a>';
	},
	nullFormatter : function(value,row,index){
		if (value == null) {
			return 0;
		}
		return value;
	},
	refreshStatTypeChart : function(title,statType){
		var data = Component.$dept_stat_datagrid.datagrid("getData");
		$("#statEmployeesContainer").hide();
		
		if (data != null && data.rows != null && data.rows.length > 0) {
			var xAxisData = [];
			var seriesData = [];
			for(var i = 0;i < data.rows.length;i++){
				xAxisData.push(data.rows[i].NAME);
				seriesData.push(data.rows[i][statType] == null ? 0 : data.rows[i][statType]);
			}
			//刷新标题
			$("#statChartContainer").panel({
				title : title
			});
			//刷新图表
			Interactive.refreshChart(title,xAxisData,seriesData);
			
			Component.$emp_stat_datagrid.datagrid("loadData",[]);
		}
		
	},
	refreshDeptStatChart : function(title,deptId){
		$("#statEmployeesContainer").show();
		var data = Component.$dept_stat_datagrid.datagrid("getData");
		if (data != null && data.rows != null && data.rows.length > 0) {
			var xAxisData = [];
			var seriesData = [];
			for(var i = 0;i < data.rows.length;i++){
				if (data.rows[i].ID == deptId) {
					xAxisData.push("旷工（天）","加班（天）","休假（天）","补休（天）","事假（天）","病假（天）","婚假（天）","产假（天）","丧假（天）","年休假（天）");
					seriesData.push(data.rows[i]["KG_SC_DAY"] == null ? 0 : data.rows[i]["KG_SC_DAY"]);
					seriesData.push(data.rows[i]["JBL_XSC_DAY"] == null ? 0 : data.rows[i]["JBL_XSC_DAY"]);
					seriesData.push(data.rows[i]["ZCXJ_SC_DAY"] == null ? 0 : data.rows[i]["ZCXJ_SC_DAY"]);
					seriesData.push(data.rows[i]["BXJ_SC_DAY"] == null ? 0 : data.rows[i]["BXJ_SC_DAY"]);
					seriesData.push(data.rows[i]["SHJ_SC_DAY"] == null ? 0 : data.rows[i]["SHJ_SC_DAY"]);
					seriesData.push(data.rows[i]["BJ_SC_DAY"] == null ? 0 : data.rows[i]["BJ_SC_DAY"]);
					seriesData.push(data.rows[i]["HJ_SC_DAY"] == null ? 0 : data.rows[i]["HJ_SC_DAY"]);
					seriesData.push(data.rows[i]["CJ_SC_DAY"] == null ? 0 : data.rows[i]["CJ_SC_DAY"]);
					seriesData.push(data.rows[i]["SJ_SC_DAY"] == null ? 0 : data.rows[i]["SJ_SC_DAY"]);
					seriesData.push(data.rows[i]["NXJ_SC_DAY"] == null ? 0 : data.rows[i]["NXJ_SC_DAY"]);
					break;
				}
			}
			//刷新标题
			$("#statChartContainer").panel({
				title : title
			});
			//刷新图表
			Interactive.refreshChart(title,xAxisData,seriesData);
			//刷新表格数据
			Component.$emp_stat_datagrid.datagrid({
				url : Constant.$emp_stat_grid_list_url,
				onBeforeLoad : function(param){
					param.statMonth = $("#statMonth").val();
					param.deptId = deptId;
				}
			})
		}
	},
	refreshChart : function(title,xAxisData,seriesData){
		currentTitle = title;
		currentXAxisData = xAxisData;
		currentSeriesData = seriesData;
		require([ 'echarts', 'echarts/chart/bar'], function(ec) {
			var chart = ec.init(document.getElementById('chart'));
			var option = {
					tooltip : {
						trigger : 'axis'
					},
					xAxis : [ {
						axisLabel: {
					        interval: 0,
					        rotate: 30,
					    },
						type : 'category',
						data : xAxisData
					} ],
					yAxis : [ {
						type : 'value'
					} ],
					series : [{
						name : title,
						type : 'bar',
						data : seriesData
					}]
				};
			chart.setOption(option);
		});
	}
}

$(function() {
	Constant.init();
	Component.init();
	Interactive.init();
});