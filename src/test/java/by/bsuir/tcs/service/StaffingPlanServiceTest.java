package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.StaffingPlanRequest;
import by.bsuir.tcs.entity.Department;
import by.bsuir.tcs.entity.Employee;
import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.entity.StaffingPlan;
import by.bsuir.tcs.repository.StaffingPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffingPlanServiceTest {

    @Mock
    private StaffingPlanRepository staffingPlanRepository;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private ProfessionService professionService;
    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private StaffingPlanService staffingPlanService;

    private Profession profession;
    private Department department;

    @BeforeEach
    void setUp() {
        profession = Profession.builder().id(5L).name("Токарь").build();
        department = Department.builder().id(3L).name("Цех 1").build();
    }

    @Test
    void importPlan_withEmptyFullName_throwsValidationError() {
        StaffingPlanRequest req = StaffingPlanRequest.builder()
                .fullName("").professionName("Токарь")
                .departmentName("Цех 1").actionType("HIRE")
                .effectiveDate(LocalDate.now()).build();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> staffingPlanService.importPlan(List.of(req)));
        assertTrue(ex.getMessage().contains("ФИО не может быть пустым"));
    }

    @Test
    void importPlan_withInvalidActionType_throwsValidationError() {
        StaffingPlanRequest req = StaffingPlanRequest.builder()
                .fullName("Петров Пётр").professionName("Токарь")
                .departmentName("Цех 1").actionType("UNKNOWN")
                .effectiveDate(LocalDate.now()).build();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> staffingPlanService.importPlan(List.of(req)));
        assertTrue(ex.getMessage().contains("Некорректный тип действия"));
    }

    @Test
    void importPlan_successfulHire_createsEmployeeAndPlan() {
        StaffingPlanRequest req = StaffingPlanRequest.builder()
                .fullName("Петров Пётр Петрович").professionName("Токарь")
                .departmentName("Цех 1").actionType("HIRE")
                .effectiveDate(LocalDate.now()).build();

        when(employeeService.findByFullName("Петров Пётр Петрович")).thenReturn(null);
        when(professionService.findAll()).thenReturn(List.of(profession));
        when(departmentService.findAll()).thenReturn(List.of(department));
        when(employeeService.create(any(Employee.class))).thenAnswer(i -> {
            Employee e = i.getArgument(0);
            e.setId(10L);
            return e;
        });
        when(staffingPlanRepository.save(any(StaffingPlan.class))).thenAnswer(i -> i.getArgument(0));

        List<StaffingPlan> result = staffingPlanService.importPlan(List.of(req));

        assertEquals(1, result.size());
        assertEquals("HIRE", result.getFirst().getActionType());
        verify(employeeService).create(any(Employee.class));
    }

    @Test
    void importPlan_repeatedHire_skipped() {
        StaffingPlanRequest req = StaffingPlanRequest.builder()
                .fullName("Иванов Иван").professionName("Токарь")
                .departmentName("Цех 1").actionType("HIRE")
                .effectiveDate(LocalDate.now()).build();

        Employee existing = Employee.builder()
                .id(1L).fullName("Иванов Иван")
                .hireDate(LocalDate.now().minusYears(1))
                .profession(profession).department(department).build();

        when(employeeService.findByFullName("Иванов Иван")).thenReturn(existing);

        List<StaffingPlan> result = staffingPlanService.importPlan(List.of(req));

        assertTrue(result.isEmpty());
        verify(staffingPlanRepository, never()).save(any());
    }

    @Test
    void importPlan_terminateAlreadyTerminated_skipsRecord() {
        StaffingPlanRequest req = StaffingPlanRequest.builder()
                .fullName("Сидоров Сидор").professionName("Токарь")
                .departmentName("Цех 1").actionType("TERMINATE")
                .effectiveDate(LocalDate.now()).build();

        Employee existing = Employee.builder()
                .id(2L).fullName("Сидоров Сидор")
                .hireDate(LocalDate.now().minusYears(2))
                .terminationDate(LocalDate.now().minusMonths(1))
                .profession(profession).department(department).build();

        when(employeeService.findByFullName("Сидоров Сидор")).thenReturn(existing);

        List<StaffingPlan> result = staffingPlanService.importPlan(List.of(req));

        assertTrue(result.isEmpty(), "Уволенный сотрудник должен быть пропущен");
        verify(staffingPlanRepository, never()).save(any());
    }

    @Test
    void importPlan_transfer_updatesProfession() {
        StaffingPlanRequest req = StaffingPlanRequest.builder()
                .fullName("Кузнецов Кузьма").professionName("Токарь")
                .departmentName("Цех 1").actionType("TRANSFER")
                .newProfessionName("Слесарь")
                .effectiveDate(LocalDate.now()).build();

        Profession newProfession = Profession.builder().id(6L).name("Слесарь").build();
        Employee existing = Employee.builder()
                .id(3L).fullName("Кузнецов Кузьма")
                .hireDate(LocalDate.now().minusYears(1))
                .profession(profession).department(department).build();

        when(employeeService.findByFullName("Кузнецов Кузьма")).thenReturn(existing);
        when(professionService.findAll()).thenReturn(List.of(profession, newProfession));
        when(employeeService.update(any(), any())).thenReturn(existing);
        when(staffingPlanRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        List<StaffingPlan> result = staffingPlanService.importPlan(List.of(req));

        assertEquals(1, result.size());
        assertEquals("TRANSFER", result.getFirst().getActionType());
        assertEquals(newProfession, existing.getProfession());
        verify(employeeService).update(existing.getId(), existing);
    }
}