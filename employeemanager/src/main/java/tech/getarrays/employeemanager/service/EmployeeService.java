package tech.getarrays.employeemanager.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.getarrays.employeemanager.dto.EmployeeDto;
import tech.getarrays.employeemanager.exception.UserNotFoundException;
import tech.getarrays.employeemanager.entity.Employee;
import tech.getarrays.employeemanager.repo.EmployeeRepo;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final ModelMapper mapper;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo, ModelMapper mapper){
        this.employeeRepo = employeeRepo;
        this.mapper = mapper;
    }

    public EmployeeDto addEmployee(Employee employee){
        employee.setEmployeeCode(UUID.randomUUID().toString());
        return mapToDTO(employeeRepo.save(employee));
    }

    public List<EmployeeDto> findAllEmployees(){
        return mapToDTOList(employeeRepo.findAll());
    }

    public  EmployeeDto updateEmployee(Employee employee){
        return mapToDTO(employeeRepo.save(employee));
    }

    public EmployeeDto findEmployeeById(Long id){
        return mapToDTO(employeeRepo.findEmployeeById(id)
                .orElseThrow(()->new UserNotFoundException("User By id " + id + "was not found")));
    }

    public void deleteEmployee(Long id){
        //Employee employee = mapToEntity(findEmployeeById(id));
        //employeeRepo.delete(employee);
        employeeRepo.deleteEmployeeById(id);
    }

    public List<EmployeeDto> mapToDTOList(List<Employee> employeeList){
        List<EmployeeDto> dtoList = employeeList.stream().map(student -> mapper.map(student, EmployeeDto.class)).collect(Collectors.toList());
        return dtoList;
    }
    public EmployeeDto mapToDTO(Employee student){
        EmployeeDto dto=mapper.map(student, EmployeeDto.class);
        return dto;
    }
    public Employee mapToEntity(EmployeeDto dto){
        Employee employee = mapper.map(dto, Employee.class);
        return employee;
    }
}
