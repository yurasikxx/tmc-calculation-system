package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.Employee;
import by.bsuir.tcs.entity.IssuedItem;
import by.bsuir.tcs.entity.Norm;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.entity.User;
import by.bsuir.tcs.repository.IssuedItemRepository;
import by.bsuir.tcs.repository.NormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssuedItemService {

    private final IssuedItemRepository issuedItemRepository;
    private final NormRepository normRepository;
    private final EmployeeService employeeService;
    private final TmcItemService tmcItemService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<IssuedItem> findAllWithFilters(String status, String tmcType, String search, Pageable pageable) {
        String statusFilter = (status == null || status.isBlank()) ? null : status;
        String typeFilter = (tmcType == null || tmcType.isBlank()) ? null : tmcType;
        String searchFilter = (search == null || search.isBlank()) ? null : "%" + search.toLowerCase().trim() + "%";
        return issuedItemRepository.findAllFiltered(statusFilter, typeFilter, searchFilter, pageable);
    }

    @Transactional(readOnly = true)
    public List<IssuedItem> findByEmployee(Long employeeId) {
        return issuedItemRepository.findByEmployeeId(employeeId);
    }

    @Transactional
    public IssuedItem issueItem(Long employeeId, Long tmcItemId, Integer quantity) {
        Employee employee = employeeService.findById(employeeId);
        TmcItem tmcItem = tmcItemService.findById(tmcItemId);
        User currentUser = userService.getCurrentUser();

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Количество должно быть положительным");
        }

        if (employee.getTerminationDate() != null && !employee.getTerminationDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Сотрудник " + employee.getFullName() + " уволен. Выдача невозможна.");
        }

        Norm applicableNorm = normRepository
                .findByProfessionId(employee.getProfession().getId())
                .stream()
                .filter(n -> n.getTmcItem().getId().equals(tmcItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Для профессии '" + employee.getProfession().getName()
                                + "' не установлена норма выдачи ТМЦ '"
                                + tmcItem.getName() + "'"));

        boolean alreadyIssued = issuedItemRepository.findByEmployeeId(employeeId).stream()
                .anyMatch(i -> i.getTmcItem().getId().equals(tmcItemId)
                        && "ISSUED".equals(i.getStatus()));

        if (alreadyIssued) {
            throw new RuntimeException("У сотрудника уже есть ТМЦ '" + tmcItem.getName() + "' в эксплуатации");
        }

        LocalDate issueDate = LocalDate.now();
        int serviceLife = tmcItem.getServiceLifeMonths() != null
                && tmcItem.getServiceLifeMonths() > 0
                ? tmcItem.getServiceLifeMonths() : 12;

        IssuedItem issuedItem = IssuedItem.builder()
                .employee(employee)
                .tmcItem(tmcItem)
                .norm(applicableNorm)
                .quantity(quantity)
                .issueDate(issueDate)
                .expectedReturnDate(issueDate.plusMonths(serviceLife))
                .status("ISSUED")
                .createdBy(currentUser)
                .build();

        return issuedItemRepository.save(issuedItem);
    }

    @Transactional
    public void returnItem(Long id) {
        IssuedItem item = issuedItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись о выдаче не найдена"));

        if (!"ISSUED".equals(item.getStatus())) {
            throw new RuntimeException("Вернуть можно только ТМЦ со статусом 'В эксплуатации'");
        }

        item.setActualReturnDate(LocalDate.now());
        item.setStatus("RETURNED");
        issuedItemRepository.save(item);
    }

    @Transactional
    public void writeOffItem(Long id) {
        IssuedItem item = issuedItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись о выдаче не найдена"));

        if (!"ISSUED".equals(item.getStatus())) {
            throw new RuntimeException("Списать можно только ТМЦ со статусом 'В эксплуатации'");
        }

        item.setStatus("WRITTEN_OFF");
        item.setActualReturnDate(LocalDate.now());
        issuedItemRepository.save(item);
    }
}