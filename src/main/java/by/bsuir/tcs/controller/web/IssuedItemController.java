package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.IssuedItem;
import by.bsuir.tcs.service.EmployeeService;
import by.bsuir.tcs.service.IssuedItemService;
import by.bsuir.tcs.service.TmcItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/issued-items")
@RequiredArgsConstructor
public class IssuedItemController {

    private final IssuedItemService issuedItemService;
    private final EmployeeService employeeService;
    private final TmcItemService tmcItemService;

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
        model.addAttribute("tmcItems", tmcItemService.findAll());
        return "issued-items/form";
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