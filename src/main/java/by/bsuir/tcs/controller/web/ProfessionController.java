package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.service.ProfessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String create(@ModelAttribute Profession profession) {
        professionService.create(profession);
        return "redirect:/professions";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("profession", professionService.findById(id));
        return "professions/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Profession profession) {
        professionService.update(id, profession);
        return "redirect:/professions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        professionService.delete(id);
        return "redirect:/professions";
    }
}