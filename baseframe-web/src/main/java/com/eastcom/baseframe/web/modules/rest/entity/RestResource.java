package com.eastcom.baseframe.web.modules.rest.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.eastcom.baseframe.web.common.entity.DataEntity;

/**
 * REST 资源
 * 
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_REST_RESOURCE")
@DynamicInsert
@DynamicUpdate
public class RestResource extends DataEntity<RestResource> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8101820566629840320L;

	private String name;
	private String url;

	public RestResource() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// 树图用到属性
	@Transient
	public String getState() {
		return "open";
	}

	private boolean checked = false;

	@Transient
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Transient
	public String getText() {
		return name;
	}
	
	@Transient
	public List<RestResource> getChildren() {
		return null;
	}
	
	@Transient
	public String getParentId() {
		return null;
	}
}
