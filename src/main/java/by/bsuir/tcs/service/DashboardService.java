package by.bsuir.tcs.service;

import by.bsuir.tcs.dto.DashboardStatsDto;
import by.bsuir.tcs.dto.RecentCalculationDto;
import by.bsuir.tcs.entity.CalculationResult;
import by.bsuir.tcs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final String[] MONTHS_RU = {
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    };

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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
        return calculationResultRepository
                .findTop10ByOrderByPeriodYearDescPeriodMonthDescCalculationDateDesc()
                .stream()
                .map(r -> RecentCalculationDto.builder()
                        .period(String.format("%02d/%d",
                                r.getPeriodMonth(), r.getPeriodYear()))
                        .tmcName(r.getTmcItem().getName())
                        .tmcCode(r.getTmcItem().getCode())
                        .requiredQuantity(r.getRequiredQuantity())
                        .formattedDate(r.getCalculationDate() != null
                                ? r.getCalculationDate().format(DT_FMT)
                                : "")
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public int[] getLastPeriod() {
        List<CalculationResult> top = calculationResultRepository
                .findTop10ByOrderByPeriodYearDescPeriodMonthDescCalculationDateDesc();
        if (top.isEmpty()) return null;
        CalculationResult first = top.getFirst();
        return new int[]{first.getPeriodYear(), first.getPeriodMonth()};
    }

    public String monthName(int month) {
        if (month < 1 || month > 12) return "—";
        return MONTHS_RU[month - 1];
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