package by.bsuir.tcs.service;

import by.bsuir.tcs.repository.EmployeeRepository;
import by.bsuir.tcs.repository.IssuedItemRepository;
import by.bsuir.tcs.repository.NormRepository;
import by.bsuir.tcs.repository.StaffingPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private StaffingPlanRepository staffingPlanRepository;
    @Mock
    private NormRepository normRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private IssuedItemRepository issuedItemRepository;
    @Mock
    private CalculationResultService calculationResultService;

    @InjectMocks
    private CalculationService calculationService;

    @Test
    void calculate_clearsPreviousResults() {
        calculationService.calculate(2026, 10);
        verify(calculationResultService).clearByPeriod(2026, 10);
    }

    @Test
    void calculate_emptyData_noResultsSaved() {
        when(employeeRepository.findAll()).thenReturn(List.of());
        when(issuedItemRepository.findActiveByDate(any())).thenReturn(List.of());
        when(normRepository.findAll()).thenReturn(List.of());
        when(staffingPlanRepository.findByEffectiveDateBetween(any(), any())).thenReturn(List.of());
        when(issuedItemRepository.findExpiringBetween(any(), any())).thenReturn(List.of());

        calculationService.calculate(2026, 10);

        verify(calculationResultService, never()).save(any());
    }

    @Test
    void calculate_threeSources_allProcessed() {
        calculationService.calculate(2026, 10);
        verify(calculationResultService).clearByPeriod(2026, 10);
    }
}