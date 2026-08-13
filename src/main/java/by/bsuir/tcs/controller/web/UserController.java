package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.User;
import by.bsuir.tcs.entity.UserRole;
import by.bsuir.tcs.repository.UserRepository;
import by.bsuir.tcs.repository.UserRoleRepository;
import by.bsuir.tcs.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userRoleRepository.findAll());
        model.addAttribute("employees", employeeService.findAll());
        return "users/form";
    }

    @PostMapping("/create")
    public String create(
            @ModelAttribute User user,
            @RequestParam Long roleId,
            @RequestParam(required = false) Long employeeId,
            RedirectAttributes redirectAttributes) {

        try {
            UserRole role = userRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Роль не найдена"));
            user.setRole(role);
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

            if (employeeId != null) {
                user.setEmployee(employeeService.findById(employeeId));
            }

            user.setIsActive(true);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании пользователя: " + e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            user.setIsActive(!user.getIsActive());
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success",
                    user.getIsActive() ? "Пользователь активирован" : "Пользователь деактивирован");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        model.addAttribute("user", user);
        model.addAttribute("roles", userRoleRepository.findAll());
        model.addAttribute("employees", employeeService.findAll());
        return "users/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute User user,
            @RequestParam Long roleId,
            @RequestParam(required = false) Long employeeId,
            RedirectAttributes redirectAttributes) {

        try {
            User existing = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            existing.setUsername(user.getUsername());

            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                existing.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }

            UserRole role = userRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Роль не найдена"));
            existing.setRole(role);

            if (employeeId != null) {
                existing.setEmployee(employeeService.findById(employeeId));
            } else {
                existing.setEmployee(null);
            }

            userRepository.save(existing);
            redirectAttributes.addFlashAttribute("success", "Пользователь обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            if (user.getUsername().equals("admin")) {
                redirectAttributes.addFlashAttribute("error", "Нельзя удалить администратора");
                return "redirect:/users";
            }

            userRepository.delete(user);
            redirectAttributes.addFlashAttribute("success", "Пользователь удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/users";
    }
}