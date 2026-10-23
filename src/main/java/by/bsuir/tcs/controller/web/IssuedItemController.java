package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Employee;
import by.bsuir.tcs.entity.IssuedItem;
import by.bsuir.tcs.entity.Norm;
import by.bsuir.tcs.service.EmployeeService;
import by.bsuir.tcs.service.IssuedItemService;
import by.bsuir.tcs.service.NormService;
import by.bsuir.tcs.service.TmcItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/issued-items")
@RequiredArgsConstructor
public class IssuedItemController {

    private final IssuedItemService issuedItemService;
    private final EmployeeService employeeService;
    private final TmcItemService tmcItemService;
    private final NormService normService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tmcType,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "issueDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Pageable pageable = PageRequest.of(
                page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy)
        );

        Page<IssuedItem> itemPage = issuedItemService.findAllWithFilters(
                status, tmcType, search, pageable
        );

        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", itemPage.getTotalPages());
        model.addAttribute("totalItems", itemPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedTmcType", tmcType);
        model.addAttribute("searchQuery", search);

        return "issued-items/list";
    }

    @GetMapping("/issue")
    public String issueForm(Model model) {
        model.addAttribute("employees", employeeService.findAllActive());
        model.addAttribute("tmcItems", List.of());
        return "issued-items/form";
    }

    @GetMapping("/available-tmc")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> availableTmc(@RequestParam Long employeeId) {
        Employee employee = employeeService.findById(employeeId);

        List<Norm> norms = normService.findByProfession(employee.getProfession().getId());

        Set<Long> alreadyIssuedTmcIds = new HashSet<>();
        for (IssuedItem ii : issuedItemService.findByEmployee(employeeId)) {
            if ("ISSUED".equals(ii.getStatus())) {
                alreadyIssuedTmcIds.add(ii.getTmcItem().getId());
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Norm norm : norms) {
            Long tmcId = norm.getTmcItem().getId();
            if (alreadyIssuedTmcIds.contains(tmcId)) continue;

            result.add(Map.of(
                    "id", tmcId,
                    "code", norm.getTmcItem().getCode(),
                    "name", norm.getTmcItem().getName()
            ));
        }

        result.sort((a, b) -> ((String) a.get("code")).compareTo((String) b.get("code")));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/issue")
    public String issue(
            @RequestParam Long employeeId,
            @RequestParam Long tmcItemId,
            @RequestParam Integer quantity,
            RedirectAttributes redirectAttributes) {
        try {
            issuedItemService.issueItem(employeeId, tmcItemId, quantity);
            redirectAttributes.addFlashAttribute("success", "ТМЦ успешно выдан сотруднику");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка выдачи: " + e.getMessage());
        }
        return "redirect:/issued-items";
    }

    @PostMapping("/return/{id}")
    public String returnItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            issuedItemService.returnItem(id);
            redirectAttributes.addFlashAttribute("success", "ТМЦ возвращён на склад");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка возврата: " + e.getMessage());
        }
        return "redirect:/issued-items";
    }

    @PostMapping("/write-off/{id}")
    public String writeOff(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            issuedItemService.writeOffItem(id);
            redirectAttributes.addFlashAttribute("success", "ТМЦ списан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка списания: " + e.getMessage());
        }
        return "redirect:/issued-items";
    }
}