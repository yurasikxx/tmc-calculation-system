package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.*;
import by.bsuir.tcs.repository.IssuedItemRepository;
import by.bsuir.tcs.repository.NormRepository;
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
class IssuedItemServiceTest {

    @Mock
    private IssuedItemRepository issuedItemRepository;
    @Mock
    private NormRepository normRepository;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private TmcItemService tmcItemService;
    @Mock
    private UserService userService;

    @InjectMocks
    private IssuedItemService issuedItemService;

    private Employee employee;
    private TmcItem tmcItem;
    private Norm norm;
    private User user;

    @BeforeEach
    void setUp() {
        Profession profession = Profession.builder().id(5L).name("Токарь").build();
        employee = Employee.builder()
                .id(1L).fullName("Иванов Иван Иванович")
                .hireDate(LocalDate.now().minusYears(1))
                .profession(profession).build();

        tmcItem = TmcItem.builder()
                .id(10L).name("Перчатки х/б")
                .serviceLifeMonths(6).build();

        norm = Norm.builder()
                .id(1L).tmcItem(tmcItem).profession(profession)
                .quantity(2).periodMonths(6).build();

        user = User.builder().id(100L).username("storekeeper").build();
    }

    @Test
    void issueItem_success() {
        when(employeeService.findById(1L)).thenReturn(employee);
        when(tmcItemService.findById(10L)).thenReturn(tmcItem);
        when(userService.getCurrentUser()).thenReturn(user);
        when(normRepository.findByProfessionId(5L)).thenReturn(List.of(norm));
        when(issuedItemRepository.findByEmployeeId(1L)).thenReturn(List.of());
        when(issuedItemRepository.save(any(IssuedItem.class))).thenAnswer(i -> i.getArgument(0));

        IssuedItem result = issuedItemService.issueItem(1L, 10L, 1);

        assertEquals("ISSUED", result.getStatus());
        assertEquals(1, result.getQuantity());
        verify(issuedItemRepository).save(any(IssuedItem.class));
    }

    @Test
    void issueItem_whenAlreadyIssued_throwsException() {
        IssuedItem existing = IssuedItem.builder()
                .id(50L).employee(employee).tmcItem(tmcItem)
                .status("ISSUED").quantity(1).build();

        when(employeeService.findById(1L)).thenReturn(employee);
        when(tmcItemService.findById(10L)).thenReturn(tmcItem);
        when(userService.getCurrentUser()).thenReturn(user);
        when(normRepository.findByProfessionId(5L)).thenReturn(List.of(norm));
        when(issuedItemRepository.findByEmployeeId(1L)).thenReturn(List.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> issuedItemService.issueItem(1L, 10L, 1));
        assertTrue(ex.getMessage().contains("уже есть ТМЦ"));
        verify(issuedItemRepository, never()).save(any(IssuedItem.class));
    }

    @Test
    void issueItem_whenEmployeeTerminated_throwsException() {
        employee.setTerminationDate(LocalDate.now().minusDays(1));
        when(employeeService.findById(1L)).thenReturn(employee);
        when(tmcItemService.findById(10L)).thenReturn(tmcItem);
        when(userService.getCurrentUser()).thenReturn(user);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> issuedItemService.issueItem(1L, 10L, 1));
        assertTrue(ex.getMessage().contains("уволен"));
    }

    @Test
    void issueItem_whenNoNorm_throwsException() {
        when(employeeService.findById(1L)).thenReturn(employee);
        when(tmcItemService.findById(10L)).thenReturn(tmcItem);
        when(userService.getCurrentUser()).thenReturn(user);
        when(normRepository.findByProfessionId(5L)).thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> issuedItemService.issueItem(1L, 10L, 1));
        assertTrue(ex.getMessage().contains("не установлена норма"));
    }

    @Test
    void returnItem_success() {
        IssuedItem item = IssuedItem.builder()
                .id(50L).status("ISSUED").quantity(1).build();
        when(issuedItemRepository.findById(50L)).thenReturn(java.util.Optional.of(item));
        when(issuedItemRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        issuedItemService.returnItem(50L);

        assertEquals("RETURNED", item.getStatus());
        assertNotNull(item.getActualReturnDate());
    }

    @Test
    void writeOffItem_success() {
        IssuedItem item = IssuedItem.builder()
                .id(50L).status("ISSUED").quantity(1).build();
        when(issuedItemRepository.findById(50L)).thenReturn(java.util.Optional.of(item));
        when(issuedItemRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        issuedItemService.writeOffItem(50L);

        assertEquals("WRITTEN_OFF", item.getStatus());
    }
}