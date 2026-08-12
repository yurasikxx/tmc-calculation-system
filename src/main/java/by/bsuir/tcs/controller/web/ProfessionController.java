package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.service.ProfessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/professions")
@RequiredArgsConstructor
public class ProfessionController {

    private final ProfessionService professionService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("professions", professionService.findAll());
        return "professions/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("profession", new Profession());
        return "professions/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Profession profession, RedirectAttributes redirectAttributes) {
        try {
            professionService.create(profession);
            redirectAttributes.addFlashAttribute("success", "Профессия успешно создана");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании профессии: " + e.getMessage());
        }
        return "redirect:/professions";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("profession", professionService.findById(id));
        return "professions/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Profession profession, RedirectAttributes redirectAttributes) {
        try {
            professionService.update(id, profession);
            redirectAttributes.addFlashAttribute("success", "Профессия успешно обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении профессии: " + e.getMessage());
        }
        return "redirect:/professions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            professionService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Профессия успешно удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении профессии: " + e.getMessage());
        }
        return "redirect:/professions";
    }
}