package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Department;
import by.bsuir.tcs.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "departments/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Department department) {
        departmentService.create(department);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("department", departmentService.findById(id));
        return "departments/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Department department) {
        departmentService.update(id, department);
        return "redirect:/departments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        departmentService.delete(id);
        return "redirect:/departments";
    }
}