package com.kbfng.worktime.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIdentityReference(alwaysAsId = true)
public class Project {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prj_id")
	private Integer prjId;
	
	@Column(name = "prj_nm", length = 50)
    private String prjNm;
	
	@Convert(converter = CorpConverter.class)
	@Column(name = "plodr_corp")
	private CorpEnum plodrCorp;
	
	@Column(length = 8)
	private String startDt;
	
	@Column(length = 8)
	private String endDt;
	
	@Convert(converter = StatConverter.class)
	@Column(name = "stat")
	private StatEnum stat;
	
	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Employee> empList;
	
	@Builder
	public Project(Integer prjId, String prjNm, CorpEnum plodrCorp, String startDt, String endDt, StatEnum stat,
			List<Employee> empList) {
		super();
		this.prjId = prjId;
		this.prjNm = prjNm;
		this.plodrCorp = plodrCorp;
		this.startDt = startDt;
		this.endDt = endDt;
		this.stat = stat;
		this.empList = empList;
	}
	
	
}
