package com.kbfng.worktime.util;

import com.kbfng.worktime.dto.EmployeeDto;
import com.kbfng.worktime.entity.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-30T10:29:58+0900",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230523-1233, environment: Java 20.0.2 (Oracle Corporation)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public Employee toEntity(EmployeeDto dto, CycleAvoidingMappingContext context) {
        if ( dto == null ) {
            return null;
        }

        Employee.EmployeeBuilder employee = Employee.builder();

        context.storeMappedInstance( dto, employee );

        employee.contract( dto.getContract() );
        employee.corp( dto.getCorp() );
        employee.ctrcEdDate( dto.getCtrcEdDate() );
        employee.ctrcStDate( dto.getCtrcStDate() );
        employee.empNm( dto.getEmpNm() );
        employee.empNo( dto.getEmpNo() );
        if ( dto.getId() != null ) {
            employee.id( dto.getId() );
        }
        employee.ipAddr( dto.getIpAddr() );
        employee.project( dto.getProject() );

        return employee.build();
    }

    @Override
    public EmployeeDto toDto(Employee entity, CycleAvoidingMappingContext context) {
        if ( entity == null ) {
            return null;
        }

        EmployeeDto.EmployeeDtoBuilder employeeDto = EmployeeDto.builder();

        context.storeMappedInstance( entity, employeeDto );

        employeeDto.contract( entity.getContract() );
        employeeDto.corp( entity.getCorp() );
        employeeDto.ctrcEdDate( entity.getCtrcEdDate() );
        employeeDto.ctrcStDate( entity.getCtrcStDate() );
        employeeDto.empNm( entity.getEmpNm() );
        employeeDto.empNo( entity.getEmpNo() );
        employeeDto.id( entity.getId() );
        employeeDto.ipAddr( entity.getIpAddr() );
        employeeDto.project( entity.getProject() );

        return employeeDto.build();
    }
}
