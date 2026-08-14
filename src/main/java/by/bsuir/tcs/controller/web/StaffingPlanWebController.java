package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.dto.StaffingPlanRequest;
import by.bsuir.tcs.entity.StaffingPlan;
import by.bsuir.tcs.service.StaffingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/staffing-plans")
@RequiredArgsConstructor
public class StaffingPlanWebController {

    private final StaffingPlanService staffingPlanService;
    private final ObjectMapper objectMapper;

    @GetMapping("/upload")
    public String uploadForm() {
        return "staffing-plans/upload";
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {
            String json = new String(file.getBytes());
            List<StaffingPlanRequest> requests = Arrays.asList(
                    objectMapper.readValue(json, StaffingPlanRequest[].class)
            );

            List<String> errors = validateRequests(requests);

            if (!errors.isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                        "Ошибки в данных: " + String.join("; ", errors));
                return "redirect:/staffing-plans/upload";
            }

            List<StaffingPlan> saved = staffingPlanService.importPlan(requests);
            redirectAttributes.addFlashAttribute("success",
                    "Загружено " + saved.size() + " кадровых событий");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при загрузке: " + e.getMessage());
        }

        return "redirect:/staffing-plans/upload";
    }

    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) String actionType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "effectiveDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<StaffingPlan> planPage;

        if (year != null && month != null) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
            planPage = staffingPlanService.findByPeriod(start, end, pageable);
        } else if (actionType != null && !actionType.isEmpty()) {
            planPage = staffingPlanService.findByActionType(actionType, pageable);
        } else {
            planPage = staffingPlanService.findAll(pageable);
        }

        model.addAttribute("plans", planPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", planPage.getTotalPages());
        model.addAttribute("totalItems", planPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedActionType", actionType);

        return "staffing-plans/list";
    }

    private List<String> validateRequests(List<StaffingPlanRequest> requests) {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < requests.size(); i++) {
            StaffingPlanRequest req = requests.get(i);
            String prefix = "Строка " + (i + 1) + ": ";

            if (req.getFullName() == null || req.getFullName().trim().isEmpty()) {
                errors.add(prefix + "Отсутствует ФИО сотрудника");
            }

            if (req.getProfessionName() == null || req.getProfessionName().trim().isEmpty()) {
                errors.add(prefix + "Отсутствует профессия сотрудника");
            }

            if (req.getDepartmentName() == null || req.getDepartmentName().trim().isEmpty()) {
                errors.add(prefix + "Отсутствует подразделение сотрудника");
            }

            if (req.getActionType() == null || req.getActionType().trim().isEmpty()) {
                errors.add(prefix + "Отсутствует тип действия");
            } else {
                String action = req.getActionType();
                if (!action.equals("HIRE") && !action.equals("TRANSFER") && !action.equals("TERMINATE")) {
                    errors.add(prefix + "Некорректный тип действия: " + action +
                            ". Допустимые: HIRE, TRANSFER, TERMINATE");
                }
            }

            if (req.getEffectiveDate() == null) {
                errors.add(prefix + "Отсутствует дата вступления в силу");
            }

            if ("TRANSFER".equals(req.getActionType())) {
                if ((req.getNewProfessionName() == null || req.getNewProfessionName().trim().isEmpty()) &&
                        (req.getNewDepartmentName() == null || req.getNewDepartmentName().trim().isEmpty())) {
                    errors.add(prefix + "Для перевода необходимо указать новую профессию или новое подразделение");
                }
            }
        }

        return errors;
    }
}