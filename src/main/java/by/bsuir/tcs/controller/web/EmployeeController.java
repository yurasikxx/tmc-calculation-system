package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Employee;
import by.bsuir.tcs.service.DepartmentService;
import by.bsuir.tcs.service.EmployeeService;
import by.bsuir.tcs.service.ProfessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ProfessionService professionService;
    private final DepartmentService departmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "employees/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("professions", professionService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "employees/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        try {
            employeeService.create(employee);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании сотрудника: " + e.getMessage());
        }
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        model.addAttribute("professions", professionService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "employees/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        try {
            employeeService.update(id, employee);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении сотрудника: " + e.getMessage());
        }
        return "redirect:/employees";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении сотрудника: " + e.getMessage());
        }
        return "redirect:/employees";
    }
}