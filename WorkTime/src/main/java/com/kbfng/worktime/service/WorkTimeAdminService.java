package com.kbfng.worktime.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kbfng.worktime.DataNotFoundException;
import com.kbfng.worktime.dto.EmployeeDto;
import com.kbfng.worktime.dto.EmployeeWorkTimeDto;
import com.kbfng.worktime.dto.ProjectDto;
import com.kbfng.worktime.dto.WorkTimeDto;
import com.kbfng.worktime.dto.WorkTimeForCorpDto;
import com.kbfng.worktime.dto.WorkTimeForProjectDto;
import com.kbfng.worktime.entity.CorpEnum;
import com.kbfng.worktime.entity.Employee;
import com.kbfng.worktime.entity.EnumValue;
import com.kbfng.worktime.entity.Project;
import com.kbfng.worktime.entity.StatEnum;
import com.kbfng.worktime.entity.WorkTime;
import com.kbfng.worktime.repository.EmployeeRepository;
import com.kbfng.worktime.repository.ProjectRepository;
import com.kbfng.worktime.repository.WorkTimeRepository;
import com.kbfng.worktime.util.EmployeeMapper;
import com.kbfng.worktime.util.ProjectMapper;
import com.kbfng.worktime.util.WorkTimeMapper;
import com.kbfng.worktime.util.WorkTimeUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class WorkTimeAdminService {

	private final ProjectRepository projectRepository;
	private final EmployeeRepository employeeRepository;
	private final WorkTimeRepository workTimeRepository;
	
	public List<ProjectDto> getPrjList(String plodrCorp) {
    	
        List<Project> entList = this.projectRepository.findProjectForCombo(plodrCorp);
        
        return entList.stream()
        		.map(m -> ProjectMapper.INSTANCE.toDto(m))
        		.collect(Collectors.toList());
	}
	
    public Page<ProjectDto> getPrjList(String plodrCorp, String statCd, int page) {
    	
        Pageable pageable = PageRequest.of(page, 10);
        Page<Project> entList = this.projectRepository.searchProjectByWhere(plodrCorp, 0, statCd, pageable);
        
        return entList.map(m -> ProjectMapper.INSTANCE.toDto(m));
	}
    
	public ProjectDto getProject(Integer prjId) {  
        Optional<Project> project = this.projectRepository.findByPrjId(prjId);
        
        if (project.isPresent()) {
            return ProjectMapper.INSTANCE.toDto(project.get());
        } else {
            throw new DataNotFoundException("프로젝트를 찾을 수 없습니다(prjId:"+prjId+")");
        }
    }
	
	public Page<EmployeeDto> getEmpList(
    		String empNo, String empNm, 
			String plodrCorp, int prjId, String belngCorp, int page) {
    	
    	Pageable pageable = PageRequest.of(page, 10);
        Page<Employee> entList = this.employeeRepository.searchEmpByWhere(
        		empNo, empNm, 
    			plodrCorp, prjId, belngCorp, pageable);
        
        return entList.map(m -> EmployeeMapper.INSTANCE.toDto(m));
    }
	
	public List<EmployeeWorkTimeDto> getEmpWorkTimeList(String listGbn, String srchYmd, int prjId, String corpCd) {
		
		List<EmployeeWorkTimeDto> resultList = null;
		
		if("T".equals(listGbn)) {
			resultList = this.workTimeRepository.searchTotalEmpWorkTime(prjId, corpCd, srchYmd);
		}
		else if("R".equals(listGbn)) {
			resultList = this.workTimeRepository.searchRegEmpWorkTime(prjId, corpCd, srchYmd);
		}
		else if("L".equals(listGbn)) {
			resultList = this.workTimeRepository.searchLateEmpWorkTime(prjId, corpCd, srchYmd);
		}
		else {
			resultList = new ArrayList<EmployeeWorkTimeDto>(); 
			List<EmployeeWorkTimeDto> totList = this.workTimeRepository.searchTotalEmpWorkTime(prjId, corpCd, srchYmd);
			List<EmployeeWorkTimeDto> regList = this.workTimeRepository.searchRegEmpWorkTime(prjId, corpCd, srchYmd);
			
			boolean isSameEmp = false;
			for(EmployeeWorkTimeDto totDto : totList) {
				isSameEmp = false;
				for(EmployeeWorkTimeDto regDto : regList) {
					if(totDto.getEmpNo().equals(regDto.getEmpNo())) {
						isSameEmp = true;
						break;
					}
				}
				if(!isSameEmp) {
					resultList.add(totDto);
				}
			}
		}
		
		return resultList;
	}
	
	public EmployeeDto getEmployee(int id) {  
		Optional<Employee> emp = this.employeeRepository.findById(id);
		
		if (emp.isPresent()) {
            return EmployeeMapper.INSTANCE.toDto(emp.get());
        } else {
            throw new DataNotFoundException("해당 직원[PK:" + id +"]을 찾을 수 없습니다");
        }
    }
	
	@Transactional
	public EmployeeDto createEmp(EmployeeDto dto) {
    	
		List<Employee> actvList  = this.employeeRepository.searchActvEmpByEmpNo(dto.getEmpNo());
		
		if(actvList != null && actvList.size() > 0) {
			throw new IllegalArgumentException();
		} else {
			Employee entity = EmployeeMapper.INSTANCE.toEntity(dto);
			entity = this.employeeRepository.save(entity);
			dto = EmployeeMapper.INSTANCE.toDto(entity);
		}
		
        return dto;
    }
	
	@Transactional
	public void updateEmp(EmployeeDto dto) {
		
		Optional<Employee> emp = this.employeeRepository.findById(dto.getId());
		
		if (emp.isPresent()) {
			Employee ent = emp.get();
			ent.update(dto);
        } else {
            throw new DataNotFoundException("해당 직원이 없습니다. PK="+dto.getId());
        }
	}
	
	@Transactional
	public void regCtrcEndYmd(int id, String ctrcEndYmd) {
		
		Optional<Employee> emp = this.employeeRepository.findById(id);
		
		if (emp.isPresent()) {
			Employee ent = emp.get();
			ent.endCtrc(ctrcEndYmd.replaceAll("-", ""));
        } else {
            throw new DataNotFoundException("해당 직원이 없습니다. PK="+id);
        }
	}
	
	@Transactional
	public void deleteEmp(int id) {
		
		Optional<Employee> emp = this.employeeRepository.findById(id);
		
		if (emp.isPresent()) {
			
			Employee ent = emp.get();
			List<WorkTime> wTimeList = this.workTimeRepository.findByEmpNo(ent.getEmpNo());
			for(WorkTime wTime : wTimeList) {
				this.workTimeRepository.deleteById(wTime.getId());
			}
            this.employeeRepository.delete(emp.get());
        } else {
        	throw new DataNotFoundException("해당 직원이 없습니다. PK="+id);
        }
	}
	
	public Page<WorkTimeDto> getWorkTimeList(String empNm, String srchStYmd, String srchEdYmd, int page) {
    	
        Pageable pageable = PageRequest.of(page, 10);
        Page<WorkTime> entList = this.workTimeRepository.searchWorkTimeByWhere(empNm, srchStYmd, srchEdYmd, pageable);
        
        return entList.map(m -> WorkTimeMapper.INSTANCE.toDto(m));
	}
	
	public Page<WorkTimeForProjectDto> getWorkTimeListForProject(String plodrCorp, int prjId, String srchYmd, int page) {
    	
		Pageable pageable = PageRequest.of(page, 10);
		Page<Project> entList = this.projectRepository.searchProjectByWhere(plodrCorp, prjId, StatEnum.PRGRS.getCode(), pageable);
        
		List<WorkTimeForProjectDto> list = new ArrayList<WorkTimeForProjectDto>(entList.getContent().size());
        WorkTimeForProjectDto row = null;
        int totalCnt, registCnt, lateCnt, noRegistCnt = 0;
        for(Project prj : entList.getContent()) {
        	
        	totalCnt = this.employeeRepository.getActiveEmpCntAtPrj(srchYmd, prj.getPrjId());
        	registCnt = this.workTimeRepository.getRegEmpCntAtPrj(srchYmd,  prj.getPrjId());
        	lateCnt = this.workTimeRepository.getLateEmpCntAtPrj(srchYmd,  prj.getPrjId());
        	noRegistCnt = totalCnt - registCnt;
        	
        	row = new WorkTimeForProjectDto();
        	
        	row.setPrjId(prj.getPrjId());
        	row.setPrjNm(prj.getPrjNm());
        	row.setDateYmd(srchYmd);
        	row.setTotalCnt(totalCnt);
        	row.setRegistCnt(registCnt);
        	row.setLateCnt(lateCnt);
        	row.setNoRegistCnt(noRegistCnt);
        	
        	list.add(row);
        }
        
        return new PageImpl<>(list, pageable, entList.getTotalElements());
    }
	
	public Page<WorkTimeForCorpDto> getWorkTimeListForBelngCorp(String belngCorp, String srchYmd, int page) {
		
		Pageable pageable = PageRequest.of(page, 10);
		
		List<EnumValue> corpList = WorkTimeUtils.getListForBelngCorp(CorpEnum.class);
		
		List<WorkTimeForCorpDto> list = new ArrayList<WorkTimeForCorpDto>();
		
		WorkTimeForCorpDto row = null;
        int totalCnt, registCnt, lateCnt, noRegistCnt = 0;
		for(EnumValue corp : corpList) {
			if("00".equals(belngCorp)) {
				
				totalCnt = this.employeeRepository.getActiveEmpCntAtCorp(srchYmd, corp.getCode());
				registCnt = this.workTimeRepository.getRegEmpCntAtCorp(srchYmd, corp.getCode());
				lateCnt = this.workTimeRepository.getLateEmpCntAtCorp(srchYmd, corp.getCode());
				noRegistCnt = totalCnt - registCnt;
				
				row = new WorkTimeForCorpDto();
				
				row.setCorpCd(corp.getCode());
				row.setCorpNm(corp.getValue());
				row.setDateYmd(srchYmd);
	        	row.setTotalCnt(totalCnt);
	        	row.setRegistCnt(registCnt);
	        	row.setLateCnt(lateCnt);
	        	row.setNoRegistCnt(noRegistCnt);
				
				list.add(row);
			} else {
				if(corp.getCode().equals(belngCorp)) {
					
					totalCnt = this.employeeRepository.getActiveEmpCntAtCorp(srchYmd, corp.getCode());
					registCnt = this.workTimeRepository.getRegEmpCntAtCorp(srchYmd, corp.getCode());
					lateCnt = this.workTimeRepository.getLateEmpCntAtCorp(srchYmd, corp.getCode());
					noRegistCnt = totalCnt - registCnt;
					
					row = new WorkTimeForCorpDto();
					
					row.setCorpCd(corp.getCode());
					row.setCorpNm(corp.getValue());
					row.setDateYmd(srchYmd);
		        	row.setTotalCnt(totalCnt);
		        	row.setRegistCnt(registCnt);
		        	row.setLateCnt(lateCnt);
		        	row.setNoRegistCnt(noRegistCnt);
					
					list.add(row);
					
					break;
				}
			}
			
		}
		
		return new PageImpl<>(list, pageable, list.size());
	}
}
