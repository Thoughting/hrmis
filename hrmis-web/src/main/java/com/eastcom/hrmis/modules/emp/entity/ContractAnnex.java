package com.eastcom.hrmis.modules.emp.entity;

import com.eastcom.baseframe.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 合同附件
 * 
 * @author wutingguang <br>
 */
@Entity
@Table(name = "T_CONTRACT_ANNEX")
@DynamicInsert
@DynamicUpdate
public class ContractAnnex extends IdEntity<ContractAnnex> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4291468207794971420L;

	private Contract contract;
	private String annexName;
	private byte[] annexContent;

	public ContractAnnex() {
	}

	public String getAnnexName() {
		return this.annexName;
	}

	public void setAnnexName(String annexName) {
		this.annexName = annexName;
	}

	@JsonIgnore
	public byte[] getAnnexContent() {
		return this.annexContent;
	}

	public void setAnnexContent(byte[] annexContent) {
		this.annexContent = annexContent;
	}

	@ManyToOne
	@JoinColumn(name = "CONTRACT_ID", referencedColumnName = "ID")
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnore
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

}
