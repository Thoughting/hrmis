package com.eastcom.baseframe.common.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 数据Entity类
 */
@MappedSuperclass
public abstract class IdEntity<T> extends BaseEntity<T> {

	private static final long serialVersionUID = 1L;

	protected String id; // 编号

	public IdEntity() {
		super();
	}
	
	public IdEntity(String id) {
		super();
		this.id = id;
	}

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
