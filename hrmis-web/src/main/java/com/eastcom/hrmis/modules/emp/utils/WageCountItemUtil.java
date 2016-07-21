package com.eastcom.hrmis.modules.emp.utils;

import com.eastcom.baseframe.common.utils.excel.CustomizeToExcel;
import com.eastcom.baseframe.common.utils.excel.ExcelColumn;
import com.eastcom.hrmis.modules.emp.entity.WageCountItem;

import java.math.BigDecimal;
import java.util.*;

/**
 * 工资计算工具类
 * 
 * @author wutingguang <br>
 */
public class WageCountItemUtil {

	/**
	 * 得到工资核算项Formatter
	 * 
	 * @param wageCountItem
	 * @param type
	 *            配置，核算，统计
	 * @return
	 */
	public static String getItemFormatter(WageCountItem wageCountItem,
			String type) {
		String formatter = "";
		if ("配置".equals(type)) {
			String nullCalcItems = "收入总额,个税（元/月）,扣减总额,实发总额,现金发放（元/月）,银行发放（元/月）";
			if (nullCalcItems.indexOf(wageCountItem.getName()) != -1) {
				formatter = "nullFormatter";
			} else {
				formatter = "editFormatter";
			}
		} else if ("核算".equals(type)) {
			String editCalcItems = "缺编顶班（元/天）,中班加班（元/天）,节日慰问金（元/月）,承包质量奖（元/月）,综合补贴（元/月）,项目管理补贴（元/月）,质量管理奖金（元/月）,住宿费（元/月）,水电费（元/月）,赔偿/其他（元/月）,现金发放（元/月）";
			if (editCalcItems.indexOf(wageCountItem.getName()) != -1) {
				formatter = "editFormatter";
			} else {
				formatter = "valueFormatter";
			}
		} else if ("统计".equals(type)) {
			formatter = "valueFormatter";
		}
		return formatter;
	}

	public static void main(String[] args) throws Exception {
		List<ExcelColumn> columns = new ArrayList<ExcelColumn>();

		ExcelColumn b = new ExcelColumn("B", "B", 20);

		List<ExcelColumn> columns2 = new ArrayList<ExcelColumn>();
		columns2.add(new ExcelColumn("C", "C", 30));
		columns2.add(new ExcelColumn("D", "D", 30));
		columns2.add(new ExcelColumn("E", "E", 30));

		b.setChildren(columns2);

		columns.add(b);

		// 造数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("C", "CCC");
		map1.put("D", 22);
		map1.put("E", new Date());
		list.add(map1);

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("C", "11");
		map2.put("D", 321);
		map2.put("E", new Date());
		list.add(map2);

		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("C", "3333");
		map3.put("D", 'd');
		map3.put("E", new BigDecimal(1111));
		list.add(map3);

		CustomizeToExcel.toFile(columns, list, "D:\\x2.xlsx");
	}
}
