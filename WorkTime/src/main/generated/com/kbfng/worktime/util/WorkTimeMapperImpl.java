package com.kbfng.worktime.util;

import com.kbfng.worktime.dto.EmployeeDto;
import com.kbfng.worktime.dto.WorkTimeDto;
import com.kbfng.worktime.entity.Employee;
import com.kbfng.worktime.entity.WorkTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-30T10:29:59+0900",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230523-1233, environment: Java 20.0.2 (Oracle Corporation)"
)
@Component
public class WorkTimeMapperImpl implements WorkTimeMapper {

    @Override
    public WorkTime toEntity(WorkTimeDto dto, CycleAvoidingMappingContext context) {
        if ( dto == null ) {
            return null;
        }

        WorkTime.WorkTimeBuilder workTime = WorkTime.builder();

        context.storeMappedInstance( dto, workTime );

        workTime.emp( employeeDtoToEmployee( dto.getEmp(), context ) );
        workTime.id( dto.getId() );
        workTime.reason( dto.getReason() );
        workTime.regDate( dto.getRegDate() );
        workTime.scanIp( dto.getScanIp() );

        return workTime.build();
    }

    @Override
    public WorkTimeDto toDto(WorkTime entity, CycleAvoidingMappingContext context) {
        if ( entity == null ) {
            return null;
        }

        WorkTimeDto.WorkTimeDtoBuilder workTimeDto = WorkTimeDto.builder();

        context.storeMappedInstance( entity, workTimeDto );

        workTimeDto.emp( employeeToEmployeeDto( entity.getEmp(), context ) );
        workTimeDto.id( entity.getId() );
        workTimeDto.reason( entity.getReason() );
        workTimeDto.regDate( entity.getRegDate() );
        workTimeDto.scanIp( entity.getScanIp() );

        return workTimeDto.build();
    }

    protected Employee employeeDtoToEmployee(EmployeeDto employeeDto, CycleAvoidingMappingContext context) {
        if ( employeeDto == null ) {
            return null;
        }

        Employee.EmployeeBuilder employee = Employee.builder();

        context.storeMappedInstance( employeeDto, employee );

        employee.contract( employeeDto.getContract() );
        employee.corp( employeeDto.getCorp() );
        employee.ctrcEdDate( employeeDto.getCtrcEdDate() );
        employee.ctrcStDate( employeeDto.getCtrcStDate() );
        employee.empNm( employeeDto.getEmpNm() );
        employee.empNo( employeeDto.getEmpNo() );
        if ( employeeDto.getId() != null ) {
            employee.id( employeeDto.getId() );
        }
        employee.ipAddr( employeeDto.getIpAddr() );
        employee.project( employeeDto.getProject() );

        return employee.build();
    }

    protected EmployeeDto employeeToEmployeeDto(Employee employee, CycleAvoidingMappingContext context) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDto.EmployeeDtoBuilder employeeDto = EmployeeDto.builder();

        context.storeMappedInstance( employee, employeeDto );

        employeeDto.contract( employee.getContract() );
        employeeDto.corp( employee.getCorp() );
        employeeDto.ctrcEdDate( employee.getCtrcEdDate() );
        employeeDto.ctrcStDate( employee.getCtrcStDate() );
        employeeDto.empNm( employee.getEmpNm() );
        employeeDto.empNo( employee.getEmpNo() );
        employeeDto.id( employee.getId() );
        employeeDto.ipAddr( employee.getIpAddr() );
        employeeDto.project( employee.getProject() );

        return employeeDto.build();
    }
}
