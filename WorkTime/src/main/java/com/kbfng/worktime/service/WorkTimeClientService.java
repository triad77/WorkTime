package com.kbfng.worktime.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kbfng.worktime.DataNotFoundException;
import com.kbfng.worktime.dto.EmployeeDto;
import com.kbfng.worktime.dto.EmployeeWorkTimeDto;
import com.kbfng.worktime.dto.WorkTimeDto;
import com.kbfng.worktime.entity.Employee;
import com.kbfng.worktime.entity.WorkTime;
import com.kbfng.worktime.repository.EmployeeRepository;
import com.kbfng.worktime.repository.WorkTimeRepository;
import com.kbfng.worktime.util.EmployeeMapper;
import com.kbfng.worktime.util.WorkTimeMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class WorkTimeClientService {

	private final EmployeeRepository employeeRepository;
	private final WorkTimeRepository workTimeRepository;

	@Transactional(readOnly = true)
    public Page<WorkTimeDto> getList(int page) {
    	List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<WorkTime> entList = this.workTimeRepository.findAll(pageable);
        
        return entList.map(m -> WorkTimeMapper.INSTANCE.toDto(m));
    }
	
	@Transactional
	public void regist(WorkTimeDto dto) {
    	WorkTime entity = WorkTimeMapper.INSTANCE.toEntity(dto);
        this.workTimeRepository.save(entity);	
    }
	
	/**
	 * 미등록 직원목록 반환
	 * @return List<EmployeeDto>
	 */
	public List<EmployeeDto> getNotRegEmployee() {  
		List<EmployeeDto> resultList = new ArrayList<EmployeeDto>(); 
		List<Employee> allList = this.employeeRepository.findAll();
		List<WorkTime> regList = this.workTimeRepository.findByRegDateBetween(LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX));
		
		boolean isSameEmp = false;
		for(Employee allDto : allList) {
			isSameEmp = false;
			for(WorkTime regDto : regList) {
				if(allDto.getEmpNo().equals(regDto.getEmp().getEmpNo())) {
					isSameEmp = true;
					break;
				}
			}
			if(!isSameEmp) {
				resultList.add(EmployeeMapper.INSTANCE.toDto(allDto));
			}
		}
		
		return resultList;
    }
	
	/**
	 * 특정직원 DTO 반환
	 * @param empNo:String - 직원번호
	 * @return EmployeeDto
	 */
	public EmployeeDto getEmployee(String empNo) {  
		Optional<Employee> emp = this.employeeRepository.findByEmpNo(empNo);
		
		if (emp.isPresent()) {
            return EmployeeMapper.INSTANCE.toDto(emp.get());
        } else {
            throw new DataNotFoundException("Project not found");
        }
    }
	
	/**
	 * 특정직원이 특정일에 등록했는지 여부
	 * @param empNo
	 * @param srchDate
	 * @return boolean
	 */
	public boolean isRegistedEmp(String empNo, LocalDate srchDate) {
		Optional<Employee> emp = employeeRepository.findByEmpNo(empNo);
		
		if (emp.isPresent()) {
			List<WorkTime> wList = workTimeRepository.findByEmpAndRegDateBetween(emp.get(), srchDate.atStartOfDay(), srchDate.atTime(LocalTime.MAX));
			if(wList.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
            throw new DataNotFoundException("Employee is not found");
        }
	}
}
