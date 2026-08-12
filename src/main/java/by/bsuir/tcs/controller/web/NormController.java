package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Norm;
import by.bsuir.tcs.service.NormService;
import by.bsuir.tcs.service.ProfessionService;
import by.bsuir.tcs.service.TmcItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/norms")
@RequiredArgsConstructor
public class NormController {

    private final NormService normService;
    private final TmcItemService tmcItemService;
    private final ProfessionService professionService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("norms", normService.findAllForCurrentUser());
        return "norms/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("norm", new Norm());
        model.addAttribute("tmcItems", tmcItemService.findAllForCurrentUser());
        model.addAttribute("professions", professionService.findAll());
        return "norms/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Norm norm, RedirectAttributes redirectAttributes) {
        try {
            normService.create(norm);
            redirectAttributes.addFlashAttribute("success", "Норма выдачи успешно создана");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании нормы: " + e.getMessage());
        }
        return "redirect:/norms";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("norm", normService.findById(id));
        model.addAttribute("tmcItems", tmcItemService.findAllForCurrentUser());
        model.addAttribute("professions", professionService.findAll());
        return "norms/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Norm norm, RedirectAttributes redirectAttributes) {
        try {
            normService.update(id, norm);
            redirectAttributes.addFlashAttribute("success", "Норма выдачи успешно обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении нормы: " + e.getMessage());
        }
        return "redirect:/norms";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            normService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Норма выдачи успешно удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении нормы: " + e.getMessage());
        }
        return "redirect:/norms";
    }
}