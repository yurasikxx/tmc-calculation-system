package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.User;
import by.bsuir.tcs.repository.UserRepository;
import by.bsuir.tcs.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
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
        return "users/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute User user, @RequestParam Long roleId) {
        user.setRole(userRoleRepository.findById(roleId).orElseThrow());
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userRepository.save(user);
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }
}