package tech.getarrays.employeemanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import tech.getarrays.employeemanager.controller.EmployeeController;
import tech.getarrays.employeemanager.dto.EmployeeDto;
import tech.getarrays.employeemanager.entity.Employee;
import tech.getarrays.employeemanager.repo.EmployeeRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeServiceTest {



    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EmployeeRepo employeeRepo;
    @InjectMocks
    private EmployeeService employeeService;
    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeService).build();
    }

    @Test
    public void findAllEmployees() throws Exception {
        List<EmployeeDto> employeeDtoList = getMockEmployeeDtoList();
        List<Employee> employeeList = getMockEmployeeList();

        when(employeeRepo.findAll()).thenReturn(employeeList);
        List<EmployeeDto> resultedValue = employeeService.findAllEmployees();
        assertEquals(employeeDtoList.size(), resultedValue.size());
    }

    @Test
    public void addEmployee() {
        EmployeeDto employeeDto = getMockEmployeeDto(1L);
        Employee employee = getMockEmployee(1L);

        when(modelMapper.map(any(EmployeeDto.class), eq(Employee.class))).thenReturn(employee);
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);
        when(modelMapper.map(any(Employee.class), eq(EmployeeDto.class))).thenReturn(employeeDto);
        EmployeeDto response = employeeService.addEmployee(employee);
        verify(employeeRepo).save(any(Employee.class));
        assertNotNull(response);
        assertEquals(employeeDto, response);
    }

    @Test
    public void updateEmployee() {
        EmployeeDto employeeDto = getMockEmployeeDto(1);
        Employee employee = getMockEmployee(1L);

        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);
        when(modelMapper.map(employeeDto, Employee.class)).thenReturn(employee);
        when(modelMapper.map(employee, EmployeeDto.class)).thenReturn(employeeDto);
        EmployeeDto result = employeeService.updateEmployee(employee);
        verify(employeeRepo).save(any(Employee.class));
        assertEquals(employeeDto,result);
    }

    @Test
    public void findEmployeeById() {
        EmployeeDto employeeDto = getMockEmployeeDto(1L);
        Employee employee = getMockEmployee(1L);

        when(employeeRepo.findEmployeeById(anyLong())).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee,EmployeeDto.class)).thenReturn(employeeDto);

        EmployeeDto result = employeeService.findEmployeeById(1L);
        assertEquals(employeeDto,result);
    }

    @Test
    public void deleteEmployee() {
        Employee employee = getMockEmployee(1L);
        employeeService.deleteEmployee(employee.getId());
        verify(employeeRepo).deleteEmployeeById(employee.getId());
    }


    private Employee getMockEmployee(long id){
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("Bob");
        employee.setEmail("bob@gmail.com");
        employee.setJobTitle("Java");
        employee.setPhone("0123456789");
        employee.setImageUrl("none");

        return employee;
    }

    private EmployeeDto getMockEmployeeDto(long id){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(id);
        employeeDto.setName("Bob");
        employeeDto.setEmail("bob@gmail.com");
        employeeDto.setJobTitle("Java");
        employeeDto.setPhone("0123456789");
        employeeDto.setImageUrl("none");

        return employeeDto;
    }

    private List<Employee> getMockEmployeeList(){
        List<Employee> employeeList = new ArrayList<>();

        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Bob");
        employee1.setEmail("bob@gmail.com");
        employee1.setJobTitle("Java");
        employee1.setImageUrl("none");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Michael");
        employee2.setEmail("michael@gmail.com");
        employee2.setJobTitle("Angular");
        employee2.setImageUrl("none");

        Employee employee3 = new Employee();
        employee3.setId(3L);
        employee3.setName("George");
        employee3.setEmail("george@gmail.com");
        employee3.setJobTitle("React");
        employee3.setImageUrl("none");

        employeeList.add(employee1);
        employeeList.add(employee2);
        employeeList.add(employee3);

        return  employeeList;
    }

    private List<EmployeeDto> getMockEmployeeDtoList(){
        List<EmployeeDto> employeeDtoList = new ArrayList<>();

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setId(1L);
        employeeDto1.setName("Bob");
        employeeDto1.setEmail("bob@gmail.com");
        employeeDto1.setJobTitle("Java");
        employeeDto1.setImageUrl("none");

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setId(2L);
        employeeDto2.setName("Michael");
        employeeDto2.setEmail("michael@gmail.com");
        employeeDto2.setJobTitle("Angular");
        employeeDto2.setImageUrl("none");

        EmployeeDto employeeDto3 = new EmployeeDto();
        employeeDto3.setId(3L);
        employeeDto3.setName("George");
        employeeDto3.setEmail("george@gmail.com");
        employeeDto3.setJobTitle("React");
        employeeDto3.setImageUrl("none");

        employeeDtoList.add(employeeDto1);
        employeeDtoList.add(employeeDto2);
        employeeDtoList.add(employeeDto3);

        return  employeeDtoList;
    }
}