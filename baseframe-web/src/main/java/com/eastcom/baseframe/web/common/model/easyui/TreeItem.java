package com.eastcom.baseframe.web.common.model.easyui;

import java.io.Serializable;
import java.util.List;

/**
 * easyui treegrid model
 * @author wutingguang <br>
 */
public class TreeItem<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2856518334470209630L;
	
	private T model;
	private List<T> children;
	
	private String state;
	
	public T getModel() {
		return model;
	}
	public void setModel(T model) {
		this.model = model;
	}
	public List<T> getChildren() {
		return children;
	}
	public void setChildren(List<T> children) {
		this.children = children;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
