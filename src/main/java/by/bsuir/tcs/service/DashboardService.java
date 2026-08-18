package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.DashboardStatsDto;
import by.bsuir.tcs.dto.RecentCalculationDto;
import by.bsuir.tcs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DepartmentRepository departmentRepository;
    private final ProfessionRepository professionRepository;
    private final EmployeeRepository employeeRepository;
    private final TmcItemRepository tmcItemRepository;
    private final NormRepository normRepository;
    private final CalculationResultRepository calculationResultRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getStats() {
        return DashboardStatsDto.builder()
                .departmentsCount(departmentRepository.count())
                .professionsCount(professionRepository.count())
                .employeesCount(employeeRepository.count())
                .tmcItemsCount(tmcItemRepository.count())
                .normsCount(normRepository.count())
                .calculationsCount(calculationResultRepository.count())
                .usersCount(userRepository.count())
                .build();
    }

    @Transactional(readOnly = true)
    public List<RecentCalculationDto> getRecentCalculations() {
        return calculationResultRepository.findTop5ByOrderByCalculationDateDesc().stream()
                .map(result -> RecentCalculationDto.builder()
                        .period(result.getPeriodMonth() + "/" + result.getPeriodYear())
                        .tmcName(result.getTmcItem().getName())
                        .tmcCode(result.getTmcItem().getCode())
                        .requiredQuantity(result.getRequiredQuantity())
                        .formattedDate(result.getCalculationDate()
                                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Object[]> getEmployeesByDepartment() {
        return employeeRepository.countEmployeesByDepartment();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getTmcItemsByType() {
        return tmcItemRepository.countByType();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getCalculationsByMonth() {
        return calculationResultRepository.countByMonth();
    }
}