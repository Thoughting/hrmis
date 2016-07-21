package com.eastcom.baseframe.web.modules.rest.entity;
// default package
// Generated 2016-3-14 9:49:28 by Hibernate Tools 3.4.0.CR1

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.eastcom.baseframe.common.utils.Cryptos;
import com.eastcom.baseframe.web.common.entity.DataEntity;
import com.eastcom.baseframe.web.modules.rest.RestConstant;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

/**
 * REST 密钥对
 * 
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_REST_SECRET")
@DynamicInsert @DynamicUpdate
public class RestSecret extends DataEntity<RestSecret> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4545328932724285164L;
	
	private String code;
	private String decryptKey;
	private Integer type = 0;
	private Integer enable;

	private List<RestResource> resources = Lists.newArrayList();
	
	public RestSecret() {
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonIgnore
	public String getDecryptKey() {
		return this.decryptKey;
	}

	public void setDecryptKey(String decryptKey) {
		this.decryptKey = decryptKey;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getEnable() {
		return this.enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "T_REST_SECRET_RESOURCE", joinColumns = { @JoinColumn(name = "FK_REST_SECRET_ID") }, inverseJoinColumns = { @JoinColumn(name = "FK_REST_RESOURCE_ID") })
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public List<RestResource> getResources() {
		return resources;
	}

	public void setResources(List<RestResource> resources) {
		this.resources = resources;
	}
	
	/**
	 * 加密之后的显示串
	 * 
	 * @return
	 */
	@Transient
	public String getEncryp() {
		return Cryptos.aesEncrypt(this.getCode(),this.getDecryptKey());
	}

	@Transient
	public String getEnableDict(){
		return DictCache.getYesOrNo(this.enable);
	}
	
	/**
	 * 资源数
	 * @return
	 */
	@Transient
	public Integer getResourceCount(){
		if (CollectionUtils.isNotEmpty(this.resources)) {
			return this.resources.size();
		}
		return 0;
	}

	@Transient
	public String getTypeDict(){
		return DictCache.getDictValue(RestConstant.DICT_REST_SECRET_TYPE, this.type);
	}
	
}
