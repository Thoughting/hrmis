package com.eastcom.baseframe.web.modules.sys.web.tag;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.collections.CollectionUtils;

import com.eastcom.baseframe.common.utils.StringUtils;
import com.eastcom.baseframe.web.common.tag.BaseTag;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.baseframe.web.modules.sys.entity.Dict;

/**
 * 
 * 字典下拉框标签 适用于字典并不是太多的时候
 * 
 * @author wutingguang <br>
 */
public class DictSelectTag extends BaseTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6905406903626944011L;

	private String id;
	private String name;
	private String value;
	private String dictType;
	private String attrHtml;
	private boolean needNull = true;
	
	@Override
	public int doStartTag() throws JspException {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" <select ");
			sb.append(StringUtils.isNotBlank(id) ? " id='" + StringUtils.trim(id) + "' " : "");
			sb.append(StringUtils.isNotBlank(name) ? " name='" + StringUtils.trim(name) + "' " : "");
			sb.append(StringUtils.isNotBlank(attrHtml) ? " " + StringUtils.trim(attrHtml) + " " : "");
			sb.append(" >");
			if (needNull) {
				sb.append("<option value=''>--请选择--</option>");
			}
			if (StringUtils.isNotBlank(dictType)) {
				List<Dict> dicts = DictCache.getDictList(dictType);
				if (CollectionUtils.isNotEmpty(dicts)) {
					for (Dict dict : dicts) {
						sb.append("<option value='" + dict.getCode() + "' ");
						if (dict.getCode().equals(value) || dict.getName().equals(value)) {
							sb.append(" selected ");
						}
						sb.append(" >" + dict.getName() + "</option>");
					}
				}
			}
			sb.append("</select>");
			pageContext.getOut().println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getAttrHtml() {
		return attrHtml;
	}

	public void setAttrHtml(String attrHtml) {
		this.attrHtml = attrHtml;
	}

	public boolean isNeedNull() {
		return needNull;
	}

	public void setNeedNull(boolean needNull) {
		this.needNull = needNull;
	}

}
