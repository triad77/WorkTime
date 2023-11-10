package com.kbfng.worktime.entity;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kbfng.worktime.dto.EmployeeDto;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIdentityReference(alwaysAsId = true)
public class Employee {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;
	
	@Column(name = "emp_no")
	@NotNull
	private String empNo;
	
	@Column(name = "ip_addr")
	@NotNull
	private String ipAddr;
	
	@Column(name = "emp_nm")
	@NotNull
	private String empNm;
	
	@Column
	@NotNull
	private String contract;
	
	@Convert(converter = CorpConverter.class)
	@Column
	@NotNull
	private CorpEnum corp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prj_id")
    @NotNull
	private Project project;
	
	@Column(name = "contract_st_date", length = 8)
	@NotNull
	private String ctrcStDate;
	
	@Column(name = "contract_ed_date", length = 8)
	@ColumnDefault("'99991231'")
	private String ctrcEdDate;

	@Builder
	public Employee(int id, String empNo, String ipAddr, String empNm, String contract, CorpEnum corp,
			Project project, String ctrcStDate, String ctrcEdDate) {
		super();
		this.id = id;
		this.empNo = empNo;
		this.ipAddr = ipAddr;
		this.empNm = empNm;
		this.contract = contract;
		this.corp = corp;
		this.project = project;
		this.ctrcStDate = ctrcStDate;
		this.ctrcEdDate = ctrcEdDate;
	}
	
	public void endCtrc(String ctrcEdDate) {
		this.ctrcEdDate = ctrcEdDate;
	}
	
	public void update(EmployeeDto dto) {
		this.ipAddr = dto.getIpAddr();
		this.empNm = dto.getEmpNm();
		this.contract = dto.getContract();
		this.corp = dto.getCorp();
		this.project = dto.getProject();
		this.ctrcStDate = dto.getCtrcStDate();
	}
}
