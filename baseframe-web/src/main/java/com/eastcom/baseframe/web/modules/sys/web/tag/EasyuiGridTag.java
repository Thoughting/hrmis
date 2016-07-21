package com.eastcom.baseframe.web.modules.sys.web.tag;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.tag.BaseTag;
import com.eastcom.baseframe.web.modules.sys.cache.DynamicGridCache;
import com.eastcom.baseframe.web.modules.sys.cache.DynamicgridItemCache;
import com.eastcom.baseframe.web.modules.sys.entity.Dynamicgrid;
import com.eastcom.baseframe.web.modules.sys.entity.DynamicgridItem;
import com.google.common.collect.Lists;

/**
 * 动态表格标签 easyui-datagrid tag
 * 
 * @author wutingguang <br>
 */
public class EasyuiGridTag extends BaseTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7841024594023604257L;

	private String id; // 表格ID
	private String name; // 表格名称
	private String attrHtml = ""; // html属性（表格option,style等）

	@Override
	public int doStartTag() throws JspException {
		try {
			StringBuilder sb = new StringBuilder();
			
			//构建表格
			Dynamicgrid dynamicgrid = DynamicGridCache.getDynamicgridByName(name);
			if (dynamicgrid != null) {
				sb.append("<table id=\"" + StringUtils.trim(id) + "\" class=\"easyui-datagrid\" ");
				sb.append("" + StringUtils.trim(attrHtml) + " ");
				sb.append("> \n");
				//构建表格头
				sb.append("	<thead> \n");
				List<List<DynamicgridItem>> allItems = Lists.newArrayList();
				List<DynamicgridItem> dynamicgridItems = DynamicgridItemCache.getDynamicgridItemsById(dynamicgrid.getId());
				transformDynamicgridItem(allItems, dynamicgridItems);
				if (CollectionUtils.isNotEmpty(allItems)) {
					for (int i = 0; i < allItems.size(); i++) {
						List<DynamicgridItem> items = allItems.get(i);
						sb.append("	<tr> \n");
						for (DynamicgridItem dynamicgridItem : items) {
							sb.append("		<th ");
							if (CollectionUtils.isNotEmpty(dynamicgridItem.getChildren())) {
								int maxColspan = getDynamicgridItemMaxColspan(0,dynamicgridItem);
								sb.append(" colspan=\"" + maxColspan + "\" ");
							} else{
								sb.append(" field=\"" + dynamicgridItem.getField() + "\" ");
								sb.append(" width=\"" + dynamicgridItem.getWidth() + "\" ");
								if (StringUtils.isNotBlank(dynamicgridItem.getAlign())) {
									sb.append(" align=\"" + dynamicgridItem.getAlign() + "\" ");
								}
								sb.append(" rowspan=\"" + (allItems.size() - i) + "\" ");
								if (StringUtils.isNotBlank(dynamicgridItem.getFormatter())) {
									sb.append(" formatter=\"" + dynamicgridItem.getFormatter() + "\" ");
								}
							}
							sb.append(">" + dynamicgridItem.getTitle() + "</th> \n");
						}
						sb.append("	</tr> \n");
					}
				}
				
				sb.append("	</thead> ");
				sb.append("</table> ");
			}
			
			pageContext.getOut().println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	
	/**
	 * 表格项转换
	 * @param list
	 */
	private void transformDynamicgridItem(List<List<DynamicgridItem>> allItems,List<DynamicgridItem> dynamicgridItems){
		if (CollectionUtils.isNotEmpty(dynamicgridItems)) {
			allItems.add(dynamicgridItems);
			List<DynamicgridItem> childItems = Lists.newArrayList();
			for (DynamicgridItem dynamicgridItem : dynamicgridItems) {
				if (CollectionUtils.isNotEmpty(dynamicgridItem.getChildren())) {
					childItems.addAll(dynamicgridItem.getChildren());
				}
			}
			transformDynamicgridItem(allItems,childItems);
		}
	}
	
	/**
	 * 得到表格最大的列合并单元格数
	 * @param maxColspan
	 * @param dynamicgridItem
	 */
	private int getDynamicgridItemMaxColspan(int maxColspan,DynamicgridItem dynamicgridItem){
		if (CollectionUtils.isNotEmpty(dynamicgridItem.getChildren())) {
			for (DynamicgridItem childDynamicgridItem : dynamicgridItem.getChildren()) {
				maxColspan = getDynamicgridItemMaxColspan(maxColspan,childDynamicgridItem);
			}
		} else {
			maxColspan++;
		}
		return maxColspan;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttrHtml() {
		return attrHtml;
	}

	public void setAttrHtml(String attrHtml) {
		this.attrHtml = attrHtml;
	}

}
