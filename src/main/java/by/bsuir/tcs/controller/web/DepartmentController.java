package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Department;
import by.bsuir.tcs.service.DepartmentService;
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
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Department> departmentPage;

        if (search != null && !search.isEmpty()) {
            departmentPage = departmentService.findBySearch(search, pageable);
        } else {
            departmentPage = departmentService.findAll(pageable);
        }

        model.addAttribute("departments", departmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", departmentPage.getTotalPages());
        model.addAttribute("totalItems", departmentPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("searchQuery", search);

        return "departments/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Department department, RedirectAttributes redirectAttributes) {
        try {
            departmentService.create(department);
            redirectAttributes.addFlashAttribute("success", "Подразделение успешно создано");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании подразделения: " + e.getMessage());
        }
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("department", departmentService.findById(id));
        return "departments/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Department department, RedirectAttributes redirectAttributes) {
        try {
            departmentService.update(id, department);
            redirectAttributes.addFlashAttribute("success", "Подразделение успешно обновлено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении подразделения: " + e.getMessage());
        }
        return "redirect:/departments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Подразделение успешно удалено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении подразделения: " + e.getMessage());
        }
        return "redirect:/departments";
    }
}