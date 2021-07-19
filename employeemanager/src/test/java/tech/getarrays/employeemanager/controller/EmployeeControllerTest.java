package tech.getarrays.employeemanager.controller;

import net.minidev.json.JSONObject;
import tech.getarrays.employeemanager.dto.EmployeeDto;
import tech.getarrays.employeemanager.entity.Employee;
import tech.getarrays.employeemanager.service.EmployeeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;


public class EmployeeControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private EmployeeController employeeController;
    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }



    @org.junit.Test
    public void getAllEmployees() throws Exception {
        List<EmployeeDto> employeeDtoList = getMockEmployeeDtoList();
        when(employeeService.findAllEmployees()).thenReturn(employeeDtoList);
        MvcResult mvcResult = mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        List<EmployeeDto> employeeDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EmployeeDto>>() {
        });
        assertEquals(employeeDtoList, employeeDtos);
    }

    @org.junit.Test
    public void getEmployeeById() throws Exception {
        EmployeeDto employeeDto = getMockEmployeeDto(1L);
        long id = employeeDto.getId();

        when(employeeService.findEmployeeById(id)).thenReturn(employeeDto);
        mockMvc.perform(get("/employees/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(employeeDto.getName()))
                .andExpect(jsonPath("$.email").value(employeeDto.getEmail()))
                .andExpect(jsonPath("$.jobTitle").value(employeeDto.getJobTitle()))
                .andExpect(jsonPath("$.phone").value(employeeDto.getPhone()))
                .andExpect(jsonPath("$.imageUrl").value(employeeDto.getImageUrl()));
    }

    @org.junit.Test
    public void addEmployee() throws Exception {
        Employee employee = getMockEmployee(1L);
        EmployeeDto employeeDto = getMockEmployeeDto(1l);

        when(employeeService.addEmployee(employee)).thenReturn(employeeDto);

        JSONObject jsonContent =new JSONObject();
        jsonContent.put("id",1L);
        jsonContent.put("name","Bob");
        jsonContent.put("email","bob@gmail.com");
        jsonContent.put("jobTitle","Java");
        jsonContent.put("phone","0123456789");
        jsonContent.put("imageUrl","none");

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(employeeDto.getId()))
                .andExpect(jsonPath("$.name").value(employeeDto.getName()))
                .andExpect(jsonPath("$.email").value(employeeDto.getEmail()))
                .andExpect(jsonPath("$.jobTitle").value(employeeDto.getJobTitle()))
                .andExpect(jsonPath("$.phone").value(employeeDto.getPhone()))
                .andExpect(jsonPath("$.imageUrl").value(employeeDto.getImageUrl()));
    }

    @org.junit.Test
    public void updateEmployee() throws Exception {
        Employee employee = getMockEmployee(1L);
        EmployeeDto employeeDto = getMockEmployeeDto(1L);

        when(employeeService.updateEmployee(employee)).thenReturn(employeeDto);

        JSONObject jsonContent =new JSONObject();
        jsonContent.put("id", 1L);
        jsonContent.put("name","Bob");
        jsonContent.put("email","bob@gmail.com");
        jsonContent.put("jobTitle","job");
        jsonContent.put("phone","0123456789");
        jsonContent.put("imageUrl","Bob");

        mockMvc.perform(put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent.toString()))
                .andExpect(status().isOk());
    }

    @org.junit.Test
    public void deleteEmployee() throws Exception {
        long id = 10L;
        mockMvc.perform(delete("/employees/{id}",id))
                .andExpect(status().isOk());
        verify(employeeService, times(1)).deleteEmployee(id);
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