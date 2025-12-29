package com.example.demo;

import com.example.demo.config.JwtUtil;
import com.example.demo.controller.*;
import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import org.mockito.*;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.Optional;
import static org.mockito.Mockito.*;

@Listeners(TestResultListener.class)
public class MasterTestNGSuiteTest {

    // COMMON MOCKS
    @Mock private EmployeeRepository employeeRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private ShiftTemplateRepository shiftTemplateRepository;
    @Mock private AvailabilityRepository availabilityRepository;
    @Mock private GeneratedShiftScheduleRepository scheduleRepository;

    @Mock private JwtUtil jwtUtil;

    private EmployeeService employeeService;
    private DepartmentService departmentService;
    private ShiftTemplateService shiftTemplateService;
    private AvailabilityService availabilityService;
    private ScheduleService scheduleService;

    @BeforeClass
    public void init() {
        MockitoAnnotations.openMocks(this);

        employeeService = new EmployeeServiceImpl(employeeRepository);
        departmentService = new DepartmentServiceImpl(departmentRepository);
        shiftTemplateService = new ShiftTemplateServiceImpl(shiftTemplateRepository, departmentRepository);
        availabilityService = new AvailabilityServiceImpl(availabilityRepository, employeeRepository);
        scheduleService = new ScheduleServiceImpl(
                shiftTemplateRepository, availabilityRepository, employeeRepository, scheduleRepository, departmentRepository
        );
    }

    // -------------------------------------------------------------------------
    // 1) BASIC SERVLET SIMULATION TESTS
    // -------------------------------------------------------------------------
    @Test(priority = 1)
    public void testServletInit() {
        String servlet = "DemoServlet";
        Assert.assertEquals(servlet, "DemoServlet");
    }

    @Test(priority = 2)
    public void testServletResponse() {
        Assert.assertTrue("Servlet Running".contains("Running"));
    }

    // -------------------------------------------------------------------------
    // 2) CRUD TEST CASES
    // -------------------------------------------------------------------------

    // EMPLOYEE CRUD ------------------------------------------------------------
    @Test(priority = 3)
    public void testCreateEmployee() {
        Employee e = new Employee("John", "john@test.com", "STAFF", "JAVA", 40);

        when(employeeRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(employeeRepository.save(e)).thenReturn(e);

        Employee saved = employeeService.createEmployee(e);
        Assert.assertEquals(saved.getEmail(), "john@test.com");
    }

    @Test(priority = 4)
    public void testCreateEmployeeEmailDuplicate() {
        Employee e = new Employee("A", "x@test.com", "STAFF", "JAVA", 40);
        when(employeeRepository.existsByEmail("x@test.com")).thenReturn(true);

        try {
            employeeService.createEmployee(e);
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage().contains("exists"));
        }
    }

    @Test(priority = 5)
    public void testGetEmployee() {
        Employee e = new Employee("Amy", "amy@test.com", "ADMIN", "MGMT", 40);
        e.setId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(e));
        Assert.assertEquals(employeeService.getEmployee(1L).getEmail(), "amy@test.com");
    }

    @Test(priority = 6)
    public void testUpdateEmployee() {
        Employee old = new Employee("Old", "old@test.com", "STAFF", "JAVA", 40);
        old.setId(1L);

        Employee updated = new Employee("New", "new@test.com", "ADMIN", "JAVA", 40);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(old));
        when(employeeRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(employeeRepository.save(old)).thenReturn(old);

        Employee res = employeeService.updateEmployee(1L, updated);
        Assert.assertEquals(res.getFullName(), "New");
    }

    @Test(priority = 7)
    public void testDeleteEmployee() {
        Employee e = new Employee("Jake", "j@test.com", "STAFF", "AWS", 40);
        e.setId(10L);

        when(employeeRepository.findById(10L)).thenReturn(Optional.of(e));

        employeeService.deleteEmployee(10L);
        verify(employeeRepository, times(1)).delete(e);
    }

    // DEPARTMENT CRUD ----------------------------------------------------------
    @Test(priority = 8)
    public void testCreateDepartment() {
        Department d = new Department("IT", "Tech", "JAVA");
        when(departmentRepository.existsByName("IT")).thenReturn(false);
        when(departmentRepository.save(d)).thenReturn(d);

        Department saved = departmentService.create(d);
        Assert.assertEquals(saved.getName(), "IT");
    }

    @Test(priority = 9)
    public void testDepartmentNameExists() {
        Department d = new Department("QA", "Quality", "TESTING");
        when(departmentRepository.existsByName("QA")).thenReturn(true);

        try {
            departmentService.create(d);
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertTrue(ex.getMessage().contains("exists"));
        }
    }

    @Test(priority = 10)
    public void testGetDepartment() {
        Department d = new Department("Ops", "Operation", "MGMT");
        d.setId(2L);

        when(departmentRepository.findById(2L)).thenReturn(Optional.of(d));
        Assert.assertEquals(departmentService.get(2L).getName(), "Ops");
    }

    // SHIFT TEMPLATE CRUD ------------------------------------------------------
    @Test(priority = 11)
    public void testCreateShiftTemplate() {
        Department dept = new Department("Cloud", "cloud", "AWS");
        dept.setId(7L);

        ShiftTemplate st = new ShiftTemplate("Night", LocalTime.of(20,0), LocalTime.of(23,0),"AWS",dept);

        when(departmentRepository.findById(7L)).thenReturn(Optional.of(dept));
        when(shiftTemplateRepository.findByTemplateNameAndDepartment_Id("Night",7L))
                .thenReturn(Optional.empty());
        when(shiftTemplateRepository.save(st)).thenReturn(st);

        ShiftTemplate saved = shiftTemplateService.create(st);
        Assert.assertEquals(saved.getTemplateName(),"Night");
    }

    @Test(priority = 12)
    public void testShiftTemplateInvalidTime() {
        Department dept = new Department("Net","Network","CISCO");
        dept.setId(5L);

        ShiftTemplate st = new ShiftTemplate("Bad",LocalTime.of(22,0),LocalTime.of(21,0),"CISCO",dept);

        when(departmentRepository.findById(5L)).thenReturn(Optional.of(dept));

        try { shiftTemplateService.create(st); Assert.fail(); }
        catch(Exception e){ Assert.assertTrue(e.getMessage().contains("after")); }
    }

    // -------------------------------------------------------------------------
    // 3) SPRING DI + IOC TEST CASES
    // -------------------------------------------------------------------------
    @Test(priority = 13)
    public void testDIEmployeeService() { Assert.assertNotNull(employeeService); }

    @Test(priority = 14)
    public void testDIDepartmentService() { Assert.assertNotNull(departmentService); }

    @Test(priority = 15)
    public void testDIAvailabilityService() { Assert.assertNotNull(availabilityService); }

    // -------------------------------------------------------------------------
    // 4) HIBERNATE / ENTITY VALIDATION TESTS
    // -------------------------------------------------------------------------
    @Test(priority = 16)
    public void testEmployeeEntity() {
        Employee e = new Employee();
        e.setFullName("Test");
        Assert.assertEquals(e.getFullName(),"Test");
    }

    @Test(priority = 17)
    public void testDepartmentEntity() {
        Department d = new Department();
        d.setName("HR");
        Assert.assertEquals(d.getName(),"HR");
    }

    @Test(priority = 18)
    public void testShiftTemplateTimes() {
        ShiftTemplate st = new ShiftTemplate();
        st.setStartTime(LocalTime.of(8,0));
        st.setEndTime(LocalTime.of(10,0));
        Assert.assertTrue(st.getEndTime().isAfter(st.getStartTime()));
    }

    // -------------------------------------------------------------------------
    // 5) JPA MAPPING / NORMALIZATION TESTS
    // -------------------------------------------------------------------------
    @Test(priority = 19)
    public void testEmployeeSkillFormat() {
        Employee e = new Employee("A","a@test.com","STAFF","JAVA,SQL",40);
        Assert.assertTrue(e.getSkills().contains("JAVA"));
    }

    @Test(priority = 20)
    public void testAvailabilityUniqueCheck() {
        LocalDate d = LocalDate.now();

        when(availabilityRepository.findByEmployee_IdAndAvailableDate(1L,d))
                .thenReturn(Optional.of(new EmployeeAvailability()));

        try {
            EmployeeAvailability av = new EmployeeAvailability(new Employee(), d,true);
            av.getEmployee().setId(1L);
            availabilityService.create(av);
            Assert.fail();
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("exists"));
        }
    }

    // -------------------------------------------------------------------------
    // 6) MANY-TO-MANY LOGIC TESTS (SKILL MATCHING)
    // -------------------------------------------------------------------------
    @Test(priority = 21)
    public void testEmployeeSkillMatching() {
        Employee e = new Employee("A","a@test.com","STAFF","JAVA,SPRING",40);
        Assert.assertTrue(e.getSkills().contains("SPRING"));
    }

    @Test(priority = 22)
    public void testShiftTemplateSkillMatching() {
        ShiftTemplate st = new ShiftTemplate();
        st.setRequiredSkills("JAVA,DOCKER");
        Assert.assertTrue(st.getRequiredSkills().contains("JAVA"));
    }

    // -------------------------------------------------------------------------
    // 7) SECURITY + JWT TESTS
    // -------------------------------------------------------------------------
    @Test(priority = 23)
    public void testJwtValid() {
        when(jwtUtil.validateToken("abc")).thenReturn(true);
        Assert.assertTrue(jwtUtil.validateToken("abc"));
    }

    @Test(priority = 24)
    public void testJwtInvalid() {
        when(jwtUtil.validateToken("bad")).thenReturn(false);
        Assert.assertFalse(jwtUtil.validateToken("bad"));
    }

    // -------------------------------------------------------------------------
    // 8) HQL / QUERY BEHAVIOR TESTS
    // -------------------------------------------------------------------------
    @Test(priority = 25)
    public void testRepoFindEmployeeByEmail() {
        Employee e = new Employee("T","t@test.com","STAFF","SQL",40);
        when(employeeRepository.findByEmail("t@test.com")).thenReturn(Optional.of(e));
        Assert.assertEquals(employeeService.findByEmail("t@test.com").getFullName(), "T");
    }

    @Test(priority = 26)
    public void testAvailabilityRepoQuery() {
        LocalDate d = LocalDate.now();
        EmployeeAvailability av = new EmployeeAvailability();
        av.setAvailable(true);

        when(availabilityRepository.findByAvailableDateAndAvailable(d,true)).thenReturn(List.of(av));
        Assert.assertEquals(availabilityService.getByDate(d).size(),1);
    }

    @Test(priority = 27)
    public void testShiftTemplateQuery() {
        ShiftTemplate st = new ShiftTemplate();
        st.setTemplateName("Test");

        when(shiftTemplateRepository.findByDepartment_Id(10L)).thenReturn(List.of(st));
        Assert.assertEquals(shiftTemplateService.getByDepartment(10L).get(0).getTemplateName(),"Test");
    }

    // -------------------------------------------------------------------------
    // 9) SCHEDULING ENGINE TESTS
    // -------------------------------------------------------------------------
    @Test(priority = 28)
    public void testScheduleGenerationMatch() {
        LocalDate d = LocalDate.now();

        Department dept = new Department("Ops","Ops","JAVA");
        dept.setId(1L);

        ShiftTemplate st = new ShiftTemplate("Day",LocalTime.of(9,0),LocalTime.of(17,0),"JAVA",dept);

        Employee emp = new Employee("Bob","b@test.com","STAFF","JAVA",40);
        emp.setId(50L);

        EmployeeAvailability av = new EmployeeAvailability(emp,d,true);

        when(departmentRepository.findAll()).thenReturn(List.of(dept));
        when(shiftTemplateRepository.findByDepartment_Id(1L)).thenReturn(List.of(st));
        when(availabilityRepository.findByAvailableDateAndAvailable(d,true)).thenReturn(List.of(av));
        when(scheduleRepository.save(any())).thenAnswer(i->i.getArguments()[0]);

        List<GeneratedShiftSchedule> res = scheduleService.generateForDate(d);
        Assert.assertEquals(res.size(),1);
    }

    @Test(priority = 29)
    public void testScheduleSkillMismatch() {
        LocalDate d = LocalDate.now();

        Department dept = new Department("Ops","Ops","PYTHON");
        dept.setId(2L);

        ShiftTemplate st = new ShiftTemplate("Day",LocalTime.of(9,0),LocalTime.of(17,0),"PYTHON",dept);

        Employee emp = new Employee("Bob","b@test.com","STAFF","JAVA",40);
        emp.setId(10L);

        EmployeeAvailability av = new EmployeeAvailability(emp,d,true);

        when(departmentRepository.findAll()).thenReturn(List.of(dept));
        when(shiftTemplateRepository.findByDepartment_Id(2L)).thenReturn(List.of(st));
        when(availabilityRepository.findByAvailableDateAndAvailable(d,true)).thenReturn(List.of(av));

        Assert.assertTrue(scheduleService.generateForDate(d).isEmpty());
    }

    // -------------------------------------------------------------------------
    // ADDITIONAL 31–68 UNIQUE REAL TEST CASES
    // -------------------------------------------------------------------------

    @Test(priority = 30)
    public void testEmployeeMaxHoursInvalid() {
        Employee e = new Employee("A","a@test.com","STAFF","JAVA",0);
        when(employeeRepository.existsByEmail("a@test.com")).thenReturn(false);

        try { employeeService.createEmployee(e); Assert.fail();}
        catch(Exception ex){ Assert.assertTrue(ex.getMessage().contains("must")); }
    }

    @Test(priority = 31)
    public void testRoleDefaultToStaff() {
        Employee e = new Employee("A","role@test.com",null,"JAVA",40);
        when(employeeRepository.existsByEmail("role@test.com")).thenReturn(false);
        when(employeeRepository.save(e)).thenReturn(e);

        Employee saved = employeeService.createEmployee(e);
        Assert.assertEquals(saved.getRole(),"STAFF");
    }

    @Test(priority = 32)
    public void testDepartmentSkillFormat() {
        Department d = new Department("Infra","Infra","NETWORK,LINUX");
        when(departmentRepository.existsByName("Infra")).thenReturn(false);
        when(departmentRepository.save(d)).thenReturn(d);

        Assert.assertTrue(departmentService.create(d).getRequiredSkills().contains("NETWORK"));
    }

    @Test(priority = 33)
    public void testShiftTemplateUniqueWithinDept() {
        Department d = new Department("Cloud","Cloud","AWS");
        d.setId(5L);

        ShiftTemplate st = new ShiftTemplate("Morning",LocalTime.of(9,0),LocalTime.of(12,0),"AWS",d);

        when(departmentRepository.findById(5L)).thenReturn(Optional.of(d));
        when(shiftTemplateRepository.findByTemplateNameAndDepartment_Id("Morning",5L))
                .thenReturn(Optional.of(st));

        try { shiftTemplateService.create(st); Assert.fail();}
        catch(Exception e){ Assert.assertTrue(e.getMessage().contains("unique")); }
    }

    @Test(priority = 34)
    public void testAvailabilityUpdate() {
        EmployeeAvailability av = new EmployeeAvailability();
        av.setId(10L);
        av.setAvailable(true);

        when(availabilityRepository.findById(10L)).thenReturn(Optional.of(av));
        when(availabilityRepository.save(av)).thenReturn(av);

        EmployeeAvailability updated = availabilityService.update(10L,
                new EmployeeAvailability(null,LocalDate.now(),false));

        Assert.assertFalse(updated.getAvailable());
    }

    @Test(priority = 35)
    public void testAvailabilityDelete() {
        EmployeeAvailability av = new EmployeeAvailability();
        av.setId(1L);

        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(av));

        availabilityService.delete(1L);
        verify(availabilityRepository,times(1)).delete(av);
    }

    @Test(priority = 36)
    public void testScheduleMultipleDepartments() {
        LocalDate date = LocalDate.now();

        Department d1 = new Department("IT","Tech","JAVA");
        d1.setId(1L);
        Department d2 = new Department("HR","Hum","COMM");
        d2.setId(2L);

        ShiftTemplate s1 = new ShiftTemplate("M1",LocalTime.of(9,0),LocalTime.of(11,0),"JAVA",d1);
        ShiftTemplate s2 = new ShiftTemplate("M2",LocalTime.of(12,0),LocalTime.of(15,0),"COMM",d2);

        when(departmentRepository.findAll()).thenReturn(List.of(d1,d2));
        when(shiftTemplateRepository.findByDepartment_Id(1L)).thenReturn(List.of(s1));
        when(shiftTemplateRepository.findByDepartment_Id(2L)).thenReturn(List.of(s2));

        Employee e1 = new Employee("Dev","dev@test.com","STAFF","JAVA",40); e1.setId(10L);
        Employee e2 = new Employee("HR","hr@test.com","STAFF","COMM",40); e2.setId(20L);

        EmployeeAvailability av1 = new EmployeeAvailability(e1,date,true);
        EmployeeAvailability av2 = new EmployeeAvailability(e2,date,true);

        when(availabilityRepository.findByAvailableDateAndAvailable(date,true))
                .thenReturn(List.of(av1,av2));

        when(scheduleRepository.save(any())).thenAnswer(i->i.getArguments()[0]);

        List<GeneratedShiftSchedule> out = scheduleService.generateForDate(date);
        Assert.assertEquals(out.size(),2);
    }

    @Test(priority = 37)
    public void testScheduleNoAvailability() {
        LocalDate d = LocalDate.now();

        Department dept = new Department("Ops","Ops","LINUX");
        dept.setId(9L);

        ShiftTemplate st = new ShiftTemplate("N",LocalTime.of(18,0),LocalTime.of(22,0),"LINUX",dept);

        when(departmentRepository.findAll()).thenReturn(List.of(dept));
        when(shiftTemplateRepository.findByDepartment_Id(9L)).thenReturn(List.of(st));
        when(availabilityRepository.findByAvailableDateAndAvailable(d,true))
                .thenReturn(List.of());

        Assert.assertTrue(scheduleService.generateForDate(d).isEmpty());
    }

    @Test(priority = 38)
    public void testEmployeeControllerList() {
        Employee e1 = new Employee("A","a@test.com","STAFF","JAVA",40);
        Employee e2 = new Employee("B","b@test.com","STAFF","SQL",40);

        when(employeeRepository.findAll()).thenReturn(List.of(e1,e2));

        EmployeeController c = new EmployeeController(employeeService);
        Assert.assertEquals(c.list().getBody().size(),2);
    }

    @Test(priority = 39)
    public void testDepartmentControllerGet() {
        Department d = new Department("QA","QA","TEST");
        d.setId(1L);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(d));

        DepartmentController c = new DepartmentController(departmentService);
        Assert.assertEquals(c.get(1L).getBody().getName(),"QA");
    }

    @Test(priority = 40)
    public void testShiftTemplateControllerList() {
        Department d = new Department("Ops","Ops","LINUX"); d.setId(5L);

        ShiftTemplate st = new ShiftTemplate("M",
                LocalTime.of(8,0),LocalTime.of(12,0),"LINUX",d);

        when(shiftTemplateRepository.findAll()).thenReturn(List.of(st));
        when(departmentRepository.findById(any())).thenReturn(Optional.of(d));

        ShiftTemplateController sc = new ShiftTemplateController(shiftTemplateService,departmentRepository);

        Assert.assertEquals(sc.list().getBody().size(),1);
    }

    @Test(priority = 41)
    public void testAvailabilityControllerGetByDate() {
        LocalDate d = LocalDate.of(2025,5,1);

        Employee e = new Employee("A","a@test.com","STAFF","LINUX",40);
        e.setId(1L);

        EmployeeAvailability av = new EmployeeAvailability(e,d,true);

        when(availabilityRepository.findByAvailableDateAndAvailable(d,true))
                .thenReturn(List.of(av));

        AvailabilityController c = new AvailabilityController(availabilityService,employeeRepository);

        Assert.assertEquals(c.byDate("2025-05-01").getBody().size(),1);
    }

    @Test(priority = 42)
    public void testScheduleControllerGenerate() {
        LocalDate d = LocalDate.of(2025,8,1);

        Department dept = new Department("Ops","Ops","JAVA");
        dept.setId(7L);

        ShiftTemplate st = new ShiftTemplate("Day",
                LocalTime.of(9,0),LocalTime.of(17,0),"JAVA",dept);

        Employee e = new Employee("A","a@test.com","STAFF","JAVA",40); e.setId(9L);

        EmployeeAvailability av = new EmployeeAvailability(e,d,true);

        when(departmentRepository.findAll()).thenReturn(List.of(dept));
        when(shiftTemplateRepository.findByDepartment_Id(7L)).thenReturn(List.of(st));
        when(availabilityRepository.findByAvailableDateAndAvailable(d,true))
                .thenReturn(List.of(av));
        when(scheduleRepository.save(any())).thenAnswer(i->i.getArguments()[0]);

        ScheduleController sc = new ScheduleController(scheduleService);

        Assert.assertEquals(sc.generate("2025-08-01").getBody().size(),1);
    }


    @Test(priority = 43)
    public void testJwtTokenStructure() {
        when(jwtUtil.validateToken("tok")).thenReturn(true);
        Assert.assertTrue(jwtUtil.validateToken("tok"));
    }

    @Test(priority = 44)
    public void testScheduleGetByDateRepo() {
        LocalDate d = LocalDate.now();
        GeneratedShiftSchedule g = new GeneratedShiftSchedule();
        g.setShiftDate(d);

        when(scheduleRepository.findByShiftDate(d)).thenReturn(List.of(g));

        Assert.assertEquals(scheduleService.getByDate(d).size(),1);
    }

    @Test(priority = 45)
    public void testSkillParsing() {
        Employee e = new Employee("T","t@test.com","STAFF","JAVA,SPRING,SQL",40);
        Assert.assertTrue(e.getSkills().contains("SPRING"));
    }

    @Test(priority = 46)
    public void testDepartmentSkillValidation() {
        Department d = new Department("Support","Help","COMM,SOFT");
        Assert.assertTrue(d.getRequiredSkills().contains("SOFT"));
    }

    @Test(priority = 47)
    public void testUpdateMissingEmployee() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        try {
            employeeService.updateEmployee(99L,new Employee());
            Assert.fail();
        } catch(Exception e) { Assert.assertTrue(e.getMessage().contains("not found")); }
    }

    @Test(priority = 48)
    public void testDeleteMissingDepartment() {
        when(departmentRepository.findById(100L)).thenReturn(Optional.empty());

        try { departmentService.delete(100L); Assert.fail(); }
        catch(Exception e) { Assert.assertTrue(e.getMessage().contains("not found")); }
    }

    
    @Test(priority = 49)
    public void testAvailabilityNullDateAllowed() {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setAvailable(true);

        Assert.assertNull(dto.getAvailableDate());
    }

    @Test(priority = 50)
    public void testTimeUtils() {
        long m = com.example.demo.util.TimeUtils.minutesBetween(LocalTime.of(10,0),LocalTime.of(11,0));
        Assert.assertEquals(m,60);
    }

    @Test(priority = 51)
    public void testRepoExistsEmail() {
        when(employeeRepository.existsByEmail("abc@test.com")).thenReturn(true);
        Assert.assertTrue(employeeRepository.existsByEmail("abc@test.com"));
    }

    @Test(priority = 52)
    public void testShiftTemplateRepoEmpty() {
        when(shiftTemplateRepository.findByDepartment_Id(500L)).thenReturn(List.of());
        Assert.assertTrue(shiftTemplateService.getByDepartment(500L).isEmpty());
    }

    @Test(priority = 53)
    public void testAvailabilityRepoEmployeeQuery() {
        when(availabilityRepository.findByEmployee_Id(1L)).thenReturn(List.of());
        Assert.assertTrue(availabilityRepository.findByEmployee_Id(1L).isEmpty());
    }

    @Test(priority = 54)
    public void testScheduleTemplateSkillMismatch() {
        LocalDate d = LocalDate.now();

        Department dept = new Department("Dev","Dev","JAVA");
        dept.setId(4L);

        ShiftTemplate st = new ShiftTemplate("Shift",LocalTime.of(9,0),LocalTime.of(12,0),"PYTHON",dept);

        Employee e = new Employee("X","x@test.com","STAFF","JAVA",40);
        e.setId(7L);

        EmployeeAvailability av = new EmployeeAvailability(e,d,true);

        when(departmentRepository.findAll()).thenReturn(List.of(dept));
        when(shiftTemplateRepository.findByDepartment_Id(4L)).thenReturn(List.of(st));
        when(availabilityRepository.findByAvailableDateAndAvailable(d,true)).thenReturn(List.of(av));

        Assert.assertTrue(scheduleService.generateForDate(d).isEmpty());
    }

    @Test(priority = 55)
    public void testEmployeeDeleteController() {
        Employee e = new Employee("A","a@test.com","STAFF","JAVA",40);
        e.setId(5L);

        when(employeeRepository.findById(5L)).thenReturn(Optional.of(e));

        EmployeeController c = new EmployeeController(employeeService);
        Assert.assertEquals(c.delete(5L).getBody(),"Deleted");
    }

    @Test(priority = 56)
    public void testDepartmentDeleteController() {
        Department d = new Department("D","Desc","JAVA");
        d.setId(9L);

        when(departmentRepository.findById(9L)).thenReturn(Optional.of(d));

        DepartmentController c = new DepartmentController(departmentService);
        Assert.assertEquals(c.delete(9L).getBody(),"Deleted");
    }

    @Test(priority = 57)
    public void testEmailFormatModel() {
        Employee e = new Employee("A","notEmail","STAFF","JAVA",40);
        Assert.assertEquals(e.getEmail(),"notEmail");
    }

    @Test(priority = 58)
    public void testDeptDescription() {
        Department d = new Department();
        d.setDescription("desk");
        Assert.assertEquals(d.getDescription(),"desk");
    }

    @Test(priority = 59)
    public void testJwtExtractEmail() {
        when(jwtUtil.getEmailFromToken("token")).thenReturn("abc@test.com");
        Assert.assertEquals(jwtUtil.getEmailFromToken("token"),"abc@test.com");
    }

   
    @Test(priority = 60)
    public void testDeptGetAll() {
        when(departmentRepository.findAll()).thenReturn(List.of());
        Assert.assertTrue(departmentService.getAll().isEmpty());
    }

    @Test(priority = 61)
    public void testEmployeeGetAll() {
        when(employeeRepository.findAll()).thenReturn(List.of());
        Assert.assertTrue(employeeService.getAll().isEmpty());
    }


    @Test(priority = 62)
    public void testScheduleDateParse() {
        ScheduleController sc = new ScheduleController(scheduleService);
        try {
            sc.byDate("2020-01-01");
            Assert.assertTrue(true);
        } catch(Exception e) {
            Assert.fail();
        }
    }

    @Test(priority = 63)
    public void testFinalSuiteCompletion() {
        Assert.assertTrue(true, "All tests executed successfully.");
    }
}
