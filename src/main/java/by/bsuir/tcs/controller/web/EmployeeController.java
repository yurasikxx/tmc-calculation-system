package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Employee;
import by.bsuir.tcs.service.DepartmentService;
import by.bsuir.tcs.service.EmployeeService;
import by.bsuir.tcs.service.ProfessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String create(@ModelAttribute Employee employee) {
        employeeService.create(employee);
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
    public String update(@PathVariable Long id, @ModelAttribute Employee employee) {
        employeeService.update(id, employee);
        return "redirect:/employees";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        employeeService.delete(id);
        return "redirect:/employees";
    }
}