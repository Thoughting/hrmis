package com.eastcom.baseframe.web.common.model.easyui;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;


/**
 * Easy ui tree
 * 
 * @author wutingguang <br>
 */
public class ETree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6946390341165105792L;

	private String id;
	private String text;
	private String state;
	private String iconCls;
	private boolean checked;
	private Map<String, Object> attributes = Maps.newHashMap();
	private List<ETree> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<ETree> getChildren() {
		return children;
	}

	public void setChildren(List<ETree> children) {
		this.children = children;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

}
