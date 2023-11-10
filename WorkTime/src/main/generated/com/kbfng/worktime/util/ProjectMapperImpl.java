package com.kbfng.worktime.util;

import com.kbfng.worktime.dto.EmployeeDto;
import com.kbfng.worktime.dto.ProjectDto;
import com.kbfng.worktime.entity.Employee;
import com.kbfng.worktime.entity.Project;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-30T10:29:58+0900",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.34.0.v20230523-1233, environment: Java 20.0.2 (Oracle Corporation)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public Project toEntity(ProjectDto dto, CycleAvoidingMappingContext context) {
        if ( dto == null ) {
            return null;
        }

        Project.ProjectBuilder project = Project.builder();

        context.storeMappedInstance( dto, project );

        project.empList( employeeDtoListToEmployeeList( dto.getEmpList(), context ) );
        project.endDt( dto.getEndDt() );
        project.plodrCorp( dto.getPlodrCorp() );
        project.prjId( dto.getPrjId() );
        project.prjNm( dto.getPrjNm() );
        project.startDt( dto.getStartDt() );
        project.stat( dto.getStat() );

        return project.build();
    }

    @Override
    public ProjectDto toDto(Project entity, CycleAvoidingMappingContext context) {
        if ( entity == null ) {
            return null;
        }

        ProjectDto.ProjectDtoBuilder projectDto = ProjectDto.builder();

        context.storeMappedInstance( entity, projectDto );

        projectDto.empList( employeeListToEmployeeDtoList( entity.getEmpList(), context ) );
        projectDto.endDt( entity.getEndDt() );
        projectDto.plodrCorp( entity.getPlodrCorp() );
        projectDto.prjId( entity.getPrjId() );
        projectDto.prjNm( entity.getPrjNm() );
        projectDto.startDt( entity.getStartDt() );
        projectDto.stat( entity.getStat() );

        return projectDto.build();
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

    protected List<Employee> employeeDtoListToEmployeeList(List<EmployeeDto> list, CycleAvoidingMappingContext context) {
        List<Employee> target = context.getMappedInstance( list, List.class );
        if ( target != null ) {
            return target;
        }

        if ( list == null ) {
            return null;
        }

        List<Employee> list1 = new ArrayList<Employee>( list.size() );
        context.storeMappedInstance( list, list1 );

        for ( EmployeeDto employeeDto : list ) {
            list1.add( employeeDtoToEmployee( employeeDto, context ) );
        }

        return list1;
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

    protected List<EmployeeDto> employeeListToEmployeeDtoList(List<Employee> list, CycleAvoidingMappingContext context) {
        List<EmployeeDto> target = context.getMappedInstance( list, List.class );
        if ( target != null ) {
            return target;
        }

        if ( list == null ) {
            return null;
        }

        List<EmployeeDto> list1 = new ArrayList<EmployeeDto>( list.size() );
        context.storeMappedInstance( list, list1 );

        for ( Employee employee : list ) {
            list1.add( employeeToEmployeeDto( employee, context ) );
        }

        return list1;
    }
}
