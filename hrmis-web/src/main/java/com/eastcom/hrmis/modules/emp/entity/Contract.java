package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.eastcom.baseframe.web.modules.sys.cache.DictCache;
import com.eastcom.hrmis.modules.emp.EmpConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 合同
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_CONTRACT")
@DynamicInsert @DynamicUpdate
public class Contract extends IdEntity<Contract> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8997977385677844427L;
	
	private Integer type;
	private String code;
	private String name;
	private String keyDesr;
	private String remark;
	private Date createDate;
	private Date updateDate;
	private String modifyUser;
	
	private List<ContractAnnex> annexs = Lists.newArrayList(); // 合同附件清单
	
	public Contract() {
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyDesr() {
		return this.keyDesr;
	}

	public void setKeyDesr(String keyDesr) {
		this.keyDesr = keyDesr;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<ContractAnnex> getAnnexs() {
		return annexs;
	}

	public void setAnnexs(List<ContractAnnex> annexs) {
		this.annexs = annexs;
	}

	@Transient
	public String getTypeDict(){
		return DictCache.getDictValue(EmpConstant.DICT_CONTRACT_TYPE, this.type);
	}
}
