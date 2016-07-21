package com.eastcom.baseframe.web.modules.sys.web.tag;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.web.common.tag.BaseTag;
import com.eastcom.baseframe.web.modules.sys.entity.Resource;
import com.eastcom.baseframe.web.modules.sys.security.cache.SecurityCache;

/**
 * 导航树图Tag
 * @author wutingguang <br>
 */
public class NavTreeTag extends BaseTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1181954366836579354L;

	private String id;
	private String html = "";
	private List<Resource> children;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<ul id=\"" + id + "\" class=\"easyui-tree\" " + html + " >");
			if (CollectionUtils.isNotEmpty(children)) {
				buildTreeHtml(sb,children);
			}
			sb.append("</ul>");
			pageContext.getOut().println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}
	
	/**
	 * 递归构建 tree html
	 * @param sb
	 * @param children
	 */
	private void buildTreeHtml(StringBuilder sb,List<Resource> children){
		for (Resource resource : children) {
			if (SecurityCache.hasPermission(resource.getPermission()) && resource.getType() == 1) {
				sb.append("<li data-options=\"attributes:{url:'" + resource.getHref() + "'}\">");
				sb.append("	<span><font color='white'><a href='javascript:void(0)'>" + resource.getName() + "</a></font></span>");
				if (CollectionUtils.isNotEmpty(resource.getChildList())) {
					sb.append("<ul>");
					buildTreeHtml(sb,resource.getChildList());
					sb.append("</ul>");
				}
				sb.append("</li>");
			}
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

}
